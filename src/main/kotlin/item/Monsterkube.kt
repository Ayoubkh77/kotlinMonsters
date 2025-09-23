package org.example.item

import org.example.monstre.IndividuMonstre

/**
 * La classe MonsterKube représente un objet permettant de capturer un monstre.
 *
 * @param id Identifiant unique de l'objet
 * @param nom Nom de l'objet
 * @param description Description de l'objet
 * @param chanceCapture Chance de capturer le monstre (entre 0.0 et 1.0)
 */
class MonsterKube(
    id: Int,
    nom: String,
    description: String,
    val chanceCapture: Double
) : Item(id, nom, description), Utilisable {

    /**
     * Applique l'effet de la MonsterKube sur le monstre cible (version complexe).
     *
     * @param cible Monstre sur lequel on utilise la MonsterKube
     * @return true si la capture a réussi, false sinon
     */
    override fun utiliser(cible: IndividuMonstre): Boolean {
        println("⚡ Vous lancez la MonsterKube sur ${cible.nom} !")

        // Ratio de PV restants (entre 0.0 et 1.0)
        val ratioVie = cible.pv.toDouble() / cible.pvMax.toDouble()

        // Chance de capture ajustée selon les PV
        val chanceEffective = (chanceCapture * (1.5 - ratioVie)).coerceAtLeast(0.05)

        // Tirage aléatoire
        val alea = Math.random()

        return if (alea <= chanceEffective) {
            println("🎉 Le monstre ${cible.nom} est capturé ! (Chance effective = ${(chanceEffective * 100).toInt()}%)")
            true
        } else {
            println("❌ La capture de ${cible.nom} a échoué... (Chance effective = ${(chanceEffective * 100).toInt()}%)")
            false
        }
    }
}
