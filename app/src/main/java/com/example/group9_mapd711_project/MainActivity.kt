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
import com.example.group9_mapd711_project.models.Pizza
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

//        val pizzaList = listOf(
//            Pizza("Margherita", "Classic", 9.0, 12.0, 15.0, 18.0),
//            Pizza("Pepperoni", "Meat Lovers", 10.0, 13.0, 16.0, 19.0),
//            Pizza("Veggie", "Vegetarian", 11.0, 14.0, 17.0, 20.0),
//            Pizza("Supreme", "Specialty", 12.0, 15.0, 18.0, 21.0),
//            Pizza("Four Cheese", "Gourmet", 13.0, 16.0, 19.0, 22.0),
//            Pizza("BBQ Chicken", "Specialty", 14.0, 17.0, 20.0, 23.0),
//            Pizza("Margarita", "Gourmet", 15.0, 18.0, 21.0, 24.0),
//            Pizza("Buffalo Ranch", "Specialty", 16.0, 19.0, 22.0, 25.0),
//            Pizza("Hawaiian", "Specialty", 14.0, 17.0, 20.0, 23.0),
//            Pizza("Spinach Artichoke", "Vegetarian", 12.0, 15.0, 18.0, 21.0),
//            Pizza("Sausage Fiesta", "Meat Lovers", 11.0, 14.0, 17.0, 20.0),
//            Pizza("Truffle Elegance", "Gourmet", 16.0, 19.0, 22.0, 25.0),
//            Pizza("Pesto Perfection", "Vegetarian", 13.0, 16.0, 19.0, 22.0),
//            Pizza("Bacon & Egg", "Specialty", 15.0, 18.0, 21.0, 24.0),
//            Pizza("Prosciutto Passion", "Gourmet", 17.0, 20.0, 23.0, 26.0),
//            Pizza("Mushroom Madness", "Vegetarian", 12.0, 15.0, 18.0, 21.0),
//            Pizza("Carnivore's Choice", "Meat Lovers", 18.0, 21.0, 24.0, 27.0),
//            Pizza("Truffle Bliss", "Gourmet", 20.0, 23.0, 26.0, 29.0),
//            Pizza("Garlic Olive", "Vegetarian", 14.0, 17.0, 20.0, 23.0),
//            Pizza("Sriracha Chicken", "Specialty", 16.0, 19.0, 22.0, 25.0),
//            Pizza("Classic Cheese", "Classic", 8.0, 11.0, 14.0, 17.0),
//            Pizza("Meatball Marvel", "Meat Lovers", 12.0, 15.0, 18.0, 21.0),
//            Pizza("Vegan Delight", "Vegetarian", 11.0, 14.0, 17.0, 20.0),
//            Pizza("Spicy Mexicana", "Specialty", 13.0, 16.0, 19.0, 22.0),
//            Pizza("Blue Cheese Bonanza", "Gourmet", 15.0, 18.0, 21.0, 24.0),
//            Pizza("Tandoori Temptation", "Specialty", 14.0, 17.0, 20.0, 23.0),
//            Pizza("Artisanal Arugula", "Gourmet", 17.0, 20.0, 23.0, 26.0),
//            Pizza("Pineapple Pleasure", "Specialty", 13.0, 16.0, 19.0, 22.0),
//            Pizza("Sweet Potato Surprise", "Vegetarian", 14.0, 17.0, 20.0, 23.0),
//            Pizza("Classic Italian", "Classic", 10.0, 13.0, 16.0, 19.0),
//            Pizza("Mediterranean Masterpiece", "Vegetarian", 13.0, 16.0, 19.0, 22.0),
//            Pizza("Turkey and Cranberry", "Specialty", 15.0, 18.0, 21.0, 24.0)
//        )
//
//        for (pizza in pizzaList){
//            firestore.collection("products").add(pizza)
//        }

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