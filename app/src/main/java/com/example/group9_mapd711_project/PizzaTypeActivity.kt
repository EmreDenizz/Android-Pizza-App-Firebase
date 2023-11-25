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

class PizzaTypeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pizza_type)

        // Initialize UI elements
        var ButtonContinue: Button = findViewById(R.id.buttonContinue)

        // Login button actions
        ButtonContinue.setOnClickListener {
            startActivity(Intent(this,PizzaTypeActivity::class.java))
        }
    }
}