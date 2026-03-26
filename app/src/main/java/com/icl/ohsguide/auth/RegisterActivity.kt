    package com.icl.ohsguide.auth

    import android.content.Intent
    import android.os.Bundle
    import android.widget.EditText
    import android.widget.Toast
    import androidx.activity.enableEdgeToEdge
    import androidx.appcompat.app.AppCompatActivity
    import androidx.core.view.ViewCompat
    import androidx.core.view.WindowInsetsCompat
    import com.google.android.material.button.MaterialButton
    import com.icl.ohsguide.R

    class RegisterActivity : AppCompatActivity() {
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            enableEdgeToEdge()
            setContentView(R.layout.activity_register)
            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
                val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
                insets
            }
            val goToLoginButton = findViewById<MaterialButton>(R.id.goToLoginButton)
            val email = findViewById<EditText>(R.id.editTextEmail)
            val password = findViewById<EditText>(R.id.passwordInput)
            val reenteredPassword = findViewById<EditText>(R.id.passwordReinput)
            val registerButton = findViewById<MaterialButton>(R.id.RegisterButton)

            goToLoginButton.setOnClickListener {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }

            registerButton.setOnClickListener {
                val emailText = email.text.toString()
                val passwordText = password.text.toString()
                val reenteredPasswordText = reenteredPassword.text.toString()

                if (emailText.isEmpty())
                    Toast.makeText(this, "Please enter an email address", Toast.LENGTH_SHORT).show()
                else if (passwordText.isEmpty() || (password.length() < 6))
                    Toast.makeText(
                        this,
                        "Please enter a password with at least 6 characters",
                        Toast.LENGTH_SHORT
                    ).show()
                else if (reenteredPasswordText != passwordText)
                    Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
                else {
                    Toast.makeText(this, "Account created", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, UsernameActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }
    }