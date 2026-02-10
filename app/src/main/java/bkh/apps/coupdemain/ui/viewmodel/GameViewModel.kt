package bkh.apps.coupdemain.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import bkh.apps.coupdemain.data.database.ChessDatabase
import bkh.apps.coupdemain.data.database.ChessGame
import bkh.apps.coupdemain.data.database.ChessMove
import bkh.apps.coupdemain.data.database.GameResult
import bkh.apps.coupdemain.data.database.GameWithMoves
import bkh.apps.coupdemain.data.repository.ChessGameRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

/**
 * ViewModel partagé pour gérer les parties d'échecs
 * Partagé entre HomeFragment, HistoriqueFragment et NotationDetailFragment
 */
class GameViewModel(application: Application) : AndroidViewModel(application) {
    
    private val repository: ChessGameRepository
    
    // Flow observables pour l'UI
    val allGames: Flow<List<ChessGame>>
    val recentGames: Flow<List<ChessGame>>
    val ongoingGames: Flow<List<ChessGame>>
    val completedGames: Flow<List<ChessGame>>
    
    init {
        val gameDao = ChessDatabase.getDatabase(application).gameDao()
        repository = ChessGameRepository(gameDao)
        
        allGames = repository.allGames
        recentGames = repository.recentGames
        ongoingGames = repository.ongoingGames
        completedGames = repository.completedGames
    }
    
    // ==================== GAMES ====================
    
    /**
     * Créer une nouvelle partie
     * Retourne l'ID de la partie créée
     */
    fun createGame(
        title: String = "Nouvelle partie",
        whitePlayer: String = "",
        blackPlayer: String = ""
    ): Long {
        var gameId: Long = 0
        viewModelScope.launch {
            gameId = repository.createGame(title, whitePlayer, blackPlayer)
        }
        return gameId
    }
    
    /**
     * Créer une nouvelle partie (suspend pour usage direct)
     */
    suspend fun createGameSuspend(
        title: String = "Nouvelle partie",
        whitePlayer: String = "",
        blackPlayer: String = ""
    ): Long {
        return repository.createGame(title, whitePlayer, blackPlayer)
    }
    
    /**
     * Récupérer une partie par ID
     */
    fun getGameById(gameId: Long): Flow<ChessGame?> {
        return repository.getGameById(gameId)
    }
    
    /**
     * Récupérer une partie avec tous ses coups
     */
    fun getGameWithMoves(gameId: Long): Flow<GameWithMoves?> {
        return repository.getGameWithMoves(gameId)
    }
    
    /**
     * Mettre à jour une partie
     */
    fun updateGame(game: ChessGame) {
        viewModelScope.launch {
            repository.updateGame(game)
        }
    }
    
    /**
     * Terminer une partie avec résultat
     */
    fun finishGame(gameId: Long, result: GameResult) {
        viewModelScope.launch {
            repository.finishGame(gameId, result)
        }
    }
    
    /**
     * Supprimer une partie (CASCADE supprime aussi les coups)
     */
    fun deleteGame(game: ChessGame) {
        viewModelScope.launch {
            repository.deleteGame(game)
        }
    }
    
    // ==================== MOVES ====================
    
    /**
     * Récupérer tous les coups d'une partie
     */
    fun getMovesForGame(gameId: Long): Flow<List<ChessMove>> {
        return repository.getMovesForGame(gameId)
    }
    
    /**
     * Ajouter un coup à une partie
     */
    fun addMove(
        gameId: Long,
        notation: String,
        moveNumber: Int,
        isWhiteMove: Boolean
    ) {
        viewModelScope.launch {
            repository.addMove(gameId, notation, moveNumber, isWhiteMove)
        }
    }
    
    /**
     * Ajouter un coup (suspend pour usage direct)
     */
    suspend fun addMoveSuspend(
        gameId: Long,
        notation: String,
        moveNumber: Int,
        isWhiteMove: Boolean
    ): Long {
        return repository.addMove(gameId, notation, moveNumber, isWhiteMove)
    }
    
    /**
     * Annuler le dernier coup d'une partie
     */
    fun undoLastMove(gameId: Long) {
        viewModelScope.launch {
            repository.undoLastMove(gameId)
        }
    }
    
    /**
     * Récupérer le dernier coup d'une partie
     */
    suspend fun getLastMove(gameId: Long): ChessMove? {
        return repository.getLastMove(gameId)
    }
    
    /**
     * Compter les coups d'une partie
     */
    suspend fun getMoveCount(gameId: Long): Int {
        return repository.getMoveCount(gameId)
    }
}
