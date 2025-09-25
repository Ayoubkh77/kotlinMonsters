package org.example.monde

import org.example.monstre.EspeceMonstre
import org.example.monstre.IndividuMonstre
import org.example.dresseur.Entraineur
import org.example.jeu.CombatMonstre
import kotlin.random.Random

class Zone(
    val id: Int,
    val nom: String,
    val expZone: Int,
    val especesMonstres: MutableList<EspeceMonstre> = mutableListOf(),
    var zoneSuivante: Zone? = null,
    var zonePrecedente: Zone? = null
) {

    // Génère un monstre sauvage aléatoire appartenant aux espèces de la zone
    fun genereMonstre(): IndividuMonstre {
        val especeChoisie = especesMonstres.random()
        val expMonstre = (expZone * Random.nextDouble(0.8, 1.2)).toInt().toDouble()

        return IndividuMonstre(
            id = Random.nextInt(1000, 9999),
            nom = especeChoisie.nom,
            espece = especeChoisie,
            entraineur = null,
            expInit = expMonstre
        )
    }

    fun rencontreMonstre(joueur: Entraineur) {
        val monstreSauvage = genereMonstre()
        val monstreJoueur = joueur.equipeMonstre.firstOrNull { it.pv > 0 }

        if (monstreJoueur != null) {
            println("🌿 En entrant dans $nom, un ${monstreSauvage.nom} sauvage apparaît !")
            val combat = CombatMonstre(joueur, monstreJoueur, monstreSauvage)
            combat.lanceCombat()
        } else {
            println("❌ Aucun monstre en état de combattre dans l'équipe du joueur.")
        }
    }

}
