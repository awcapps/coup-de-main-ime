package bkh.apps.coupdemain.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.Typeface
import bkh.apps.coupdemain.data.database.ChessGame
import bkh.apps.coupdemain.data.database.ChessMove
import java.time.format.DateTimeFormatter
import java.util.Locale

/**
 * Générateur d'images JPG de feuilles de notation
 * Crée une image qui ressemble à une vraie feuille de notation d'échecs
 */
object NotationImageExporter {
    
    private const val IMAGE_WIDTH = 1200
    private const val IMAGE_HEIGHT = 1600
    private const val PADDING = 80
    private const val LINE_HEIGHT = 40
    
    /**
     * Génère une image Bitmap de la feuille de notation
     */
    fun generateImage(context: Context, game: ChessGame, moves: List<ChessMove>): Bitmap {
        val bitmap = Bitmap.createBitmap(IMAGE_WIDTH, IMAGE_HEIGHT, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        
        // Fond blanc
        canvas.drawColor(Color.WHITE)
        
        // Paint pour le texte
        val titlePaint = Paint().apply {
            color = Color.BLACK
            textSize = 60f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            isAntiAlias = true
        }
        
        val headerPaint = Paint().apply {
            color = Color.DKGRAY
            textSize = 40f
            typeface = Typeface.DEFAULT
            isAntiAlias = true
        }
        
        val movePaint = Paint().apply {
            color = Color.BLACK
            textSize = 36f
            typeface = Typeface.MONOSPACE
            isAntiAlias = true
        }
        
        val linePaint = Paint().apply {
            color = Color.LTGRAY
            strokeWidth = 2f
            style = Paint.Style.STROKE
        }
        
        var yPosition = PADDING.toFloat()
        
        // Titre
        canvas.drawText("FEUILLE DE NOTATION", PADDING.toFloat(), yPosition, titlePaint)
        yPosition += 80
        
        // Ligne séparatrice
        canvas.drawLine(
            PADDING.toFloat(), 
            yPosition, 
            (IMAGE_WIDTH - PADDING).toFloat(), 
            yPosition, 
            linePaint
        )
        yPosition += 60
        
        // Date
        val dateFormatter = DateTimeFormatter.ofPattern("d MMMM yyyy - HH:mm", Locale.FRENCH)
        canvas.drawText("Date : ${game.date.format(dateFormatter)}", PADDING.toFloat(), yPosition, headerPaint)
        yPosition += 50
        
        // Joueurs
        val whiteName = if (game.userPlaysWhite) "Blancs : Vous" else "Blancs : Adversaire"
        val blackName = if (game.userPlaysWhite) "Noirs : Adversaire" else "Noirs : Vous"
        canvas.drawText(whiteName, PADDING.toFloat(), yPosition, headerPaint)
        yPosition += 45
        canvas.drawText(blackName, PADDING.toFloat(), yPosition, headerPaint)
        yPosition += 50
        
        // Résultat
        canvas.drawText("Résultat : ${game.result.notation}", PADDING.toFloat(), yPosition, headerPaint)
        yPosition += 70
        
        // Ligne séparatrice
        canvas.drawLine(
            PADDING.toFloat(), 
            yPosition, 
            (IMAGE_WIDTH - PADDING).toFloat(), 
            yPosition, 
            linePaint
        )
        yPosition += 50
        
        // Titre des coups
        canvas.drawText("COUPS", PADDING.toFloat(), yPosition, titlePaint)
        yPosition += 60
        
        // Dessiner les coups en deux colonnes
        val columnWidth = (IMAGE_WIDTH - 2 * PADDING) / 2
        var currentMoveNumber = 0
        
        for (move in moves) {
            if (move.isWhiteMove) {
                // Coup blanc - nouvelle ligne
                currentMoveNumber = move.moveNumber
                
                // Numéro du coup
                val moveNumberText = "$currentMoveNumber."
                canvas.drawText(moveNumberText, PADDING.toFloat(), yPosition, movePaint)
                
                // Coup blanc
                canvas.drawText(move.notation, (PADDING + 100).toFloat(), yPosition, movePaint)
            } else {
                // Coup noir - même ligne
                canvas.drawText(move.notation, (PADDING + columnWidth).toFloat(), yPosition, movePaint)
                yPosition += LINE_HEIGHT.toFloat()
                
                // Ligne de séparation légère tous les 5 coups
                if (currentMoveNumber % 5 == 0) {
                    canvas.drawLine(
                        PADDING.toFloat(),
                        yPosition - 5,
                        (IMAGE_WIDTH - PADDING).toFloat(),
                        yPosition - 5,
                        linePaint
                    )
                }
            }
            
            // Éviter de dépasser l'image
            if (yPosition > IMAGE_HEIGHT - 100) {
                break
            }
        }
        
        // Footer
        yPosition = (IMAGE_HEIGHT - 80).toFloat()
        canvas.drawLine(
            PADDING.toFloat(), 
            yPosition, 
            (IMAGE_WIDTH - PADDING).toFloat(), 
            yPosition, 
            linePaint
        )
        yPosition += 50
        
        val footerPaint = Paint().apply {
            color = Color.GRAY
            textSize = 28f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.ITALIC)
            isAntiAlias = true
        }
        canvas.drawText("Généré par Coup de Main", PADDING.toFloat(), yPosition, footerPaint)
        
        return bitmap
    }
    
    /**
     * Génère un nom de fichier JPG basé sur la date
     */
    fun generateFileName(game: ChessGame): String {
        val dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss", Locale.FRENCH)
        return "notation_${game.date.format(dateFormatter)}.jpg"
    }
}
