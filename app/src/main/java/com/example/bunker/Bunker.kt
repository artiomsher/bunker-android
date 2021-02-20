package com.example.bunker

import java.io.Serializable

class Bunker(numOfPlayers: Int = 0, bunkerId: Int = 0) : Serializable {
    var id = bunkerId
    var activePlayers = numOfPlayers
    var apocalypseDescription: String? = null
    var sizeInMeters: Int? = null
    var additionalInfo: String? = null
    var otherCharacteristicsDuringTheGame: String? = null
    var capacityOfBunker: Int? = null
}