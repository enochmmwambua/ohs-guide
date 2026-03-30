package com.icl.ohsguide.auth

import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.google.android.material.button.MaterialButton
import com.icl.ohsguide.R
import com.icl.ohsguide.auth.data.User
import com.icl.ohsguide.auth.data.UserDatabase
import kotlinx.coroutines.launch

class UsernameActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_create_username)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.finishSetupButton)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val usernameBox = findViewById<EditText>(R.id.usernameInput)
        val finishButton = findViewById<MaterialButton>(R.id.finishSetupButton)

        finishButton.setOnClickListener {
            val usernameText = usernameBox.text.toString()

            if (usernameText.isEmpty()) {
                Toast.makeText(this, "Please enter a username", Toast.LENGTH_SHORT).show()
            } else if (usernameText.length < 3) {
                Toast.makeText(this, "Username must be at least 3 characters", Toast.LENGTH_SHORT).show()
            } else {
                val passedEmail = intent.getStringExtra("extraEmail") ?: ""
                val passedPassword = intent.getStringExtra("extraPassword") ?: ""

                lifecycleScope.launch {
                    val userDao = UserDatabase.getDatabase(this@UsernameActivity).userDao()
                    val newUser = User(
                        email = passedEmail,
                        username = usernameText,
                        passwordHash = passedPassword
                    )
                    val database = UserDatabase.getDatabase(applicationContext)
                    database.userDao().insertUser(newUser)
                }

                Toast.makeText(this, "Welcome, $usernameText!", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }
}