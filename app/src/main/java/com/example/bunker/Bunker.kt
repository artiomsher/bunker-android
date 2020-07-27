package com.example.bunker

class Bunker(numOfPlayers: Int, bunkerId: Int) {
    private val bunkerId = bunkerId
    private val activePlayers = numOfPlayers
    private val sizeInMeters: Int? = null
    private val additionalInfo: String? = null
    private val otherCharacteristicsDuringTheGame: String? = null
    private var sizeInPlayers: Int? = null
    fun getSizeInPlayers(): Int? {
        return sizeInPlayers
    }
    fun setSizeInPlayers(sizeInPlayers: Int) {
        this.sizeInPlayers = sizeInPlayers
    }
}