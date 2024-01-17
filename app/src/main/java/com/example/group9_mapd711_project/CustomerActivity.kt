/**
 * @author Emre Deniz
 * @date Jan 17, 2024
 */

package com.example.group9_mapd711_project

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class CustomerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer)

        // Initialize UI elements
        var PayButton: Button = findViewById(R.id.buttonPay)

        // Login button actions
        PayButton.setOnClickListener {
            startActivity(Intent(this,ConfirmActivity::class.java))
        }
    }
}