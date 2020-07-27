package com.example.bunker

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.math.log


class MainActivity : AppCompatActivity() {
    private lateinit var mAuth : FirebaseAuth
    private lateinit var loginBtn: ImageButton
    private lateinit var username: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            this.supportActionBar!!.hide()
        } catch (e: NullPointerException) {
        }
        setContentView(R.layout.activity_main)
        mAuth = FirebaseAuth.getInstance()
        username = findViewById(R.id.nickname)
        updateNickname()
    }

    override fun onStart() {
        super.onStart()
        val button = findViewById<Button>(R.id.createButton)

        button.setOnClickListener{
            val intent = Intent(this, LobbyActivity::class.java)
            startActivity(intent)
        }

        mAuth = FirebaseAuth.getInstance()

        loginBtn = findViewById(R.id.profileBtn)

        loginBtn.setOnClickListener{
            if(mAuth.currentUser == null) {
                val intent = Intent(this, SignupActivity::class.java)
                startActivity(intent)
            } else {
                logoutDialog()
            }


        }
        updateNickname()
    }

    override fun onResume() {
        super.onResume()
        updateNickname()
    }
    private fun joinDialog(savedInstanceState: View?) {
        val builder = AlertDialog.Builder(this)
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(layoutInflater.inflate(R.layout.dialog_join, null))
            // Add action buttons
            .setPositiveButton(R.string.dialog_join,
                    DialogInterface.OnClickListener { dialog, id ->
                        // joined the game...
                    })
            .setNegativeButton(R.string.dialog_close,
                    DialogInterface.OnClickListener { dialog, id ->
                        dialog.cancel()
                    })
            builder.create()
            builder.show()
    }
    private fun logoutDialog() {
        val builder = AlertDialog.Builder(this)
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setMessage(getString(R.string.logoutAlert))
            .setPositiveButton(getString(R.string.logoutPositive), DialogInterface.OnClickListener {
            dialog, id ->
                FirebaseAuth.getInstance().signOut()
                updateNickname()
        })
            .setNegativeButton(getString(R.string.logoutNegative), DialogInterface.OnClickListener {
                dialog, id -> dialog.cancel()
            })
        builder.create()
        builder.show()
    }
    private fun updateNickname() {
        mAuth = FirebaseAuth.getInstance()
        if(mAuth.currentUser == null) {
            username.text = "NOT LOGGED IN"
        }
        else {
            username.text = mAuth.currentUser?.displayName
        }
    }
}