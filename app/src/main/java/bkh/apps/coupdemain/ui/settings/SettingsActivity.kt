package bkh.apps.coupdemain.ui.settings

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import bkh.apps.coupdemain.R
import bkh.apps.coupdemain.data.preferences.KeyboardPreferences
import com.google.android.material.appbar.MaterialToolbar
import kotlinx.coroutines.launch

/**
 * Activité de paramétrage du clavier
 * Accessible depuis les paramètres système du clavier IME
 */
class SettingsActivity : AppCompatActivity() {
    
    private lateinit var preferences: KeyboardPreferences
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        
        preferences = KeyboardPreferences(this)
        
        // Setup toolbar
        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            title = "Paramètres"
        }
        
        // Charger le fragment de préférences
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.settings_container, SettingsFragment())
                .commit()
        }
    }
    
    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}
