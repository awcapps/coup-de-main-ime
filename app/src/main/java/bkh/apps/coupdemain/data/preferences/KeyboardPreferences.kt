package bkh.apps.coupdemain.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * DataStore pour les préférences du clavier
 * Gère les paramètres de personnalisation (taille, thème, vibration, son)
 */
class KeyboardPreferences(private val context: Context) {
    
    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
            name = "keyboard_preferences"
        )
        
        // Clés de préférences
        val BUTTON_SIZE = stringPreferencesKey("button_size")
        val ENABLE_VIBRATION = booleanPreferencesKey("enable_vibration")
        val ENABLE_SOUND = booleanPreferencesKey("enable_sound")
        val VIBRATION_DURATION = intPreferencesKey("vibration_duration")
        val LAYOUT_MODE = stringPreferencesKey("layout_mode")
        val COLOR_SCHEME = stringPreferencesKey("color_scheme")
        
        // Valeurs par défaut
        const val DEFAULT_BUTTON_SIZE = "LARGE"
        const val DEFAULT_VIBRATION = true
        const val DEFAULT_SOUND = false
        const val DEFAULT_VIBRATION_DURATION = 30 // millisecondes
        const val DEFAULT_LAYOUT = "DEFAULT"
        const val DEFAULT_COLOR_SCHEME = "SYSTEM"
    }
    
    /**
     * Taille des boutons
     */
    enum class ButtonSize(val heightDp: Int, val textSizeSp: Int) {
        SMALL(44, 14),
        MEDIUM(52, 16),    // Défaut actuel
        LARGE(64, 18)
    }
    
    /**
     * Modes de disposition du clavier
     */
    enum class LayoutMode {
        DEFAULT,          // Pièces en haut (actuel)
        PIECES_BOTTOM     // Pièces en bas
    }
    
    /**
     * Schémas de couleurs
     */
    enum class ColorScheme {
        SYSTEM, // Suit le thème système
        DARK,   // Thème sombre
        LIGHT   // Thème clair (fond blanc)
    }
    
    /**
     * Récupère la taille des boutons
     */
    val buttonSize: Flow<ButtonSize> = context.dataStore.data.map { preferences ->
        val sizeString = preferences[BUTTON_SIZE] ?: DEFAULT_BUTTON_SIZE
        try {
            ButtonSize.valueOf(sizeString)
        } catch (e: IllegalArgumentException) {
            ButtonSize.MEDIUM
        }
    }
    
    /**
     * Récupère l'état de la vibration
     */
    val isVibrationEnabled: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[ENABLE_VIBRATION] ?: DEFAULT_VIBRATION
    }
    
    /**
     * Récupère l'état du son
     */
    val isSoundEnabled: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[ENABLE_SOUND] ?: DEFAULT_SOUND
    }
    
    /**
     * Récupère la durée de vibration
     */
    val vibrationDuration: Flow<Int> = context.dataStore.data.map { preferences ->
        preferences[VIBRATION_DURATION] ?: DEFAULT_VIBRATION_DURATION
    }
    
    /**
     * Récupère le mode de disposition
     */
    val layoutMode: Flow<LayoutMode> = context.dataStore.data.map { preferences ->
        val layoutString = preferences[LAYOUT_MODE] ?: DEFAULT_LAYOUT
        try {
            LayoutMode.valueOf(layoutString)
        } catch (e: IllegalArgumentException) {
            LayoutMode.DEFAULT
        }
    }
    
    /**
     * Récupère le schéma de couleurs
     */
    val colorScheme: Flow<ColorScheme> = context.dataStore.data.map { preferences ->
        val colorString = preferences[COLOR_SCHEME] ?: DEFAULT_COLOR_SCHEME
        try {
            ColorScheme.valueOf(colorString)
        } catch (e: IllegalArgumentException) {
            ColorScheme.SYSTEM
        }
    }
    
    /**
     * Modifie la taille des boutons
     */
    suspend fun setButtonSize(size: ButtonSize) {
        context.dataStore.edit { preferences ->
            preferences[BUTTON_SIZE] = size.name
        }
    }
    
    /**
     * Active/désactive la vibration
     */
    suspend fun setVibrationEnabled(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[ENABLE_VIBRATION] = enabled
        }
    }
    
    /**
     * Active/désactive le son
     */
    suspend fun setSoundEnabled(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[ENABLE_SOUND] = enabled
        }
    }
    
    /**
     * Modifie la durée de vibration (en millisecondes)
     */
    suspend fun setVibrationDuration(durationMs: Int) {
        require(durationMs in 10..100) { "Duration must be between 10 and 100 ms" }
        context.dataStore.edit { preferences ->
            preferences[VIBRATION_DURATION] = durationMs
        }
    }
    
    /**
     * Modifie le mode de disposition
     */
    suspend fun setLayoutMode(mode: LayoutMode) {
        context.dataStore.edit { preferences ->
            preferences[LAYOUT_MODE] = mode.name
        }
    }
    
    /**
     * Modifie le schéma de couleurs
     */
    suspend fun setColorScheme(scheme: ColorScheme) {
        context.dataStore.edit { preferences ->
            preferences[COLOR_SCHEME] = scheme.name
        }
    }
    
    /**
     * Réinitialise toutes les préférences
     */
    suspend fun resetToDefaults() {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}
