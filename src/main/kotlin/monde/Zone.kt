package org.example.monde


import org.example.monstre.EspeceMonstre
import java.time.ZoneId

class Zone(
    val id:  Int,
    val nom: String,
    val expZone: Int,
    val especesMonstres: MutableList<EspeceMonstre> = mutableListOf(),
    var zoneSuivante: Zone? = null,
    var zonePrecedente: Zone? = null
) {
    // TODO : faire la méthode genereMonstre()
    // TODO : faire la méthode rencontreMonstre()
}
