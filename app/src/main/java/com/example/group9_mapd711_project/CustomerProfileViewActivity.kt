package com.example.group9_mapd711_project

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.group9_mapd711_project.databinding.ActivityCustomerProfileViewBinding
import com.example.group9_mapd711_project.models.Customer
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject

class CustomerProfileViewActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCustomerProfileViewBinding
    private lateinit var firestore: FirebaseFirestore
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var currentCustomer: Customer
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCustomerProfileViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        val customerSnap = firestore.collection("customers").document(firebaseAuth.currentUser!!.uid)
        customerSnap.get().addOnSuccessListener {
            if (it.exists()){
                currentCustomer = it.toObject<Customer>()!!

                binding.customerFullNames.text = "${currentCustomer.firstName} ${currentCustomer.lastName}"
                binding.customerEmailAddress.text = currentCustomer.emailAddress
                binding.customerPhoneNumber.text = currentCustomer.phoneNumber
            }
        }.addOnFailureListener {  }

        binding.goBackButton.setOnClickListener { finish() }

        binding.editProfileRedirectButton.setOnClickListener {
            startActivity(Intent(this, CustomerEditProfileActivity::class.java))
        }

        binding.customerLogoutButton.setOnClickListener {
            firebaseAuth.signOut()
            startActivity(Intent(this, MainActivity::class.java))
        }
    }
}