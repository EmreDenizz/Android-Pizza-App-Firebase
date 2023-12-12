/**
 * @Group 9
 * @author Muindo Gituku (301372521)
 * @author Emre Deniz (301371047)
 * @author Nkemjika Obi (301275091)
 * @date Dec 11, 2023
 * @description: Android Project
 */

package com.example.group9_mapd711_project

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.group9_mapd711_project.databinding.ActivityRegisterBinding
import com.example.group9_mapd711_project.models.Customer
import com.example.group9_mapd711_project.models.CustomerAddress
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.random.Random

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var firestore: FirebaseFirestore
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var newCustomer:Customer
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        binding.goBackButton.setOnClickListener { finish() }

        binding.registerButton.setOnClickListener {
            val emailAddress = binding.customerEmailRegEdit.text.trim().toString()
            val phoneNumber = binding.customerPhoneRegEdit.text.trim().toString()
            val firstName = binding.customerFirstNameRegEdit.text.trim().toString()
            val lastName = binding.customerLastNameRegEdit.text.trim().toString()
            val password = binding.customerPassRegEdit.text.trim().toString()
            val confirmPass = binding.customerConfirmPassRegEdit.text.trim().toString()

            if (phoneNumber.isNotEmpty()&&emailAddress.isNotEmpty()&&firstName.isNotEmpty()&&lastName.isNotEmpty()&&password.isNotEmpty()&&confirmPass.isNotEmpty()&&!emailAddress.contains("@admin.com",ignoreCase = true)){
                val username = "${firstName.toLowerCase()}_${lastName.toLowerCase()}${Random.nextInt(1,1000).toString()}"
                if (password == confirmPass){
                    firebaseAuth.createUserWithEmailAndPassword(emailAddress,password).addOnCompleteListener {
                        newCustomer = Customer(firstName = firstName,lastName=lastName, userName = username, emailAddress = emailAddress, phoneNumber = phoneNumber, deliveryAddress = mutableListOf())
                        firestore.collection("customers").document(it.result.user!!.uid).set(newCustomer).addOnCompleteListener {
                            startActivity(Intent(this, MainActivity::class.java))
                        }
                    }
                }
                else{
                    binding.customerPassRegEdit.error = "Password does not match"
                    binding.customerConfirmPassRegEdit.error = "Password does not match"
                }
            }
        }

        binding.customerLoginRedirectButton.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }
}