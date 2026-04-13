package com.icl.ohsguide.auth

import android.app.ProgressDialog
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
import androidx.lifecycle.lifecycleScope
import com.google.android.material.button.MaterialButton
import com.icl.ohsguide.R
import com.icl.ohsguide.auth.data.Preferences
import com.icl.ohsguide.auth.data.UserDatabase
import com.icl.ohsguide.auth.models.LoginRequests
import com.icl.ohsguide.auth.models.LoginResponse
import com.icl.ohsguide.auth.network.ApiClient
import com.icl.ohsguide.auth.network.RetrofitCalls
import kotlinx.coroutines.launch
import kotlin.jvm.java
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


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
        val goToRegisterButton = findViewById<MaterialButton>(R.id.goToRegisterButton)

        submitButton.setOnClickListener {
            val usernameText = username.text.toString()
            val passwordText = password.text.toString()

            if (usernameText.isEmpty() || passwordText.isEmpty()) {
                Toast.makeText(this, "Please fill in all the fields", Toast.LENGTH_SHORT).show()
            } else {
                val call = ApiClient.apiService.login(LoginRequests(email = usernameText, password = passwordText))
                val dialog = ProgressDialog(this)
                dialog.setTitle("Authenticating")
                dialog.setMessage("Please wait...")
                dialog.show()
                call.enqueue(object : Callback<LoginResponse> {
                    override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                        dialog.dismiss()
                        if (response.isSuccessful) {
                            val post = response.body()
                            val token = post?.access_token

                            if (token != null) {
                                val preferences = Preferences(this@MainActivity)
                                preferences.saveAuthToken(token)
                            }

                            Toast.makeText(this@MainActivity, "Login successful", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this@MainActivity, MainPageActivity::class.java)
                            startActivity(intent)
                            finish()

                        // Handle the retrieved post data
                        } else {
                            val errorBody = response.errorBody()?.string()
                            val errorCode = response.code()
                            if (errorCode == 401) {
                                Toast.makeText(this@MainActivity, "Invalid credentials", Toast.LENGTH_SHORT).show()
                            }
                            else if (errorCode == 404) {
                                Toast.makeText(this@MainActivity, "User not found", Toast.LENGTH_SHORT).show()
                            }
                            // Handle error
                        }
                    }

                    override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                        dialog.dismiss()
                        // Handle failure
                    }
                })
                /*lifecycleScope.launch {
                    val database = UserDatabase.getDatabase(applicationContext)
                    val user = database.userDao().getUserByUsername(usernameText)

                    if (user != null && user.passwordHash == passwordText) {
                        Toast.makeText(
                            this@MainActivity, "Welcome back, ${user.username}!", Toast.LENGTH_SHORT
                        ).show()

                        val intent = Intent(this@MainActivity, MainPageActivity::class.java)
                        intent.putExtra("username", user.username)
                        intent.putExtra("email", user.email)
                        startActivity(intent)
                        finish()
                    } else if (user == null) {
                        Toast.makeText(this@MainActivity, "User not found", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        Toast.makeText(this@MainActivity, "Invalid password", Toast.LENGTH_SHORT)
                            .show()
                    }
                }*/
            }
        }

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