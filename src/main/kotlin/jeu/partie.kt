package org.example.jeu

import org.example.dresseur.Entraineur
import org.example.monstre.EspeceMonstre
import org.example.monstre.IndividuMonstre
import org.example.monde.Zone

class Partie(
    val id: Int,
    val joueur: Entraineur,
    var zoneActuelle: Zone
) {

    // --- Méthode pour choisir un starter ---
    fun choixStarter(starters: List<EspeceMonstre>) {
        val startersIndividus = starters.mapIndexed { index, espece ->
            IndividuMonstre(
                id = 100 + index,
                nom = espece.nom,
                espece = espece,
                entraineur = joueur,
                expInit = 50.0
            )
        }

        println("=== Choisissez votre starter ===")
        startersIndividus.forEachIndexed { index, monstre ->
            println("${index + 1}. ${monstre.nom} - ${monstre.espece.description}")
        }

        print("Entrez le numéro de votre choix (1-3): ")
        val choix = readLine()?.toIntOrNull()

        val starterChoisi = if (choix != null && choix in 1..startersIndividus.size) {
            startersIndividus[choix - 1]
        } else {
            println("❌ Choix invalide, ${startersIndividus.first().nom} vous est attribué par défaut.")
            startersIndividus.first()
        }

        joueur.equipeMonstre.add(starterChoisi)
        starterChoisi.entraineur = joueur

        println("🎉 Vous avez choisi ${starterChoisi.nom} comme starter !")
    }

    // --- Méthode pour modifier l’ordre de l’équipe ---
    fun modifierOrdreEquipe() {
        if (joueur.equipeMonstre.size < 2) {
            println("⚠️ Impossible de modifier l’ordre, vous avez moins de 2 monstres.")
            return
        }

        println("=== Modifier l’ordre de l’équipe ===")
        joueur.equipeMonstre.forEachIndexed { index, monstre ->
            println("${index + 1}. ${monstre.nom}")
        }

        print("Entrez le numéro du premier monstre à déplacer : ")
        val pos1 = readLine()?.toIntOrNull()?.minus(1)
        print("Entrez la nouvelle position du monstre : ")
        val pos2 = readLine()?.toIntOrNull()?.minus(1)

        if (pos1 != null && pos2 != null &&
            pos1 in joueur.equipeMonstre.indices &&
            pos2 in joueur.equipeMonstre.indices
        ) {
            val tmp = joueur.equipeMonstre[pos1]
            joueur.equipeMonstre[pos1] = joueur.equipeMonstre[pos2]
            joueur.equipeMonstre[pos2] = tmp
            println("✅ ${tmp.nom} a changé de place avec ${joueur.equipeMonstre[pos1].nom}")
        } else {
            println("❌ Positions invalides.")
        }
    }

    // --- Méthode pour examiner l’équipe ---
    fun examineEquipe() {
        var continuer = true
        while (continuer) {
            println("\n=== Équipe de ${joueur.nom} ===")
            joueur.equipeMonstre.forEachIndexed { index, monstre ->
                println("${index + 1}. ${monstre.nom} - PV: ${monstre.pv}/${monstre.pvMax} (Niv. ${monstre.niveau})")
            }
            println("\nTapez le numéro du monstre pour voir ses détails.")
            println("Tapez 'm' pour modifier l’ordre de l’équipe.")
            println("Tapez 'q' pour revenir au menu principal.")

            when (val choix = readLine()) {
                "q" -> continuer = false
                "m" -> modifierOrdreEquipe()
                else -> {
                    val num = choix?.toIntOrNull()
                    if (num != null && num in 1..joueur.equipeMonstre.size) {
                        joueur.equipeMonstre[num - 1].afficheDetail()
                    } else {
                        println("❌ Choix invalide.")
                    }
                }
            }
        }
    }

    // --- Méthode principale pour jouer ---
    fun jouer() {
        var continuer = true
        while (continuer) {
            println("\n📍 Vous êtes dans la zone : ${zoneActuelle.nom}")
            println("Que voulez-vous faire ?")
            println("1. Rencontrer un monstre sauvage")
            println("2. Examiner l’équipe")
            println("3. Aller à la zone suivante")
            println("4. Aller à la zone précédente")
            println("5. Quitter le jeu")

            when (readLine()?.toIntOrNull()) {
                1 -> zoneActuelle.rencontreMonstre(joueur)
                2 -> examineEquipe()
                3 -> allerZoneSuivante()
                4 -> allerZonePrecedente()
                5 -> {
                    println("👋 Merci d’avoir joué !")
                    continuer = false
                }
                else -> println("❌ Choix invalide.")
            }
        }
    }

    // --- Navigation entre zones ---
    private fun allerZoneSuivante() {
        if (zoneActuelle.zoneSuivante != null) {
            zoneActuelle = zoneActuelle.zoneSuivante!!
            println("➡️ Vous avancez vers ${zoneActuelle.nom}")
        } else {
            println("❌ Il n’y a pas de zone suivante.")
        }
    }

    private fun allerZonePrecedente() {
        if (zoneActuelle.zonePrecedente != null) {
            zoneActuelle = zoneActuelle.zonePrecedente!!
            println("⬅️ Vous retournez vers ${zoneActuelle.nom}")
        } else {
            println("❌ Il n’y a pas de zone précédente.")
        }
    }
}
