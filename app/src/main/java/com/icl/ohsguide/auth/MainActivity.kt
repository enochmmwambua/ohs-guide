package com.icl.ohsguide.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.button.MaterialButton
import com.icl.ohsguide.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val submitButton = findViewById<Button>(R.id.signin)
        val username = findViewById<EditText>(R.id.username)
        val password = findViewById<EditText>(R.id.passwordInput)
        val rememberMe = findViewById<Switch>(R.id.switch1)
        val forgotPassButton = findViewById<MaterialButton>(R.id.ForgotPassButton)

        submitButton.setOnClickListener {
            val usernameText = username.text.toString()
            val passwordText = password.text.toString()
            if (usernameText.isEmpty()){
                Toast.makeText(this, "Please enter a username", Toast.LENGTH_SHORT).show()
            } else if (passwordText.isEmpty()) {
                Toast.makeText(this, "Please enter a password", Toast.LENGTH_SHORT).show()
            } else if (rememberMe.isChecked) {
                Toast.makeText(this, "Welcome back, $usernameText", Toast.LENGTH_SHORT).show()
            } else
                Toast.makeText(this, "Welcome", Toast.LENGTH_SHORT).show()
        }
        val goToRegisterButton = findViewById<MaterialButton>(R.id.goToRegisterButton)

        goToRegisterButton.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        forgotPassButton.setOnClickListener {
            val intent = Intent(this, ForgotPasswordActivity::class.java)
            startActivity(intent)
        }

    }
}