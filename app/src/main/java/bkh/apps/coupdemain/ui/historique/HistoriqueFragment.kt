package bkh.apps.coupdemain.ui.historique

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import bkh.apps.coupdemain.MainActivity
import bkh.apps.coupdemain.R
import bkh.apps.coupdemain.ui.viewmodel.GameViewModel
import com.google.android.material.card.MaterialCardView
import kotlinx.coroutines.launch

class HistoriqueFragment : Fragment() {

    private val viewModel: GameViewModel by activityViewModels()
    private lateinit var recyclerView: RecyclerView
    private lateinit var emptyStateCard: MaterialCardView
    private lateinit var adapter: HistoriqueAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_historique, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        recyclerView = view.findViewById(R.id.recycler_games)
        emptyStateCard = view.findViewById(R.id.empty_state_card)
        
        // Configuration RecyclerView
        adapter = HistoriqueAdapter { gameId ->
            openGameDetail(gameId)
        }
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter
        
        // Observer toutes les parties
        observeAllGames()
    }
    
    private fun observeAllGames() {
        lifecycleScope.launch {
            viewModel.allGames.collect { games ->
                if (games.isEmpty()) {
                    showEmptyState()
                } else {
                    showGames(games)
                }
            }
        }
    }
    
    private fun showEmptyState() {
        recyclerView.visibility = View.GONE
        emptyStateCard.visibility = View.VISIBLE
    }
    
    private fun showGames(games: List<bkh.apps.coupdemain.data.database.ChessGame>) {
        recyclerView.visibility = View.VISIBLE
        emptyStateCard.visibility = View.GONE
        adapter.submitList(games)
    }
    
    private fun openGameDetail(gameId: Long) {
        (requireActivity() as? MainActivity)?.openGameDetail(gameId)
    }
}

