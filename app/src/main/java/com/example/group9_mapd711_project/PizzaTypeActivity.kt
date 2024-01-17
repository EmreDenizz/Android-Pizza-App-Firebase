/**
 * @author Emre Deniz
 * @date Jan 17, 2024
 */

package com.example.group9_mapd711_project

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.GridView
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
    private lateinit var pizzaTypesGridView: GridView
    private lateinit var availablePizzaImages:List<Int>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPizzaTypeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pref = applicationContext.getSharedPreferences("Build_Pizza_Order", 0) //initialize an instance of shared preference
        val editor = pref.edit()

        pizzaTypesGridView = binding.availableOptionsGrid

        availablePizzaImages = arrayListOf(R.drawable.pizza,R.drawable.pizza_1,R.drawable.pizza_2,R.drawable.pizza_3)

        binding.selectedPizzaRestaurantName.text = pref.getString("selected_restaurant_name", "No Restaurant Selected")
        binding.selectedPizzaRestaurantAddress.text = pref.getString("selected_restaurant_address","No Restaurant Selected")

        binding.backFAB.setOnClickListener {
            finish()
        }

        firebaseFirestore = FirebaseFirestore.getInstance()

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

                pizzaTypesGridView.adapter = PizzaOptionsListAdapter(uploadedPizzas,availablePizzaImages,this)

                pizzaTypesGridView.setOnItemClickListener { _, _, position, id ->
                    val selectedPizzaType = uploadedPizzas[position]

                    editor.putString("selected_pizza_name",selectedPizzaType.pizzaName)
                    editor.putString("selected_pizza_ID",selectedPizzaType.pizzaID)
                    editor.putString("selected_pizza_category",selectedPizzaType.pizzaCategory)
                    editor.commit()

                    startActivity(Intent(this,BuildPizzaOrderActivity::class.java))
                }
            }
        }.addOnFailureListener {
            Log.d("Fetching Pizzas", "Fetching a list of pizzas failed with exception, $it")
        }
    }
}