/**
 * @author Emre Deniz
 * @date Jan 17, 2024
 */

package com.example.group9_mapd711_project

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.group9_mapd711_project.databinding.ActivityNormalAddNewAddressBinding
import com.example.group9_mapd711_project.models.Customer
import com.example.group9_mapd711_project.models.CustomerAddress
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject

class NormalAddNewAddressActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNormalAddNewAddressBinding
    private lateinit var firestore: FirebaseFirestore
    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityNormalAddNewAddressBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        binding.goBackButton.setOnClickListener { finish() }

        binding.uploadDeliveryAddressButton.setOnClickListener {
            val address = binding.deliveryAddressEdit.text.trim().toString()
            val addressTag = binding.deliveryAddressTagEdit.text.trim().toString()
            val city = binding.deliveryCityEdit.text.trim().toString()
            val provinceCode = binding.deliveryProvinceCodeEdit.text.trim().toString()
            val postalCode = binding.deliveryPostalCodeEdit.text.trim().toString()
            val country = binding.deliveryCountryEdit.text.trim().toString()

            if (addressTag.isNotEmpty()&&city.isNotEmpty()&&provinceCode.isNotEmpty()&&provinceCode.length==2&&postalCode.isNotEmpty()&&postalCode.length==6&&country.isNotEmpty()&&address.isNotEmpty()){
                val customerDocRef = firestore.collection("customers").document(firebaseAuth.currentUser!!.uid)
                customerDocRef.get().addOnSuccessListener {
                    if (it.exists()){
                        val currentCustomer = it.toObject<Customer>()!!

                        val newAddress = CustomerAddress(deliverAddress = address,addressTag=addressTag, city = city, provinceCode = provinceCode, country = country, postalCode = postalCode)
                        currentCustomer.deliveryAddress.add(newAddress)

                        customerDocRef.set(currentCustomer).addOnSuccessListener {
                            val intent = Intent(this, CustomerViewMyAddressesListActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                            startActivity(intent)
                            finish()  // Optional: Close the current activity
                        }.addOnFailureListener {  }
                    }
                }.addOnFailureListener {  }
            }
        }
    }
}