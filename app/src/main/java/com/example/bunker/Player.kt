package com.example.bunker

import java.io.Serializable

class Player(private val name: String, private val age: Int, private val gender: String) : Serializable {

    private val id: Int? = null
    private var job: String? = null
    private var medicalCondition: String? = null
    private var additionalInfo: String? = null
    private var backpack: String? = null
    private var phobia: String? = null

}
