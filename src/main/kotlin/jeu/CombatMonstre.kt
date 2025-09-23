package org.example.jeu

import org.example.monstre.IndividuMonstre
import org.example.dresseur.Entraineur
import org.example.item.MonsterKube
import org.example.item.Utilisable

class CombatMonstre(
    val joueur: Entraineur,
    val monstreJoueur: IndividuMonstre,
    val monstreSauvage: IndividuMonstre
) {
    var round: Int = 1

    // Le joueur perd si aucun monstre de son équipe n'a de PV > 0
    fun gameOver(): Boolean {
        return joueur.equipeMonstre.all { it.pv <= 0 }
    }

    // Le joueur gagne si capture ou monstre sauvage à 0 PV
    fun joueurGagne(): Boolean {
        if (monstreSauvage.pv <= 0) {
            println("${joueur.nom} a gagné !")

            val gainExp = (monstreSauvage.exp * 0.20).toInt()
            monstreJoueur.exp += gainExp

            println("${monstreJoueur.nom} gagne $gainExp exp")
            return true
        } else if (monstreSauvage.entraineur == joueur) {
            println("${monstreSauvage.nom} a été capturé !")
            return true
        }
        return false
    }

    fun actionAdversaire() {
        if (monstreSauvage.pv > 0) {
            monstreSauvage.attaquer(monstreJoueur)
        }
    }

    // Le joueur choisit l'action (attaque ou item)
    fun actionJoueur(): Boolean {
        println("Choisissez une action :")
        println("1. Attaquer")
        println("2. Utiliser un objet")
        val choix = readLine()?.toIntOrNull()

        when (choix) {
            1 -> {
                monstreJoueur.attaquer(monstreSauvage)
                if (monstreSauvage.pv <= 0) {

                    return false
                }
            }
            2 -> {
                println("Objets disponibles :")
                joueur.sacAItems.forEachIndexed { index, item ->
                    println("${index + 1}. ${item.nom}")
                }
                println("Choisissez un objet à utiliser (numéro) :")
                val choixItem = readLine()?.toIntOrNull()
                if (choixItem != null && choixItem in 1..joueur.sacAItems.size) {
                    val item = joueur.sacAItems[choixItem - 1]
                    val effet = if (item is MonsterKube) {
                        val captureReussie = item.utiliser(monstreSauvage)
                        if (captureReussie) {
                            monstreSauvage.entraineur = joueur // capture
                            joueur.equipeMonstre.add(monstreSauvage)
                        }
                        captureReussie
                    } else {
                        if (item is Utilisable){
                            item.utiliser(monstreSauvage)
                        } else {
                            println("Objet inutilisable")
                           false
                        }
                        // Supposons qu'un item non capture soigne le monstre

                    }

                } else {
                    println("Choix invalide.")
                }
            }
            else -> println("Action invalide.")
        }
        return true
    }

    // Tentative de capture (exemple basique)
    fun tenterCapture(): Boolean {
        val monsterKube = joueur.sacAItems.filterIsInstance<MonsterKube>().firstOrNull()
        if (monsterKube != null) {
            val capture = monsterKube.utiliser(monstreSauvage)
            if (capture) {
                monstreSauvage.entraineur = joueur
                joueur.equipeMonstre.add(monstreSauvage)
                joueur.sacAItems.remove(monsterKube)
                return true
            }
        }
        return false
    }
    fun jouer() {
        println("======== Début Round : $round ========")

        // Affichage des stats du monstre sauvage
        println("Niveau : ${monstreSauvage.niveau}")
        println("PV : ${monstreSauvage.pv} / ${monstreSauvage.pvMax}")
        monstreSauvage.afficheAsciiFace() // méthode affichant l'art ASCII du monstre sauvage (face)

        // Affichage des stats du monstre joueur
        monstreJoueur.afficheAsciiDos() // méthode affichant l'art ASCII du monstre joueur (dos)
        println("Niveau : ${monstreJoueur.niveau}")
        println("PV : ${monstreJoueur.pv} / ${monstreJoueur.pvMax}")

        // Choix de l'ordre en fonction de la vitesse
        if (monstreJoueur.vitesse >= monstreSauvage.vitesse) {
            // Le monstre joueur agit en premier
            if (!this.actionJoueur()) {
                return // arrêt du tour si actionJoueur() retourne false
            }
            if (monstreSauvage.pv > 0) {
                this.actionAdversaire()
            }
        } else {
            // Le monstre sauvage agit en premier
            if (monstreSauvage.pv > 0) {
                this.actionAdversaire()
            }
            if (monstreJoueur.pv > 0) {
                if (!this.actionJoueur()) {
                    return
                }
            }
        }

        round++
    }
    fun lanceCombat() {
        while (!gameOver() && !joueurGagne()) {
            jouer()
            println("======== Fin du Round : $round ========")
            round++
        }
        if (gameOver()) {
            joueur.equipeMonstre.forEach { it.pv = it.pvMax }
            println("Game Over !")
        }
    }

}
