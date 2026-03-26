    package com.icl.ohsguide.auth

    import android.content.Intent
    import android.os.Bundle
    import android.widget.EditText
    import android.widget.TextView
    import android.widget.Toast
    import androidx.activity.enableEdgeToEdge
    import androidx.appcompat.app.AppCompatActivity
    import androidx.core.view.ViewCompat
    import androidx.core.view.WindowInsetsCompat
    import androidx.core.widget.addTextChangedListener
    import com.google.android.material.button.MaterialButton
    import com.icl.ohsguide.R

    class ForgotPasswordActivity : AppCompatActivity() {
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            enableEdgeToEdge()
            setContentView(R.layout.activity_forgot_password)
            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
                val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
                insets
            }
            val passwordBox = findViewById<EditText>(R.id.forgotPassword)
            val reenteredPasswordBox = findViewById<EditText>(R.id.forgotPasswordReinput)
            val resetButton = findViewById<MaterialButton>(R.id.finishSetupButton)

            val lengthReqText = findViewById<TextView>(R.id.lengthRequirementText)
            val matchReqText = findViewById<TextView>(R.id.matchRequirementText)

            passwordBox.addTextChangedListener { text ->
                val currentPassword = text.toString()

                if (currentPassword.length >= 6) {
                    lengthReqText.paintFlags = lengthReqText.paintFlags or android.graphics.Paint.STRIKE_THRU_TEXT_FLAG
                } else {
                    lengthReqText.paintFlags = lengthReqText.paintFlags and android.graphics.Paint.STRIKE_THRU_TEXT_FLAG.inv()
                }

                checkPasswordsMatch(currentPassword, reenteredPasswordBox.text.toString(), matchReqText)
            }

            reenteredPasswordBox.addTextChangedListener { text ->
                checkPasswordsMatch(passwordBox.text.toString(), text.toString(), matchReqText)
            }

            resetButton.setOnClickListener {
                val passwordText = passwordBox.text.toString()
                val reenteredText = reenteredPasswordBox.text.toString()

                if (passwordText.isEmpty() || passwordText.length < 6) {
                    Toast.makeText(this, "Please enter a valid 6-character password", Toast.LENGTH_SHORT).show()
                } else if (reenteredText != passwordText) {
                    Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Password reset successfully!", Toast.LENGTH_SHORT).show()

                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }

        private fun checkPasswordsMatch(original: String, reentered: String, matchText: TextView) {
            if (original.isNotEmpty() && original == reentered) {
                matchText.paintFlags = matchText.paintFlags or android.graphics.Paint.STRIKE_THRU_TEXT_FLAG
            } else {
                matchText.paintFlags = matchText.paintFlags and android.graphics.Paint.STRIKE_THRU_TEXT_FLAG.inv()
            }
        }
    }