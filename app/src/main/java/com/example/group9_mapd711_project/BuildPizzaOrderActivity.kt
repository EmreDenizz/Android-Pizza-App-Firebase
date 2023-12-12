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
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.gridlayout.widget.GridLayout
import com.example.group9_mapd711_project.databinding.ActivityBuildPizzaOrderBinding
import com.example.group9_mapd711_project.models.Pizza
import com.example.group9_mapd711_project.models.PizzaTopping
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject

class BuildPizzaOrderActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBuildPizzaOrderBinding
    private lateinit var firebaseFirestore: FirebaseFirestore
    private  var selectedToppingsList: MutableList<PizzaTopping> = mutableListOf()
    private  var availablePizzaToppingsList: MutableList<PizzaTopping> = mutableListOf()
    private lateinit var toppingsListGridView: GridLayout
    private lateinit var selectedPizzaProduct:Pizza

    private var pizzaSize = "Small"
    private var itemCount = 1
    private var pizzatotalCost = 0.00

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityBuildPizzaOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pref = applicationContext.getSharedPreferences("Build_Pizza_Order", 0) //initialize an instance of shared preference
        val editor = pref.edit()

        binding.selectedPizzaName.text = pref.getString("selected_pizza_name","No Pizza Selected")
        binding.selectedPizzaCategory.text = pref.getString("selected_pizza_category","No Pizza Selected")

        firebaseFirestore = FirebaseFirestore.getInstance()
        toppingsListGridView = binding.toppingsListGrided

        val pizzaTypeRef = firebaseFirestore.collection("products").document(pref.getString("selected_pizza_ID","No Pizza Selected")?:"No Pizza Selected")
        pizzaTypeRef.get().addOnSuccessListener {
            if (it.exists()){
                selectedPizzaProduct = it.toObject<Pizza>()!!
                selectedPizzaProduct.pizzaID = it.id

                binding.currentTotalBillText.text = selectedPizzaProduct.smallPrice.toString()
            }
        }.addOnFailureListener {  }

        binding.backFAB.setOnClickListener {
            finish()
        }

        // Increase button
        binding.addUnitButton.setOnClickListener {
            itemCount++
            val totalCost = calculateTotalCost(pizzaSize, selectedToppingsList)
            updateUIWithCost(totalCost)
        }

        // Decrease button
        binding.removeUnitButton.setOnClickListener {
            if (itemCount > 1) {
                itemCount--
                val totalCost = calculateTotalCost(pizzaSize, selectedToppingsList)
                updateUIWithCost(totalCost)
            }
        }

        binding.unitsCountText.text = itemCount.toString()

        binding.pizzaSizesRadioGrp.setOnCheckedChangeListener { _, checkedId ->
            pizzaSize = when (checkedId) {
                R.id.smallRadio -> "Small"
                R.id.mediumRadio -> "Medium"
                R.id.largeRadio -> "Large"
                R.id.deluxeRadio -> "Deluxe"
                else -> "Small" // Handle default case
            }

            val totalCost = calculateTotalCost(pizzaSize, selectedToppingsList)
            updateUIWithCost(totalCost)
        }

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
                toppingsListGridView.rowCount = (availablePizzaToppingsList.size + 1) / 2
                
                availablePizzaToppingsList.forEachIndexed { index, pizzaTopping ->
                    val linearLayout = LinearLayout(this)
                    linearLayout.orientation = LinearLayout.VERTICAL

                    // Create TextView for topping name
                    val textViewName = TextView(this)
                    textViewName.text = pizzaTopping.toppingName

                    // Create TextView for topping price
                    val textViewPrice = TextView(this)
                    textViewPrice.text = "$${pizzaTopping.toppingPrice}"

                    // Add TextViews to LinearLayout
                    linearLayout.addView(textViewName)
                    linearLayout.addView(textViewPrice)

                    // Create CheckBox
                    val checkBox = CheckBox(this)
                    checkBox.text = null
                    //checkBox.buttonDrawable = ContextCompat.getDrawable(this, R.drawable.checkbox_selector)
                    checkBox.setOnCheckedChangeListener { _, isChecked ->
                        if (isChecked) {
                            selectedToppingsList.add(pizzaTopping)
                        } else {
                            selectedToppingsList.remove(pizzaTopping)
                        }
                        val totalCost = calculateTotalCost(pizzaSize, selectedToppingsList)
                        updateUIWithCost(totalCost)
                    }

                    // Set GridLayout parameters for two columns
                    val params = GridLayout.LayoutParams()
                    params.columnSpec = GridLayout.spec(index % 3)
                    params.rowSpec = GridLayout.spec(index / 3)


                    // Add the LinearLayout with TextViews and CheckBox to your GridLayout
                    val horlinearLayout = LinearLayout(this)
                    horlinearLayout.orientation = LinearLayout.HORIZONTAL
                    horlinearLayout.layoutParams = params
                    horlinearLayout.setPadding(0,15,20,20)

                    horlinearLayout.addView(checkBox)
                    horlinearLayout.addView(linearLayout)

                    toppingsListGridView.addView(horlinearLayout)
                    //toppingsListGridView.addView(linearLayout)

                }
            }
        }.addOnFailureListener {  }

        binding.placeOrderButton.setOnClickListener {
            if (selectedToppingsList.isNotEmpty()){
                editor.putString("selected_pizza_size", pizzaSize)
                editor.putString("order_total_price", pizzatotalCost.toString())
                editor.putString("order_units_count", itemCount.toString())
                editor.putString(
                    "order_toppings_list",
                    selectedToppingsList.joinToString(",") { it.pizzaToppingID })
                editor.commit()

                startActivity(Intent(this, ConfirmOrderAddressActivity::class.java))
            }
            else{
                Toast.makeText(this,"No Toppings Selected",Toast.LENGTH_SHORT).show()
            }
        }

    }
    // Calculate the total cost based on size and selected toppings
    private fun calculateTotalCost(size: String, selectedToppings: List<PizzaTopping>): Double {

        val basePrice = when (size) {
            "Small" -> selectedPizzaProduct.smallPrice
            "Medium" -> selectedPizzaProduct.mediumPrice
            "Large" -> selectedPizzaProduct.largePrice
            "Deluxe" -> selectedPizzaProduct.deluxePrice
            else -> selectedPizzaProduct.smallPrice // Handle default case
        }

        val toppingsPrice = selectedToppings.sumByDouble { it.toppingPrice }
        return (basePrice + toppingsPrice)*itemCount
    }

    // Update the UI with the total cost
    private fun updateUIWithCost(totalCost: Double) {
        pizzatotalCost = "%.2f".format(totalCost).toDouble()
        binding.currentTotalBillText.text = String.format("%.2f",pizzatotalCost)
        binding.unitsCountText.text = itemCount.toString()
    }
}