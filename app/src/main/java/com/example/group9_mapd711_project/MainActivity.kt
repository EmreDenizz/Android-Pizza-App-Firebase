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
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize UI elements
        var LoginButton: Button = findViewById(R.id.loginButton)
        var RegisterButton: Button = findViewById(R.id.registerButton)

        // Login button actions
        LoginButton.setOnClickListener {
            startActivity(Intent(this,LoginActivity::class.java))
        }

        // Register button actions
        RegisterButton.setOnClickListener {
            startActivity(Intent(this,RegisterActivity::class.java))
        }
    }
}