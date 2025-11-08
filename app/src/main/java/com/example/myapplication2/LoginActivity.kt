package com.example.myapplication2

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.thingclips.smart.android.user.api.ILoginCallback
import com.thingclips.smart.android.user.bean.User
import com.thingclips.smart.home.sdk.ThingHomeSdk
import com.thingclips.smart.sdk.api.IResultCallback

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val countryCodeEditText = findViewById<EditText>(R.id.countryCodeEditText)
        val emailEditText = findViewById<EditText>(R.id.emailEditText)
        val passwordEditText = findViewById<EditText>(R.id.passwordEditText)
        val registerButton = findViewById<Button>(R.id.registerButton)
        val loginButton = findViewById<Button>(R.id.loginButton)

        registerButton.setOnClickListener {
            val countryCode = countryCodeEditText.text.toString()
            val email = emailEditText.text.toString()

            ThingHomeSdk.getUserInstance().sendVerifyCodeWithUserName(email, "", countryCode, 1, object : IResultCallback {
                override fun onError(code: String, error: String) {
                    Toast.makeText(this@LoginActivity, "Error sending code: $error", Toast.LENGTH_SHORT).show()
                }

                override fun onSuccess() {
                    Toast.makeText(this@LoginActivity, "Verification code sent. Please check your email.", Toast.LENGTH_SHORT).show()
                }
            })
        }

        loginButton.setOnClickListener {
            val countryCode = countryCodeEditText.text.toString()
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            ThingHomeSdk.getUserInstance().loginWithEmail(countryCode, email, password, object : ILoginCallback {
                override fun onSuccess(user: User) {
                    Toast.makeText(this@LoginActivity, "Login Successful!", Toast.LENGTH_SHORT).show()
                    // Navigate to the main activity
                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish() // Close the login activity
                }

                override fun onError(code: String, error: String) {
                    Toast.makeText(this@LoginActivity, "Login Failed: $error", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
}