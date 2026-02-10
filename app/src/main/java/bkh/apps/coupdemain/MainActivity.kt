package bkh.apps.coupdemain

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import bkh.apps.coupdemain.ui.home.HomeFragment
import bkh.apps.coupdemain.ui.historique.HistoriqueFragment
import bkh.apps.coupdemain.ui.notation.NotationDetailFragment
import bkh.apps.coupdemain.ui.onboarding.OnboardingActivity
import bkh.apps.coupdemain.ui.settings.SettingsFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        // Appliquer le thème avant setContentView
        SettingsFragment.applyThemeFromPreferences(this)
        
        super.onCreate(savedInstanceState)
        
        // Vérifier si onboarding déjà complété
        if (!OnboardingActivity.isCompleted(this)) {
            startActivity(Intent(this, OnboardingActivity::class.java))
            // Ne pas finish() - on laisse l'utilisateur revenir après onboarding
        }
        
        setContentView(R.layout.activity_main)

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        
        // Load home fragment by default
        if (savedInstanceState == null) {
            loadFragment(HomeFragment())
        }

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    loadFragment(HomeFragment())
                    true
                }
                R.id.nav_keyboard -> {
                    loadFragment(HistoriqueFragment())
                    true
                }
                R.id.nav_settings -> {
                    loadFragment(SettingsFragment())
                    true
                }
                else -> false
            }
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
    
    /**
     * Ouvrir le fragment de détail d'une partie
     * Appelé depuis HomeFragment ou HistoriqueFragment
     */
    fun openGameDetail(gameId: Long) {
        val fragment = NotationDetailFragment.newInstance(gameId)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }
}
