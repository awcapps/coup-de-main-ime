package bkh.apps.coupdemain.data.repository

import bkh.apps.coupdemain.data.database.ChessGame
import bkh.apps.coupdemain.data.database.ChessMove
import bkh.apps.coupdemain.data.database.GameDao
import bkh.apps.coupdemain.data.database.GameResult
import bkh.apps.coupdemain.data.database.GameWithMoves
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import java.time.LocalDateTime

/**
 * Repository pour gérer les données de parties d'échecs
 * Couche d'abstraction entre le DAO et le ViewModel
 */
class ChessGameRepository(private val gameDao: GameDao) {
    
    // ==================== GAMES ====================
    
    /**
     * Toutes les parties (Flow réactif)
     */
    val allGames: Flow<List<ChessGame>> = gameDao.getAllGames()
    
    /**
     * 3 dernières parties pour l'écran d'accueil
     */
    val recentGames: Flow<List<ChessGame>> = gameDao.getRecentGames()
    
    /**
     * Parties en cours
     */
    val ongoingGames: Flow<List<ChessGame>> = gameDao.getOngoingGames()
    
    /**
     * Parties terminées
     */
    val completedGames: Flow<List<ChessGame>> = gameDao.getCompletedGames()
    
    /**
     * Récupérer une partie par ID
     */
    fun getGameById(gameId: Long): Flow<ChessGame?> {
        return gameDao.getGameById(gameId)
    }
    
    /**
     * Récupérer une partie avec tous ses coups
     */
    fun getGameWithMoves(gameId: Long): Flow<GameWithMoves?> {
        return gameDao.getGameWithMoves(gameId)
    }
    
    /**
     * Créer une nouvelle partie
     */
    suspend fun createGame(
        title: String = "Nouvelle partie",
        whitePlayer: String = "",
        blackPlayer: String = ""
    ): Long {
        val game = ChessGame(
            title = title,
            whitePlayer = whitePlayer,
            blackPlayer = blackPlayer,
            result = GameResult.IN_PROGRESS,
            date = LocalDateTime.now(),
            moveCount = 0,
            isCompleted = false
        )
        return gameDao.insertGame(game)
    }
    
    /**
     * Mettre à jour une partie
     */
    suspend fun updateGame(game: ChessGame) {
        gameDao.updateGame(game)
    }
    
    /**
     * Terminer une partie avec résultat
     */
    suspend fun finishGame(gameId: Long, result: GameResult) {
        val currentGame = gameDao.getGameById(gameId).first()
        currentGame?.let { game ->
            val updatedGame = game.copy(
                result = result,
                isCompleted = true
            )
            gameDao.updateGame(updatedGame)
        }
    }
    
    /**
     * Supprimer une partie (CASCADE supprime aussi les coups)
     */
    suspend fun deleteGame(game: ChessGame) {
        gameDao.deleteGame(game)
    }
    
    // ==================== MOVES ====================
    
    /**
     * Récupérer tous les coups d'une partie
     */
    fun getMovesForGame(gameId: Long): Flow<List<ChessMove>> {
        return gameDao.getMovesForGame(gameId)
    }
    
    /**
     * Ajouter un coup à une partie
     */
    suspend fun addMove(
        gameId: Long,
        notation: String,
        moveNumber: Int,
        isWhiteMove: Boolean
    ): Long {
        val move = ChessMove(
            gameId = gameId,
            moveNumber = moveNumber,
            notation = notation,
            isWhiteMove = isWhiteMove,
            timestamp = LocalDateTime.now()
        )
        val moveId = gameDao.insertMove(move)
        
        // Mettre à jour le compteur de coups de la partie
        val moveCount = gameDao.getMoveCount(gameId)
        // TODO: récupérer et mettre à jour le ChessGame
        
        return moveId
    }
    
    /**
     * Annuler le dernier coup d'une partie
     */
    suspend fun undoLastMove(gameId: Long) {
        gameDao.deleteLastMove(gameId)
        
        // Mettre à jour le compteur de coups
        val moveCount = gameDao.getMoveCount(gameId)
        // TODO: récupérer et mettre à jour le ChessGame
    }
    
    /**
     * Supprimer un coup spécifique
     */
    suspend fun deleteMove(move: ChessMove) {
        gameDao.deleteMove(move)
    }
    
    /**
     * Récupérer le dernier coup d'une partie
     */
    suspend fun getLastMove(gameId: Long): ChessMove? {
        return gameDao.getLastMove(gameId)
    }
    
    /**
     * Compter les coups d'une partie
     */
    suspend fun getMoveCount(gameId: Long): Int {
        return gameDao.getMoveCount(gameId)
    }
}
