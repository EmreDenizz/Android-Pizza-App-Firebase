/**
 * @author Emre Deniz
 * @date Jan 17, 2024
 */

package com.example.group9_mapd711_project

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.example.group9_mapd711_project.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        binding.goBackButton.setOnClickListener { finish() }

        binding.loginButton.setOnClickListener {
            val customerEmail = binding.customerEmailLogEdit.text.trim().toString()
            val customerPassword = binding.customerPassLogEdit.text.trim().toString()

            if (customerPassword.isNotEmpty()&&customerEmail.isNotEmpty()&&!customerEmail.contains("@admin.com",ignoreCase = true)){
                firebaseAuth.signInWithEmailAndPassword(customerEmail,customerPassword).addOnCompleteListener {
                    if (it.isSuccessful){
                        startActivity(Intent(this, MainActivity::class.java))
                    }
                    else{
                        Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
                    }
                }
            }
            else{
                if (customerEmail.isEmpty()){
                    binding.customerEmailLogEdit.error = "Enter your Email Address"
                }
                if (customerEmail.isNotEmpty()&&customerEmail.contains("@admin.com",ignoreCase = true)){
                    binding.customerEmailLogEdit.error = "Do not use your Admin Address"
                }
                if (customerPassword.isEmpty()){
                    binding.customerPassLogEdit.error = "Enter your Password"
                }
            }
        }

        binding.customerRegisterRedirectButton.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }
}