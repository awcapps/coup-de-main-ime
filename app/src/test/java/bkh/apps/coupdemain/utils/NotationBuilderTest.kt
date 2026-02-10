package bkh.apps.coupdemain.utils

import org.junit.Test
import org.junit.Assert.*

/**
 * Tests unitaires pour NotationBuilder
 */
class NotationBuilderTest {
    
    @Test
    fun `construction coup simple pion`() {
        val move = NotationBuilder()
            .addCoordinate('e', 4)
            .build()
        
        assertEquals("e4", move)
        assertTrue(NotationBuilder.isValidMove(move))
    }
    
    @Test
    fun `construction coup pièce simple`() {
        val move = NotationBuilder()
            .addPiece(Piece.KNIGHT)
            .addCoordinate('f', 3)
            .build()
        
        assertEquals("Cf3", move)
        assertTrue(NotationBuilder.isValidMove(move))
    }
    
    @Test
    fun `construction capture`() {
        val move = NotationBuilder()
            .addPiece(Piece.QUEEN)
            .addCapture()
            .addCoordinate('d', 5)
            .build()
        
        assertEquals("Dxd5", move)
        assertTrue(NotationBuilder.isValidMove(move))
    }
    
    @Test
    fun `construction capture pion`() {
        val move = NotationBuilder()
            .addColumn('e')
            .addCapture()
            .addCoordinate('d', 5)
            .build()
        
        assertEquals("exd5", move)
        assertTrue(NotationBuilder.isValidMove(move))
    }
    
    @Test
    fun `construction échec`() {
        val move = NotationBuilder()
            .addPiece(Piece.QUEEN)
            .addCoordinate('h', 5)
            .addSymbol(Symbol.CHECK)
            .build()
        
        assertEquals("Dh5+", move)
        assertTrue(NotationBuilder.isValidMove(move))
    }
    
    @Test
    fun `construction mat`() {
        val move = NotationBuilder()
            .addPiece(Piece.KNIGHT)
            .addCoordinate('f', 7)
            .addSymbol(Symbol.CHECKMATE)
            .build()
        
        assertEquals("Cf7#", move)
        assertTrue(NotationBuilder.isValidMove(move))
    }
    
    @Test
    fun `construction petit roque`() {
        val move = NotationBuilder()
            .setCastle(CastleType.KINGSIDE)
            .build()
        
        assertEquals("O-O", move)
        assertTrue(NotationBuilder.isValidMove(move))
    }
    
    @Test
    fun `construction grand roque`() {
        val move = NotationBuilder()
            .setCastle(CastleType.QUEENSIDE)
            .build()
        
        assertEquals("O-O-O", move)
        assertTrue(NotationBuilder.isValidMove(move))
    }
    
    @Test
    fun `construction promotion`() {
        val move = NotationBuilder()
            .addCoordinate('e', 8)
            .addPromotion(Piece.QUEEN)
            .build()
        
        assertEquals("e8=D", move)
        assertTrue(NotationBuilder.isValidMove(move))
    }
    
    @Test
    fun `construction promotion avec échec`() {
        val move = NotationBuilder()
            .addColumn('f')
            .addCapture()
            .addCoordinate('g', 1)
            .addPromotion(Piece.KNIGHT)
            .addSymbol(Symbol.CHECK)
            .build()
        
        assertEquals("fxg1=C+", move)
        assertTrue(NotationBuilder.isValidMove(move))
    }
    
    @Test
    fun `désambiguïsation colonne`() {
        val move = NotationBuilder()
            .addPiece(Piece.KNIGHT)
            .addColumn('b')
            .addCoordinate('d', 7)
            .build()
        
        assertEquals("Cbd7", move)
        assertTrue(NotationBuilder.isValidMove(move))
    }
    
    @Test
    fun `désambiguïsation rangée`() {
        val move = NotationBuilder()
            .addPiece(Piece.ROOK)
            .addRow(1)
            .addCoordinate('e', 1)
            .build()
        
        assertEquals("R1e1", move)
        assertTrue(NotationBuilder.isValidMove(move))
    }
    
    @Test
    fun `désambiguïsation complète`() {
        val move = NotationBuilder()
            .addPiece(Piece.QUEEN)
            .addCoordinate('h', 4)
            .addCoordinate('e', 1)
            .build()
        
        assertEquals("Dh4e1", move)
        assertTrue(NotationBuilder.isValidMove(move))
    }
    
    @Test
    fun `validation coups invalides`() {
        assertFalse(NotationBuilder.isValidMove(""))
        assertFalse(NotationBuilder.isValidMove("e9"))
        assertFalse(NotationBuilder.isValidMove("i4"))
        assertFalse(NotationBuilder.isValidMove("Xe4"))
        assertFalse(NotationBuilder.isValidMove("e"))
        assertFalse(NotationBuilder.isValidMove("4"))
    }
    
    @Test
    fun `parse coup simple`() {
        val components = NotationBuilder.parse("e4")
        
        assertNotNull(components)
        assertEquals('e', components?.toColumn)
        assertEquals(4, components?.toRow)
        assertNull(components?.piece)
        assertFalse(components?.isCapture ?: true)
    }
    
    @Test
    fun `parse coup avec pièce`() {
        val components = NotationBuilder.parse("Cf3")
        
        assertNotNull(components)
        assertEquals(Piece.KNIGHT, components?.piece)
        assertEquals('f', components?.toColumn)
        assertEquals(3, components?.toRow)
    }
    
    @Test
    fun `parse capture`() {
        val components = NotationBuilder.parse("Dxd5")
        
        assertNotNull(components)
        assertEquals(Piece.QUEEN, components?.piece)
        assertTrue(components?.isCapture ?: false)
        assertEquals('d', components?.toColumn)
        assertEquals(5, components?.toRow)
    }
    
    @Test
    fun `parse échec et mat`() {
        val checkMove = NotationBuilder.parse("Dh5+")
        assertEquals(Symbol.CHECK, checkMove?.check)
        
        val mateMove = NotationBuilder.parse("Cf7#")
        assertEquals(Symbol.CHECKMATE, mateMove?.check)
    }
    
    @Test
    fun `parse roques`() {
        val kingsideCastle = NotationBuilder.parse("O-O")
        assertEquals(CastleType.KINGSIDE, kingsideCastle?.castle)
        
        val queensideCastle = NotationBuilder.parse("O-O-O")
        assertEquals(CastleType.QUEENSIDE, queensideCastle?.castle)
    }
    
    @Test
    fun `parse promotion`() {
        val components = NotationBuilder.parse("e8=D")
        
        assertNotNull(components)
        assertEquals(Piece.QUEEN, components?.promotion)
        assertEquals('e', components?.toColumn)
        assertEquals(8, components?.toRow)
    }
    
    @Test
    fun `parse désambiguïsation`() {
        val colDisambig = NotationBuilder.parse("Cbd7")
        assertEquals('b', colDisambig?.fromColumn)
        
        val rowDisambig = NotationBuilder.parse("R1e1")
        assertEquals(1, rowDisambig?.fromRow)
    }
    
    @Test
    fun `builder réutilisable avec clear`() {
        val builder = NotationBuilder()
        
        val move1 = builder.addCoordinate('e', 4).build()
        assertEquals("e4", move1)
        
        val move2 = builder.clear().addPiece(Piece.KNIGHT).addCoordinate('f', 3).build()
        assertEquals("Cf3", move2)
    }
    
    @Test
    fun `preview pendant construction`() {
        val builder = NotationBuilder()
        
        builder.addPiece(Piece.QUEEN)
        assertEquals("D", builder.preview())
        
        builder.addCapture()
        assertEquals("Dx", builder.preview())
        
        builder.addCoordinate('d', 5)
        assertEquals("Dxd5", builder.preview())
    }
}
