package com.example.bunker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class PlayerCharacteristicActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player_characteristic)
        try {
            this.supportActionBar!!.hide()
        } catch (e: NullPointerException) {
        }
    }
}