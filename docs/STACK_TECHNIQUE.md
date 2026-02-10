# STACK TECHNIQUE - Coup de Main

## 🏗️ Architecture Globale

```
┌─────────────────────────────────────────────────────────────┐
│                   COUP DE MAIN ANDROID                      │
└─────────────────────────────────────────────────────────────┘

┌───────────────┐           ┌──────────────────────────────┐
│  MainActivity │           │   ChessKeyboardIME Service   │
│  (App dédiée) │           │   (InputMethodService)       │
└───────┬───────┘           └────────────┬─────────────────┘
        │                                │
        │  ┌─────────────────────────────┴──────┐
        │  │                                    │
        ▼  ▼                                    ▼
┌────────────────┐                    ┌──────────────────┐
│ ChessKeyboard  │◄───────────────────┤  Shared          │
│ View (Widget)  │                    │  Components      │
└────────┬───────┘                    └──────────────────┘
         │                                     │
         │  ┌──────────────────────────────────┼─────────┐
         │  │                                  │         │
         ▼  ▼                                  ▼         ▼
┌──────────────┐  ┌──────────────┐  ┌──────────────┐  ┌──────────┐
│   Room DB    │  │  Preferences │  │   Notation   │  │  Theme   │
│  (parties)   │  │  (settings)  │  │   Builder    │  │  Engine  │
└──────────────┘  └──────────────┘  └──────────────┘  └──────────┘
```

---

## 📱 Technologies Android

### Langage : Kotlin

**Pourquoi Kotlin ?**
- ✅ **Standard Android moderne** (Google recommande depuis 2019)
- ✅ **Moins verbeux** que Java (50% moins de code)
- ✅ **Null-safety** intégrée (moins de crashes NPE)
- ✅ **Coroutines** pour async/await propre
- ✅ **Interopérabilité Java** (bibliothèques existantes)

**Version :**
- Kotlin `1.9.20+`
- Compatible avec Android API 21+ (Android 5.0 Lollipop)

### Build System : Gradle

**Configuration :**
```kotlin
// build.gradle.kts (app-level)
android {
    compileSdk = 34
    
    defaultConfig {
        applicationId = "fr.bkh.coupdemain"
        minSdk = 21          // Android 5.0 (94% appareils)
        targetSdk = 34       // Android 14
        versionCode = 1
        versionName = "1.0.0-alpha"
    }
    
    buildFeatures {
        viewBinding = true   // Type-safe view access
    }
    
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    
    kotlinOptions {
        jvmTarget = "17"
    }
}
```

---

## 🎨 UI Framework : Material Design 3

### Material Components

**Bibliothèque :**
```kotlin
dependencies {
    implementation("com.google.android.material:material:1.11.0")
}
```

**Composants utilisés :**
- `MaterialCardView` : Cartes parties dans l'historique
- `FloatingActionButton` : Nouvelle partie
- `BottomNavigationView` : Navigation app (si multi-sections)
- `PreferenceScreen` : Settings clavier
- `MaterialButton` : Boutons clavier personnalisés

### Thèmes

**Palette de couleurs** (inspirée d'[IA_TASKS](../../IA_TASKS/config.py)) :

```xml
<!-- res/values/colors.xml -->
<resources>
    <!-- Dark theme (default) -->
    <color name="background_dark">#0D1117</color>
    <color name="card_dark">#161B22</color>
    <color name="accent_blue">#58A6FF</color>
    <color name="text_primary">#C9D1D9</color>
    <color name="text_secondary">#8B949E</color>
    
    <!-- Light theme -->
    <color name="background_light">#FFFFFF</color>
    <color name="card_light">#F6F8FA</color>
    <color name="accent_blue">#0969DA</color>
    <color name="text_primary">#24292F</color>
    
    <!-- High contrast -->
    <color name="background_hc">#000000</color>
    <color name="card_hc">#1C1C1C</color>
    <color name="accent_hc">#FFD700</color>
    <color name="text_hc">#FFFFFF</color>
</resources>
```

---

## ⌨️ IME (Input Method Editor)

### InputMethodService

**Composant Android central pour créer un clavier système :**

```kotlin
class ChessKeyboardIME : InputMethodService() {
    
    private lateinit var keyboardView: ChessKeyboardView
    private val prefs by lazy { ChessKeyboardPreferences(this) }
    
    override fun onCreateInputView(): View {
        keyboardView = ChessKeyboardView(this, prefs)
        return keyboardView
    }
    
    override fun onKey(primaryCode: Int, keyCodes: IntArray?) {
        val ic = currentInputConnection ?: return
        
        when (primaryCode) {
            CODE_PAWN -> ic.commitText("", 1)
            CODE_KNIGHT -> ic.commitText("N", 1)
            CODE_BISHOP -> ic.commitText("B", 1)
            // ... autres pièces
            in 'a'.code..'h'.code -> ic.commitText(primaryCode.toChar().toString(), 1)
            in '1'.code..'8'.code -> ic.commitText(primaryCode.toChar().toString(), 1)
            CODE_DELETE -> ic.deleteSurroundingText(1, 0)
            CODE_DONE -> ic.commitText("\n", 1)
        }
    }
}
```

### Déclaration AndroidManifest.xml

```xml
<service
    android:name=".ime.ChessKeyboardIME"
    android:label="Coup de Main"
    android:permission="android.permission.BIND_INPUT_METHOD"
    android:exported="true">
    <intent-filter>
        <action android:name="android.view.InputMethod" />
    </intent-filter>
    <meta-data
        android:name="android.view.im"
        android:resource="@xml/method" />
</service>
```

### Configuration IME

```xml
<!-- res/xml/method.xml -->
<input-method xmlns:android="http://schemas.android.com/apk/res/android"
    android:settingsActivity="fr.bkh.coupdemain.ui.settings.SettingsActivity"
    android:supportsSwitchingToNextInputMethod="true" />
```

---

## 🗄️ Base de Données : Room

### Architecture Room

**Pourquoi Room ?**
- ✅ **Type-safe** : Erreurs détectées à la compilation
- ✅ **Moins de boilerplate** que SQLite direct
- ✅ **LiveData/Flow** intégré pour réactivité
- ✅ **Migrations** gérées proprement

**Dépendances :**
```kotlin
dependencies {
    val roomVersion = "2.6.1"
    implementation("androidx.room:room-runtime:$roomVersion")
    implementation("androidx.room:room-ktx:$roomVersion")
    kapt("androidx.room:room-compiler:$roomVersion")
}
```

### Schéma Base de Données

```kotlin
// Entity : ChessGame
@Entity(tableName = "games")
data class ChessGame(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val whitePlayer: String,
    val blackPlayer: String,
    val result: String?,                  // "1-0", "0-1", "1/2-1/2", null
    val pgn: String,
    val fenStart: String = DEFAULT_FEN,
    val fenCurrent: String?,
    val status: GameStatus,               // ONGOING, COMPLETED, ABANDONED
    val createdAt: Long,
    val updatedAt: Long,
    val completedAt: Long?,
    val moveCount: Int = 0
)

// Entity : ChessMove (optionnel, pour analyse détaillée)
@Entity(
    tableName = "moves",
    foreignKeys = [ForeignKey(
        entity = ChessGame::class,
        parentColumns = ["id"],
        childColumns = ["gameId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index("gameId")]
)
data class ChessMove(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val gameId: Long,
    val moveNumber: Int,
    val san: String,                      // "e4", "Nf3"
    val uci: String,                      // "e2e4", "g1f3"
    val fenAfter: String,
    val timestamp: Long
)

// DAO
@Dao
interface GameDao {
    @Query("SELECT * FROM games ORDER BY createdAt DESC")
    fun getAllGames(): Flow<List<ChessGame>>
    
    @Query("SELECT * FROM games WHERE status = :status")
    fun getGamesByStatus(status: GameStatus): Flow<List<ChessGame>>
    
    @Insert
    suspend fun insertGame(game: ChessGame): Long
    
    @Update
    suspend fun updateGame(game: ChessGame)
    
    @Delete
    suspend fun deleteGame(game: ChessGame)
}

// Database
@Database(
    entities = [ChessGame::class, ChessMove::class],
    version = 1,
    exportSchema = true
)
abstract class ChessDatabase : RoomDatabase() {
    abstract fun gameDao(): GameDao
}
```

---

## 💾 Stockage Settings : SharedPreferences

### ChessKeyboardPreferences

```kotlin
class ChessKeyboardPreferences(context: Context) {
    private val prefs = context.getSharedPreferences(
        "chess_keyboard_prefs", 
        Context.MODE_PRIVATE
    )
    
    var buttonSize: ButtonSize
        get() = ButtonSize.valueOf(
            prefs.getString("button_size", "MEDIUM") ?: "MEDIUM"
        )
        set(value) = prefs.edit()
            .putString("button_size", value.name)
            .apply()
    
    var theme: KeyboardTheme
        get() = KeyboardTheme.valueOf(
            prefs.getString("theme", "DARK") ?: "DARK"
        )
        set(value) = prefs.edit()
            .putString("theme", value.name)
            .apply()
    
    var keyboardHeightPercent: Int
        get() = prefs.getInt("keyboard_height", 35)
        set(value) = prefs.edit()
            .putInt("keyboard_height", value)
            .apply()
    
    var vibrationEnabled: Boolean
        get() = prefs.getBoolean("vibration_enabled", true)
        set(value) = prefs.edit()
            .putBoolean("vibration_enabled", value)
            .apply()
    
    var vibrationIntensity: Int
        get() = prefs.getInt("vibration_intensity", 50)
        set(value) = prefs.edit()
            .putInt("vibration_intensity", value)
            .apply()
}

enum class ButtonSize(val dp: Int) {
    SMALL(48), MEDIUM(56), LARGE(64)
}

enum class KeyboardTheme {
    LIGHT, DARK, HIGH_CONTRAST
}
```

---

## 🎯 Architecture MVVM

### Pattern Recommandé

```
View (Fragment/Activity)
    ↓
ViewModel (gère état UI)
    ↓
Repository (abstraction données)
    ↓
Data Source (Room, SharedPrefs)
```

**Exemple : GameListViewModel**

```kotlin
class GameListViewModel(
    private val repository: ChessGameRepository
) : ViewModel() {
    
    val allGames: LiveData<List<ChessGame>> = 
        repository.getAllGames().asLiveData()
    
    val ongoingGames: LiveData<List<ChessGame>> = 
        repository.getOngoingGames().asLiveData()
    
    fun deleteGame(game: ChessGame) {
        viewModelScope.launch {
            repository.deleteGame(game)
        }
    }
}
```

---

## 🧩 Composants Partagés

### ChessKeyboardView (Widget Réutilisable)

```kotlin
class ChessKeyboardView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    
    private val prefs = ChessKeyboardPreferences(context)
    private var onKeyListener: ((Int) -> Unit)? = null
    
    init {
        inflate(context, R.layout.view_chess_keyboard, this)
        setupButtons()
        applyTheme(prefs.theme)
    }
    
    fun setOnKeyListener(listener: (Int) -> Unit) {
        onKeyListener = listener
    }
    
    private fun setupButtons() {
        // Grid de boutons : pièces, coordonnées, symboles
        val buttonSize = prefs.buttonSize.dp
        // ... création dynamique
    }
}
```

### NotationBuilder (Logique Métier)

```kotlin
class NotationBuilder {
    
    private val moveBuffer = StringBuilder()
    
    fun addPiece(piece: ChessPiece) {
        moveBuffer.append(piece.symbol)
    }
    
    fun addCoordinate(coord: String) {
        moveBuffer.append(coord)
    }
    
    fun addSymbol(symbol: String) {
        moveBuffer.append(symbol)
    }
    
    fun getCurrentMove(): String = moveBuffer.toString()
    
    fun isValidMove(): Boolean {
        // Regex validation : e4, Nf3, Qxe5+, etc.
        return SAN_REGEX.matches(moveBuffer.toString())
    }
    
    fun clear() {
        moveBuffer.clear()
    }
    
    companion object {
        private val SAN_REGEX = 
            Regex("^([KQRBN])?([a-h])?([1-8])?([x])?([a-h][1-8])(=[QRBN])?([+#])?$")
    }
}
```

---

## 📐 Layouts Responsives

### Qualifiers de Ressources

```
res/
├── layout/                    # Phone (défaut)
│   ├── activity_main.xml
│   └── fragment_game_list.xml
├── layout-sw600dp/            # Tablettes 7" minimum
│   └── activity_main.xml      # Master-detail layout
├── layout-sw720dp/            # Tablettes 10"+
│   └── activity_main.xml
├── layout-land/               # Paysage phone
└── values/
    ├── dimens.xml             # Tailles par défaut
    └── dimens.xml (sw600dp)   # Tailles tablette
```

**Exemple dimens.xml :**

```xml
<!-- res/values/dimens.xml (phone) -->
<resources>
    <dimen name="keyboard_button_size">56dp</dimen>
    <dimen name="keyboard_spacing">6dp</dimen>
    <dimen name="keyboard_height_percent">40</dimen>
</resources>

<!-- res/values-sw600dp/dimens.xml (tablet) -->
<resources>
    <dimen name="keyboard_button_size">64dp</dimen>
    <dimen name="keyboard_spacing">8dp</dimen>
    <dimen name="keyboard_height_percent">30</dimen>
</resources>
```

---

## 🔄 Navigation Component

**Gestion navigation multi-écrans :**

```xml
<!-- res/navigation/nav_graph.xml -->
<navigation>
    <fragment
        android:id="@+id/gameListFragment"
        android:name=".ui.games.GameListFragment">
        <action
            android:id="@+id/action_list_to_detail"
            app:destination="@id/gameDetailFragment"/>
    </fragment>
    
    <fragment
        android:id="@+id/gameDetailFragment"
        android:name=".ui.detail.GameDetailFragment">
        <argument
            android:name="gameId"
            app:argType="long"/>
    </fragment>
</navigation>
```

---

## 🧪 Tests

### Unit Tests (JUnit + Mockk)

```kotlin
dependencies {
    testImplementation("junit:junit:4.13.2")
    testImplementation("io.mockk:mockk:1.13.8")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")
}
```

**Exemple test NotationBuilder :**

```kotlin
class NotationBuilderTest {
    
    private lateinit var builder: NotationBuilder
    
    @Before
    fun setup() {
        builder = NotationBuilder()
    }
    
    @Test
    fun `pawn move is valid`() {
        builder.addCoordinate("e4")
        assertTrue(builder.isValidMove())
        assertEquals("e4", builder.getCurrentMove())
    }
    
    @Test
    fun `knight move with capture is valid`() {
        builder.addPiece(ChessPiece.KNIGHT)
        builder.addSymbol("x")
        builder.addCoordinate("f3")
        assertTrue(builder.isValidMove())
        assertEquals("Nxf3", builder.getCurrentMove())
    }
}
```

### UI Tests (Espresso)

```kotlin
dependencies {
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation("androidx.test:runner:1.5.2")
}
```

---

## 📦 Dépendances Complètes

```kotlin
dependencies {
    // Kotlin
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.9.20")
    
    // AndroidX Core
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    
    // Material Design
    implementation("com.google.android.material:material:1.11.0")
    
    // Room Database
    val roomVersion = "2.6.1"
    implementation("androidx.room:room-runtime:$roomVersion")
    implementation("androidx.room:room-ktx:$roomVersion")
    kapt("androidx.room:room-compiler:$roomVersion")
    
    // Lifecycle (ViewModel, LiveData)
    val lifecycleVersion = "2.7.0"
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycleVersion")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:$lifecycleVersion")
    
    // Navigation Component
    val navVersion = "2.7.6"
    implementation("androidx.navigation:navigation-fragment-ktx:$navVersion")
    implementation("androidx.navigation:navigation-ui-ktx:$navVersion")
    
    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
    
    // Preferences (Settings)
    implementation("androidx.preference:preference-ktx:1.2.1")
    
    // Tests
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}
```

---

## 🛠️ Outils de Développement

### Android Studio

**Version recommandée :**
- Android Studio Hedgehog | 2023.1.1+
- JDK 17 (embedded)
- Gradle 8.2+

### Émulateur Android

**Configuration recommandée pour tests :**
- **Phone** : Pixel 5 (API 30)
- **Tablet 7"** : Nexus 7 (API 29)
- **Tablet 10"** : Pixel C (API 30)

### Device Physique (Idéal)

**Tester sur vraie tablette :**
- Samsung Galaxy Tab A / S
- Lenovo Tab M10
- iPad avec Android (si dispo)

---

## 🚀 Build & Distribution

### Build Types

```kotlin
buildTypes {
    debug {
        applicationIdSuffix = ".debug"
        isDebuggable = true
    }
    
    release {
        isMinifyEnabled = true
        proguardFiles(
            getDefaultProguardFile("proguard-android-optimize.txt"),
            "proguard-rules.pro"
        )
        signingConfig = signingConfigs.getByName("release")
    }
}
```

### Signature APK

**Génération keystore :**
```bash
keytool -genkey -v -keystore coup-de-main.jks \
  -keyalg RSA -keysize 2048 -validity 10000 \
  -alias coup-de-main-key
```

### Google Play Store

**Préparation :**
- Compte Google Play Developer : $25 one-time
- Politique de confidentialité (obligatoire)
- Screenshots tablettes + phones
- Description FR + EN
- Icône 512x512px

---

## 📊 Performance

### Métriques Cibles

| Métrique | Cible | Justification |
|----------|-------|---------------|
| **App size** | < 10 MB | Téléchargement rapide |
| **Cold start** | < 2s | UX fluide |
| **Latency clavier** | < 50ms | Feedback immédiat |
| **Memory usage** | < 100 MB | Tablettes low-end |
| **Battery drain** | Minimal | Compétition longue durée |

### Optimisations

- ProGuard/R8 pour minification
- Lazy loading des parties (pagination)
- Cache des presets clavier
- Background threads pour DB I/O

---

## 🔒 Sécurité & Permissions

### Permissions Requises

```xml
<manifest>
    <!-- Aucune permission dangereuse requise ! -->
    <!-- IME = permission système automatique -->
    
    <!-- Optionnel (pour export fichiers) -->
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"
        android:maxSdkVersion="28" />
</manifest>
```

### Privacy

- **Pas de tracking** analytics
- **Pas de réseau** (100% offline)
- **Pas de données personnelles** (seulement parties d'échecs)
- **Open-source** (transparence totale)

---

## 📝 Changelog Technique

| Version | Date | Changements |
|---------|------|-------------|
| 1.0.0-alpha | 2026-02-02 | Architecture initiale définie |

---

_Stack technique validée par BKH (10+ ans exp Android). Dernière mise à jour : 2026-02-02_
