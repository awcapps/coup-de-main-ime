package bkh.apps.coupdemain.ime

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.content.res.Configuration
import android.graphics.Color
import android.inputmethodservice.InputMethodService
import android.os.VibrationEffect
import android.os.Vibrator
import android.speech.tts.TextToSpeech
import android.util.TypedValue
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.TextView
import bkh.apps.coupdemain.R
import bkh.apps.coupdemain.data.preferences.KeyboardPreferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.Locale

class ChessKeyboardIME : InputMethodService() {

    private var currentNotation = StringBuilder()
    private var previewText: TextView? = null
    private var keyboardView: View? = null
    
    // Settings & Vibration
    private lateinit var preferences: KeyboardPreferences
    private var vibrator: Vibrator? = null
    private var tts: TextToSpeech? = null
    private var ttsReady = false
    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
    private var settingsObserverJob: Job? = null
    
    // Cache settings
    private var vibrationEnabled = true
    private var vibrationDuration = 30
    private var buttonSize = KeyboardPreferences.ButtonSize.MEDIUM
    private var layoutMode = KeyboardPreferences.LayoutMode.DEFAULT
    private var colorScheme = KeyboardPreferences.ColorScheme.DARK

    override fun onCreate() {
        super.onCreate()
        preferences = KeyboardPreferences(this)
        vibrator = getSystemService(VIBRATOR_SERVICE) as? Vibrator
        
        // Initialiser TTS
        tts = TextToSpeech(this) { status ->
            if (status == TextToSpeech.SUCCESS) {
                val result = tts?.setLanguage(Locale.FRENCH)
                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    android.util.Log.e("ChessKeyboardIME", "TTS: Français non supporté")
                    ttsReady = false
                } else {
                    android.util.Log.i("ChessKeyboardIME", "TTS: Initialisé avec succès en français")
                    ttsReady = true
                }
            } else {
                android.util.Log.e("ChessKeyboardIME", "TTS: Échec initialisation")
                ttsReady = false
            }
        }
        
        loadInitialSettings()
        observeSettings()
    }

    private fun loadInitialSettings() {
        serviceScope.launch {
            vibrationEnabled = preferences.isVibrationEnabled.first()
            vibrationDuration = preferences.vibrationDuration.first()
            buttonSize = preferences.buttonSize.first()
            layoutMode = preferences.layoutMode.first()
            colorScheme = preferences.colorScheme.first()
        }
    }

    /**
     * Observer les changements de settings en temps réel
     */
    private fun observeSettings() {
        settingsObserverJob = serviceScope.launch {
            // Observer vibration
            launch {
                preferences.isVibrationEnabled.collect { enabled ->
                    vibrationEnabled = enabled
                }
            }
            launch {
                preferences.vibrationDuration.collect { duration ->
                    vibrationDuration = duration
                }
            }
            // Observer taille boutons
            launch {
                preferences.buttonSize.collect { size ->
                    if (size != buttonSize) {
                        buttonSize = size
                        applyButtonSize()
                    }
                }
            }
            // Observer layout mode
            launch {
                preferences.layoutMode.collect { mode ->
                    if (mode != layoutMode) {
                        layoutMode = mode
                        recreateKeyboard()
                    }
                }
            }
            // Observer color scheme
            launch {
                preferences.colorScheme.collect { scheme ->
                    if (scheme != colorScheme) {
                        colorScheme = scheme
                        applyColorScheme()
                    }
                }
            }
        }
    }

    /**
     * Applique la nouvelle taille des boutons dynamiquement
     */
    private fun applyButtonSize() {
        keyboardView?.let { view ->
            val heightPx = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                buttonSize.heightDp.toFloat(),
                resources.displayMetrics
            ).toInt()

            // Liste de tous les IDs de boutons
            val buttonIds = listOf(
                R.id.btn_rook, R.id.btn_knight, R.id.btn_bishop, R.id.btn_queen, R.id.btn_king,
                R.id.btn_col_a, R.id.btn_col_b, R.id.btn_col_c, R.id.btn_col_d,
                R.id.btn_col_e, R.id.btn_col_f, R.id.btn_col_g, R.id.btn_col_h,
                R.id.btn_row_1, R.id.btn_row_2, R.id.btn_row_3, R.id.btn_row_4,
                R.id.btn_row_5, R.id.btn_row_6, R.id.btn_row_7, R.id.btn_row_8,
                R.id.btn_capture, R.id.btn_check, R.id.btn_checkmate,
                R.id.btn_castle_short, R.id.btn_castle_long,
                R.id.btn_delete, R.id.btn_space, R.id.btn_enter
            )

            buttonIds.forEach { id ->
                view.findViewById<Button>(id)?.apply {
                    layoutParams = layoutParams.apply {
                        height = heightPx
                    }
                }
            }
        }
    }

    /**
     * Recrée le clavier avec le nouveau layout (appelé quand layout mode change)
     */
    private fun recreateKeyboard() {
        setInputView(onCreateInputView())
    }

    /**
     * Applique le schéma de couleurs au clavier dynamiquement
     */
    private fun applyColorScheme() {
        keyboardView?.let { view ->
            // Déterminer si on doit utiliser le mode sombre
            val isDarkMode = when (colorScheme) {
                KeyboardPreferences.ColorScheme.SYSTEM -> {
                    (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES
                }
                KeyboardPreferences.ColorScheme.DARK -> true
                KeyboardPreferences.ColorScheme.LIGHT -> false
            }
            
            // Couleurs selon le mode
            val backgroundColor = if (isDarkMode) {
                getColor(R.color.keyboard_background_dark)
            } else {
                getColor(R.color.keyboard_background_light)
            }
            
            val buttonBackground = if (isDarkMode) {
                getColor(R.color.keyboard_button_background_dark)
            } else {
                getColor(R.color.keyboard_button_background_light)
            }
            
            val textColor = if (isDarkMode) {
                getColor(R.color.keyboard_button_text_dark)
            } else {
                getColor(R.color.keyboard_button_text_light)
            }
            
            val previewBackground = if (isDarkMode) {
                getColor(R.color.keyboard_preview_background_dark)
            } else {
                getColor(R.color.keyboard_preview_background_light)
            }
            
            val previewTextColor = if (isDarkMode) {
                getColor(R.color.keyboard_preview_text_dark)
            } else {
                getColor(R.color.keyboard_preview_text_light)
            }

            // Appliquer le fond au ScrollView parent
            view.setBackgroundColor(backgroundColor)
            
            // Appliquer aux boutons
            val buttonIds = listOf(
                R.id.btn_rook, R.id.btn_knight, R.id.btn_bishop, R.id.btn_queen, R.id.btn_king,
                R.id.btn_col_a, R.id.btn_col_b, R.id.btn_col_c, R.id.btn_col_d,
                R.id.btn_col_e, R.id.btn_col_f, R.id.btn_col_g, R.id.btn_col_h,
                R.id.btn_row_1, R.id.btn_row_2, R.id.btn_row_3, R.id.btn_row_4,
                R.id.btn_row_5, R.id.btn_row_6, R.id.btn_row_7, R.id.btn_row_8,
                R.id.btn_capture, R.id.btn_check, R.id.btn_checkmate,
                R.id.btn_castle_short, R.id.btn_castle_long,
                R.id.btn_delete, R.id.btn_space, R.id.btn_enter
            )

            buttonIds.forEach { id ->
                view.findViewById<Button>(id)?.apply {
                    setBackgroundColor(buttonBackground)
                    setTextColor(textColor)
                }
            }
            
            // Appliquer au preview
            view.findViewById<TextView>(R.id.tv_notation_preview)?.apply {
                setBackgroundColor(previewBackground)
                setTextColor(previewTextColor)
            }
        }
    }

    override fun onCreateInputView(): View {
        // Choisir le layout en fonction du mode
        val layoutResource = when (layoutMode) {
            KeyboardPreferences.LayoutMode.DEFAULT -> R.layout.keyboard_view
            KeyboardPreferences.LayoutMode.PIECES_BOTTOM -> R.layout.keyboard_view_alt
        }
        
        keyboardView = layoutInflater.inflate(layoutResource, null)
        previewText = keyboardView?.findViewById(R.id.tv_notation_preview)      
        setupKeyboardButtons(keyboardView!!)
        applyButtonSize() // Appliquer taille initiale
        applyColorScheme() // Appliquer couleurs initiales
        return keyboardView!!
    }

    override fun onStartInput(attribute: EditorInfo?, restarting: Boolean) {
        super.onStartInput(attribute, restarting)
        currentNotation.clear()
        updatePreview()
    }

    private fun setupKeyboardButtons(view: View) {
        // Pièces (avec feedback vocal)
        setupButton(view, R.id.btn_rook, "R", "Tour")
        setupButton(view, R.id.btn_knight, "C", "Cavalier")
        setupButton(view, R.id.btn_bishop, "F", "Fou")
        setupButton(view, R.id.btn_queen, "D", "Dame")
        setupButton(view, R.id.btn_king, "K", "Roi")
        setupButton(view, R.id.btn_pawn, "", "Pion")  // Pion ne rajoute rien
        
        // Colonnes (avec feedback vocal)
        setupButton(view, R.id.btn_col_a, "a", "a")
        setupButton(view, R.id.btn_col_b, "b", "b")
        setupButton(view, R.id.btn_col_c, "c", "c")
        setupButton(view, R.id.btn_col_d, "d", "d")
        setupButton(view, R.id.btn_col_e, "e", "e")
        setupButton(view, R.id.btn_col_f, "f", "f")
        setupButton(view, R.id.btn_col_g, "g", "g")
        setupButton(view, R.id.btn_col_h, "h", "h")
        
        // Rangées (avec feedback vocal)
        setupButton(view, R.id.btn_row_1, "1", "1")
        setupButton(view, R.id.btn_row_2, "2", "2")
        setupButton(view, R.id.btn_row_3, "3", "3")
        setupButton(view, R.id.btn_row_4, "4", "4")
        setupButton(view, R.id.btn_row_5, "5", "5")
        setupButton(view, R.id.btn_row_6, "6", "6")
        setupButton(view, R.id.btn_row_7, "7", "7")
        setupButton(view, R.id.btn_row_8, "8", "8")
        
        // Modificateurs (avec feedback vocal)
        setupButton(view, R.id.btn_capture, "x", "capture")
        setupButton(view, R.id.btn_check, "+", "échec")
        setupButton(view, R.id.btn_checkmate, "#", "échec et mat")
        setupButton(view, R.id.btn_castle_short, "O-O", "petit roque")
        setupButton(view, R.id.btn_castle_long, "O-O-O", "grand roque")
        
        // Contrôles
        view.findViewById<Button>(R.id.btn_delete)?.setOnClickListener {
            performHapticFeedback()
            if (currentNotation.isNotEmpty()) {
                currentNotation.deleteAt(currentNotation.length - 1)
                updatePreview()
            } else {
                currentInputConnection?.deleteSurroundingText(1, 0)
            }
        }

        view.findViewById<Button>(R.id.btn_space)?.setOnClickListener {
            performHapticFeedback()
            commitNotation()
            currentInputConnection?.commitText(" ", 1)
        }

        view.findViewById<Button>(R.id.btn_enter)?.setOnClickListener {
            performHapticFeedback()
            
            // Vérifier si le coup est valide
            val notation = currentNotation.toString()
            if (notation.isNotEmpty()) {
                if (isValidChessMove(notation)) {
                    // Coup valide - commit normalement
                    commitNotation()
                } else {
                    // Coup invalide - flash rouge et vider
                    flashInvalidMove()
                    currentNotation.clear()
                    updatePreview()
                }
            }
        }
    }

    private fun setupButton(view: View, buttonId: Int, text: String) {
        view.findViewById<Button>(buttonId)?.setOnClickListener {
            performHapticFeedback()
            currentNotation.append(text)
            updatePreview()
        }
    }
    
    /**
     * Configure un bouton avec feedback vocal
     */
    private fun setupButton(view: View, buttonId: Int, text: String, vocalName: String) {
        view.findViewById<Button>(buttonId)?.setOnClickListener {
            performHapticFeedback()
            speakPiece(vocalName)
            currentNotation.append(text)
            updatePreview()
        }
    }

    /**
     * Vibre si la vibration est activée dans les settings
     */
    /**
     * Prononce le nom de la pièce avec TTS
     */
    private fun speakPiece(pieceName: String) {
        android.util.Log.d("ChessKeyboardIME", "speakPiece: '$pieceName', ttsReady=$ttsReady, tts=${tts != null}")
        if (ttsReady && pieceName.isNotEmpty() && tts != null) {
            val result = tts?.speak(pieceName, TextToSpeech.QUEUE_FLUSH, null, null)
            android.util.Log.d("ChessKeyboardIME", "TTS speak result: $result")
        }
    }

    private fun performHapticFeedback() {
        if (vibrationEnabled && vibrator?.hasVibrator() == true) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                vibrator?.vibrate(
                    VibrationEffect.createOneShot(
                        vibrationDuration.toLong(),
                        VibrationEffect.DEFAULT_AMPLITUDE
                    )
                )
            } else {
                @Suppress("DEPRECATION")
                vibrator?.vibrate(vibrationDuration.toLong())
            }
        }
    }

    private fun commitNotation() {
        if (currentNotation.isNotEmpty()) {
            currentInputConnection?.commitText(currentNotation.toString(), 1)
            currentNotation.clear()
            updatePreview()
        }
    }

    private fun updatePreview() {
        previewText?.text = if (currentNotation.isEmpty()) "Notation" else currentNotation.toString()
    }
    
    /**
     * Vérifie si un coup est valide selon la notation algébrique (SAN)
     * Patterns valides:
     * - Roques: O-O, O-O-O
     * - Coups: [RCFDK]?[a-h]?[1-8]?x?[a-h][1-8](=[RCFD])?[+#]?
     * Exemples: e4, Cf3, Fxe5, d8=D, O-O, Dh5#, Cbc6
     */
    private fun isValidChessMove(move: String): Boolean {
        if (move.isEmpty()) return false
        
        // Pattern pour les roques
        val castlePattern = Regex("^O-O(-O)?([+#])?$")
        if (castlePattern.matches(move)) return true
        
        // Pattern pour les coups normaux (notation algébrique)
        // [RCFDK]? : Pièce optionnelle (R=Tour, C=Cavalier, F=Fou, D=Dame, K=Roi, vide=pion)
        // [a-h]?[1-8]? : Désambiguïsation optionnelle
        // x? : Capture optionnelle
        // [a-h][1-8] : Destination (obligatoire)
        // (=[RCFD])? : Promotion optionnelle
        // [+#]? : Échec/Mat optionnel
        val movePattern = Regex("^[RCFDK]?[a-h]?[1-8]?x?[a-h][1-8](=[RCFD])?[+#]?$")
        return movePattern.matches(move)
    }
    
    /**
     * Anime la preview en rouge pour indiquer un coup invalide
     */
    private fun flashInvalidMove() {
        previewText?.let { textView ->
            // Obtenir la couleur de fond actuelle (drawable background)
            val isDarkMode = when (colorScheme) {
                KeyboardPreferences.ColorScheme.SYSTEM -> {
                    (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES
                }
                KeyboardPreferences.ColorScheme.DARK -> true
                KeyboardPreferences.ColorScheme.LIGHT -> false
            }
            
            val originalColor = if (isDarkMode) {
                getColor(R.color.keyboard_preview_background_dark)
            } else {
                getColor(R.color.keyboard_preview_background_light)
            }
            val redColor = Color.RED
            
            // Animation de couleur de fond: original -> rouge -> original
            val colorAnim = ValueAnimator.ofObject(ArgbEvaluator(), originalColor, redColor, originalColor)
            colorAnim.duration = 600 // 600ms total (300ms vers rouge, 300ms retour)
            colorAnim.addUpdateListener { animator ->
                textView.setBackgroundColor(animator.animatedValue as Int)
            }
            colorAnim.start()
        }
    }

    override fun onFinishInput() {
        super.onFinishInput()
        commitNotation()
    }
    
    override fun onDestroy() {
        super.onDestroy()
        settingsObserverJob?.cancel()
        serviceScope.cancel()
        
        // Libérer TTS
        tts?.stop()
        tts?.shutdown()
        tts = null
    }
}
