package bkh.apps.coupdemain.ui.onboarding

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import bkh.apps.coupdemain.R

class OnboardingPagerAdapter(private val activity: OnboardingActivity) : 
    RecyclerView.Adapter<OnboardingPagerAdapter.OnboardingViewHolder>() {

    private val pages = listOf(
        OnboardingPage(
            R.drawable.ic_menu_compass,
            "Bienvenue dans Coup de Main",
            "Un clavier optimisé pour saisir la notation d'échecs facilement"
        ),
        OnboardingPage(
            R.drawable.ic_menu_edit,
            "Notation simplifiée",
            "Appuyez sur les pièces (♜R, ♞C, ♝F, ♛D, ♚K) et les coordonnées (a-h, 1-8) pour composer vos coups"
        ),
        OnboardingPage(
            R.drawable.ic_menu_preferences,
            "Personnalisable",
            "Ajustez la taille des boutons, la vibration et le thème selon vos préférences"
        ),
        OnboardingPage(
            R.drawable.ic_menu_edit,
            "Configurer le clavier maintenant ?",
            "Voulez-vous activer et configurer le clavier Coup de Main dès maintenant dans les paramètres système ?"
        )
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OnboardingViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_onboarding_page, parent, false)
        return OnboardingViewHolder(view)
    }

    override fun onBindViewHolder(holder: OnboardingViewHolder, position: Int) {
        holder.bind(pages[position])
    }

    override fun getItemCount() = pages.size

    class OnboardingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val icon: ImageView = itemView.findViewById(R.id.iv_icon)
        private val title: TextView = itemView.findViewById(R.id.tv_title)
        private val description: TextView = itemView.findViewById(R.id.tv_description)

        fun bind(page: OnboardingPage) {
            icon.setImageResource(page.iconRes)
            title.text = page.title
            description.text = page.description
        }
    }

    data class OnboardingPage(
        val iconRes: Int,
        val title: String,
        val description: String
    )
}
