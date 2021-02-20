package com.example.bunker

import android.app.Activity
import android.content.Intent
import androidx.core.util.Pair
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityOptionsCompat

class PlayerGameAdapter(private val imageModelArrayList: MutableList<PlayerModel>) : RecyclerView.Adapter<PlayerGameAdapter.ViewHolder>() {
    inner class ViewHolder(var layout:View) : RecyclerView.ViewHolder(layout) {
        var imgView: ImageView
        var txtMsg: TextView

        init {
            imgView = layout.findViewById<View>(R.id.imageViewPlayer) as ImageView
            txtMsg = layout.findViewById<View>(R.id.textViewPlayer) as TextView
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val v = inflater.inflate(R.layout.player_game_view, parent, false)

        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val info = imageModelArrayList[position]
        holder.imgView.setImageResource(info.getImage_drawables())
        holder.txtMsg.text = info.getUsernames()
        holder.imgView.setOnClickListener {
            val intent = Intent(it.context, PlayerCharacteristicActivity::class.java)
            val imageViewPair = Pair.create<View, String>(holder.imgView, "characteristics")
            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(it.context as Activity, imageViewPair)
            intent.putExtra("username", info.getUsernames())
            it.context.startActivity(intent, options.toBundle())
        }
    }

    override fun getItemCount(): Int {
        return imageModelArrayList.size
    }
}