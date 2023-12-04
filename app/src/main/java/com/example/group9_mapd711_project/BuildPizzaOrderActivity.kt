package com.example.group9_mapd711_project

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.GridView
import com.example.group9_mapd711_project.adapters.ToppingsOptionsAdapter
import com.example.group9_mapd711_project.databinding.ActivityBuildPizzaOrderBinding
import com.example.group9_mapd711_project.models.PizzaTopping
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject

class BuildPizzaOrderActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBuildPizzaOrderBinding
    private lateinit var firebaseFirestore: FirebaseFirestore
    private lateinit var selectedToppingsList: ArrayList<PizzaTopping>
    private lateinit var availablePizzaToppingsList: ArrayList<PizzaTopping>
    private lateinit var toppingsListGridView: GridView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityBuildPizzaOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseFirestore = FirebaseFirestore.getInstance()
        toppingsListGridView = binding.toppingsListGridView

        binding.backFAB.setOnClickListener {
            finish()
        }

        selectedToppingsList = ArrayList()
        availablePizzaToppingsList = ArrayList()

        val toppingsReference = firebaseFirestore.collection("toppings").get()
        toppingsReference.addOnSuccessListener {
            if (it.isEmpty){

            }
            else{
               for (toppingDoc in it){
                   val toppingObject = toppingDoc.toObject<PizzaTopping>()
                   toppingObject.pizzaToppingID = toppingDoc.id

                   availablePizzaToppingsList.add(toppingObject)
               }

                val toppingAdapter = ToppingsOptionsAdapter(availablePizzaToppingsList,selectedToppingsList,this)
                toppingsListGridView.adapter = toppingAdapter
            }
        }.addOnFailureListener {  }
    }
}