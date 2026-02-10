package bkh.apps.coupdemain.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * Entity représentant une partie d'échecs
 */
@Entity(tableName = "chess_games")
data class ChessGame(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    
    val title: String,              // Ex: "Partie du soir", "Training vs Hadrien"
    val whitePlayer: String,        // Nom joueur blancs
    val blackPlayer: String,        // Nom joueur noirs
    val result: GameResult,         // 1-0, 0-1, 1/2-1/2, EN_COURS
    val date: LocalDateTime,        // Date de création
    val moveCount: Int = 0,         // Nombre de coups joués
    val isCompleted: Boolean = false, // Partie terminée ou en cours
    val userPlaysWhite: Boolean = true // Indique si l'utilisateur joue les blancs
)

/**
 * Résultat d'une partie
 */
enum class GameResult(val notation: String) {
    WHITE_WIN("1-0"),
    BLACK_WIN("0-1"),
    DRAW("1/2-1/2"),
    IN_PROGRESS("*")
}

/**
 * TypeConverter pour LocalDateTime
 */
class Converters {
    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME
    
    @TypeConverter
    fun fromTimestamp(value: String?): LocalDateTime? {
        return value?.let { LocalDateTime.parse(it, formatter) }
    }
    
    @TypeConverter
    fun dateToTimestamp(date: LocalDateTime?): String? {
        return date?.format(formatter)
    }
    
    @TypeConverter
    fun fromGameResult(result: GameResult): String {
        return result.name
    }
    
    @TypeConverter
    fun toGameResult(value: String): GameResult {
        return GameResult.valueOf(value)
    }
}
