package com.example.bunker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.core.util.Pair
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

@Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class GameActivity : AppCompatActivity() {
    private var bunker: Bunker? = null
    private val db = Firebase.firestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            this.supportActionBar!!.hide()
        } catch (e: NullPointerException) {
        }
        setContentView(R.layout.activity_game)
        if(intent.hasExtra("bunker")) {
            this.bunker = intent.getSerializableExtra("bunker") as Bunker
        }
        val bunkerData = findViewById<ImageView>(R.id.imageViewBunker)
        bunkerData.setOnClickListener {
            if(this.bunker == null) {
                db.collection("bunker").document(intent.getStringExtra("gameID").toString()).get().addOnSuccessListener {
                    this.bunker = it.toObject<Bunker>()
                }
            }
            val intent = Intent(this, BunkerDataActivity::class.java)
            val imageViewPair = Pair.create<View, String>(bunkerData, "bunkerCharacteristics")
            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, imageViewPair)
            intent.putExtra("bunker", this.bunker)
            startActivity(intent, options.toBundle())
        }
        adapterImplementation()
    }
    private fun adapterImplementation() {
        val listOfPlayers = intent.getStringArrayListExtra("listOfPlayers")
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
        val layoutManager = GridLayoutManager(this, 2)
        val mAdapter = PlayerGameAdapter(imageModelArrayList)
        val dividerItemDecorationVertical = DividerItemDecoration(
            recyclerView.context,
            DividerItemDecoration.VERTICAL
        )
        val dividerItemDecorationHorizontal = DividerItemDecoration(
            recyclerView.context,
            DividerItemDecoration.HORIZONTAL
        )
        recyclerView.addItemDecoration(dividerItemDecorationHorizontal)
        recyclerView.addItemDecoration(dividerItemDecorationVertical)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = mAdapter
    }


}