package bkh.apps.coupdemain.ui.home

import android.graphics.Typeface
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import bkh.apps.coupdemain.MainActivity
import bkh.apps.coupdemain.R
import bkh.apps.coupdemain.data.database.ChessGame
import bkh.apps.coupdemain.data.database.GameResult
import bkh.apps.coupdemain.ui.viewmodel.GameViewModel
import com.google.android.material.card.MaterialCardView
import com.google.android.material.chip.Chip
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.time.format.DateTimeFormatter
import java.util.Locale

class HomeFragment : Fragment() {

    private val viewModel: GameViewModel by activityViewModels()
    private lateinit var recentGamesContainer: LinearLayout
    private lateinit var emptyStateCard: MaterialCardView
    private lateinit var fabNewGame: FloatingActionButton
    
    private val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    private val titleFormatter = DateTimeFormatter.ofPattern("d MMMM yyyy HH:mm", Locale.FRENCH)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recentGamesContainer = view.findViewById(R.id.recent_games_container)
        emptyStateCard = view.findViewById(R.id.empty_state_card)
        fabNewGame = view.findViewById(R.id.fab_new_game)
        
        // Observer les 3 dernières parties
        observeRecentGames()
        
        // FAB pour créer nouvelle partie
        fabNewGame.setOnClickListener {
            createNewGame()
        }
    }
    
    private fun observeRecentGames() {
        lifecycleScope.launch {
            viewModel.recentGames.collect { games ->
                if (games.isEmpty()) {
                    showEmptyState()
                } else {
                    showRecentGames(games)
                }
            }
        }
    }
    
    private fun showEmptyState() {
        recentGamesContainer.visibility = View.GONE
        emptyStateCard.visibility = View.VISIBLE
    }
    
    private fun showRecentGames(games: List<ChessGame>) {
        recentGamesContainer.visibility = View.VISIBLE
        emptyStateCard.visibility = View.GONE
        recentGamesContainer.removeAllViews()
        
        games.forEach { game ->
            val gameCard = createGameCard(game)
            recentGamesContainer.addView(gameCard)
        }
    }
    
    private fun createGameCard(game: ChessGame): View {
        val cardView = layoutInflater.inflate(R.layout.item_game_card, recentGamesContainer, false)
        
        cardView.findViewById<TextView>(R.id.tv_game_title).text = game.title
        cardView.findViewById<TextView>(R.id.tv_move_count).text = "${game.moveCount} coups"
        cardView.findViewById<TextView>(R.id.tv_result).text = game.result.notation
        
        // Joueurs avec mise en gras selon userPlaysWhite
        val whiteText = if (game.whitePlayer.isNotEmpty()) "♔ ${game.whitePlayer}" else "♔ Blancs"
        val blackText = if (game.blackPlayer.isNotEmpty()) "♚ ${game.blackPlayer}" else "♚ Noirs"
        
        val tvWhite = cardView.findViewById<TextView>(R.id.tv_white_player)
        val tvBlack = cardView.findViewById<TextView>(R.id.tv_black_player)
        
        // Mettre en gras le joueur que l'utilisateur joue
        if (game.userPlaysWhite) {
            tvWhite.text = SpannableString(whiteText).apply {
                setSpan(StyleSpan(Typeface.BOLD), 0, length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            }
            tvBlack.text = blackText
        } else {
            tvWhite.text = whiteText
            tvBlack.text = SpannableString(blackText).apply {
                setSpan(StyleSpan(Typeface.BOLD), 0, length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            }
        }
        
        // Chip statut
        val chipStatus = cardView.findViewById<Chip>(R.id.chip_status)
        if (game.isCompleted) {
            chipStatus.text = "Terminée"
            chipStatus.chipBackgroundColor = resources.getColorStateList(android.R.color.holo_green_light, null)
        } else {
            chipStatus.text = "En cours"
            chipStatus.chipBackgroundColor = resources.getColorStateList(android.R.color.holo_orange_light, null)
        }
        
        // Click sur la carte → ouvrir détail
        cardView.setOnClickListener {
            openGameDetail(game.id)
        }
        
        // ContentDescription pour accessibilité TalkBack
        val statusText = if (game.isCompleted) "Terminée" else "En cours"
        val formattedDate = game.date.format(titleFormatter)
        cardView.contentDescription = "Partie du $formattedDate, ${game.moveCount} coups, statut $statusText, résultat ${game.result.notation}"
        
        // Animation fade in
        cardView.alpha = 0f
        cardView.animate()
            .alpha(1f)
            .setDuration(300)
            .start()
        
        return cardView
    }
    
    private fun createNewGame() {
        lifecycleScope.launch {
            val now = java.time.LocalDateTime.now()
            val title = titleFormatter.format(now)
            
            val gameId = viewModel.createGameSuspend(
                title = title,
                whitePlayer = "",
                blackPlayer = ""
            )
            openGameDetail(gameId)
        }
    }
    
    private fun openGameDetail(gameId: Long) {
        (requireActivity() as? MainActivity)?.openGameDetail(gameId)
    }
}

