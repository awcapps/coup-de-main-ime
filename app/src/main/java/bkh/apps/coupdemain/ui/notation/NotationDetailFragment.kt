package bkh.apps.coupdemain.ui.notation

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import bkh.apps.coupdemain.R
import bkh.apps.coupdemain.data.database.ChessGame
import bkh.apps.coupdemain.data.database.ChessMove
import bkh.apps.coupdemain.data.database.GameResult
import bkh.apps.coupdemain.ui.viewmodel.GameViewModel
import bkh.apps.coupdemain.utils.PgnExporter
import bkh.apps.coupdemain.utils.NotationImageExporter
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class NotationDetailFragment : Fragment() {

    private val viewModel: GameViewModel by activityViewModels()
    
    private lateinit var toolbar: MaterialToolbar
    private lateinit var tvTurnIndicator: TextView
    private lateinit var tvMovesHistory: TextView
    private lateinit var etMoveInput: EditText
    private lateinit var fabAddMove: FloatingActionButton
    private lateinit var btnFinishGame: MaterialButton
    private lateinit var btnExportGame: MaterialButton
    
    private var gameId: Long = -1L
    private var currentGame: ChessGame? = null
    private val movesList = mutableListOf<ChessMove>()
    private var nextMoveNumber = 1
    private var isWhiteTurn = true
    private var userPlayingWhite = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        gameId = arguments?.getLong(ARG_GAME_ID) ?: -1L
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_notation_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        toolbar = view.findViewById(R.id.toolbar)
        tvTurnIndicator = view.findViewById(R.id.tv_turn_indicator)
        tvMovesHistory = view.findViewById(R.id.tv_moves_history)
        etMoveInput = view.findViewById(R.id.et_move_input)
        fabAddMove = view.findViewById(R.id.fab_add_move)
        btnFinishGame = view.findViewById(R.id.btn_finish_game)
        btnExportGame = view.findViewById(R.id.btn_export_game)
        
        setupToolbar()
        setupFab()
        setupFinishButton()
        setupExportButton()
        
        loadGameData()
        observeMoves()
        
        // Afficher dialog choix couleur uniquement si aucun coup joué
        lifecycleScope.launch {
            val game = viewModel.getGameById(gameId).first()
            if (game?.moveCount == 0 && savedInstanceState == null) {
                showColorChoiceDialog()
            }
        }
    }
    
    private fun setupToolbar() {
        toolbar.setNavigationIcon(android.R.drawable.ic_menu_close_clear_cancel)
        toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }
        
        // Ajouter menu (Annuler + Supprimer)
        toolbar.inflateMenu(R.menu.menu_notation)
        toolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.action_delete -> {
                    showDeleteDialog()
                    true
                }
                R.id.action_undo -> {
                    undoLastMove()
                    true
                }
                else -> false
            }
        }
    }
    
    private fun setupFab() {
        fabAddMove.setOnClickListener {
            showMoveInputDialog()
        }
    }
    
    private fun setupFinishButton() {
        btnFinishGame.setOnClickListener {
            showFinishDialog()
        }
    }
    
    private fun showMoveInputDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_move_input, null)
        val tvTitle = dialogView.findViewById<TextView>(R.id.tv_dialog_title)
        val etNotation = dialogView.findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.et_move_notation)
        
        val colorName = if (isWhiteTurn) "Blancs" else "Noirs"
        val symbol = if (isWhiteTurn) "♔" else "♚"
        tvTitle.text = "$symbol $colorName - Coup ${nextMoveNumber}${if (isWhiteTurn) "" else "..."}"
        
        val dialog = com.google.android.material.dialog.MaterialAlertDialogBuilder(requireContext())
            .setView(dialogView)
            .setPositiveButton("Ajouter") { _, _ ->
                val notation = etNotation.text.toString().trim()
                if (notation.isNotEmpty()) {
                    addMoveWithNotation(notation)
                } else {
                    Toast.makeText(requireContext(), "Notation vide", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Annuler", null)
            .create()
            
        dialog.show()
        
        // Focus automatique et ouverture clavier
        etNotation.requestFocus()
        etNotation.postDelayed({
            val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(etNotation, InputMethodManager.SHOW_IMPLICIT)
        }, 200)
    }
    
    private fun showColorChoiceDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_choose_color, null)
        val dialog = com.google.android.material.dialog.MaterialAlertDialogBuilder(requireContext())
            .setView(dialogView)
            .setCancelable(false)
            .create()
        
        dialogView.findViewById<View>(R.id.btn_play_white).setOnClickListener {
            userPlayingWhite = true
            updateTurnIndicator()
            
            // Sauvegarder dans la DB
            currentGame?.let { game ->
                lifecycleScope.launch {
                    viewModel.updateGame(game.copy(userPlaysWhite = true))
                }
            }
            
            dialog.dismiss()
            etMoveInput.requestFocus()
        }
        
        dialogView.findViewById<View>(R.id.btn_play_black).setOnClickListener {
            userPlayingWhite = false
            updateTurnIndicator()
            
            // Sauvegarder dans la DB
            currentGame?.let { game ->
                lifecycleScope.launch {
                    viewModel.updateGame(game.copy(userPlaysWhite = false))
                }
            }
            
            dialog.dismiss()
            etMoveInput.requestFocus()
        }
        
        dialog.show()
    }
    
    private fun loadGameData() {
        lifecycleScope.launch {
            val game = viewModel.getGameById(gameId).first()
            currentGame = game
            
            game?.let {
                // Titre auto avec date
                val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
                toolbar.title = it.date.format(dateFormatter)
                
                // Si partie terminée, masquer bouton finish et FAB, afficher résultat
                if (it.result != GameResult.IN_PROGRESS) {
                    btnFinishGame.visibility = View.GONE
                    fabAddMove.visibility = View.GONE
                    btnExportGame.visibility = View.VISIBLE  // Afficher export
                    toolbar.subtitle = "Résultat : ${it.result.notation}"
                    // Masquer l'icône undo dans le menu
                    toolbar.menu?.findItem(R.id.action_undo)?.isVisible = false
                } else {
                    btnFinishGame.visibility = View.VISIBLE
                    fabAddMove.visibility = View.VISIBLE
                    btnExportGame.visibility = View.GONE  // Masquer export
                    toolbar.subtitle = null
                    toolbar.menu?.findItem(R.id.action_undo)?.isVisible = true
                }
            }
        }
    }
    
    private fun observeMoves() {
        lifecycleScope.launch {
            viewModel.getMovesForGame(gameId).collect { moves ->
                movesList.clear()
                movesList.addAll(moves)
                updateMovesHistory()
                
                // Calculer le prochain coup
                if (moves.isNotEmpty()) {
                    val lastMove = moves.last()
                    if (lastMove.isWhiteMove) {
                        nextMoveNumber = lastMove.moveNumber
                        isWhiteTurn = false
                    } else {
                        nextMoveNumber = lastMove.moveNumber + 1
                        isWhiteTurn = true
                    }
                } else {
                    nextMoveNumber = 1
                    isWhiteTurn = true
                }
                
                updateTurnIndicator()
            }
        }
    }
    
    private fun updateTurnIndicator() {
        val turnSymbol = if (isWhiteTurn) "♔" else "♚"
        val turnColor = if (isWhiteTurn) "Blancs" else "Noirs"
        
        val userColor = if (userPlayingWhite) "Blancs" else "Noirs"
        val userSymbol = if (userPlayingWhite) "♔" else "♚"
        
        tvTurnIndicator.text = "$turnSymbol Au tour des $turnColor • $userSymbol Vous jouez les $userColor"
    }
    
    private fun updateMovesHistory() {
        if (movesList.isEmpty()) {
            tvMovesHistory.text = "Aucun coup joué"
            return
        }
        
        val history = StringBuilder()
        var currentMoveNumber = 0
        
        movesList.forEach { move ->
            if (move.isWhiteMove) {
                if (currentMoveNumber > 0) history.append("\n")
                currentMoveNumber = move.moveNumber
                history.append("${move.moveNumber}. ${move.notation}")
            } else {
                history.append(" ${move.notation}")
            }
        }
        
        tvMovesHistory.text = history.toString()
    }
    
    private fun addMoveWithNotation(notation: String) {
        lifecycleScope.launch {
            viewModel.addMoveSuspend(
                gameId = gameId,
                notation = notation,
                moveNumber = nextMoveNumber,
                isWhiteMove = isWhiteTurn
            )
            
            // Mettre à jour le compteur de coups
            currentGame?.let { game ->
                val updatedGame = game.copy(moveCount = game.moveCount + 1)
                viewModel.updateGame(updatedGame)
                currentGame = updatedGame
            }
        }
    }
    
    private fun undoLastMove() {
        if (movesList.isEmpty()) {
            Toast.makeText(requireContext(), "Aucun coup à annuler", Toast.LENGTH_SHORT).show()
            return
        }
        
        lifecycleScope.launch {
            viewModel.undoLastMove(gameId)
            
            currentGame?.let { game ->
                if (game.moveCount > 0) {
                    val updatedGame = game.copy(moveCount = game.moveCount - 1)
                    viewModel.updateGame(updatedGame)
                    currentGame = updatedGame
                }
            }
            
            Toast.makeText(requireContext(), "Coup annulé", Toast.LENGTH_SHORT).show()
        }
    }
    
    private fun showFinishDialog() {
        val options = arrayOf("Victoire Blancs (1-0)", "Victoire Noirs (0-1)", "Nulle (1/2-1/2)")
        
        AlertDialog.Builder(requireContext())
            .setTitle("Terminer la partie")
            .setItems(options) { _, which ->
                val result = when (which) {
                    0 -> GameResult.WHITE_WIN
                    1 -> GameResult.BLACK_WIN
                    2 -> GameResult.DRAW
                    else -> GameResult.IN_PROGRESS
                }
                finishGame(result)
            }
            .setNegativeButton("Annuler", null)
            .show()
    }
    
    private fun showDeleteDialog() {
        com.google.android.material.dialog.MaterialAlertDialogBuilder(requireContext())
            .setTitle("Supprimer cette partie ?")
            .setMessage("Cette action est irréversible. Tous les coups seront supprimés.")
            .setPositiveButton("Supprimer") { _, _ ->
                deleteGame()
            }
            .setNegativeButton("Annuler", null)
            .show()
    }
    
    private fun deleteGame() {
        lifecycleScope.launch {
            currentGame?.let { game ->
                viewModel.deleteGame(game)
                
                Toast.makeText(
                    requireContext(),
                    "Partie supprimée",
                    Toast.LENGTH_SHORT
                ).show()
                
                requireActivity().onBackPressed()
            }
        }
    }
    
    private fun finishGame(result: GameResult) {
        lifecycleScope.launch {
            viewModel.finishGame(gameId, result)
            
            Toast.makeText(
                requireContext(),
                "Partie terminée: ${result.notation}",
                Toast.LENGTH_LONG
            ).show()
            
            requireActivity().onBackPressed()
        }
    }
    
    private fun setupExportButton() {
        btnExportGame.setOnClickListener {
            showExportDialog()
        }
    }
    
    private fun showExportDialog() {
        val options = arrayOf("Format PGN (texte)", "Format JPG (image)")
        
        AlertDialog.Builder(requireContext())
            .setTitle("Choisir le format d'export")
            .setItems(options) { _, which ->
                when (which) {
                    0 -> exportAsPgn()
                    1 -> exportAsImage()
                }
            }
            .setNegativeButton("Annuler", null)
            .show()
    }
    
    private fun exportAsPgn() {
        lifecycleScope.launch {
            val game = currentGame ?: return@launch
            val moves = viewModel.getMovesForGame(gameId).first()
            
            try {
                // Générer le contenu PGN
                val pgnContent = PgnExporter.generatePgn(game, moves)
                
                // Créer le fichier dans le cache
                val fileName = PgnExporter.generateFileName(game)
                val file = File(requireContext().cacheDir, fileName)
                file.writeText(pgnContent)
                
                // Partager
                shareFile(file, "text/plain", "Partager la partie (PGN)")
                
            } catch (e: Exception) {
                Toast.makeText(
                    requireContext(),
                    "Erreur lors de l'export: ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
    
    private fun exportAsImage() {
        lifecycleScope.launch {
            val game = currentGame ?: return@launch
            val moves = viewModel.getMovesForGame(gameId).first()
            
            try {
                // Générer l'image
                val bitmap = NotationImageExporter.generateImage(requireContext(), game, moves)
                
                // Sauvegarder dans le cache
                val fileName = NotationImageExporter.generateFileName(game)
                val file = File(requireContext().cacheDir, fileName)
                FileOutputStream(file).use { out ->
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 95, out)
                }
                
                // Libérer le bitmap
                bitmap.recycle()
                
                // Partager
                shareFile(file, "image/jpeg", "Partager la notation (Image)")
                
            } catch (e: Exception) {
                Toast.makeText(
                    requireContext(),
                    "Erreur lors de l'export: ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
    
    private fun shareFile(file: File, mimeType: String, title: String) {
        try {
            // Utiliser FileProvider pour partager le fichier
            val uri = FileProvider.getUriForFile(
                requireContext(),
                "${requireContext().packageName}.fileprovider",
                file
            )
            
            // Créer l'intent de partage
            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = mimeType
                putExtra(Intent.EXTRA_STREAM, uri)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            
            // Lancer le chooser
            startActivity(Intent.createChooser(shareIntent, title))
            
        } catch (e: Exception) {
            Toast.makeText(
                requireContext(),
                "Erreur lors du partage: ${e.message}",
                Toast.LENGTH_LONG
            ).show()
        }
    }
    
    companion object {
        private const val ARG_GAME_ID = "game_id"
        
        fun newInstance(gameId: Long): NotationDetailFragment {
            return NotationDetailFragment().apply {
                arguments = Bundle().apply {
                    putLong(ARG_GAME_ID, gameId)
                }
            }
        }
    }
}
