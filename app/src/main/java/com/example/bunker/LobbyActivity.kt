package com.example.bunker

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.io.BufferedReader
import java.io.InputStreamReader
import kotlin.collections.ArrayList


class LobbyActivity : AppCompatActivity() {
    private var gameID = 0
    private var listOfPlayers = arrayListOf<String>()
    private val db = Firebase.firestore
    private lateinit var openLobby : Button
    private lateinit var textViewForID : TextView
    private lateinit var progressBar: ProgressBar
    private var bunker: Bunker? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            this.supportActionBar!!.hide()
        } catch (e: NullPointerException) {
        }
        setContentView(R.layout.activity_lobby)
        openLobby = findViewById(R.id.lobbyBtn)
        textViewForID = findViewById(R.id.textViewID)
        progressBar = findViewById(R.id.progressBar)
        progressBar.visibility = View.GONE
        if(intent.hasExtra("gameID")) {
            openLobby.visibility = View.GONE
            gameID = intent.getIntExtra("gameID", 0)
            textViewForID.text = getString(R.string.lobbyID, gameID)
            getPlayersFromFirestore()
            val docRef = db.collection("games").document((gameID).toString())
            docRef.addSnapshotListener { snap: DocumentSnapshot?, _ ->
                if (snap != null) {
                    if(snap.get("phase") == "game") {
                        val intent = Intent(this, GameActivity::class.java)
                        intent.putExtra("gameID", gameID.toString())
                        intent.putExtra("listOfPlayers", listOfPlayers)
                        startActivity(intent)
                    }
                }
                if(listOfPlayers.isNotEmpty()) {
                    getPlayersFromFirestore()
                }
            }
        }
        openLobby.setOnClickListener {
            if(openLobby.text != getString(R.string.lobbyBtnPlay)) {
                createLobby()
            } else {
                db.collection("games").document(gameID.toString())
                    .update("phase", "game")
                    .addOnFailureListener{
                        Log.d("FAILURE", "Cannot change game phase")
                    }
            }
        }
    }


    private fun getPlayersFromFirestore() {
        progressBar.visibility = View.VISIBLE
        db.collection("games").document((gameID).toString()).get()
            .addOnSuccessListener { document ->
                    listOfPlayers = document.data?.get("currentPlayers") as ArrayList<String>
            }.addOnCompleteListener {
                adapterImplementation()
            }
    }

    private fun createLobby() {
        val mAuth = FirebaseAuth.getInstance()
        val timestamp = Timestamp.now()
        progressBar.visibility = View.VISIBLE
        db.collection("games").orderBy("time", Query.Direction.DESCENDING).limit(1).get()
            .addOnSuccessListener { queryDocumentSnapshots ->
                for (snap in queryDocumentSnapshots) {
                    gameID = snap.getLong("id")!!.toInt() + 1
                }
                val game = hashMapOf(
                    "id" to (gameID),
                    "time" to timestamp.toDate(),
                    "numOfPlayers" to 1,
                    "host" to mAuth.currentUser!!.displayName,
                    "currentPlayers" to arrayListOf(mAuth.currentUser!!.displayName),
                    "phase" to getString(R.string.gamePhaseLobby)
                )
                db.collection("games").document((gameID).toString()).set(game)
                openLobby.text = getString(R.string.lobbyBtnPlay)
                textViewForID.text = getString(R.string.lobbyID, gameID)
                progressBar.visibility = View.GONE

            }.addOnCompleteListener {
                getPlayersFromFirestore()
                val docRef = db.collection("games").document((gameID).toString())
                docRef.addSnapshotListener { snap: DocumentSnapshot?, _ ->
                    if (snap != null) {
                        if(snap.get("phase") == "game") {
                            val intent = Intent(this, GameActivity::class.java)
                            intent.putExtra("gameID", gameID)
                            intent.putExtra("listOfPlayers", listOfPlayers)
                            intent.putExtra("bunker", this.bunker)
                            startActivity(intent)
                        }
                    }
                    getPlayersFromFirestore()
                }
            }
    }

    private fun adapterImplementation() {
        val myImageList = R.drawable.profile
        val list = ArrayList<PlayerModel>()
        listOfPlayers.forEach { singleElement ->
            val imageModel = PlayerModel()
            imageModel.setUsernames(singleElement)
            imageModel.setImage_drawables(myImageList)
            list.add(imageModel)
        }
        val imageModelArrayList: ArrayList<PlayerModel> = list
        val recyclerView = findViewById<View>(R.id.recyclerView) as RecyclerView
        val layoutManager = LinearLayoutManager(this)
        val mAdapter = PlayerAdapter(imageModelArrayList)
        val dividerItemDecoration = DividerItemDecoration(
            recyclerView.context,
            layoutManager.orientation
        )
        recyclerView.addItemDecoration(dividerItemDecoration)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = mAdapter
        mAdapter.notifyDataSetChanged()
        progressBar.visibility = View.GONE
        createBunkerForHost()
    }

    private fun createBunkerForHost() {
        this.bunker = Bunker(listOfPlayers.size, gameID)
        this.bunker!!.apocalypseDescription = readFile(getString(R.string.apocalypseFile))
/*        val apocalypse = hashMapOf(
            "id" to gameID,
            "apocalypse" to this.bunker!!.apocalypseDescription
        )*/
        db.collection("bunker").document(gameID.toString())
            .set(this.bunker!!)
    }
    private fun readFile(filename: String): String {
        val reader = BufferedReader(InputStreamReader(assets.open(filename)))
        val arrayOfApocalypses = arrayListOf<String>()
        for (line in reader.lines()) {
            arrayOfApocalypses.add(line)
        }
        return arrayOfApocalypses[(0 until arrayOfApocalypses.size).random()]
    }


}