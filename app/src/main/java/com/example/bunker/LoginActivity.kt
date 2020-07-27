package com.example.bunker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var passEt: EditText
    private lateinit var loginEt: EditText
    private lateinit var login: Button
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        try {
            this.supportActionBar!!.hide()
        } catch (e: NullPointerException) {
        }

        auth = FirebaseAuth.getInstance()

        passEt = findViewById(R.id.password)
        loginEt = findViewById(R.id.username)

        login = findViewById(R.id.login)


        login.setOnClickListener {
            val email: String = loginEt.text.toString()
            val password: String = passEt.text.toString()
            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_LONG).show()
            } else {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, OnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "Successfully logged in", Toast.LENGTH_LONG).show()
                            val intent = Intent(this, MainActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(this, "Unsuccessful login", Toast.LENGTH_LONG).show()
                        }
                    })
            }

        }
    }
}