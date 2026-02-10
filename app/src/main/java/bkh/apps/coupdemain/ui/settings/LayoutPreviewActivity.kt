package bkh.apps.coupdemain.ui.settings

import android.content.res.Configuration
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import bkh.apps.coupdemain.R
import bkh.apps.coupdemain.data.preferences.KeyboardPreferences
import com.google.android.material.appbar.MaterialToolbar
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

/**
 * Activity pour prévisualiser le clavier avec les paramètres actuels
 */
class LayoutPreviewActivity : AppCompatActivity() {

    private lateinit var preferences: KeyboardPreferences
    private var keyboardView: View? = null
    private var currentLayout: KeyboardPreferences.LayoutMode = KeyboardPreferences.LayoutMode.DEFAULT
    private var currentColorScheme: KeyboardPreferences.ColorScheme = KeyboardPreferences.ColorScheme.DARK

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_layout_preview)

        preferences = KeyboardPreferences(this)

        // Setup toolbar
        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }

        // Charger le layout initial
        lifecycleScope.launch {
            currentLayout = preferences.layoutMode.first()
            currentColorScheme = preferences.colorScheme.first()
            applyLayout()
        }

        // Observer les changements de settings
        observeSettings()
    }

    private fun observeSettings() {
        lifecycleScope.launch {
            // Observer layout mode
            launch {
                preferences.layoutMode.collect { layout ->
                    if (layout != currentLayout) {
                        currentLayout = layout
                        applyLayout()
                    }
                }
            }

            // Observer color scheme
            launch {
                preferences.colorScheme.collect { colors ->
                    if (colors != currentColorScheme) {
                        currentColorScheme = colors
                        applyColorScheme()
                    }
                }
            }

            // Observer button size
            launch {
                preferences.buttonSize.collect { size ->
                    applyButtonSize(size)
                }
            }
        }
    }

    private fun applyLayout() {
        val container = findViewById<android.widget.FrameLayout>(R.id.keyboard_preview_container)
        container?.removeAllViews()
        
        val layoutResource = when (currentLayout) {
            KeyboardPreferences.LayoutMode.DEFAULT -> R.layout.keyboard_view
            KeyboardPreferences.LayoutMode.PIECES_BOTTOM -> R.layout.keyboard_view_alt
        }
        
        keyboardView = layoutInflater.inflate(layoutResource, container, false)
        container?.addView(keyboardView)
        
        // Réappliquer les paramètres actuels
        lifecycleScope.launch {
            applyButtonSize(preferences.buttonSize.first())
            applyColorScheme()
        }
    }

    private fun applyColorScheme() {
        keyboardView?.let { view ->
            // Déterminer si on doit utiliser le mode sombre
            val isDarkMode = when (currentColorScheme) {
                KeyboardPreferences.ColorScheme.SYSTEM -> {
                    // Vérifier le thème système
                    (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES
                }
                KeyboardPreferences.ColorScheme.DARK -> true
                KeyboardPreferences.ColorScheme.LIGHT -> false
            }
            
            val backgroundColor = if (isDarkMode) {
                getColor(R.color.keyboard_background_dark)
            } else {
                getColor(R.color.keyboard_background_light)
            }

            val buttonColor = if (isDarkMode) {
                getColor(R.color.keyboard_button_background_dark)
            } else {
                getColor(R.color.keyboard_button_background_light)
            }
            
            val textColor = if (isDarkMode) {
                getColor(R.color.keyboard_button_text_dark)
            } else {
                getColor(R.color.keyboard_button_text_light)
            }

            view.setBackgroundColor(backgroundColor)

            // Appliquer aux boutons
            val buttonIds = getAllButtonIds()
            buttonIds.forEach { buttonId ->
                view.findViewById<Button>(buttonId)?.apply {
                    setBackgroundColor(buttonColor)
                    setTextColor(textColor)
                }
            }
        }
    }

    private fun applyButtonSize(size: KeyboardPreferences.ButtonSize) {
        keyboardView?.let { view ->
            val heightPx = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                size.heightDp.toFloat(),
                resources.displayMetrics
            ).toInt()

            val buttonIds = getAllButtonIds()
            buttonIds.forEach { buttonId ->
                view.findViewById<Button>(buttonId)?.apply {
                    layoutParams = layoutParams.apply {
                        height = heightPx
                    }
                }
            }
        }
    }

    private fun getAllButtonIds(): List<Int> {
        return listOf(
            R.id.btn_rook, R.id.btn_knight, R.id.btn_bishop, R.id.btn_queen, R.id.btn_king, R.id.btn_pawn,
            R.id.btn_col_a, R.id.btn_col_b, R.id.btn_col_c, R.id.btn_col_d,
            R.id.btn_col_e, R.id.btn_col_f, R.id.btn_col_g, R.id.btn_col_h,
            R.id.btn_row_1, R.id.btn_row_2, R.id.btn_row_3, R.id.btn_row_4,
            R.id.btn_row_5, R.id.btn_row_6, R.id.btn_row_7, R.id.btn_row_8,
            R.id.btn_capture, R.id.btn_check, R.id.btn_checkmate, R.id.btn_castle_short,
            R.id.btn_castle_long, R.id.btn_space, R.id.btn_delete, R.id.btn_enter
        )
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
