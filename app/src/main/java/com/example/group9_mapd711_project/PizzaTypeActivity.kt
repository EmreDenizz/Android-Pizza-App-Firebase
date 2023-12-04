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
import android.util.Log
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.group9_mapd711_project.adapters.PizzaOptionsListAdapter
import com.example.group9_mapd711_project.databinding.ActivityPizzaTypeBinding
import com.example.group9_mapd711_project.models.Pizza
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject

class PizzaTypeActivity : AppCompatActivity() {

    private lateinit var firebaseFirestore: FirebaseFirestore
    private lateinit var binding: ActivityPizzaTypeBinding
    private lateinit var pizzaOptionsListAdapter: PizzaOptionsListAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPizzaTypeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent = intent
        binding.selectedPizzaRestaurantName.text = intent.getStringExtra("selected_rest_name")
        binding.selectedPizzaRestaurantAddress.text = intent.getStringExtra("selected_rest_address")

        binding.backFAB.setOnClickListener {
            finish()
        }

        firebaseFirestore = FirebaseFirestore.getInstance()

        // Initialize the adapter with an empty list
        pizzaOptionsListAdapter = PizzaOptionsListAdapter(emptyList()) { pizzaId ->
            // Handle item click, e.g., start a new activity with the selected pizza ID
            val intent = Intent(this, BuildPizzaOrderActivity::class.java)
            intent.putExtra("selected_pizza_id", pizzaId)
            startActivity(intent)
        }
        binding.availablePizzaOptionsRecycle.layoutManager = LinearLayoutManager(this)
        binding.availablePizzaOptionsRecycle.adapter = pizzaOptionsListAdapter

        val pizzasQuerySnaphot = firebaseFirestore.collection("products").get()
        pizzasQuerySnaphot.addOnSuccessListener {
            if (it.isEmpty){

            }
            else{
                val uploadedPizzas = mutableListOf<Pizza>()
                for (pizzaDoc in it){
                    val pizzaObject = pizzaDoc.toObject<Pizza>()
                    pizzaObject.pizzaID = pizzaDoc.id
                    uploadedPizzas.add(pizzaObject)
                }
                pizzaOptionsListAdapter.updateData(uploadedPizzas)
            }
        }.addOnFailureListener {
            Log.d("Fetching Pizzas", "Fetching a list of pizzas failed with exception, $it")
        }
    }
}