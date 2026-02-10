package bkh.apps.coupdemain.data.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object pour les parties d'échecs
 */
@Dao
interface GameDao {
    
    // ==================== GAMES ====================
    
    /**
     * Insérer une nouvelle partie
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGame(game: ChessGame): Long
    
    /**
     * Mettre à jour une partie existante
     */
    @Update
    suspend fun updateGame(game: ChessGame)
    
    /**
     * Supprimer une partie (CASCADE supprime aussi les coups)
     */
    @Delete
    suspend fun deleteGame(game: ChessGame)
    
    /**
     * Récupérer toutes les parties (Flow pour réactivité)
     * Triées par date DESC (plus récentes en premier)
     */
    @Query("SELECT * FROM chess_games ORDER BY date DESC")
    fun getAllGames(): Flow<List<ChessGame>>
    
    /**
     * Récupérer une partie par ID
     */
    @Query("SELECT * FROM chess_games WHERE id = :gameId")
    fun getGameById(gameId: Long): Flow<ChessGame?>
    
    /**
     * Récupérer les 3 dernières parties pour l'accueil
     */
    @Query("SELECT * FROM chess_games ORDER BY date DESC LIMIT 3")
    fun getRecentGames(): Flow<List<ChessGame>>
    
    /**
     * Récupérer toutes les parties en cours
     */
    @Query("SELECT * FROM chess_games WHERE isCompleted = 0 ORDER BY date DESC")
    fun getOngoingGames(): Flow<List<ChessGame>>
    
    /**
     * Récupérer toutes les parties terminées
     */
    @Query("SELECT * FROM chess_games WHERE isCompleted = 1 ORDER BY date DESC")
    fun getCompletedGames(): Flow<List<ChessGame>>
    
    // ==================== MOVES ====================
    
    /**
     * Insérer un coup
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMove(move: ChessMove): Long
    
    /**
     * Supprimer un coup spécifique
     */
    @Delete
    suspend fun deleteMove(move: ChessMove)
    
    /**
     * Récupérer tous les coups d'une partie
     * Triés par moveNumber ASC (ordre chronologique)
     */
    @Query("SELECT * FROM chess_moves WHERE gameId = :gameId ORDER BY moveNumber ASC, isWhiteMove DESC")
    fun getMovesForGame(gameId: Long): Flow<List<ChessMove>>
    
    /**
     * Récupérer le dernier coup d'une partie
     */
    @Query("SELECT * FROM chess_moves WHERE gameId = :gameId ORDER BY moveNumber DESC, isWhiteMove ASC LIMIT 1")
    suspend fun getLastMove(gameId: Long): ChessMove?
    
    /**
     * Supprimer le dernier coup d'une partie (pour fonction Annuler)
     */
    @Query("DELETE FROM chess_moves WHERE id = (SELECT id FROM chess_moves WHERE gameId = :gameId ORDER BY moveNumber DESC, isWhiteMove ASC LIMIT 1)")
    suspend fun deleteLastMove(gameId: Long)
    
    /**
     * Compter le nombre de coups d'une partie
     */
    @Query("SELECT COUNT(*) FROM chess_moves WHERE gameId = :gameId")
    suspend fun getMoveCount(gameId: Long): Int
    
    // ==================== COMPOSITE ====================
    
    /**
     * Récupérer une partie avec tous ses coups
     */
    @Transaction
    @Query("SELECT * FROM chess_games WHERE id = :gameId")
    fun getGameWithMoves(gameId: Long): Flow<GameWithMoves?>
}

/**
 * Relation One-to-Many : Une partie a plusieurs coups
 */
data class GameWithMoves(
    @Embedded val game: ChessGame,
    @Relation(
        parentColumn = "id",
        entityColumn = "gameId"
    )
    val moves: List<ChessMove>
)
