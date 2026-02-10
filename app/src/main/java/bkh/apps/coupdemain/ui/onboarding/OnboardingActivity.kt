package bkh.apps.coupdemain.ui.onboarding

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.viewpager2.widget.ViewPager2
import bkh.apps.coupdemain.R
import bkh.apps.coupdemain.databinding.ActivityOnboardingBinding
import com.google.android.material.button.MaterialButton
import com.google.android.material.tabs.TabLayoutMediator

class OnboardingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOnboardingBinding
    private lateinit var viewPager: ViewPager2
    
    companion object {
        private const val PREFS_NAME = "onboarding"
        private const val KEY_COMPLETED = "completed"
        
        fun isCompleted(context: Context): Boolean {
            return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
                .getBoolean(KEY_COMPLETED, false)
        }
        
        fun markCompleted(context: Context) {
            context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
                .edit()
                .putBoolean(KEY_COMPLETED, true)
                .apply()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        setupViewPager()
        setupButtons()
    }
    
    private fun setupViewPager() {
        viewPager = binding.viewPager
        val adapter = OnboardingPagerAdapter(this)
        viewPager.adapter = adapter
        
        TabLayoutMediator(binding.tabLayout, viewPager) { _, _ -> }.attach()
        
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                updateButtons(position, adapter.itemCount)
            }
        })
    }
    
    private fun setupButtons() {
        binding.btnNext.setOnClickListener {
            if (viewPager.currentItem < (viewPager.adapter?.itemCount ?: 0) - 1) {
                viewPager.currentItem += 1
            } else {
                // Sur la dernière page, "Non" = continuer vers l'app
                finishOnboarding()
            }
        }
        
        binding.btnSkip.setOnClickListener {
            if (viewPager.currentItem == (viewPager.adapter?.itemCount ?: 0) - 1) {
                // Sur la dernière page, "Passer" n'existe plus, ce texte devient "Non"
                finishOnboarding()
            } else {
                // Sur les autres pages, passer tout l'onboarding
                finishOnboarding()
            }
        }
        
        binding.btnActivateIme.setOnClickListener {
            // "Oui" = marquer onboarding complété, lancer MainActivity, puis ouvrir les paramètres
            markCompleted(this)
            
            // Lancer MainActivity dans la pile d'activités
            val intent = Intent(this, bkh.apps.coupdemain.MainActivity::class.java)
            startActivity(intent)
            
            // Ouvrir les paramètres IME par dessus
            openImeSettings()
            
            // Fermer l'onboarding
            finish()
        }
    }
    
    private fun updateButtons(position: Int, totalPages: Int) {
        when {
            position == totalPages - 1 -> {
                // Dernière page: afficher "Oui" et "Non"
                binding.btnActivateIme.visibility = MaterialButton.VISIBLE
                binding.btnActivateIme.text = "Oui, configurer maintenant"
                
                binding.btnNext.text = "Non, plus tard"
                binding.btnNext.visibility = MaterialButton.VISIBLE
                
                binding.btnSkip.visibility = MaterialButton.GONE
                
                // Ajuster les contraintes de btnNext pour qu'il soit aligné avec btnActivateIme
                val params = binding.btnNext.layoutParams as ConstraintLayout.LayoutParams
                params.startToEnd = ConstraintLayout.LayoutParams.UNSET
                params.startToStart = ConstraintLayout.LayoutParams.PARENT_ID
                params.marginStart = resources.getDimensionPixelSize(R.dimen.button_margin_horizontal)
                binding.btnNext.layoutParams = params
            }
            else -> {
                binding.btnNext.text = "Suivant"
                binding.btnSkip.visibility = MaterialButton.VISIBLE
                binding.btnActivateIme.visibility = MaterialButton.GONE
                
                // Restaurer les contraintes normales de btnNext
                val params = binding.btnNext.layoutParams as ConstraintLayout.LayoutParams
                params.startToStart = ConstraintLayout.LayoutParams.UNSET
                params.startToEnd = binding.btnSkip.id
                params.marginStart = resources.getDimensionPixelSize(R.dimen.button_margin_small)
                binding.btnNext.layoutParams = params
            }
        }
    }
    
    private fun isImeEnabled(): Boolean {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val enabledImes = imm.enabledInputMethodList
        return enabledImes.any { it.packageName == packageName }
    }
    
    private fun openImeSettings() {
        startActivity(Intent(Settings.ACTION_INPUT_METHOD_SETTINGS))
    }
    
    private fun finishOnboarding() {
        markCompleted(this)
        
        // Lancer MainActivity
        val intent = Intent(this, bkh.apps.coupdemain.MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        
        finish()
    }
}
