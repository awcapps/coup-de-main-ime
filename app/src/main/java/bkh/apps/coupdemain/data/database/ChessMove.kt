package bkh.apps.coupdemain.data.database

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.LocalDateTime

/**
 * Entity représentant un coup d'échecs
 */
@Entity(
    tableName = "chess_moves",
    foreignKeys = [
        ForeignKey(
            entity = ChessGame::class,
            parentColumns = ["id"],
            childColumns = ["gameId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["gameId"])]
)
data class ChessMove(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    
    val gameId: Long,               // Référence à la partie
    val moveNumber: Int,            // Numéro du coup (1, 2, 3...)
    val notation: String,           // Notation SAN (ex: "Nf3", "e4", "O-O")
    val isWhiteMove: Boolean,       // true = coup des blancs, false = coup des noirs
    val timestamp: LocalDateTime = LocalDateTime.now() // Horodatage du coup
)
