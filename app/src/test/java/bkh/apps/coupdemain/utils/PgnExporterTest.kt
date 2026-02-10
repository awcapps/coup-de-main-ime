package bkh.apps.coupdemain.utils

import bkh.apps.coupdemain.data.database.ChessGame
import bkh.apps.coupdemain.data.database.ChessMove
import bkh.apps.coupdemain.data.database.GameResult
import org.junit.Test
import org.junit.Assert.*
import java.time.LocalDateTime

/**
 * Tests unitaires pour PgnExporter
 * Vérifie le format PGN standard et la compatibilité Lichess
 */
class PgnExporterTest {

    private fun createTestGame(
        userPlaysWhite: Boolean = true,
        result: GameResult = GameResult.WHITE_WIN,
        date: LocalDateTime = LocalDateTime.of(2026, 2, 6, 21, 30, 0)
    ): ChessGame {
        return ChessGame(
            id = 1L,
            title = "Partie de test",
            date = date,
            whitePlayer = "Joueur Blanc",
            blackPlayer = "Joueur Noir",
            result = result,
            isCompleted = true,
            moveCount = 3,
            userPlaysWhite = userPlaysWhite
        )
    }

    private fun createTestMoves(): List<ChessMove> {
        return listOf(
            ChessMove(id = 1, gameId = 1, moveNumber = 1, notation = "e4", isWhiteMove = true, timestamp = LocalDateTime.now()),
            ChessMove(id = 2, gameId = 1, moveNumber = 1, notation = "e5", isWhiteMove = false, timestamp = LocalDateTime.now()),
            ChessMove(id = 3, gameId = 1, moveNumber = 2, notation = "Cf3", isWhiteMove = true, timestamp = LocalDateTime.now()),
            ChessMove(id = 4, gameId = 1, moveNumber = 2, notation = "Cc6", isWhiteMove = false, timestamp = LocalDateTime.now()),
            ChessMove(id = 5, gameId = 1, moveNumber = 3, notation = "Fb5", isWhiteMove = true, timestamp = LocalDateTime.now())
        )
    }

    @Test
    fun `generatePgn contient tous les headers obligatoires (Seven Tag Roster)`() {
        val game = createTestGame()
        val moves = createTestMoves()
        
        val pgn = PgnExporter.generatePgn(game, moves)
        
        // Vérifier présence des 7 headers obligatoires
        assertTrue("PGN doit contenir [Event]", pgn.contains("[Event \"Partie notée\"]"))
        assertTrue("PGN doit contenir [Site]", pgn.contains("[Site \"Coup de Main App\"]"))
        assertTrue("PGN doit contenir [Date]", pgn.contains("[Date \"2026.02.06\"]"))
        assertTrue("PGN doit contenir [Round]", pgn.contains("[Round \"-\"]"))
        assertTrue("PGN doit contenir [White]", pgn.contains("[White \"Utilisateur\"]"))
        assertTrue("PGN doit contenir [Black]", pgn.contains("[Black \"Adversaire\"]"))
        assertTrue("PGN doit contenir [Result]", pgn.contains("[Result \"1-0\"]"))
    }

    @Test
    fun `generatePgn headers optionnels Time et Application présents`() {
        val game = createTestGame()
        val moves = createTestMoves()
        
        val pgn = PgnExporter.generatePgn(game, moves)
        
        assertTrue("PGN doit contenir [Time]", pgn.contains("[Time \"21:30:00\"]"))
        assertTrue("PGN doit contenir [Application]", pgn.contains("[Application \"Coup de Main v1.0\"]"))
    }

    @Test
    fun `generatePgn joueurs inversés si userPlaysWhite false`() {
        val game = createTestGame(userPlaysWhite = false)
        val moves = createTestMoves()
        
        val pgn = PgnExporter.generatePgn(game, moves)
        
        assertTrue("Blancs doivent être Adversaire", pgn.contains("[White \"Adversaire\"]"))
        assertTrue("Noirs doivent être Utilisateur", pgn.contains("[Black \"Utilisateur\"]"))
    }

    @Test
    fun `generatePgn format des coups correct (1 e4 e5 2 Cf3 Cc6)`() {
        val game = createTestGame()
        val moves = createTestMoves()
        
        val pgn = PgnExporter.generatePgn(game, moves)
        
        // Extraire les coups (après la ligne vide)
        val lines = pgn.lines().toList()
        val movesStartIndex = lines.indexOfFirst { it.isEmpty() } + 1
        val movesSection = lines.drop(movesStartIndex).joinToString("\n")
        
        // Vérifier format : "1. e4 e5 2. Cf3 Cc6 3. Fb5"
        assertTrue("Les coups doivent contenir '1. e4 e5'", movesSection.contains("1. e4 e5"))
        assertTrue("Les coups doivent contenir '2. Cf3 Cc6'", movesSection.contains("2. Cf3 Cc6"))
        assertTrue("Les coups doivent contenir '3. Fb5'", movesSection.contains("3. Fb5"))
    }

    @Test
    fun `generatePgn résultat sur nouvelle ligne après ligne vide (standard PGN)`() {
        val game = createTestGame(result = GameResult.DRAW)
        val moves = createTestMoves()
        
        val pgn = PgnExporter.generatePgn(game, moves)
        
        // Vérifier que le résultat est sur une nouvelle ligne après ligne vide
        val lines = pgn.lines().toList()
        val lastNonEmptyLine = lines.lastOrNull { it.isNotEmpty() }
        
        assertEquals("Le résultat doit être '1/2-1/2' sur la dernière ligne", "1/2-1/2", lastNonEmptyLine)
        
        // Vérifier qu'il y a une ligne vide avant le résultat
        val movesSection = lines.dropWhile { it.startsWith("[") || it.isEmpty() }
        val hasEmptyLineBeforeResult = movesSection.dropLast(1).any { it.isEmpty() }
        assertTrue("Il doit y avoir une ligne vide avant le résultat", hasEmptyLineBeforeResult)
    }

    @Test
    fun `generatePgn tous les résultats possibles`() {
        val moves = createTestMoves()
        
        // Test WHITE_WIN (1-0)
        val pgnWhiteWin = PgnExporter.generatePgn(createTestGame(result = GameResult.WHITE_WIN), moves)
        assertTrue("PGN doit contenir résultat 1-0", pgnWhiteWin.lines().last() == "1-0")
        
        // Test BLACK_WIN (0-1)
        val pgnBlackWin = PgnExporter.generatePgn(createTestGame(result = GameResult.BLACK_WIN), moves)
        assertTrue("PGN doit contenir résultat 0-1", pgnBlackWin.lines().last() == "0-1")
        
        // Test DRAW (1/2-1/2)
        val pgnDraw = PgnExporter.generatePgn(createTestGame(result = GameResult.DRAW), moves)
        assertTrue("PGN doit contenir résultat 1/2-1/2", pgnDraw.lines().last() == "1/2-1/2")
        
        // Test IN_PROGRESS (*)
        val pgnInProgress = PgnExporter.generatePgn(createTestGame(result = GameResult.IN_PROGRESS), moves)
        assertTrue("PGN doit contenir résultat *", pgnInProgress.lines().last() == "*")
    }

    @Test
    fun `generatePgn ligne vide entre headers et coups`() {
        val game = createTestGame()
        val moves = createTestMoves()
        
        val pgn = PgnExporter.generatePgn(game, moves)
        val lines = pgn.lines().toList()
        
        // Trouver la ligne vide après les headers
        val lastHeaderIndex = lines.indexOfLast { it.startsWith("[") }
        val nextLine = lines[lastHeaderIndex + 1]
        
        assertTrue("Il doit y avoir une ligne vide après les headers", nextLine.isEmpty())
    }

    @Test
    fun `generatePgn partie sans coups (seulement résultat)`() {
        val game = createTestGame(result = GameResult.IN_PROGRESS)
        val moves = emptyList<ChessMove>()
        
        val pgn = PgnExporter.generatePgn(game, moves)
        
        // Vérifier que le PGN contient quand même le résultat
        assertTrue("PGN doit contenir le résultat même sans coups", pgn.contains("*"))
    }

    @Test
    fun `generatePgn coups avec roques et symboles spéciaux`() {
        val game = createTestGame()
        val moves = listOf(
            ChessMove(id = 1, gameId = 1, moveNumber = 1, notation = "e4", isWhiteMove = true, timestamp = LocalDateTime.now()),
            ChessMove(id = 2, gameId = 1, moveNumber = 1, notation = "e5", isWhiteMove = false, timestamp = LocalDateTime.now()),
            ChessMove(id = 3, gameId = 1, moveNumber = 2, notation = "O-O", isWhiteMove = true, timestamp = LocalDateTime.now()),
            ChessMove(id = 4, gameId = 1, moveNumber = 2, notation = "O-O-O", isWhiteMove = false, timestamp = LocalDateTime.now()),
            ChessMove(id = 5, gameId = 1, moveNumber = 3, notation = "Dh5#", isWhiteMove = true, timestamp = LocalDateTime.now())
        )
        
        val pgn = PgnExporter.generatePgn(game, moves)
        val movesSection = pgn.lines().dropWhile { it.startsWith("[") || it.isEmpty() }.first()
        
        assertTrue("PGN doit contenir O-O (petit roque)", movesSection.contains("O-O"))
        assertTrue("PGN doit contenir O-O-O (grand roque)", movesSection.contains("O-O-O"))
        assertTrue("PGN doit contenir Dh5# (échec et mat)", movesSection.contains("Dh5#"))
    }

    @Test
    fun `generateFileName format correct yyyyMMdd_HHmmss`() {
        val date = LocalDateTime.of(2026, 2, 6, 21, 30, 45)
        val game = createTestGame(date = date)
        
        val filename = PgnExporter.generateFileName(game)
        
        assertEquals("Le nom de fichier doit être au format correct", "partie_20260206_213045.pgn", filename)
    }

    @Test
    fun `generateFileName utilise date de la partie`() {
        val date1 = LocalDateTime.of(2025, 12, 25, 10, 15, 30)
        val game1 = createTestGame(date = date1)
        
        val filename1 = PgnExporter.generateFileName(game1)
        
        assertTrue("Le nom doit contenir la date 20251225", filename1.contains("20251225"))
        assertTrue("Le nom doit contenir l'heure 101530", filename1.contains("101530"))
    }

    @Test
    fun `generatePgn validation format Lichess (intégration complète)`() {
        // Ce test vérifie le format complet qui doit être accepté par Lichess
        val game = createTestGame()
        val moves = listOf(
            ChessMove(id = 1, gameId = 1, moveNumber = 1, notation = "e4", isWhiteMove = true, timestamp = LocalDateTime.now()),
            ChessMove(id = 2, gameId = 1, moveNumber = 1, notation = "e5", isWhiteMove = false, timestamp = LocalDateTime.now()),
            ChessMove(id = 3, gameId = 1, moveNumber = 2, notation = "Cf3", isWhiteMove = true, timestamp = LocalDateTime.now()),
            ChessMove(id = 4, gameId = 1, moveNumber = 2, notation = "Cc6", isWhiteMove = false, timestamp = LocalDateTime.now())
        )
        
        val pgn = PgnExporter.generatePgn(game, moves)
        
        // Vérifications critiques pour validation Lichess :
        // 1. Headers obligatoires présents
        assertTrue(pgn.contains("[Event"))
        assertTrue(pgn.contains("[Site"))
        assertTrue(pgn.contains("[Date"))
        assertTrue(pgn.contains("[Round"))
        assertTrue(pgn.contains("[White"))
        assertTrue(pgn.contains("[Black"))
        assertTrue(pgn.contains("[Result"))
        
        // 2. Ligne vide après headers
        val lines = pgn.lines().toList()
        val lastHeaderIndex = lines.indexOfLast { it.startsWith("[") }
        assertTrue(lines[lastHeaderIndex + 1].isEmpty())
        
        // 3. Résultat sur nouvelle ligne après ligne vide
        val lastLine = lines.last { it.isNotEmpty() }
        assertTrue("Le résultat doit être sur la dernière ligne", listOf("1-0", "0-1", "1/2-1/2", "*").contains(lastLine))
        
        // 4. Format des coups correct - doit contenir des numéros de coups
        val movesLine = lines.dropWhile { it.startsWith("[") || it.isEmpty() }.first()
        assertTrue("Les coups doivent contenir un numéro de coup '1.'", movesLine.contains("1."))
        // Le format PGN peut être varié (O-O, Cf3, e4, etc.), on vérifie juste qu'il y a bien des coups
        assertTrue("Les coups doivent contenir 'e4'", movesLine.contains("e4"))
        assertTrue("Les coups doivent contenir 'Cf3'", movesLine.contains("Cf3"))
        
        println("✅ PGN généré (validation Lichess) :")
        println(pgn)
    }
}
