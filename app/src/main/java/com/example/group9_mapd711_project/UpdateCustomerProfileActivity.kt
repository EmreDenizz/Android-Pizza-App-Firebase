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
import com.example.group9_mapd711_project.databinding.ActivityUpdateCustomerProfileBinding
import com.example.group9_mapd711_project.models.Customer
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import kotlin.random.Random

class UpdateCustomerProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUpdateCustomerProfileBinding
    private lateinit var firestore: FirebaseFirestore
    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityUpdateCustomerProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pref = applicationContext.getSharedPreferences("Build_Pizza_Order", 0) //initialize an instance of shared preference
        val editor = pref.edit()

        firebaseAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        val currentCustomerProfileRef = firestore.collection("customers").document(firebaseAuth.currentUser!!.uid)
        currentCustomerProfileRef.get().addOnSuccessListener {
            if (it.exists()){
                val currentCustomer = it.toObject<Customer>()!!
                currentCustomer.customerID = it.id

                binding.customerFirstNameUpdEdit.setText(currentCustomer.firstName)
                binding.customerLastNameUpdEdit.setText(currentCustomer.lastName)
                binding.customerUsernameUpdEdit.setText(currentCustomer.userName)
                binding.customerPhoneUpdEdit.setText(currentCustomer.phoneNumber)
            }
        }.addOnFailureListener {  }

        binding.goBackButton.setOnClickListener { finish() }

        binding.updateProfileButton.setOnClickListener {
            val first_name = binding.customerFirstNameUpdEdit.text.trim().toString()
            val last_name = binding.customerLastNameUpdEdit.text.trim().toString()
            val user_name = binding.customerUsernameUpdEdit.text.trim().toString()
            val phone_number  = binding.customerPhoneUpdEdit.text.trim().toString()

            if (first_name.isNotEmpty()&&last_name.isNotEmpty()&&phone_number.isNotEmpty()){
                val new_user_name = if (user_name.isNotEmpty()) user_name else "${first_name.toLowerCase()}_${last_name.toLowerCase()}${Random.nextInt(1,1000)}"

                currentCustomerProfileRef.update(
                    mapOf(
                        "firstName" to first_name,
                        "lastName" to last_name,
                        "userName" to new_user_name,
                        "phoneNumber" to phone_number
                    )
                ).addOnSuccessListener {
                    editor.putString("current_customer_names", "$first_name $last_name")
                    editor.putString("current_customer_phone", phone_number)
                    editor.commit()

                    val intent = Intent(this, CustomerProfileViewActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                    finish()  // Optional: Close the current activity
                }.addOnFailureListener {  }
            }
            else{
                if (first_name.isEmpty()){
                    binding.customerFirstNameUpdEdit.error = "Provide First Name"
                }
                if (last_name.isEmpty()){
                    binding.customerFirstNameUpdEdit.error = "Provide Last Name"
                }
                if (phone_number.isEmpty()){
                    binding.customerFirstNameUpdEdit.error = "Provide Phone Number"
                }
            }
        }
    }
}