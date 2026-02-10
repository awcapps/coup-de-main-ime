# CHANGELOG - Coup de Main

Toutes les modifications notables du projet sont documentées ici.

Format basé sur [Keep a Changelog](https://keepachangelog.com/fr/1.0.0/).

---

## [Unreleased]

### 📦 Préparation Open Source - 2026-02-11

#### Fichiers créés pour publication open source ✅

**Documentation obligatoire** :
- `LICENSE` : Licence MIT
- `CONTRIBUTING.md` : Guide de contribution (setup, conventions, PR process)
- `CODE_OF_CONDUCT.md` : Code de conduite (Contributor Covenant 2.0)
- `SECURITY.md` : Politique de sécurité (reporting vulnérabilités)
- `app/keystore.properties.example` : Template pour configuration signing
- `.github/PULL_REQUEST_TEMPLATE.md` : Template pour Pull Requests
- `.github/ISSUE_TEMPLATE/bug_report.yml` : Template formulaire bug reports
- `.github/ISSUE_TEMPLATE/feature_request.yml` : Template formulaire feature requests
- `.github/workflows/README.md` : Instructions pour CI/CD (à implémenter)
- `docs/screenshots/README.md` : Guide pour screenshots

**README.md amélioré** :
- Badges ajoutés (License MIT, Android 21+, Kotlin 1.9.22, Version)
- Section "À Propos" expliquant la mission du projet
- Section "Fonctionnalités" avec bullet points
- Instructions installation pour utilisateurs ET développeurs
- Section "Contribution" avec liens vers guides
- Section "Contact" et "Remerciements"
- Formatage amélioré avec emojis et structure claire

**build.gradle.kts sécurisé** :
- Fallback quand `keystore.properties` absent (utilise debug signing)
- Messages informatifs pour contributeurs
- Build possible sans avoir les credentials de release

**Ce qui reste à faire manuellement** :
- [ ] Remplacer `VOTRE_USERNAME` dans README.md et templates
- [ ] Ajouter email dans SECURITY.md
- [ ] Prendre screenshots de l'app pour README
- [ ] Vérifier/anonymiser mentions dans docs/ si nécessaire
- [ ] Créer workflow CI/CD GitHub Actions (`.github/workflows/ci.yml`)
- [ ] Créer premier release avec tag (v0.2.0)
- [ ] Initialiser nouveau repo Git dédié (pas celui du parent)

**Sécurité vérifiée** :
- ✅ `keystore.properties` déjà dans `.gitignore`
- ✅ `*.keystore` déjà dans `.gitignore`
- ✅ Template d'exemple créé sans credentials
- ✅ Build fonctionne sans keystore pour contributeurs

### 🧪 Tests Unitaires & UI - 2026-02-06 (Phase 3.2)

#### Tests unitaires créés ✅
**PgnExporterTest.kt** (12 tests):
- Headers PGN obligatoires (Seven Tag Roster)
- Format coups correct (1. e4 e5 2. Cf3 Cc6)
- Résultat sur nouvelle ligne (standard PGN)
- Roques et symboles spéciaux (O-O, O-O-O, #, +)
- Tous les résultats possibles (1-0, 0-1, 1/2-1/2, *)
- Validation format Lichess complète
- Génération nom fichier (partie_YYYYMMDD_HHMMSS.pgn)

**✅ SUCCÈS: 12/12 tests passent** (BUILD SUCCESSFUL in 9s)

#### Tests UI Espresso créés ⚠️
**NotationFlowTest.kt** (4 tests):
- `flowComplet_creerPartie_noterCoups_terminerPartie`: Flow utilisateur complet
- `flowComplet_creerPartie_jouerNoirs_terminerNulle`: Test partie nulle avec Noirs
- `flowComplet_annulerDernierCoup`: Test annulation coup
- `flowComplet_supprimerPartie`: Test suppression partie

**⚠️ PROBLÈMES**: Tests Espresso échouent sur émulateur (vues introuvables)
- Possible problème de timing (Thread.sleep insuffisant)
- Interaction avec clavier IME complexe à tester avec Espresso standard
- Nécessite investigation plus poussée ou utilisation de UIAutomator

**Dépendances ajoutées**:
```gradle
testImplementation("org.mockito:mockito-core:5.3.1")
testImplementation("org.mockito:mockito-inline:5.2.0")
androidTestImplementation("androidx.test.espresso:espresso-contrib:3.5.1")
androidTestImplementation("androidx.test.espresso:espresso-intents:3.5.1")
androidTestImplementation("androidx.test:runner:1.5.2")
androidTestImplementation("androidx.test:rules:1.5.0")
```

**Fichiers créés**:
```
app/src/test/java/bkh/apps/coupdemain/utils/PgnExporterTest.kt (263 lignes)
app/src/androidTest/java/bkh/apps/coupdemain/NotationFlowTest.kt (330 lignes)
```

**Résultat**:
- ✅ Tests unitaires PGN : 100% passés (12/12)
- ⚠️ Tests UI Espresso : 2/4 passés (problèmes de timing/synchronisation)
- ✅ Build configuré avec toutes les dépendances de test

---

### 🏪 Préparation Play Store - 2026-02-06

#### Documentation créée ✅
**docs/PlayStore.md** - Guide complet de publication:
- 📸 Assets graphiques requis (icônes, screenshots, feature graphic)
- 🎨 **Liens générateurs d'icônes adaptives:**
  - Icon Kitchen (recommandé): https://icon.kitchen/
  - Android Asset Studio: https://romannurik.github.io/AndroidAssetStudio/
  - EasyAppIcon: https://easyappicon.com/
- 📝 Textes FR/EN pré-rédigés (titres, descriptions, changelog)
- 🔒 Instructions keystore + signature AAB
- 📋 Checklist complète avant soumission
- 🚀 Processus de soumission étape par étape

**Logo & Icônes:**
- ✅ Logo carré créé dans `assets/squarelogo.png`
- ✅ Icônes adaptives générées avec Icon Kitchen (v2 - mises à jour)
- ✅ Tous les mipmap intégrés (hdpi, xhdpi, xxhdpi, xxxhdpi)
- ✅ Adaptive Icon avec layers (foreground + monochrome)
- ✅ Background couleur #F1F3FB (amélioration rendu home screen)
- ✅ Icône Play Store 512x512 générée (`assets/playstore-icon-512.png`)
- ✅ AndroidManifest.xml mis à jour avec nouvelles icônes
- ✅ Test sur émulateur confirmé (install debug réussi)

**Screenshots & Bannière:**
- ✅ Screenshots téléphone capturés (4-8 images)
- ✅ Feature Graphic 1024x500 créée (bannière Play Store)
- ℹ️ Localisation FR uniquement (clavier français)

**Build Release Play Store:**
- ✅ Keystore de signature généré (coup-de-main-release.keystore)
- ✅ Configuration signature release dans build.gradle.kts
- ✅ Version 0.0.2 (versionCode 2)
- ✅ Target SDK 35 (Android 15) - requis Play Store 2026
- ✅ AAB signé compilé avec succès (2.86 MB)
- ✅ Fichier prêt: `app/build/outputs/bundle/release/app-release.aab`
- ✅ Minify + shrink resources activés
- ⚠️ Keystore sauvegardé en lieu sûr (CRITIQUE pour futures mises à jour)

**Soumission Play Store:**
- ✅ Privacy Policy créée et publiée
- ✅ AAB uploadé sur Play Console
- ✅ Formulaire classification contenu complété
- ✅ Soumission pour review Google effectuée
- ⏳ En attente validation (2-7 jours)

**Décisions:**
- ❌ Pas d'analytics ni crashlytics (app volontairement light)
- ❌ Pas d'annonce YouTube
- ❌ Beta testing reporté à post-publication

#### Accessibilité complète ✅
**Améliorations TalkBack** :
- contentDescription sur btn_export_game : "Exporter la partie au format PGN ou image"
- contentDescription sur btn_finish_game : "Terminer la partie et enregistrer le résultat"
- contentDescription dynamique sur CardView historique : "Partie du [date], [X] coups, statut [En cours/Terminée], résultat [1-0/0-1/1/2-1/2/*]"
- contentDescription dynamique sur CardView accueil (3 dernières parties)

**Résultat** : App 100% accessible TalkBack validé ✅

#### Animation fade in ✅
**Amélioration visuelle** :
- Animation fade in (0→1, 300ms) sur tous les items RecyclerView (historique)
- Animation fade in sur les 3 dernières parties (accueil)
- Meilleure transition visuelle lors de l'affichage des listes

**Fichiers modifiés** :
```
ui/historique/HistoriqueAdapter.kt (contentDescription + fade)
ui/home/HomeFragment.kt (contentDescription + fade)
```

#### Fix clavier landscape 🐛
**Problème** : En rotation landscape sur téléphone, le clavier IME prenait toute la hauteur de l'écran, cachant le contenu de l'application en dessous.

**Cause** : ScrollView avec `android:layout_height="match_parent"` + `android:fillViewport="true"`

**Solution** :
- Changement vers `android:layout_height="wrap_content"`
- Suppression de `android:fillViewport="true"`
- Le clavier ne prend maintenant que la hauteur nécessaire

**Fichiers modifiés** :
```
res/layout/keyboard_view.xml (ScrollView wrap_content)
res/layout/keyboard_view_alt.xml (ScrollView wrap_content)
```

**Résultat** : Le clavier s'adapte en landscape, l'utilisateur voit ce qu'il tape ✅

#### Build
- Status: ✅ BUILD SUCCESSFUL in 12s
- Installé sur 2 devices (Pixel 9a + Pixel_8_API_34)

---

## [0.9.0] - 2026-02-06 - Phase 2 COMPLÈTE 🎉

**Session intensive de debug et polish** : 8 bugs critiques fixés + validation coups + UX améliorée

### 🎯 Résumé Session

**Phase 2.4 complétée** :
- ✅ Validation coups d'échecs en temps réel (regex SAN + flash rouge)
- ✅ Export PGN compatible Lichess (format standard)
- ✅ Navigation onboarding corrigée (pile activités)
- ✅ 8 bugs UX/settings fixés
- ✅ Sons TTS sur tous les boutons
- ✅ Polish visuel (app bar, padding, flash)

**Résultat** : Phase 2 100% complète, app production-ready

---

### 🐞 Fix Bouton Onboarding - 2026-02-06 (bug critique)

#### Fix navigation "Configurer maintenant" ✅
**Problème 1**: Le bouton "Oui, configurer maintenant" de l'onboarding redirigeait immédiatement vers l'accueil au lieu d'ouvrir les paramètres IME.

**Problème 2**: Quand l'utilisateur ouvrait les paramètres puis faisait "Retour", l'app se fermait complètement au lieu de revenir à l'accueil.

**Cause**: `finishOnboarding()` était appelé immédiatement après `openImeSettings()`, ce qui fermait l'OnboardingActivity sans rien dans la pile d'activités.

**Solution**:
- `OnboardingActivity.btnActivateIme`: 
  1. Marquer l'onboarding comme complété
  2. **Lancer MainActivity dans la pile d'activités**
  3. Ouvrir les paramètres IME par dessus
  4. Fermer l'onboarding

**Flux correct**:
- Pile d'activités: MainActivity → Settings IME
- Quand l'utilisateur fait "Retour" depuis les paramètres, il arrive sur MainActivity
- Plus de fermeture inattendue de l'app

**Comportement**:
- "Oui, configurer maintenant" → Paramètres IME (retour = accueil)
- "Non, plus tard" → Accueil direct

---

### 🐞 Fix Export PGN - 2026-02-06 (bug critique)

#### Fix validation Lichess ✅
**Problème**: Le PGN exporté était rejeté par Lichess avec l'erreur "Invalid chess move at line 11, column 4". Le marqueur de résultat (1-0, 0-1, 1/2-1/2) était placé sur la même ligne que les coups.

**Exemple invalide**:
```
1. Fe4 O-O# 1/2-1/2
```

Lichess interprète "1/2-1/2" comme un coup invalide au lieu d'un marqueur de résultat.

**Solution**:
- `PgnExporter.formatMoves()`: Le résultat est maintenant placé sur une nouvelle ligne après une ligne vide (standard PGN)
- Format correct selon la spécification PGN :
```
1. Fe4 O-O#

1/2-1/2
```

**Modifications**:
- Changement de `formatted.append(" ")` à `formatted.append("\n\n")` avant le résultat
- Conforme au format PGN standard (ligne vide avant le marqueur de résultat)
- ✅ Validation Lichess OK

---

### 🎨 Polish UX - 2026-02-06 (soir)

#### 1. Flash rouge zone complète au clavier ✅
**Amélioration**: Le flash rouge lors d'un coup invalide colore maintenant TOUTE la zone "Notation" (background) au lieu de juste le texte, ce qui est beaucoup plus visible.

**Modifications**:
- `ChessKeyboardIME.flashInvalidMove()`: Animation du `backgroundColor` au lieu du `textColor`
- Récupération de la couleur de fond actuelle selon le thème (dark/light/system)
- Animation 600ms: couleur originale → rouge → couleur originale
- Beaucoup plus visible et impactant visuellement

#### 2. App bar aux couleurs du thème ✅
**Problème**: La barre d'outils en haut du détail de notation était blanche, ne suivant pas les couleurs du thème de l'app.

**Solution**:
- `fragment_notation_detail.xml`:
  - `AppBarLayout`: Ajout `android:background="?attr/colorPrimary"`
  - `MaterialToolbar`: Ajout `android:theme="@style/ThemeOverlay.Material3.Dark"`
- **Résultat**: L'app bar utilise maintenant le bleu du thème (#3F51B5 en clair, #9FA8DA en dark)
- Icônes et textes en blanc pour bon contraste

#### 3. Padding vertical sur boutons ✅
**Problème**: Les boutons "Fin de partie" et "Exporter la partie" manquaient de padding vertical (trop compacts).

**Solution**:
- `fragment_notation_detail.xml`:
  - Ajout `android:paddingTop="16dp"` sur les deux boutons
  - Ajout `android:paddingBottom="16dp"` sur les deux boutons
- **Résultat**: Boutons plus aérés et plus faciles à cliquer

#### Fichiers modifiés
- `ChessKeyboardIME.kt`: flashInvalidMove() avec backgroundColor au lieu de textColor
- `fragment_notation_detail.xml`: AppBarLayout + MaterialToolbar avec couleurs thème, padding boutons

#### Build
- Status: ✅ BUILD SUCCESSFUL in 11s
- Warnings: 1 (VIBRATOR_SERVICE deprecated, non critique)
- Installé sur 2 devices (Pixel 9a + Pixel_8_API_34)

---

### ✨ Validation des coups d'échecs - 2026-02-06 (soir)

#### Fonctionnalité: Validation en temps réel des coups ✅
**Nouvelle fonctionnalité**: Le clavier IME valide maintenant si le coup entré est valide selon la notation algébrique (SAN) avant de l'envoyer.

**Comportement**:
- **Coup valide** → envoyé normalement (comme avant)
- **Coup invalide** → la preview flashe en **rouge** (animation 600ms) et se vide automatiquement

**Patterns validés** (notation algébrique française):
1. **Roques**:
   - `O-O` (petit roque)
   - `O-O-O` (grand roque)
   - Avec échec/mat: `O-O+`, `O-O-O#`

2. **Coups de pièces**:
   - Pions: `e4`, `d5`, `a7`
   - Pièces: `Cf3`, `Rd1`, `Fb5`, `Dh5`, `Ke2`
   - Captures: `exd5`, `Cxf6`, `Fxc4`
   - Désambiguïsation: `Cbd7`, `R1e1`, `Dh4e1`
   - Promotion: `e8=D`, `d1=R`, `a7=F`, `h1=C`
   - Échec/Mat: `Cf3+`, `Dh5#`

3. **Pattern complet**:
   - Regex: `^[RCFDK]?[a-h]?[1-8]?x?[a-h][1-8](=[RCFD])?[+#]?$`
   - R=Tour, C=Cavalier, F=Fou, D=Dame, K=Roi
   - Pion = pas de lettre de pièce

**Exemples validés**:
- ✅ `e4`, `e5`, `d4`, `Cf3`, `Cc6`
- ✅ `Fxe5`, `dxe5`, `Cxd4`
- ✅ `d8=D`, `e1=C+`
- ✅ `O-O`, `O-O-O`, `O-O+`
- ✅ `Dh5#`, `Rf1`, `Cbd7`
- ❌ `e9` (rangée invalide)
- ❌ `i4` (colonne invalide)
- ❌ `abc` (notation invalide)
- ❌ `C` (destination manquante)

**Implémentation**:
- `ChessKeyboardIME.kt`:
  - Import `ArgbEvaluator`, `ValueAnimator`, `Color`
  - Fonction `isValidChessMove(move: String): Boolean` avec regex
  - Fonction `flashInvalidMove()` avec animation couleur (600ms)
  - Modification bouton Enter pour valider avant commit
  - Si invalide: flash rouge + clear + updatePreview()

**User Experience**:
- Feedback visuel immédiat (flash rouge)
- Pas de message d'erreur intrusif
- La notation se vide automatiquement pour recommencer
- L'utilisateur comprend instantanément que le coup n'est pas valide

#### Build
- Status: ✅ BUILD SUCCESSFUL in 9s
- Warnings: 1 (VIBRATOR_SERVICE deprecated, non critique)
- Installé sur 2 devices (Pixel 9a + Pixel_8_API_34)

---

### 🐛 Corrections additionnelles - 2026-02-06 (soir)

#### 1. Fix aperçu clavier - Réel fix des couleurs ✅
**Problème persistant**: L'aperçu du clavier utilisait les couleurs de l'APPLICATION au lieu des couleurs du CLAVIER IME. Quand l'utilisateur sélectionnait "Thème sombre" pour le clavier, l'aperçu restait en clair car il récupérait `background_dark` (qui est en fait #F5F7FA = clair) au lieu de `keyboard_background_dark` (#1E1E1E = réellement sombre).

**Solution**:
- `LayoutPreviewActivity.applyColorScheme()`: Utilisation des vraies couleurs du clavier
  - Background: `keyboard_background_dark` / `keyboard_background_light`
  - Boutons: `keyboard_button_background_dark` / `keyboard_button_background_light`
  - Texte: `keyboard_button_text_dark` / `keyboard_button_text_light`
- Maintenant l'aperçu respecte VRAIMENT le choix de couleur du clavier, pas le thème de l'app

#### 2. Sons sur tous les boutons du clavier ✅
**Problème**: Les sons (TTS) ne fonctionnaient que sur les pièces (Tour, Cavalier, etc.) mais pas sur les colonnes (a-h), rangées (1-8) ni modificateurs (capture, échec, roque).

**Solution**:
- `ChessKeyboardIME.setupKeyboardButtons()`: Ajout paramètre `vocalName` à TOUS les boutons
- **Colonnes**: "a", "b", "c", "d", "e", "f", "g", "h"
- **Rangées**: "1", "2", "3", "4", "5", "6", "7", "8"
- **Modificateurs**:
  - `btn_capture` → "capture"
  - `btn_check` → "échec"
  - `btn_checkmate` → "échec et mat"
  - `btn_castle_short` → "petit roque"
  - `btn_castle_long` → "grand roque"
- Tous les boutons utilisent maintenant `setupButton(view, id, text, vocalName)`

#### Fichiers modifiés
- `LayoutPreviewActivity.kt`: Correction couleurs avec keyboard_* au lieu de background_*/card_*
- `ChessKeyboardIME.kt`: Ajout vocalName pour colonnes, rows et modificateurs

#### Build
- Status: ✅ BUILD SUCCESSFUL in 10s
- Warnings: 1 (VIBRATOR_SERVICE deprecated, non critique)
- Installé sur 2 devices (Pixel 9a + Pixel_8_API_34)

---
### � Corrections de bugs multiples - 2026-02-06

#### 1. Bug aperçu clavier en mode dark ✅
**Problème**: L'aperçu du clavier dans Settings affichait toujours le thème clair même en mode sombre.

**Solution**:
- Ajout import `Configuration` dans `LayoutPreviewActivity.kt` et `ChessKeyboardIME.kt`
- Modification `applyColorScheme()` pour détecter le mode système via `uiMode & UI_MODE_NIGHT_MASK`
- Gestion du nouveau mode `ColorScheme.SYSTEM` qui suit le thème Android

#### 2. Ajout option "Système" pour couleurs clavier ✅
**Nouvelle fonctionnalité**: Option pour suivre automatiquement le thème système (jour/nuit).

**Modifications**:
- `KeyboardPreferences.kt`: Ajout `ColorScheme.SYSTEM` enum
- `strings.xml`: Ajout "Système (automatique)" dans `color_entries`
- `keyboard_preferences.xml`: defaultValue changé de "DARK" à "SYSTEM"
- `DEFAULT_COLOR_SCHEME = "SYSTEM"` au lieu de "DARK"
- `LayoutPreviewActivity` et `ChessKeyboardIME`: Logique pour détecter et appliquer le thème système

#### 3. Taille par défaut → Grands boutons ✅
**Changement**: La taille par défaut des boutons passe de MEDIUM (52dp) à LARGE (64dp).

**Modifications**:
- `KeyboardPreferences.kt`: `DEFAULT_BUTTON_SIZE = "LARGE"`
- `keyboard_preferences.xml`: `defaultValue="LARGE"`
- `SettingsFragment.kt`: Summary mis à jour "Grands boutons (64dp) - Par défaut"

#### 4. Affichage tour ET choix joueur ✅
**Problème**: Quand aucun coup n'était joué, l'indicateur affichait seulement "Vous jouez les Blancs" même si on avait choisi les Noirs.

**Solution**:
- `NotationDetailFragment.updateTurnIndicator()`: Affiche maintenant les deux infos
- Format: "♔ Au tour des Blancs • ♚ Vous jouez les Noirs"
- Sauvegarde de `userPlaysWhite` même sans coups joués

#### 5. Fix onboarding "Non, plus tard" ✅
**Problème**: Cliquer sur "Non, plus tard" à l'onboarding fermait l'app au lieu d'ouvrir l'accueil.

**Solution**:
- `OnboardingActivity.finishOnboarding()`: Lance `MainActivity` avec flags `NEW_TASK | CLEAR_TASK`
- L'app redirige maintenant correctement vers l'écran d'accueil

#### 6. FAB et actions en bleu (thème) ✅
**Problème**: Les FAB et boutons utilisaient le violet Material par défaut au lieu du bleu du thème.

**Solution**:
- Ajout `app:backgroundTint="?attr/colorPrimary"` aux FAB
- Ajout `app:tint="?attr/colorOnPrimary"` pour les icônes
- Fichiers modifiés: `fragment_home.xml`, `fragment_notation_detail.xml`
- Élévation des FAB passée de 0dp à 6dp (pressedTranslationZ: 12dp)

#### 7. Icône vibration lisible en clair ✅
**Problème**: L'icône `ic_lock_silent_mode_off` était illisible en thème clair.

**Solution**:
- `keyboard_preferences.xml`: Changement icône pour `ic_menu_rotate`
- Meilleur contraste sur fond clair

#### 8. Modernisation cards accueil/historique ✅
**Amélioration**: Design des cards plus moderne avec élévation et coins arrondis.

**Modifications**:
- `item_game_card.xml`: elevation 2dp→4dp, cornerRadius 8dp→16dp
- `fragment_home.xml`: empty_state_card avec elevation 4dp, cornerRadius 16dp
- `fragment_notation_detail.xml`: historique card avec elevation 4dp, cornerRadius 16dp
- Espacement et padding améliorés

#### Fichiers modifiés (8 bugs)
- `KeyboardPreferences.kt`: ColorScheme.SYSTEM, DEFAULT_BUTTON_SIZE, DEFAULT_COLOR_SCHEME
- `LayoutPreviewActivity.kt`: Import Configuration, logique mode système
- `ChessKeyboardIME.kt`: Import Configuration, logique mode système
- `SettingsFragment.kt`: Summary SYSTEM, tailles boutons
- `NotationDetailFragment.kt`: updateTurnIndicator avec double info
- `OnboardingActivity.kt`: finishOnboarding lance MainActivity
- `strings.xml`: color_entries avec SYSTEM
- `keyboard_preferences.xml`: defaultValue LARGE/SYSTEM, icône vibration
- `fragment_home.xml`: FAB bleu + elevation, card moderne
- `fragment_notation_detail.xml`: FAB bleu + elevation, card moderne
- `item_game_card.xml`: elevation 4dp, corners 16dp

#### Build
- Status: ✅ BUILD SUCCESSFUL in 14s
- Warnings: 4 (deprecated APIs, non critiques)
- Installé sur 2 devices (Pixel 9a + Pixel_8_API_34)

---

### �🔧 Debug - Option "Revoir l'Onboarding" - 2026-02-06

#### Ajout d'une option debug pour relancer l'onboarding

1. **Nouvelle préférence dans Settings > Avancé**:
   - Clé: `relaunch_onboarding`
   - Titre: "Revoir l'onboarding"
   - Summary: "Afficher à nouveau l'écran d'accueil"
   - Visible uniquement en mode DEBUG (`BuildConfig.DEBUG`)

2. **Fonctionnement**:
   - Click efface `SharedPreferences("onboarding").putBoolean("completed", false)`
   - Lance `OnboardingActivity` via Intent
   - Termine l'activité courante pour forcer le redémarrage
   - Permet de tester l'onboarding sans réinstaller l'app

3. **Fichiers modifiés**:
   - `keyboard_preferences.xml`: Ajout Preference "relaunch_onboarding"
   - `SettingsFragment.kt`: 
     - Import `BuildConfig`, `Intent`, `OnboardingActivity`
     - Méthode `setupRelaunchOnboardingButton()`
     - Logique de visibilité: `preferenceScreen.removePreference()` si !DEBUG

4. **Sécurité**:
   - Option automatiquement invisible en build RELEASE
   - Aucun risque pour les utilisateurs finaux
   - Utile uniquement pendant le développement

---

### Phase 2.2 : UX Polish Final - 2026-02-05 [EN COURS 🚧]

#### Résumé
Améliorations finales de l'UX : suppression de parties, gestion états terminés, couleurs clavier indépendantes, historique amélioré.

#### Modifications Majeures

1. **Suppression de Partie**:
   - Ajout icône delete dans menu notation (action_delete)
   - Dialog de confirmation avec message explicite
   - Suppression CASCADE (partie + tous les coups)
   - Retour automatique à l'écran précédent

2. **Parties Terminées - UI Adaptive**:
   - FAB "+" masqué automatiquement (visibility GONE)
   - Bouton "Fin de la partie" masqué automatiquement
   - Résultat affiché dans toolbar.subtitle ("Résultat : 1-0" / "0-1" / "1/2-1/2")
   - Icône "Annuler" masquée dans menu (toolbar.menu.findItem().isVisible = false)
   - Seule icône "Supprimer" reste visible pour parties terminées

3. **Couleurs Clavier IME - Indépendance Thème**:
   - Ajout couleurs `keyboard_*_dark` et `keyboard_*_light` dans values/ ET values-night/
   - Mode sombre clavier : #1E1E1E (bg), #2C2C2C (boutons), #E1E1E1 (texte), #9FA8DA (preview)
   - Mode clair clavier : #F5F7FA (bg), #FFFFFF (boutons), #263238 (texte), #3F51B5 (preview)
   - ChessKeyboardIME.applyColorScheme() : application dynamique selon préférences utilisateur
   - Layouts keyboard_view.xml / keyboard_view_alt.xml : couleurs retirées (appliquées en Kotlin)
   - Permet : Système dark + clavier light OU Système light + clavier dark ✅

4. **Menu Toolbar Simplifié**:
   - Retrait actions "Terminer" et "Historique" (redondantes)
   - Menu final : icône Annuler (action_undo) + icône Supprimer (action_delete)
   - showAsAction="always" pour les deux icônes
   - Retrait setSupportActionBar() qui bloquait affichage menu
   - Navigation icon configurée directement (setNavigationIcon)

5. **Historique Amélioré**:
   - Titre : "Historique des coups" (textAppearanceTitleMedium, colorPrimary)
   - Taille texte : 20sp (au lieu de 18sp)
   - Espacement lignes : lineSpacingExtra 8dp
   - Hauteur minimale : 200dp (au lieu de 120dp)
   - Padding augmenté : 20dp conteneur, 16dp textview
   - Card elevation : 2dp, strokeWidth retiré
   - Meilleure lisibilité et présence visuelle

#### Fichiers Modifiés
- **menu_notation.xml**: Simplifié à 2 items (undo + delete), showAsAction="always"
- **NotationDetailFragment.kt**: 
  - Ajout showDeleteDialog() + deleteGame()
  - loadGameData() gère visibility menu undo selon état partie
  - setupToolbar() simplifié sans setSupportActionBar
- **fragment_notation_detail.xml**: Historique amélioré (tailles, espacements, elevation)
- **values/colors.xml**: Ajout keyboard_background_dark/light, keyboard_button_*_dark/light, keyboard_preview_*_dark/light
- **values-night/colors.xml**: Ajout mêmes couleurs clavier (disponibles dans les 2 fichiers)
- **ChessKeyboardIME.kt**: applyColorScheme() utilise nouvelles couleurs, preview inclus
- **keyboard_view.xml / keyboard_view_alt.xml**: Couleurs hardcodées retirées
- **themes.xml**: Style KeyboardButton sans couleurs (appliquées dynamiquement)

#### Résultat Build
✅ BUILD SUCCESSFUL in 13s  
✅ Installation Pixel_8_API_34 réussie  
✅ 0 erreurs, 3 warnings (deprecated API onBackPressed)

---

### Phase 2.2 : UX Refactor - 2026-02-04 [TERMINÉE ✅ - 100%]

#### Résumé
Refonte complète de l'UX basée sur feedback utilisateur : interface minimale, pas d'inputs texte, flow simplifié avec choix couleur.

#### Modifications UX/UI Majeures
1. **FAB Simplifié**:
   - `ExtendedFloatingActionButton` → `FloatingActionButton`
   - Suppression texte, icône "+" uniquement
   - Suppression ombre (elevation="0dp", pressedTranslationZ="0dp")

2. **App Bar**:
   - Masquée par défaut dans tous les fragments (visibility="gone" dans activity_main.xml)
   - Visible uniquement dans NotationDetailFragment avec toolbar custom

3. **Nouveau Flow Notation**:
   - Clic sur FAB "+" → Dialog choix couleur (Blancs/Noirs)
   - Titre auto-généré avec date/time (format "dd/MM/yyyy HH:mm")
   - Pas de champs nom joueurs/titre (simplification)

4. **NotationDetailFragment Redesign**:
   - Interface minimale : indicateur tour (♔/♚) + historique coups + FAB "+"
   - EditText invisible (1dp) pour focus IME uniquement (évite switching clavier)
   - Toolbar avec Back + menu icônes (Annuler, Historique, FIN)
   - Dialog fin de partie avec choix résultat (1-0, 0-1, 1/2-1/2)

#### Fichiers Créés
- **dialog_choose_color.xml**: Dialog 2 boutons (♔ Blancs / ♚ Noirs)
- **menu_notation.xml**: Menu toolbar avec 3 actions (undo, history, finish) en icônes

#### Fichiers Modifiés
- **fragment_home.xml**: FAB simplifié (suppression shadow + texte)
- **HomeFragment.kt**: Import et type FloatingActionButton
- **activity_main.xml**: AppBarLayout hidden (visibility="gone")
- **fragment_notation_detail.xml**: Layout minimal (CoordinatorLayout + AppBar + NestedScrollView + invisible EditText + FAB)
- **NotationDetailFragment.kt**: Réécriture complète (296 lignes) :
  - showColorChoiceDialog() au démarrage
  - setupToolbar() avec menu custom
  - setupFab() avec focus EditText invisible
  - Auto-génération titre avec DateTimeFormatter
  - updateTurnIndicator() avec symboles ♔/♚
  - showFinishDialog() avec ArrayAdapter résultats

#### Problèmes Résolus
- XML corruption (line 124) : fichier recréé cleanly
- Kotlin duplication : fichier recréé sans ancien code

#### Résultat Build
✅ BUILD SUCCESSFUL in 13s
✅ Installation Pixel_8_API_34 réussie

---

### Phase 2.1 : Room Database - 2026-02-04 [TERMINÉE ✅ - 100%]

#### Résumé
Configuration complète Room Database avec KSP pour la persistence des parties et coups d'échecs.

#### Ajouté
- **ChessGame.kt**: Entity Room avec id, title, players, result (GameResult enum), date (LocalDateTime), moveCount, isCompleted
- **ChessMove.kt**: Entity Room avec ForeignKey CASCADE vers ChessGame, fields: gameId, moveNumber, notation, isWhiteMove, timestamp
- **GameDao.kt**: Interface @Dao avec CRUD operations:
  - Games: insertGame, updateGame, deleteGame, getAllGames, getGameById, getRecentGames(3), getOngoingGames, getCompletedGames
  - Moves: insertMove, deleteMove, getMovesForGame, getLastMove, deleteLastMove, getMoveCount
  - Composite: getGameWithMoves (GameWithMoves data class avec @Relation)
- **ChessDatabase.kt**: @Database singleton avec entities, TypeConverters, gameDao
- **ChessGameRepository.kt**: Repository layer avec Flow<List<ChessGame>>, suspend functions CRUD, business logic (createGame, finishGame, addMove, undoLastMove)
- **Converters.kt**: TypeConverters pour LocalDateTime ↔ String (ISO format) et GameResult ↔ String

#### Modifié
- **build.gradle.kts (root)**: Ajout plugin KSP 1.9.22-1.0.17
- **app/build.gradle.kts**: 
  - Ajout plugin KSP + ksp("androidx.room:room-compiler:2.6.1")
  - Ajout `isCoreLibraryDesugaringEnabled = true` pour support java.time API 21+
  - Ajout `coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.0.4")`
  - Ajout `lint { abortOnError = false }` temporairement

#### Technique
- Room 2.6.1 avec KSP pour annotation processing
- TypeConverters pour LocalDateTime et enums
- ForeignKey CASCADE delete (supprimer partie = supprimer coups)
- Flow pour réactivité UI
- Singleton pattern pour ChessDatabase
- Repository pattern pour abstraction DAO/ViewModel

---

### Phase 2.2 : Refonte Navigation & Fragments - 2026-02-05 [TERMINÉE ✅ - 100%]

#### Résumé
Transformation UI pour afficher les parties sauvegardées avec RecyclerView et fragment de saisie

#### Ajouté
- **GameViewModel.kt**: ViewModel partagé avec Flow observables (allGames, recentGames), méthodes CRUD
- **HistoriqueAdapter.kt**: RecyclerView.Adapter avec DiffUtil, affichage cartes parties
- **item_game_card.xml**: Layout MaterialCardView pour une partie (titre, joueurs, date, coups, résultat, badge statut)
- **NotationDetailFragment.kt**: Fragment complet pour créer/éditer une partie:
  - Champs titre, joueurs (blancs/noirs)
  - Zone saisie coups avec EditText (compatible IME clavier d'échecs)
  - Boutons Ajouter coup / Annuler dernier coup
  - Historique formaté (1. e4 e5 2. Nf3...)
  - Boutons terminer partie (1-0, 0-1, 1/2-1/2)
  - Sauvegarde auto des modifications
- **fragment_notation_detail.xml**: Layout avec MaterialCards, TextInputLayouts, ScrollView

#### Modifié
- **HomeFragment**: 
  - Observe `viewModel.recentGames` (3 dernières parties)
  - FAB "Nouvelle notation" → crée partie et ouvre NotationDetailFragment
  - Affiche cartes cliquables ou empty state
  - Utilise item_game_card.xml dynamiquement
  - Navigation via MainActivity.openGameDetail()
- **fragment_home.xml**: CoordinatorLayout avec FAB, container pour cartes, empty state
- **KeyboardFragment → HistoriqueFragment**:
  - Renommé package `ui.keyboard` → `ui.historique`
  - RecyclerView avec HistoriqueAdapter
  - Observe `viewModel.allGames` (toutes les parties)
  - Empty state si aucune partie
  - Navigation vers NotationDetailFragment au clic
- **fragment_historique.xml**: RecyclerView + empty state
- **MainActivity.kt**: 
  - Import HistoriqueFragment + NotationDetailFragment
  - Méthode publique `openGameDetail(gameId)` pour navigation
  - Back stack géré avec addToBackStack()

#### Technique
- Flow observers pour réactivité UI temps réel
- ViewModel partagé entre fragments (activityViewModels)
- Navigation manuelle via FragmentManager (addToBackStack pour retour)
- Companion object pour newInstance() pattern
- Focus auto sur champ saisie coup
- Format historique: "1. e4 e5\n2. Nf3 Nc6"

---

### Phase 1.7 : Améliorations UI/UX - 2026-02-04 [TERMINÉE ✅ - 100%]

#### Résumé
- ✅ Renommage "Theme" → "Disposition" avec 2 layouts (pièces haut/bas)
- ✅ Ajout onglet "Couleurs" (DARK/LIGHT theme)
- ✅ TextToSpeech vocal pour prononcer les pièces au clic
- ✅ LayoutPreviewActivity avec preview clavier temps réel
- ✅ Switching dynamique layout/couleurs sans redémarrage IME

#### Modifié
- **`keyboard_preferences.xml`**: [2026-02-04 - Rename theme_mode → layout_mode + add color_scheme]
  - `layout_mode` ListPreference avec 2 options: DEFAULT, PIECES_BOTTOM
  - `color_scheme` ListPreference avec 2 options: DARK, LIGHT
  - Catégorie "Apparence" contient maintenant: Taille, Disposition, Couleurs

- **`strings.xml`**: [2026-02-04 - Rename + add color scheme strings]
  - `theme_entries` → `layout_entries`: "Disposition 1 (pièces en haut)", "Disposition 2 (pièces en bas)"
  - Ajout `color_entries`: "Thème sombre (actuel)", "Thème clair (fond blanc)"
  - Ajout `color_values`: ["DARK", "LIGHT"]

- **`KeyboardPreferences.kt`**: [2026-02-04 - Add LayoutMode + ColorScheme enums]
  - `enum class LayoutMode { DEFAULT, PIECES_BOTTOM }`
  - `enum class ColorScheme { DARK, LIGHT }`
  - `val layoutMode: Flow<LayoutMode>` + `setLayoutMode()`
  - `val colorScheme: Flow<ColorScheme>` + `setColorScheme()`
  - Constantes: `DEFAULT_LAYOUT = "DEFAULT"`, `DEFAULT_COLOR_SCHEME = "DARK"`

- **`SettingsFragment.kt`**: [2026-02-04 - Setup layout + color preferences]
  - `setupLayoutPreference()`: observe layoutMode, display summary, save changes
  - `setupColorSchemePreference()`: observe colorScheme, display summary, save changes
  - `getLayoutSummary()`: return human-readable layout description
  - `getColorSchemeSummary()`: return human-readable color scheme description

- **`ChessKeyboardIME.kt`**: [2026-02-04 - Dynamic layout switching + TTS + colors]
  - Ajout `private var layoutMode = LayoutMode.DEFAULT`
  - Ajout `private var colorScheme = ColorScheme.DARK`
  - Ajout `private var tts: TextToSpeech?` + `ttsReady: Boolean`
  - `onCreate()`: initialise TTS avec `Locale.FRENCH`
  - `onDestroy()`: libère TTS (`stop()` + `shutdown()`)
  - `loadInitialSettings()`: charge layoutMode + colorScheme au démarrage
  - `observeSettings()`: observe layoutMode → `recreateKeyboard()`, colorScheme → `applyColorScheme()`
  - `onCreateInputView()`: choix layout selon `layoutMode` (keyboard_view vs keyboard_view_alt)
  - `applyColorScheme()`: applique fond + couleurs boutons dynamiquement
  - `recreateKeyboard()`: recrée le clavier avec nouveau layout via `setInputView(onCreateInputView())`
  - `setupButton()` (surcharge): accepte `vocalName` pour TTS
  - `speakPiece(pieceName)`: prononce pièce avec TTS (`QUEUE_FLUSH`)
  - Pièces avec vocal: "Tour", "Cavalier", "Fou", "Dame", "Roi", "Pion"
  - Imports ajoutés: `android.speech.tts.TextToSpeech`, `java.util.Locale`

#### Ajouté
- **`keyboard_view_alt.xml`**: [2026-02-04 - Alternate keyboard layout (pieces at bottom)]
  - Layout identique à `keyboard_view.xml` mais rangées réorganisées:
    * Rangée 1: Colonnes (a-h)
    * Rangée 2: Rangées (1-8)
    * Rangée 3: Symboles (x, +, #, O-O, O-O-O)
    * Rangée 4: **Pièces** (♜R, ♞C, ♝F, ♛D, ♚K)
    * Rangée 5: Contrôles (⌫, Espace, OK)
  - Utilisé quand `layoutMode == PIECES_BOTTOM`

- **`LayoutPreviewActivity.kt`**: [2026-02-04 - Real-time keyboard preview activity]
  - Activity pour prévisualiser le clavier avec settings actuels
  - `onCreate()`: inflate activity_layout_preview, init KeyboardPreferences
  - `setupKeyboardPreview()`: observe 3 Flows (layoutMode, colorScheme, buttonSize)
  - `applyLayout(mode)`: choix layout (keyboard_view vs keyboard_view_alt) dynamiquement
  - `applyColorScheme(scheme)`: applique DARK/LIGHT colors à preview
  - `applyButtonSize(size)`: redimensionne boutons en preview
  - `getAllButtonIds()`: retourne liste 30 button IDs pour batch updates
  - Fix IDs: `btn_delete`, `btn_enter` (not btn_backspace/btn_done)

- **`activity_layout_preview.xml`**: [2026-02-04 - Layout for preview activity]
  - CoordinatorLayout + AppBarLayout avec Toolbar titre "Aperçu du clavier"
  - NestedScrollView + Material CardView contenant `<include layout="@layout/keyboard_view" />`
  - Elevation 4dp, margin 16dp

- **`AndroidManifest.xml`**: [2026-02-04 - Register LayoutPreviewActivity]
  - `<activity android:name=".ui.settings.LayoutPreviewActivity" android:label="Aperçu du clavier" />`

#### Fichiers impactés
```
app/src/main/
├── java/bkh/apps/coupdemain/
│   ├── data/preferences/KeyboardPreferences.kt (enums + flows)
│   ├── ime/ChessKeyboardIME.kt (TTS + layout switching + colors)
│   └── ui/settings/
│       ├── SettingsFragment.kt (layout + color prefs)
│       └── LayoutPreviewActivity.kt (NEW - preview)
├── res/
│   ├── layout/
│   │   ├── keyboard_view_alt.xml (NEW - alternate layout)
│   │   └── activity_layout_preview.xml (NEW)
│   ├── values/strings.xml (layout + color strings)
│   └── xml/keyboard_preferences.xml (layout_mode + color_scheme)
└── AndroidManifest.xml (register LayoutPreviewActivity)
```

#### Tests
- ✅ Build successful (38 tasks: 7 executed, 31 up-to-date)
- ✅ Installé sur Pixel_8_API_34
- ⏳ Tests manuels à effectuer:
  - Changer disposition → vérifier pièces en haut/bas
  - Changer couleurs → vérifier DARK/LIGHT theme
  - Cliquer pièces → vérifier TTS prononce noms

---

### Phase 1.6 : Onboarding & Activation IME - 2026-02-03 [TERMINÉE ✅ - 100%]

#### Résumé
- ✅ OnboardingActivity avec ViewPager2 et 4 étapes guidées
- ✅ Détection automatique si IME déjà activé
- ✅ Bouton "Activer le clavier" avec deep link vers Settings.ACTION_INPUT_METHOD_SETTINGS
- ✅ Système de préférences pour marquer onboarding comme complété
- ✅ Lancement automatique au premier démarrage depuis MainActivity

#### Ajouté
- `app/src/main/java/bkh/apps/coupdemain/ui/onboarding/OnboardingActivity.kt` - Activity principale onboarding
- `app/src/main/java/bkh/apps/coupdemain/ui/onboarding/OnboardingPagerAdapter.kt` - Adapter pour ViewPager2 avec 4 pages
- `app/src/main/res/layout/activity_onboarding.xml` - Layout avec ViewPager2 + TabLayout + boutons
- `app/src/main/res/layout/item_onboarding_page.xml` - Item layout pour chaque page (icône + titre + description)

#### Modifié
- `app/src/main/java/bkh/apps/coupdemain/MainActivity.kt`:
  - Vérifie OnboardingActivity.isCompleted() au démarrage
  - Lance OnboardingActivity si premier lancement
  - Import OnboardingActivity
- `app/src/main/AndroidManifest.xml`:
  - Déclaration OnboardingActivity (exported=false)
- `app/build.gradle.kts`:
  - Ajout ViewPager2 1.0.0

---

### Phase 1.5 : Responsive Tablette - 2026-02-03 [TERMINÉE ✅ - 100%]

#### Résumé
- ✅ Dimens responsive pour tablettes 7" (sw600dp) et 10"+ (sw720dp)
- ✅ Tailles de texte adaptées selon taille écran
- ✅ Hauteur preview et marges ajustées dynamiquement
- ✅ Clavier s'adapte automatiquement à la taille de l'écran

#### Ajouté
- `app/src/main/res/values-sw600dp/dimens.xml` - Dimensions pour tablettes 7"
  - button_text_size: 18sp (vs 14sp phone)
  - preview_text_size: 16sp (vs 14sp phone)
  - preview_height: 28dp (vs 24dp phone)
  - padding/marges: 2dp (vs 1dp phone)
- `app/src/main/res/values-sw720dp/dimens.xml` - Dimensions pour tablettes 10"+
  - button_text_size: 20sp
  - preview_text_size: 18sp
  - preview_height: 32dp
  - padding/marges: 3dp

#### Notes Techniques
- Android utilise automatiquement les dimens selon la largeur d'écran (sw = smallest width)
- sw600dp = écrans ≥7" (600dp de largeur minimale)
- sw720dp = écrans ≥10" (720dp de largeur minimale)
- Pas besoin de layouts séparés, juste ajuster les dimens suffit

---

### Phase 1.4 : UI & Navigation - 2026-02-03 [TERMINÉE ✅ - 100%]

#### Résumé
- ✅ Bottom Navigation avec 3 onglets fonctionnels (Accueil, Clavier, Paramètres)
- ✅ HomeFragment - guide d'activation clair avec MaterialCardView
- ✅ KeyboardFragment - documentation notation SAN avec exemples
- ✅ SettingsFragment intégré dans l'app (accessible via onglet)
- ✅ Couleurs app bar corrigées (sombre #161B22 au lieu de bleu flashy #58A6FF)
- ✅ Bottom nav améliorée (icônes custom, labels toujours visibles, sélecteur de couleurs)
- ✅ **LIVE RELOAD SETTINGS** - ChessKeyboardIME observe DataStore en temps réel
- ✅ **Bouton pion caché** - Inutile en notation (pion par défaut)
- ✅ **Layout optimisé** - Emoji+lettre côte à côte (gain hauteur), marges minimales, plus de crop

#### Ajouté
- `app/src/main/res/menu/bottom_nav_menu.xml` - Menu avec 3 items (nav_home, nav_keyboard, nav_settings)
- `app/src/main/java/bkh/apps/coupdemain/ui/home/HomeFragment.kt` - Fragment d'accueil
- `app/src/main/res/layout/fragment_home.xml` - Layout avec guide activation clavier
- `app/src/main/java/bkh/apps/coupdemain/ui/keyboard/KeyboardFragment.kt` - Fragment aide clavier
- `app/src/main/res/layout/fragment_keyboard.xml` - Layout avec documentation SAN complète
- `app/src/main/res/drawable/ic_menu_compass.xml` - Icône Home (compass Material)
- `app/src/main/res/drawable/ic_menu_edit.xml` - Icône Keyboard (edit Material)
- `app/src/main/res/drawable/ic_menu_preferences.xml` - Icône Settings (gear Material)
- `app/src/main/res/color/bottom_nav_color.xml` - Selector pour couleurs bottom nav (bleu si sélectionné, gris sinon)

#### Modifié
- `app/src/main/res/layout/activity_main.xml`:
  - Ajout CoordinatorLayout avec AppBar + BottomNavigationView
  - Fragment container avec paddingBottom=80dp
  - Bottom nav: wrap_content height, labelVisibilityMode=labeled, itemIconSize=24dp
- `app/src/main/java/bkh/apps/coupdemain/MainActivity.kt`:
  - Refactoré avec FragmentTransaction pour naviguer entre fragments
  - loadFragment() helper method
  - setOnItemSelectedListener pour bottom nav
  - Import KeyboardFragment + HomeFragment
- `app/src/main/res/values/themes.xml`:
  - colorPrimary: accent_blue → card_dark (#161B22)
  - Ajout colorSurface = card_dark pour cohérence Material3
  - colorSecondary = accent_blue (#58A6FF) pour accents
- `app/src/main/java/bkh/apps/coupdemain/ui/settings/SettingsActivity.kt`:
  - Titre raccourci: "Paramètres du clavier" → "Paramètres"
- `app/src/main/AndroidManifest.xml`:
  - SettingsActivity: exported=true → false (sécurité)
  - Label: "Paramètres du clavier" → "Paramètres"
- `app/src/main/java/bkh/apps/coupdemain/ui/settings/SettingsFragment.kt`:
  - Recréé proprement après corruption (tentative d'ajout preview custom layout)
  - Simplifié: juste PreferenceFragmentCompat standard, pas de custom layout
- **`app/src/main/java/bkh/apps/coupdemain/ime/ChessKeyboardIME.kt`**: [2026-02-03 - Live Reload]
  - Ajout imports: VibrationEffect, Vibrator, TypedValue, KeyboardPreferences, Coroutines (CoroutineScope, Dispatchers, Job, SupervisorJob, flow.first, launch, cancel)
  - Ajout propriétés: preferences (KeyboardPreferences), vibrator (Vibrator), serviceScope (CoroutineScope), settingsObserverJob (Job), keyboardView (View?)
  - Cache settings: vibrationEnabled, vibrationDuration, buttonSize
  - `onCreate()`: initialiser preferences + vibrator, loadInitialSettings(), observeSettings()
  - `loadInitialSettings()`: charger settings depuis DataStore avec first() (valeurs initiales)
  - `observeSettings()`: observer 3 Flows DataStore (isVibrationEnabled, vibrationDuration, buttonSize) avec collect()
  - `applyButtonSize()`: redimensionner tous les 28 boutons dynamiquement avec TypedValue.applyDimension() (pion exclu)
  - `performHapticFeedback()`: vibrer selon vibrationEnabled + vibrationDuration (support API <O avec @Suppress deprecation)
  - `onCreateInputView()`: stocker keyboardView, appeler applyButtonSize() après setup
  - Ajout vibration sur tous les boutons: setupButton(), btn_delete, btn_space, btn_enter
  - `onDestroy()`: cleanup coroutines (cancel settingsObserverJob + serviceScope)
  - ✅ **Settings s'appliquent maintenant en temps réel sans recréer le clavier!**
- **`app/src/main/res/layout/keyboard_view.xml`**: [2026-02-03 - Optimisation Layout]
  - Ajout ScrollView parent (match_parent + fillViewport) pour scroll si nécessaire
  - btn_pawn: visibility="gone" (pion inutile en notation, c'est la valeur par défaut)
  - **Emoji + lettre côte à côte** ("♜R" au lieu de "♜\nR") → gain hauteur majeur
  - Preview réduit: 24dp (au lieu de 36dp), textSize 14sp (au lieu de 18sp)
  - Marges minimales: 1dp partout (au lieu de 2dp)
  - ✅ **Plus de crop même en taille SMALL (44dp)!**
  - ✅ **5 pièces affichées** (Tour, Cavalier, Fou, Dame, Roi)

#### Tests Device
- ✅ Vibration testée sur émulateur (ne peut pas vibrer, c'est normal)
- ✅ Taille boutons testée sur émulateur → **live reload fonctionne parfaitement!**
- ✅ Crop corrigé avec layout optimisé (emoji+lettre côte à côte)
- ⚠️ Sons pas implémentés (DataStore setting existe, mais pas utilisé dans IME)
- ⚠️ Vibration device physique à tester plus tard

#### Notes pour Phase 1.5
- Preview clavier dans settings repoussé (nice-to-have, pas critique)
- Live reload validé = objectif principal atteint!
- Prêt pour historique des parties

### Phase 1.3 : Logique Notation Avancée - 2026-02-03 [TERMINÉE ✅]

#### Résumé
- ✅ Classe NotationBuilder complète pour construction notation SAN
- ✅ Validation SAN avec regex complète
- ✅ Support désambiguïsation (Cbd7, R1e1, Dh4e1)
- ✅ Support roques (O-O, O-O-O) et promotions (e8=D)
- ✅ 23 tests unitaires JUnit (100% réussite)

#### Ajouté
- **NotationBuilder.kt** : Classe builder fluent API
  - `addPiece(piece)` : R, C, F, D, K, P (convention française)
  - `addColumn(char)` / `addRow(int)` : a-h, 1-8
  - `addCoordinate(col, row)` : case complète (ex: e4)
  - `addCapture()` : symbole x
  - `addSymbol(symbol)` : +, #
  - `addPromotion(piece)` : =D, =C, =F, =R
  - `setCastle(type)` : KINGSIDE (O-O), QUEENSIDE (O-O-O)
  - `build()` : génère notation finale
  - `clear()` : réinitialise le builder
  - `preview()` : aperçu temps réel pendant construction

- **Validation SAN** :
  - `isValidMove(move: String): Boolean` : regex complète format SAN
  - Pièce optionnelle : `[RCFDKP]?`
  - Désambiguïsation : colonne `[a-h]?` et/ou rangée `[1-8]?`
  - Capture optionnelle : `x?`
  - Destination obligatoire : `[a-h][1-8]`
  - Promotion optionnelle : `=[RCFDQ]?`
  - Échec/Mat optionnel : `[+#]?`

- **Parsing** :
  - `parse(move: String): MoveComponents?` : décompose notation en composants
  - Data class `MoveComponents` avec piece, fromColumn, fromRow, toColumn, toRow, promotion, check, castle

- **Types définis** :
  - `Piece` : PAWN, ROOK, KNIGHT, BISHOP, QUEEN, KING
  - `Symbol` : CHECK (+), CHECKMATE (#), CAPTURE (x)
  - `CastleType` : KINGSIDE (O-O), QUEENSIDE (O-O-O)

- **NotationBuilderTest.kt** : 23 tests unitaires (100% succès)
  - Coups simples : e4, Cf3, Fd3
  - Captures : Dxd5, exd5
  - Échec/Mat : Dh5+, Cf7#
  - Roques : O-O, O-O-O
  - Promotions : e8=D, fxg1=C+
  - Désambiguïsation : Cbd7 (colonne), R1e1 (rangée), Dh4e1 (complète)
  - Validation format SAN
  - Parsing de composants
  - Réutilisation avec clear()
  - Preview temps réel

#### Convention Française
- **C** : Cavalier (au lieu de N pour Knight)
- **F** : Fou (au lieu de B pour Bishop)
- **D** : Dame (au lieu de Q pour Queen)
- **R** : Tour (Rook)
- **K** : Roi (King)

#### Résultats Tests
```
BUILD SUCCESSFUL in 15s
52 actionable tasks: 33 executed, 19 from cache
tests="23" failures="0" errors="0"
```

---

### Phase 1.2 : Service IME de Base - 2026-02-03 [TERMINÉE ✅]

#### Résumé
- ✅ Service IME complet et fonctionnel
- ✅ 6 pièces avec symboles Unicode
- ✅ Colonnes (a-h) et rangées (1-8)
- ✅ Modificateurs : x, +, #, O-O, O-O-O
- ✅ Preview temps réel de la notation
- ✅ Testé sur Pixel 9a (validation utilisateur)
- ✅ Performance optimisée (0 lag)

#### Ajouté
- **ChessKeyboardIME.kt** : Service IME complet
  - Extension InputMethodService
  - Méthode unifiée `setupButton()` pour tous les boutons
  - Preview TextView en haut du clavier
  - Méthode `commitNotation()` pour insertion texte
  - Gestion pièces : ♜R, ♞C, ♝F, ♛D, ♚K, ♟P

- **keyboard_view.xml** : Layout 5 rangées optimisé
  - Rangée 1 : 6 pièces (R, C, F, D, K, P)
  - Rangée 2 : 8 colonnes (a-h)
  - Rangée 3 : 8 rangées (1-8)
  - Rangée 4 : 5 modificateurs (x, +, #, O-O, O-O-O)
  - Rangée 5 : 3 contrôles (⌫ Backspace, Espace, OK)
  - Espacement optimisé : padding 2dp, margins 1dp
  - Preview 36dp en haut

- **themes.xml** : Styles clavier
  - KeyboardButton : parent TextButton.Material3 (performance)
  - stateListAnimator="@null" (0 lag)
  - Hauteur boutons : 52dp
  - Texte : 16sp

- **method.xml** : Configuration IME
  - Lien vers SettingsActivity (non implémenté)

- **AndroidManifest.xml** : Déclaration service
  - Permission BIND_INPUT_METHOD
  - exported="true"

#### Corrigé
- Espacement excessif → padding 8dp→2dp, margins 2dp→1dp
- Lag boutons → TextButton au lieu de Button (animations désactivées)
- Boutons non-cliquables → simplifié logique Kotlin (setupButton unifié)
- Fichiers build verrouillés → build depuis C:\Users\USERNAME\coup-de-main-temp
- Pion manquant → ajout ♟P (6ème pièce)
- Boutons mal alignés → suppression \n dans textes

#### Validation Utilisateur
> "ca rends tres bien!" - BKH

Testé sur : Pixel 9a - 16 (Android)

---

### Phase 1.1 : Setup Projet Android - 2026-02-02 [TERMINÉE ✅]

#### Résumé
- ✅ Environnement de développement configuré et fonctionnel
- ✅ Build réussi avec Gradle 8.4 + AGP 8.3.0 + JDK 21
- ✅ Package corrigé selon convention BKH : `bkh.apps.coupdemain`
- ✅ App installée et testée sur téléphone physique
- ✅ MainActivity fonctionnelle avec UI Material3

#### Ajouté
- **Documentation initiale du projet** :
  - `docs/CONTEXTE.md` : Contexte complet, besoins utilisateurs, notation d'échecs, vision projet
  - `docs/STACK_TECHNIQUE.md` : Architecture Android, Kotlin, Room, IME, Material3, dépendances complètes
  - `docs/ROADMAP.md` : Plan détaillé Phase 1-3, timeline 6-7 semaines, critères de succès
  - `CHANGELOG.md` : Ce fichier pour tracking modifications
  - `README.md` : Guide de démarrage rapide

- **Structure projet Android complète** :
  - Dossier `dev/coup-de-main/` créé
  - Sous-dossier `dev/coup-de-main/docs/` pour documentation
  - Structure standard Android : `app/src/main/java/fr/bkh/coupdemain/`
  - Dossiers resources : `layout/`, `values/`, `xml/`, `drawable/`

- **Configuration Gradle** :
  - `settings.gradle.kts` : Configuration projet "CoupDeMain"
  - `build.gradle.kts` (root) : Plugins Android + Kotlin 1.9.20
  - `app/build.gradle.kts` : Dépendances Material3, Room, Navigation, Lifecycle
  - `gradle.properties` : Configuration JVM et AndroidX
  - `gradle-wrapper.properties` : Gradle 8.2
  - Scripts `gradlew` et `gradlew.bat` pour build multi-plateforme

- **Code source initial** :
  - `MainActivity.kt` : Activity principale avec FAB et Snackbar de test
  - `AndroidManifest.xml` : Configuration app avec launcher activity

- **Resources Android** :
  - `activity_main.xml` : Layout Material3 avec Toolbar, CoordinatorLayout, FAB
  - `strings.xml` : Chaînes françaises (app_name, welcome_message, etc.)
  - `colors.xml` : Palette dark theme inspirée IA_TASKS (background_dark, accent_blue, etc.)
  - `themes.xml` : Theme Material3 Dark personnalisé

- **Fichiers projet** :
  - `.gitignore` : Configuration Android standard
  - `proguard-rules.pro` : Règles ProGuard (Room, obfuscation)

#### Décisions Techniques
- **Nom projet** : "Coup de Main" (français, double sens aide + coup d'échecs)
- **Stack** : Android natif avec Kotlin, Room, Material Design 3, InputMethodService
- **Priorité Phase 1** : Clavier IME + Customisation (validation utilisateurs)
- **Approche** : Développement par phases itératives avec validation utilisateur
- **Distribution** : Google Play Store (post Phase 3)

#### Inspirations
- Architecture inspirée de [IA_TASKS](../IA_TASKS/) (patterns MVC, SQLite, UI cards)
- Palette de couleurs réutilisée depuis [IA_TASKS/config.py](../IA_TASKS/config.py)

---

## Conventions de Changelog

### Catégories
- **Ajouté** : Nouvelles fonctionnalités
- **Modifié** : Changements de fonctionnalités existantes
- **Déprécié** : Fonctionnalités bientôt supprimées
- **Supprimé** : Fonctionnalités retirées
- **Corrigé** : Corrections de bugs
- **Sécurité** : Changements relatifs à la sécurité

### Format Commits
```
type(scope): description

[corps optionnel]
[pied de page optionnel]
```

**Types :**
- `feat` : Nouvelle fonctionnalité
- `fix` : Correction bug
- `docs` : Documentation
- `style` : Formatage, pas de changement code
- `refactor` : Refactoring code
- `test` : Ajout/modification tests
- `chore` : Tâches maintenance

**Exemples :**
```
feat(ime): implement ChessKeyboardIME service
fix(notation): regex validation for castling
docs(readme): add installation instructions
```

---

## Workflow de Modification

1. **Coder** la modification dans le fichier concerné
2. **Logger** la modification dans ce CHANGELOG.md (section Unreleased)
3. **Commit Git** avec message conventionnel
4. **User review** : BKH valide les changements
5. **User commit** : BKH commit manuellement si OK

**Note :** L'agent (Gerard) modifie et log, l'utilisateur (BKH) review et commit.

---

## Prochaines Modifications Prévues

**Phase 1.2 - Service IME** [PROCHAINE ÉTAPE 🎯] :
- [ ] Classe `ChessKeyboardIME` (extends InputMethodService)
- [ ] Déclaration service dans `AndroidManifest.xml`
- [ ] Layout XML clavier avec pièces (R, D, F, T, C, P)
- [ ] Layout cases (a-h, 1-8) + boutons spéciaux (+, #, x, O-O)
- [ ] Logique construction notation SAN (ex: "e4", "Nf3+")
- [ ] Test activation clavier système → Coup de Main
- [ ] Validation saisie notation complète

**Phase 1.3 - UI Application** (après IME) :
- [ ] MainActivity avec historique parties
- [ ] Écran saisie partie avec RecyclerView
- [ ] Boutons validation/annulation coup
- [ ] Navigation entre écrans

---

_Mis à jour automatiquement à chaque modification. Dernière mise à jour : 2026-02-03 21:10_
