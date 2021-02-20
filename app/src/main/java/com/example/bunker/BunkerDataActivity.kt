package com.example.bunker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.w3c.dom.Text

class BunkerDataActivity : AppCompatActivity() {
    private lateinit var apocalypseDescription: TextView
    private var listOfCharacteristics = arrayListOf<String>()
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        try {
            this.supportActionBar!!.hide()
        } catch (e: NullPointerException) {
        }
        setContentView(R.layout.activity_bunker_data)
        apocalypseDescription = findViewById<TextView>(R.id.bunkerDescription)
        val bunker = intent.getSerializableExtra("bunker") as? Bunker
        //Log.d("Bunker", bunker?.apocalypseDescription)
        apocalypseDescription.text = bunker?.apocalypseDescription

    }
    private fun adapterImplementation() {
        val myImageList = R.drawable.profile
        val list = ArrayList<PlayerModel>()
        listOfCharacteristics.forEach { singleElement ->
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
    }
}