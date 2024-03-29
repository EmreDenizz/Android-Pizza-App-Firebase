/**
 * @author Emre Deniz
 * @date Jan 17, 2024
 */

package com.example.group9_mapd711_project

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.group9_mapd711_project.databinding.ActivityGetStartedBinding

class GetStartedActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGetStartedBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityGetStartedBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Login button actions
        binding.loginButton.setOnClickListener {
            startActivity(Intent(this,LoginActivity::class.java))
        }

        // Register button actions
        binding.registerButton.setOnClickListener {
            startActivity(Intent(this,RegisterActivity::class.java))
        }
    }
}