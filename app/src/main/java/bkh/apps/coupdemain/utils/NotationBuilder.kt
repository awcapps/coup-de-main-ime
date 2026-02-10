package bkh.apps.coupdemain.utils

/**
 * Constructeur de notation d'échecs au format SAN (Standard Algebraic Notation)
 * 
 * Exemples de notations valides :
 * - Coups simples : e4, Nf3, Bb5
 * - Captures : Nxe5, exd5
 * - Échec/Mat : Qh5+, Nf7#
 * - Roques : O-O (petit), O-O-O (grand)
 * - Promotion : e8=Q, f1=N+
 * - Désambiguïsation : Nbd7, R1e1, Qh4e1
 */
class NotationBuilder {
    
    private val notation = StringBuilder()
    
    /**
     * Ajoute une pièce (R, N, B, Q, K)
     * Le pion n'a pas de lettre
     */
    fun addPiece(piece: Piece): NotationBuilder {
        when (piece) {
            Piece.ROOK -> notation.append("R")
            Piece.KNIGHT -> notation.append("C")  // Convention française
            Piece.BISHOP -> notation.append("F")
            Piece.QUEEN -> notation.append("D")
            Piece.KING -> notation.append("K")
            Piece.PAWN -> { /* Le pion n'a pas de symbole */ }
        }
        return this
    }
    
    /**
     * Ajoute une colonne (a-h) pour désambiguïsation ou destination
     */
    fun addColumn(column: Char): NotationBuilder {
        require(column in 'a'..'h') { "Colonne invalide: $column" }
        notation.append(column)
        return this
    }
    
    /**
     * Ajoute une rangée (1-8) pour désambiguïsation ou destination
     */
    fun addRow(row: Int): NotationBuilder {
        require(row in 1..8) { "Rangée invalide: $row" }
        notation.append(row)
        return this
    }
    
    /**
     * Ajoute une case complète (ex: e4, d5)
     */
    fun addCoordinate(column: Char, row: Int): NotationBuilder {
        addColumn(column)
        addRow(row)
        return this
    }
    
    /**
     * Ajoute le symbole de capture (x)
     */
    fun addCapture(): NotationBuilder {
        notation.append("x")
        return this
    }
    
    /**
     * Ajoute un symbole (échec, mat, etc.)
     */
    fun addSymbol(symbol: Symbol): NotationBuilder {
        when (symbol) {
            Symbol.CHECK -> notation.append("+")
            Symbol.CHECKMATE -> notation.append("#")
            Symbol.CAPTURE -> notation.append("x")
        }
        return this
    }
    
    /**
     * Ajoute une promotion (ex: =Q)
     */
    fun addPromotion(piece: Piece): NotationBuilder {
        require(piece != Piece.PAWN && piece != Piece.KING) { 
            "Promotion invalide: $piece" 
        }
        notation.append("=")
        addPiece(piece)
        return this
    }
    
    /**
     * Construit un roque
     */
    fun setCastle(type: CastleType): NotationBuilder {
        notation.clear()
        when (type) {
            CastleType.KINGSIDE -> notation.append("O-O")
            CastleType.QUEENSIDE -> notation.append("O-O-O")
        }
        return this
    }
    
    /**
     * Construit la notation finale
     */
    fun build(): String {
        return notation.toString()
    }
    
    /**
     * Réinitialise le builder
     */
    fun clear(): NotationBuilder {
        notation.clear()
        return this
    }
    
    /**
     * Obtient l'état actuel de la notation (pour preview)
     */
    fun preview(): String {
        return notation.toString()
    }
    
    companion object {
        
        /**
         * Valide si une notation respecte le format SAN
         */
        fun isValidMove(move: String): Boolean {
            if (move.isBlank()) return false
            
            // Roques
            if (move == "O-O" || move == "O-O-O") return true
            
            // Regex SAN standard
            // Format: [Pièce][colonne_départ][rangée_départ][x][colonne_dest][rangée_dest][=Promotion][+#]
            val sanRegex = Regex(
                "^([RCFDKP])?([a-h])?([1-8])?(x)?([a-h])([1-8])(=[RCFDQ])?(\\+|#)?$"
            )
            
            return sanRegex.matches(move)
        }
        
        /**
         * Parse une notation SAN et retourne ses composants
         */
        fun parse(move: String): MoveComponents? {
            if (!isValidMove(move)) return null
            
            // Cas spéciaux des roques
            if (move == "O-O") return MoveComponents(castle = CastleType.KINGSIDE)
            if (move == "O-O-O") return MoveComponents(castle = CastleType.QUEENSIDE)
            
            val sanRegex = Regex(
                "^([RCFDKP])?([a-h])?([1-8])?(x)?([a-h])([1-8])(=[RCFDQ])?(\\+|#)?$"
            )
            
            val match = sanRegex.matchEntire(move) ?: return null
            
            return MoveComponents(
                piece = match.groups[1]?.value?.let { parsePiece(it) },
                fromColumn = match.groups[2]?.value?.get(0),
                fromRow = match.groups[3]?.value?.toIntOrNull(),
                isCapture = match.groups[4]?.value != null,
                toColumn = match.groups[5]?.value?.get(0) ?: return null,
                toRow = match.groups[6]?.value?.toIntOrNull() ?: return null,
                promotion = match.groups[7]?.value?.substring(1)?.let { parsePiece(it) },
                check = when (match.groups[8]?.value) {
                    "+" -> Symbol.CHECK
                    "#" -> Symbol.CHECKMATE
                    else -> null
                }
            )
        }
        
        private fun parsePiece(symbol: String): Piece {
            return when (symbol) {
                "R" -> Piece.ROOK
                "C" -> Piece.KNIGHT
                "F" -> Piece.BISHOP
                "D" -> Piece.QUEEN
                "K" -> Piece.KING
                "P" -> Piece.PAWN
                else -> Piece.PAWN
            }
        }
    }
}

/**
 * Types de pièces
 */
enum class Piece {
    PAWN, ROOK, KNIGHT, BISHOP, QUEEN, KING
}

/**
 * Symboles de notation
 */
enum class Symbol {
    CHECK, CHECKMATE, CAPTURE
}

/**
 * Types de roque
 */
enum class CastleType {
    KINGSIDE,   // O-O (petit roque)
    QUEENSIDE   // O-O-O (grand roque)
}

/**
 * Composants d'un coup parsé
 */
data class MoveComponents(
    val piece: Piece? = null,
    val fromColumn: Char? = null,
    val fromRow: Int? = null,
    val isCapture: Boolean = false,
    val toColumn: Char? = null,
    val toRow: Int? = null,
    val promotion: Piece? = null,
    val check: Symbol? = null,
    val castle: CastleType? = null
)
