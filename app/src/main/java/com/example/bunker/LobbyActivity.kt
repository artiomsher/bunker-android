package com.example.bunker

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


@Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class LobbyActivity : AppCompatActivity() {
    private var gameID = 0
    private var listOfPlayers = arrayListOf<String>()
    private val db = Firebase.firestore
    private lateinit var openLobby : Button
    private lateinit var textViewForID : TextView
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            this.supportActionBar!!.hide()
        } catch (e: NullPointerException) {
        }
        setContentView(R.layout.activity_lobby)
        openLobby = findViewById<Button>(R.id.lobbyBtn)
        textViewForID = findViewById<TextView>(R.id.textViewID)
        progressBar = findViewById<ProgressBar>(R.id.progressBar)
        progressBar.visibility = View.GONE
        if(intent.hasExtra("gameID")) {
            openLobby.visibility = View.GONE
            gameID = intent.getIntExtra("gameID", 0)
            textViewForID.text = getString(R.string.lobbyID, gameID)
            getPlayersFromFirestore()

        }
        openLobby.setOnClickListener {
            createLobby()
        }
    }


    private fun getPlayersFromFirestore() {
        progressBar.visibility = View.VISIBLE
        db.collection("games").document((gameID).toString()).get()
            .addOnSuccessListener { document ->
                    listOfPlayers = document.data!!.get("currentPlayers") as ArrayList<String>
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
                    "currentPlayers" to arrayListOf(mAuth.currentUser!!.displayName)
                )
                db.collection("games").document((gameID).toString()).set(game)
                openLobby.text = getString(R.string.lobbyBtnPlay)
                textViewForID.text = getString(R.string.lobbyID, gameID)
                progressBar.visibility = View.GONE
            }.addOnCompleteListener {
                getPlayersFromFirestore()
                val docRef = db.collection("games").document((gameID).toString())
                docRef.addSnapshotListener { snap: DocumentSnapshot?, e ->
                    Log.w("Snap", "Listening.", e)
                    getPlayersFromFirestore()
                    return@addSnapshotListener
                }
            }
    }

    private fun adapterImplementation() {
        val myImageList = R.drawable.profile
        val list = ArrayList<PlayerModel>()
        listOfPlayers.forEach() { singleElement ->
            val imageModel = PlayerModel()
            imageModel.setUsernames(singleElement)
            Log.d("forEach", singleElement)
            imageModel.setImage_drawables(myImageList)
            list.add(imageModel)
        }
        val imageModelArrayList: ArrayList<PlayerModel> = list
        val recyclerView = findViewById<View>(R.id.recyclerView) as RecyclerView

        val layoutManager = LinearLayoutManager(this)
        val mAdapter = PlayerAdapter(imageModelArrayList)
        val dividerItemDecoration = DividerItemDecoration(
            recyclerView!!.context,
            layoutManager.orientation
        )
        recyclerView.addItemDecoration(dividerItemDecoration)
        recyclerView.layoutManager = layoutManager

        recyclerView.adapter = mAdapter
        mAdapter.notifyDataSetChanged()
        progressBar.visibility = View.GONE
    }


}