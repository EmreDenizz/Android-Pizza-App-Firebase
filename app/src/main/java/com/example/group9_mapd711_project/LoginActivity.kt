/**
 * @Group 9
 * @author Emre Deniz (301371047)
 * @author Muindo Gituku (301372521)
 * @author Nkemjika Obi (301275091)
 * @date Nov 24, 2023
 * @description: Android Project
 */

package com.example.group9_mapd711_project

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Initialize UI elements
        var LoginButton: Button = findViewById(R.id.loginButton)

        // Login button actions
        LoginButton.setOnClickListener {
            startActivity(Intent(this,CitySelectorActivity::class.java))
        }
    }
}