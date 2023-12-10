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
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import com.example.group9_mapd711_project.databinding.ActivityMainBinding
import com.example.group9_mapd711_project.models.Customer
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()
    }

    override fun onStart() {
        super.onStart()

        // Check if the user is already signed in, and if so, navigate to the home activity
        if (firebaseAuth.currentUser != null) {
            val customerDocRef = firestore.collection("customers").document(firebaseAuth.currentUser!!.uid).get()
            customerDocRef.addOnSuccessListener {
                if (it.exists()){
                    val currentCustomer = it.toObject<Customer>()!!
                    if (currentCustomer.deliveryAddress.isNotEmpty()){
                        val pref = applicationContext.getSharedPreferences("Build_Pizza_Order", 0) //initialize an instance of shared preference
                        val editor = pref.edit()

                        editor.putString("current_customer_names","${currentCustomer.firstName} ${currentCustomer.lastName}")
                        editor.putString("current_customer_email", currentCustomer.emailAddress)
                        editor.putString("current_customer_phone", currentCustomer.phoneNumber)
                        editor.putString("current_customer_uid", firebaseAuth.currentUser!!.uid)
                        editor.commit()

                        startActivity(Intent(this,CitySelectorActivity::class.java))
                    }
                    else{
                        startActivity(Intent(this,AddDeliveryAddressActivity::class.java))
                    }
                }
            }.addOnFailureListener {  }
        }
        else{
            startActivity(Intent(this,GetStartedActivity::class.java))
        }
    }
}