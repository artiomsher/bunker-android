package com.example.bunker

import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*


class LobbyActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            this.supportActionBar!!.hide()
        } catch (e: NullPointerException) {
        }
        setContentView(R.layout.activity_lobby)
        val openLobby = findViewById<Button>(R.id.lobbyBtn)
        openLobby.setOnClickListener {
            createLobby()
            if(openLobby.text == "Open lobby") {
                openLobby.text = getString(R.string.lobbyBtnPlay)
            }
            else {

            }
        }
    }

    private fun createLobby() {
        var gameID = 0
        val db = Firebase.firestore
        val date = Date()
        val mAuth = FirebaseAuth.getInstance()
        db.collection("games").orderBy("time", Query.Direction.DESCENDING).limit(1).get()
            .addOnSuccessListener { queryDocumentSnapshots ->
                for (snap in queryDocumentSnapshots) {
                    gameID = snap.getLong("id")!!.toInt()
                }
                val game = hashMapOf(
                    "id" to gameID + 1,
                    "time" to "" + date,
                    "numOfPlayers" to 1,
                    "host" to mAuth.currentUser!!.displayName,
                    "currentPlayers" to arrayListOf(mAuth.currentUser!!.displayName)
                )
                db.collection("games").document(date.toString()).set(game)
            }
    }
}