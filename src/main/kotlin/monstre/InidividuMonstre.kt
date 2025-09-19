package org.example.monstre

import Entraineur
import kotlin.math.pow
import kotlin.random.Random

class IndividuMonstre(
    val id: Int,
    val nom: String,
    val espece: EspeceMonstre,
    val entraineur: Entraineur?,
    expInit: Double
) {

    var niveau: Int = 1

    var attaque: Int = espece.baseAttaque + Random.nextInt(-2, 3)
    var defense: Int = espece.baseDefense + Random.nextInt(-2, 3)
    var vitesse: Int = espece.baseVitesse + Random.nextInt(-2, 3)
    var attaqueSpe: Int = espece.baseAttaqueSpe + Random.nextInt(-2, 3)
    var defenseSpe: Int = espece.baseDefenseSpe + Random.nextInt(-2, 3)
    var pvMax: Int = espece.basePv + Random.nextInt(-5, 6)
    var potentiel: Double = Random.nextDouble(0.5, 2.0)

    var exp: Double = 0.0
        set(value) {
            field = value
            while (field >= palierExp(niveau)) {
                levelUp()
                println("Le monstre $nom est maintenant niveau $niveau")
            }
        }

    var pv: Int = pvMax
        set(value) {
            field = when {
                value < 0 -> 0
                value > pvMax -> pvMax
                else -> value
            }
        }

    // Initialisation dans init
    init {
        this.exp = expInit
    }

    /** Calcule l'expérience nécessaire pour atteindre un niveau donné */
    fun palierExp(niveau: Int): Double {
        return 100 * (niveau - 1).toDouble().pow(2.0)
    }

    /** Incrémente le niveau et applique les gains */
    fun levelUp() {
        niveau += 1

        val ancienPvMax = pvMax

        attaque += (espece.modAttaque * potentiel).toInt() + Random.nextInt(-2, 3)
        defense += (espece.modDefense * potentiel).toInt() + Random.nextInt(-2, 3)
        vitesse += (espece.modVitesse * potentiel).toInt() + Random.nextInt(-2, 3)
        attaqueSpe += (espece.modAttaqueSpe * potentiel).toInt() + Random.nextInt(-2, 3)
        defenseSpe += (espece.modDefenseSpe * potentiel).toInt() + Random.nextInt(-2, 3)

        val gainPvMax = (espece.modPv * potentiel).toInt() + Random.nextInt(-5, 6)
        pvMax += gainPvMax
        pv += gainPvMax
    }
    fun attaquer(cible: IndividuMonstre) {
        // Calcul des dégâts simples : différence entre attaque et défense cible (minimum 1)
        val degats = (this.attaque - cible.defense).coerceAtLeast(1)

        // On enlève les dégâts aux PV de la cible
        cible.pv = (cible.pv - degats).coerceAtLeast(0)

        // Affichage du résultat de l'attaque
        println("${this.nom} attaque ${cible.nom} et inflige $degats dégâts.")
        println("${cible.nom} a maintenant ${cible.pv} PV.")
    }

}
