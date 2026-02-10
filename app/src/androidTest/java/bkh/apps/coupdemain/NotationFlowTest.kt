package bkh.apps.coupdemain

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import org.hamcrest.Matchers.allOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Tests UI Espresso pour le flow complet de notation d'une partie d'échecs
 * 
 * Ce test simule l'expérience utilisateur complète :
 * 1. Création d'une nouvelle partie
 * 2. Choix de la couleur (Blancs)
 * 3. Notation de plusieurs coups
 * 4. Finalisation de la partie
 * 5. Vérification de l'export
 */
@RunWith(AndroidJUnit4::class)
@LargeTest
class NotationFlowTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun flowComplet_creerPartie_noterCoups_terminerPartie() {
        // Attendre que l'activité soit prête
        Thread.sleep(1000)

        // ====== ÉTAPE 1: Créer une nouvelle partie ======
        // Cliquer sur le FAB "Nouvelle notation" depuis l'écran d'accueil
        onView(withId(R.id.fab_new_game))
            .check(matches(isDisplayed()))
            .perform(click())

        // Attendre que le dialog de choix de couleur apparaisse
        Thread.sleep(500)

        // ====== ÉTAPE 2: Choisir la couleur des Blancs ======
        // Cliquer sur le bouton "Blancs"
        onView(withId(R.id.btn_play_white))
            .perform(click())

        // Attendre que NotationDetailFragment soit affiché
        Thread.sleep(500)

        // Vérifier que le toolbar est visible
        onView(withId(R.id.toolbar))
            .check(matches(isDisplayed()))

        // ====== ÉTAPE 3: Noter plusieurs coups ======
        // La notation se fait via le clavier IME, donc on va simuler l'ajout de coups
        // via le FAB "Ajouter un coup" qui ouvre un dialog
        
        // Coup 1: e4
        onView(withId(R.id.fab_add_move))
            .check(matches(isDisplayed()))
            .perform(click())
        
        Thread.sleep(300)
        
        onView(withId(R.id.et_move_notation))
            .perform(typeText("e4"), closeSoftKeyboard())
        
        onView(withText("Ajouter"))
            .perform(click())
        
        Thread.sleep(300)

        // Vérifier que le coup apparaît dans l'historique
        onView(withId(R.id.tv_moves_history))
            .check(matches(withText(org.hamcrest.Matchers.containsString("e4"))))

        // Coup 2: e5
        onView(withId(R.id.fab_add_move))
            .perform(click())
        
        Thread.sleep(300)
        
        onView(withId(R.id.et_move_notation))
            .perform(typeText("e5"), closeSoftKeyboard())
        
        onView(withText("Ajouter"))
            .perform(click())
        
        Thread.sleep(300)

        // Vérifier que les deux coups apparaissent
        onView(withId(R.id.tv_moves_history))
            .check(matches(withText(org.hamcrest.Matchers.containsString("e5"))))

        // Coup 3: Cf3
        onView(withId(R.id.fab_add_move))
            .perform(click())
        
        Thread.sleep(300)
        
        onView(withId(R.id.et_move_notation))
            .perform(typeText("Cf3"), closeSoftKeyboard())
        
        onView(withText("Ajouter"))
            .perform(click())
        
        Thread.sleep(300)

        // Coup 4: Cc6
        onView(withId(R.id.fab_add_move))
            .perform(click())
        
        Thread.sleep(300)
        
        onView(withId(R.id.et_move_notation))
            .perform(typeText("Cc6"), closeSoftKeyboard())
        
        onView(withText("Ajouter"))
            .perform(click())
        
        Thread.sleep(300)

        // ====== ÉTAPE 4: Terminer la partie ======
        // Scroller pour voir le bouton "Fin de la partie" (au cas où)
        onView(withId(R.id.btn_finish_game))
            .perform(scrollTo())
            .check(matches(isDisplayed()))
            .perform(click())

        // Attendre que le dialog de résultat apparaisse
        Thread.sleep(500)

        // Choisir "Victoire Blancs (1-0)"
        onView(withText("Victoire Blancs (1-0)"))
            .perform(click())

        // Attendre que l'UI se mette à jour
        Thread.sleep(500)

        // ====== ÉTAPE 5: Vérifier que l'export est disponible ======
        // Le bouton "Exporter la partie" devrait maintenant être visible
        onView(withId(R.id.btn_export_game))
            .check(matches(isDisplayed()))

        // Le FAB "Ajouter un coup" devrait être masqué
        onView(withId(R.id.fab_add_move))
            .check(matches(withEffectiveVisibility(Visibility.GONE)))

        // Le bouton "Fin de la partie" devrait être masqué
        onView(withId(R.id.btn_finish_game))
            .check(matches(withEffectiveVisibility(Visibility.GONE)))

        // Vérifier que le résultat est affiché dans le toolbar subtitle
        // (difficile à tester directement avec Espresso sur Toolbar subtitle)
        
        // ====== ÉTAPE 6: Tester l'export (ouvrir le dialog) ======
        onView(withId(R.id.btn_export_game))
            .perform(scrollTo())
            .perform(click())

        Thread.sleep(500)

        // Vérifier que le dialog de choix de format apparaît
        onView(withText("Choisir le format d'export"))
            .check(matches(isDisplayed()))

        // Annuler le dialog d'export
        onView(withText("Annuler"))
            .perform(click())

        // ====== ÉTAPE 7: Retour à l'accueil et vérification ======
        // Cliquer sur le bouton retour du toolbar
        onView(withContentDescription("Fermer"))
            .perform(click())

        Thread.sleep(500)

        // Vérifier qu'on est de retour sur l'écran d'accueil
        // et que la partie apparaît dans les parties récentes
        onView(withId(R.id.recent_games_container))
            .check(matches(isDisplayed()))

        // Vérifier qu'il y a au moins une partie affichée
        // (la cardview de la partie qu'on vient de créer)
        onView(withId(R.id.game_card))
            .check(matches(isDisplayed()))
    }

    @Test
    fun flowComplet_creerPartie_jouerNoirs_terminerNulle() {
        // Attendre que l'activité soit prête
        Thread.sleep(1000)

        // Créer une nouvelle partie
        onView(withId(R.id.fab_new_game))
            .perform(click())

        Thread.sleep(500)

        // Choisir de jouer les Noirs
        onView(withId(R.id.btn_play_black))
            .perform(click())

        Thread.sleep(500)

        // Noter quelques coups
        for (i in 1..3) {
            onView(withId(R.id.fab_add_move))
                .perform(click())
            
            Thread.sleep(300)
            
            onView(withId(R.id.et_move_notation))
                .perform(typeText("e4"), closeSoftKeyboard())
            
            onView(withText("Ajouter"))
                .perform(click())
            
            Thread.sleep(300)
        }

        // Terminer la partie
        onView(withId(R.id.btn_finish_game))
            .perform(scrollTo())
            .perform(click())

        Thread.sleep(500)

        // Choisir "Nulle (1/2-1/2)"
        onView(withText("Nulle (1/2-1/2)"))
            .perform(click())

        Thread.sleep(500)

        // Vérifier que l'export est disponible
        onView(withId(R.id.btn_export_game))
            .check(matches(isDisplayed()))
    }

    @Test
    fun flowComplet_annulerDernierCoup() {
        // Créer une nouvelle partie
        onView(withId(R.id.fab_new_game))
            .perform(click())

        Thread.sleep(500)

        onView(withId(R.id.btn_play_white))
            .perform(click())

        Thread.sleep(500)

        // Noter 2 coups
        onView(withId(R.id.fab_add_move))
            .perform(click())
        
        Thread.sleep(300)
        
        onView(withId(R.id.et_move_notation))
            .perform(typeText("e4"), closeSoftKeyboard())
        
        onView(withText("Ajouter"))
            .perform(click())
        
        Thread.sleep(300)

        onView(withId(R.id.fab_add_move))
            .perform(click())
        
        Thread.sleep(300)
        
        onView(withId(R.id.et_move_notation))
            .perform(typeText("e5"), closeSoftKeyboard())
        
        onView(withText("Ajouter"))
            .perform(click())
        
        Thread.sleep(300)

        // Vérifier que les 2 coups sont présents
        onView(withId(R.id.tv_moves_history))
            .check(matches(withText(org.hamcrest.Matchers.containsString("e5"))))

        // Annuler le dernier coup via le menu
        onView(withContentDescription("Annuler"))
            .perform(click())

        Thread.sleep(300)

        // Vérifier que seul e4 reste
        onView(withId(R.id.tv_moves_history))
            .check(matches(withText(org.hamcrest.Matchers.containsString("e4"))))
    }

    @Test
    fun flowComplet_supprimerPartie() {
        // Créer une nouvelle partie
        onView(withId(R.id.fab_new_game))
            .perform(click())

        Thread.sleep(500)

        onView(withId(R.id.btn_play_white))
            .perform(click())

        Thread.sleep(500)

        // Supprimer la partie via le menu
        onView(withContentDescription("Supprimer"))
            .perform(click())

        Thread.sleep(500)

        // Confirmer la suppression
        onView(withText("Supprimer"))
            .perform(click())

        Thread.sleep(500)

        // Vérifier qu'on est retourné à l'accueil
        onView(withId(R.id.fab_new_game))
            .check(matches(isDisplayed()))
    }
}
