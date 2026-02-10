package bkh.apps.coupdemain.utils

import bkh.apps.coupdemain.data.database.ChessGame
import bkh.apps.coupdemain.data.database.ChessMove
import bkh.apps.coupdemain.data.database.GameResult
import java.time.format.DateTimeFormatter
import java.util.Locale

/**
 * Générateur de fichiers PGN (Portable Game Notation)
 * Format standard pour l'exportation de parties d'échecs
 */
object PgnExporter {
    
    /**
     * Génère le contenu PGN complet pour une partie
     * 
     * @param game La partie à exporter
     * @param moves Liste des coups de la partie
     * @return Chaîne PGN formatée selon le standard
     */
    fun generatePgn(game: ChessGame, moves: List<ChessMove>): String {
        val pgn = StringBuilder()
        
        // Headers PGN obligatoires (Seven Tag Roster)
        pgn.appendLine("[Event \"Partie notée\"]")
        pgn.appendLine("[Site \"Coup de Main App\"]")
        
        // Date au format YYYY.MM.DD
        val dateFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd", Locale.FRENCH)
        pgn.appendLine("[Date \"${game.date.format(dateFormatter)}\"]")
        
        // Round
        pgn.appendLine("[Round \"-\"]")
        
        // Joueurs (utiliser le champ userPlaysWhite pour déterminer)
        val whiteName = if (game.userPlaysWhite) "Utilisateur" else "Adversaire"
        val blackName = if (game.userPlaysWhite) "Adversaire" else "Utilisateur"
        pgn.appendLine("[White \"$whiteName\"]")
        pgn.appendLine("[Black \"$blackName\"]")
        
        // Résultat
        pgn.appendLine("[Result \"${game.result.notation}\"]")
        
        // Headers optionnels
        val timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss", Locale.FRENCH)
        pgn.appendLine("[Time \"${game.date.format(timeFormatter)}\"]")
        pgn.appendLine("[Application \"Coup de Main v1.0\"]")
        
        // Ligne vide entre headers et coups
        pgn.appendLine()
        
        // Coups formatés
        pgn.append(formatMoves(moves, game.result))
        
        return pgn.toString()
    }
    
    /**
     * Formate les coups en notation PGN standard
     * Format : "1. e4 e5 2. Nf3 Nc6 3. Bb5 a6"
     */
    private fun formatMoves(moves: List<ChessMove>, result: GameResult): String {
        val formatted = StringBuilder()
        var currentMoveNumber = 0
        
        for (move in moves) {
            if (move.isWhiteMove) {
                // Nouveau coup, ajouter le numéro
                currentMoveNumber = move.moveNumber
                if (formatted.isNotEmpty()) {
                    formatted.append(" ")
                }
                formatted.append("$currentMoveNumber. ${move.notation}")
            } else {
                // Coup noir, juste ajouter la notation
                formatted.append(" ${move.notation}")
            }
        }
        
        // Ajouter le résultat à la fin sur une nouvelle ligne (standard PGN)
        if (formatted.isNotEmpty()) {
            formatted.append("\n\n")
        }
        formatted.append(result.notation)
        
        return formatted.toString()
    }
    
    /**
     * Génère un nom de fichier PGN basé sur la date
     */
    fun generateFileName(game: ChessGame): String {
        val dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss", Locale.FRENCH)
        return "partie_${game.date.format(dateFormatter)}.pgn"
    }
}
