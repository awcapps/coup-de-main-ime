package bkh.apps.coupdemain.ui.historique

import android.graphics.Typeface
import android.text.SpannableString
import android.text.Spanned
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import bkh.apps.coupdemain.R
import bkh.apps.coupdemain.data.database.ChessGame
import com.google.android.material.card.MaterialCardView
import com.google.android.material.chip.Chip
import java.time.format.DateTimeFormatter
import java.util.Locale

class HistoriqueAdapter(
    private val onGameClick: (Long) -> Unit
) : ListAdapter<ChessGame, HistoriqueAdapter.GameViewHolder>(GameDiffCallback()) {

    private val titleFormatter = DateTimeFormatter.ofPattern("d MMMM yyyy HH:mm", Locale.FRENCH)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_game_card, parent, false)
        return GameViewHolder(view, onGameClick, titleFormatter)
    }

    override fun onBindViewHolder(holder: GameViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class GameViewHolder(
        itemView: View,
        private val onGameClick: (Long) -> Unit,
        private val titleFormatter: DateTimeFormatter
    ) : RecyclerView.ViewHolder(itemView) {

        private val cardView: MaterialCardView = itemView.findViewById(R.id.game_card)
        private val tvTitle: TextView = itemView.findViewById(R.id.tv_game_title)
        private val tvMoveCount: TextView = itemView.findViewById(R.id.tv_move_count)
        private val tvResult: TextView = itemView.findViewById(R.id.tv_result)
        private val tvWhitePlayer: TextView = itemView.findViewById(R.id.tv_white_player)
        private val tvBlackPlayer: TextView = itemView.findViewById(R.id.tv_black_player)
        private val chipStatus: Chip = itemView.findViewById(R.id.chip_status)

        fun bind(game: ChessGame) {
            tvTitle.text = game.title
            tvMoveCount.text = "${game.moveCount} coups"
            tvResult.text = game.result.notation

            // Joueurs avec mise en gras selon userPlaysWhite
            val whiteText = if (game.whitePlayer.isNotEmpty()) "♔ ${game.whitePlayer}" else "♔ Blancs"
            val blackText = if (game.blackPlayer.isNotEmpty()) "♚ ${game.blackPlayer}" else "♚ Noirs"
            
            // Mettre en gras le joueur que l'utilisateur joue
            if (game.userPlaysWhite) {
                tvWhitePlayer.text = SpannableString(whiteText).apply {
                    setSpan(StyleSpan(Typeface.BOLD), 0, length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                }
                tvBlackPlayer.text = blackText
            } else {
                tvWhitePlayer.text = whiteText
                tvBlackPlayer.text = SpannableString(blackText).apply {
                    setSpan(StyleSpan(Typeface.BOLD), 0, length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                }
            }

            // Chip statut
            if (game.isCompleted) {
                chipStatus.text = "Terminée"
                chipStatus.setChipBackgroundColorResource(android.R.color.holo_green_light)
            } else {
                chipStatus.text = "En cours"
                chipStatus.setChipBackgroundColorResource(android.R.color.holo_orange_light)
            }

            // Click listener
            cardView.setOnClickListener {
                onGameClick(game.id)
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
        }
    }

    class GameDiffCallback : DiffUtil.ItemCallback<ChessGame>() {
        override fun areItemsTheSame(oldItem: ChessGame, newItem: ChessGame): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ChessGame, newItem: ChessGame): Boolean {
            return oldItem == newItem
        }
    }
}
