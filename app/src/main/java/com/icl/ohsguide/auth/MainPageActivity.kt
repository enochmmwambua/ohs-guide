package com.icl.ohsguide.auth

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import com.icl.ohsguide.R
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.icl.ohsguide.auth.data.Preferences
import com.icl.ohsguide.auth.models.ProviderStringResponse
import com.icl.ohsguide.auth.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainPageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_page_activity)

        val usernameText = findViewById<TextView>(R.id.usernameDisplay)
        val emailText = findViewById<TextView>(R.id.emailDisplay)

        val preferences = Preferences(this)
        val savedtoken = preferences.fetchAuthToken()
        if (savedtoken == null) {
            Toast.makeText(this, "Please Log In!", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
            return
        }

        usernameText.text = "Loading Profile"
        emailText.text = "..."

        val authHeader = "Bearer $savedtoken"
        val call = ApiClient.apiService.me(authHeader)

        call.enqueue(object : Callback<ProviderStringResponse> {
            override fun onResponse(
                call: Call<ProviderStringResponse>,
                response: Response<ProviderStringResponse>
            ) {
                if (response.isSuccessful) {
                    val profileData = response.body()
                    val myUser = profileData?.user

                    usernameText.text = myUser?.firstName + " " + myUser?.lastName ?: myUser?.username ?: "Guest"
                    emailText.text = myUser?.email ?: "No email provided"
                } else {
                    Toast.makeText(this@MainPageActivity, "Session Expired. Please Log In Again!", Toast.LENGTH_SHORT).show()
                    preferences.logoutUser()
                    val intent = Intent(this@MainPageActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }

            }

            override fun onFailure(call: Call<ProviderStringResponse>, t: Throwable) {
                usernameText.text = "Offline Mode"
                Toast.makeText(this@MainPageActivity, "Network Error ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}




        //val passedUsername = intent.getStringExtra("username") ?: "Guest"
        //val passedEmail = intent.getStringExtra("email") ?: "No email provided"

        //usernameText.text = passedUsername
        //emailText.text = passedEmail
