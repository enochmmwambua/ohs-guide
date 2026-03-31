package com.icl.ohsguide.auth

import android.os.Bundle
import android.widget.TextView
import com.icl.ohsguide.R
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainPageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_page_activity)

        val usernameText = findViewById<TextView>(R.id.usernameDisplay)
        val emailText = findViewById<TextView>(R.id.emailDisplay)



        //val passedUsername = intent.getStringExtra("username") ?: "Guest"
        //val passedEmail = intent.getStringExtra("email") ?: "No email provided"

        //usernameText.text = passedUsername
        //emailText.text = passedEmail

    }
}