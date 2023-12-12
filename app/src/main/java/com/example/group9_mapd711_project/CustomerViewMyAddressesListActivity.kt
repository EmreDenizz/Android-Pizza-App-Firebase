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
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.group9_mapd711_project.adapters.DeliveryAddressesAdapter
import com.example.group9_mapd711_project.databinding.ActivityCustomerViewMyAddressesListBinding
import com.example.group9_mapd711_project.models.Customer
import com.example.group9_mapd711_project.models.CustomerAddress
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject

class CustomerViewMyAddressesListActivity : AppCompatActivity() {
    private lateinit var binding:ActivityCustomerViewMyAddressesListBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var currentCustomer: Customer
    private lateinit var customerAddresses:MutableList<CustomerAddress>
    private lateinit var deliveryAddressesAdapter: DeliveryAddressesAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCustomerViewMyAddressesListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.goBackButton.setOnClickListener {
            val intent = Intent(this, CustomerProfileViewActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()  // Optional: Close the current activity
        }

        binding.addNewAddressRedirectButton.setOnClickListener {
            startActivity(Intent(this, NormalAddNewAddressActivity::class.java))
        }

        firestore = FirebaseFirestore.getInstance()
        firebaseAuth = FirebaseAuth.getInstance()
        customerAddresses = mutableListOf()

        deliveryAddressesAdapter = DeliveryAddressesAdapter(emptyList()){ customerAddress ->
            currentCustomer.deliveryAddress.remove(customerAddress)

            firestore.collection("customers").document(firebaseAuth.currentUser!!.uid).set(currentCustomer)
                .addOnSuccessListener {
                    Toast.makeText(this,"Delivery Address Deleted",Toast.LENGTH_SHORT).show()

                    firestore.collection("customers").document(firebaseAuth.currentUser!!.uid).get().addOnSuccessListener {
                        val updatedCustomer = it.toObject<Customer>()!!
                        customerAddresses = updatedCustomer.deliveryAddress

                        deliveryAddressesAdapter.updateData(customerAddresses)
                    }
                }
                .addOnFailureListener {  }
        }

        binding.savedAddressesRecycler.layoutManager = LinearLayoutManager(this)
        binding.savedAddressesRecycler.adapter = deliveryAddressesAdapter

        val allCustomerAddressesRef = firestore.collection("customers").document(firebaseAuth.currentUser!!.uid).get()
        allCustomerAddressesRef
            .addOnSuccessListener {
                if (it.exists()){
                    currentCustomer = it.toObject<Customer>()!!
                    customerAddresses = currentCustomer.deliveryAddress

                    deliveryAddressesAdapter.updateData(customerAddresses)
                }
            }
            .addOnFailureListener {  }
    }

    override fun onBackPressed() {
        super.onBackPressed()

        // Create intent for the list of all orders activity
        val intent = Intent(this, CustomerProfileViewActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        startActivity(intent)
        finish()
    }
}