package com.example.counter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    private lateinit var button: Button
    private lateinit var emailInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var statusText: TextView

    private var count = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button = findViewById(R.id.login_button)
        emailInput = findViewById(R.id.email_input)
        passwordInput = findViewById(R.id.password_input)
        statusText = findViewById(R.id.status_text)

        button.setOnClickListener {
            val email = emailInput.text.toString()
            val password = passwordInput.text.toString()

            if (email == "quality+global@equityzen.com" && password == "ezmoney1") {
                statusText.text = "User is logged in"
            } else {
                statusText.text = "Username or password is incorrect"
            }
            statusText.visibility = View.VISIBLE
        }
    }
}