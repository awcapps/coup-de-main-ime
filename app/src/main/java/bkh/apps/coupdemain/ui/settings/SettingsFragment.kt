package bkh.apps.coupdemain.ui.settings

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import androidx.preference.*
import bkh.apps.coupdemain.BuildConfig
import bkh.apps.coupdemain.R
import bkh.apps.coupdemain.data.preferences.KeyboardPreferences
import bkh.apps.coupdemain.ui.onboarding.OnboardingActivity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

/**
 * Fragment des préférences du clavier
 * Utilise AndroidX Preference library pour l'UI
 */
class SettingsFragment : PreferenceFragmentCompat() {

    private lateinit var preferences: KeyboardPreferences

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.keyboard_preferences, rootKey)

        preferences = KeyboardPreferences(requireContext())

        setupButtonSizePreference()
        setupVibrationPreferences()
        setupSoundPreference()
        setupLayoutPreference()
        setupColorSchemePreference()
        setupThemePreference()
        setupRelaunchOnboardingButton()
        setupResetButton()
    }

    /**
     * Configure la préférence de taille des boutons
     */
    private fun setupButtonSizePreference() {
        val buttonSizePref = findPreference<ListPreference>("button_size")

        buttonSizePref?.apply {
            // Charger la valeur actuelle
            lifecycleScope.launch {
                val currentSize = preferences.buttonSize.first()
                value = currentSize.name
                summary = getSizeSummary(currentSize)
            }

            // Écouter les changements
            setOnPreferenceChangeListener { _, newValue ->
                val size = KeyboardPreferences.ButtonSize.valueOf(newValue as String)
                lifecycleScope.launch {
                    preferences.setButtonSize(size)
                }
                summary = getSizeSummary(size)
                true
            }
        }
    }

    /**
     * Configure les préférences de vibration
     */
    private fun setupVibrationPreferences() {
        val vibrationSwitch = findPreference<SwitchPreferenceCompat>("enable_vibration")
        val vibrationDuration = findPreference<SeekBarPreference>("vibration_duration")

        vibrationSwitch?.apply {
            lifecycleScope.launch {
                isChecked = preferences.isVibrationEnabled.first()
            }

            setOnPreferenceChangeListener { _, newValue ->
                val enabled = newValue as Boolean
                lifecycleScope.launch {
                    preferences.setVibrationEnabled(enabled)
                }
                vibrationDuration?.isEnabled = enabled
                true
            }
        }

        vibrationDuration?.apply {
            min = 10
            max = 100
            showSeekBarValue = true

            lifecycleScope.launch {
                value = preferences.vibrationDuration.first()
                isEnabled = preferences.isVibrationEnabled.first()
            }

            setOnPreferenceChangeListener { _, newValue ->
                val duration = newValue as Int
                lifecycleScope.launch {
                    preferences.setVibrationDuration(duration)
                }
                true
            }
        }
    }

    /**
     * Configure la préférence de son
     */
    private fun setupSoundPreference() {
        val soundSwitch = findPreference<SwitchPreferenceCompat>("enable_sound")

        soundSwitch?.apply {
            lifecycleScope.launch {
                isChecked = preferences.isSoundEnabled.first()
            }

            setOnPreferenceChangeListener { _, newValue ->
                val enabled = newValue as Boolean
                lifecycleScope.launch {
                    preferences.setSoundEnabled(enabled)
                }
                true
            }
        }
    }

    /**
     * Configure la préférence de disposition du clavier
     */
    private fun setupLayoutPreference() {
        val layoutPref = findPreference<ListPreference>("layout_mode")

        layoutPref?.apply {
            lifecycleScope.launch {
                val currentLayout = preferences.layoutMode.first()
                value = currentLayout.name
                summary = getLayoutSummary(currentLayout)
            }

            setOnPreferenceChangeListener { _, newValue ->
                val layout = KeyboardPreferences.LayoutMode.valueOf(newValue as String)
                lifecycleScope.launch {
                    preferences.setLayoutMode(layout)
                }
                summary = getLayoutSummary(layout)
                true
            }
        }
    }

    /**
     * Configure la préférence de schéma de couleurs
     */
    private fun setupColorSchemePreference() {
        val colorPref = findPreference<ListPreference>("color_scheme")

        colorPref?.apply {
            lifecycleScope.launch {
                val currentColors = preferences.colorScheme.first()
                value = currentColors.name
                summary = getColorSchemeSummary(currentColors)
            }

            setOnPreferenceChangeListener { _, newValue ->
                val colors = KeyboardPreferences.ColorScheme.valueOf(newValue as String)
                lifecycleScope.launch {
                    preferences.setColorScheme(colors)
                }
                summary = getColorSchemeSummary(colors)
                true
            }
        }
    }

    /**
     * Configure la préférence de thème de l'application
     */
    private fun setupThemePreference() {
        val themePref = findPreference<ListPreference>("theme_preference")

        themePref?.apply {
            val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
            val currentTheme = sharedPreferences.getString("theme_preference", "system") ?: "system"
            value = currentTheme
            summary = getThemeSummary(currentTheme)

            setOnPreferenceChangeListener { _, newValue ->
                val theme = newValue as String
                applyTheme(theme)
                summary = getThemeSummary(theme)
                true
            }
        }
    }

    private fun applyTheme(theme: String) {
        val mode = when (theme) {
            "light" -> AppCompatDelegate.MODE_NIGHT_NO
            "dark" -> AppCompatDelegate.MODE_NIGHT_YES
            else -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
        }
        AppCompatDelegate.setDefaultNightMode(mode)
    }

    /**
     * Configure le bouton de relance de l'onboarding (debug uniquement)
     */
    private fun setupRelaunchOnboardingButton() {
        val relaunchPref = findPreference<Preference>("relaunch_onboarding")
        
        if (!BuildConfig.DEBUG) {
            // Cacher la préférence en mode release
            relaunchPref?.let { preferenceScreen.removePreference(it) }
        } else {
            // En mode debug, configurer le click listener
            relaunchPref?.setOnPreferenceClickListener {
                // Effacer le flag d'onboarding complété
                requireContext().getSharedPreferences("onboarding", Context.MODE_PRIVATE)
                    .edit()
                    .putBoolean("completed", false)
                    .apply()
                
                // Lancer l'onboarding
                val intent = Intent(requireContext(), OnboardingActivity::class.java)
                startActivity(intent)
                
                // Terminer l'activité actuelle pour forcer le redémarrage
                requireActivity().finish()
                
                true
            }
        }
    }

    /**
     * Configure le bouton de réinitialisation
     */
    private fun setupResetButton() {
        val resetPref = findPreference<Preference>("reset_preferences")

        resetPref?.setOnPreferenceClickListener {
            lifecycleScope.launch {
                preferences.resetToDefaults()
                // Recharger les préférences
                preferenceScreen?.removeAll()
                onCreatePreferences(null, null)
            }
            true
        }
    }

    private fun getColorSchemeSummary(colors: KeyboardPreferences.ColorScheme): String {
        return when (colors) {
            KeyboardPreferences.ColorScheme.SYSTEM -> "Système (automatique)"
            KeyboardPreferences.ColorScheme.DARK -> "Thème sombre"
            KeyboardPreferences.ColorScheme.LIGHT -> "Thème clair (fond blanc)"
        }
    }

    private fun getSizeSummary(size: KeyboardPreferences.ButtonSize): String {
        return when (size) {
            KeyboardPreferences.ButtonSize.SMALL -> "Petits boutons (44dp)"
            KeyboardPreferences.ButtonSize.MEDIUM -> "Taille normale (52dp)"
            KeyboardPreferences.ButtonSize.LARGE -> "Grands boutons (64dp) - Par défaut"
        }
    }

    private fun getLayoutSummary(layout: KeyboardPreferences.LayoutMode): String {
        return when (layout) {
            KeyboardPreferences.LayoutMode.DEFAULT -> "Disposition 1 (pièces en haut)"
            KeyboardPreferences.LayoutMode.PIECES_BOTTOM -> "Disposition 2 (pièces en bas)"
        }
    }

    private fun getThemeSummary(theme: String): String {
        return when (theme) {
            "light" -> "Thème Clair"
            "dark" -> "Thème Sombre"
            else -> "Système (automatique)"
        }
    }

    companion object {
        fun applyThemeFromPreferences(context: Context) {
            val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
            val theme = sharedPreferences.getString("theme_preference", "system") ?: "system"
            val mode = when (theme) {
                "light" -> AppCompatDelegate.MODE_NIGHT_NO
                "dark" -> AppCompatDelegate.MODE_NIGHT_YES
                else -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
            }
            AppCompatDelegate.setDefaultNightMode(mode)
        }
    }
}
