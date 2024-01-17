/**
 * @author Emre Deniz
 * @date Jan 17, 2024
 */

package com.example.group9_mapd711_project

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.group9_mapd711_project.databinding.ActivityAddDeliveryAddressBinding
import com.example.group9_mapd711_project.models.Customer
import com.example.group9_mapd711_project.models.CustomerAddress
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject

class AddDeliveryAddressActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddDeliveryAddressBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var currentCustomer: Customer
    private lateinit var newAddress: CustomerAddress
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddDeliveryAddressBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        binding.uploadDeliveryAddressButton.setOnClickListener {
            val addressTag = binding.deliveryAddressTagEdit.text.trim().toString()
            val city = binding.deliveryCityEdit.text.trim().toString()
            val provinceCode = binding.deliveryProvinceCodeEdit.text.trim().toString()
            val postalCode = binding.deliveryPostalCodeEdit.text.trim().toString()
            val country = binding.deliveryCountryEdit.text.trim().toString()
            val address = binding.deliveryAddressEdit.text.trim().toString()

            if (addressTag.isNotEmpty()&&city.isNotEmpty()&&provinceCode.isNotEmpty()&&provinceCode.length==2&&postalCode.isNotEmpty()&&postalCode.length==6&&country.isNotEmpty()&&address.isNotEmpty()){
                val customerDocRef = firestore.collection("customers").document(firebaseAuth.currentUser!!.uid)
                customerDocRef.get().addOnSuccessListener {
                    if (it.exists()){
                        currentCustomer = it.toObject<Customer>()!!

                        newAddress = CustomerAddress(deliverAddress = address,addressTag=addressTag, city = city, provinceCode = provinceCode, country = country, postalCode = postalCode)
                        currentCustomer.deliveryAddress.add(newAddress)

                       customerDocRef.set(currentCustomer).addOnSuccessListener {
                           startActivity(Intent(this, MainActivity::class.java))
                       }.addOnFailureListener {  }
                    }
                }.addOnFailureListener {  }
            }
        }
    }
}