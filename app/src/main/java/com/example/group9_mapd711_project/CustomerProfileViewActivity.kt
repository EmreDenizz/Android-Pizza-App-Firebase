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

        binding.goBackButton.setOnClickListener {
            val intent = Intent(this, CitySelectorActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()  // Optional: Close the current activity
        }

        binding.viewMyOrdersCard.setOnClickListener {
            startActivity(Intent(this, CustomerViewAllOrdersActivity::class.java))
        }

        binding.viewMyAddressesCard.setOnClickListener {
            startActivity(Intent(this, CustomerViewMyAddressesListActivity::class.java))
        }

        binding.updateMyProfileCard.setOnClickListener {
            startActivity(Intent(this, UpdateCustomerProfileActivity::class.java))
        }

        binding.customerLogoutButton.setOnClickListener {
            firebaseAuth.signOut()
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()

        // Create intent for the list of all orders activity
        val intent = Intent(this, CitySelectorActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        startActivity(intent)
        finish()
    }
}