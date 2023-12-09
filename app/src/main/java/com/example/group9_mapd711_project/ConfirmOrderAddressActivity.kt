package com.example.group9_mapd711_project

import android.graphics.Color
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.view.marginStart
import com.example.group9_mapd711_project.databinding.ActivityConfirmOrderAddressBinding
import com.example.group9_mapd711_project.models.Customer
import com.example.group9_mapd711_project.models.CustomerAddress
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject

class ConfirmOrderAddressActivity : AppCompatActivity() {
    private lateinit var binding: ActivityConfirmOrderAddressBinding
    private lateinit var currentCustomer: Customer
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var addressesRadioGroup: RadioGroup
    private var selectedDeliveryAddress: CustomerAddress? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityConfirmOrderAddressBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pref = applicationContext.getSharedPreferences("Build_Pizza_Order", 0) //initialize an instance of shared preference
        val editor = pref.edit()

        binding.goBackButton.setOnClickListener { finish() }

        binding.selectedPizzaName.text = pref.getString("selected_pizza_name","No Pizza Selected")
        binding.selectedPizzaCategory.text = pref.getString("selected_pizza_category","No Pizza Selected")
        binding.totalPizzaPrice.text = pref.getString("order_total_price","No Pizza Selected")

        binding.orderQuantityText.text = "${pref.getInt("order_units_count",0)} boxes"
        binding.orderToppingsCountText.text = "${pref.getString("order_toppings_list","No Order Submitted")!!.split(",").size} toppings"
        binding.orderPizzaSizeText.text = "${pref.getString("selected_pizza_size", "No Pizza Selected")} Pizza"

        binding.selectedPizzaRestaurantName.text = pref.getString("selected_restaurant_name","No Restaurant Selected")
        binding.selectedPizzaRestaurantAddress.text = pref.getString("selected_restaurant_address","No Restaurant Selected")
        binding.selectedPizzaRestaurantRatingBar.rating = pref.getString("selected_restaurant_rating","No Ratings")!!.toFloat()
        binding.selectedPizzaRestaurantRatingCount.text = "(${pref.getString("selected_restaurant_rating_count","No Ratings")})"

        firebaseAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()
        addressesRadioGroup = binding.addressesRadioGroup

        val customerSnap = firestore.collection("customers").document(firebaseAuth.currentUser!!.uid)
        customerSnap.get().addOnSuccessListener {
            if (it.exists()){
                currentCustomer = it.toObject<Customer>()!!
                val customerAddressesList = currentCustomer.deliveryAddress

                for ((index, address) in customerAddressesList.withIndex()){
                    val frameLayout = FrameLayout(this)
                    frameLayout.id = index
                    frameLayout.layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )
                    frameLayout.setPadding(10,10,10,10)

                    val cardView = CardView(this)
                    // Set a border radius for the CardView
                    val cornerRadiusInPixels = TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP, 8f, resources.displayMetrics
                    )
                    cardView.radius = cornerRadiusInPixels
                    cardView.clipChildren = true

                    // Set elevation for the CardView
                    val elevationInPixels = TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP, 4f, resources.displayMetrics
                    )
                    cardView.cardElevation = elevationInPixels
                    cardView.setBackgroundResource(R.drawable.radio_button_bg_selector)

                    val addressLinearLayout = LinearLayout(this)
                    addressLinearLayout.orientation = LinearLayout.VERTICAL
                    addressLinearLayout.layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )
                    addressLinearLayout.setPadding(
                        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 10f, resources.displayMetrics).toInt(),
                        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 10f, resources.displayMetrics).toInt(),
                        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 10f, resources.displayMetrics).toInt(),
                        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 10f, resources.displayMetrics).toInt()
                    )

                    // First TextView
                    val textViewAddressTag = TextView(this)
                    textViewAddressTag.text = address.addressTag
                    addressLinearLayout.addView(textViewAddressTag)

                    // Second TextView
                    val textViewAddressCity = TextView(this)
                    textViewAddressCity.text = "${address.deliverAddress}, ${address.city}"
                    addressLinearLayout.addView(textViewAddressCity)

                    // Third TextView
                    val textViewProvCountPostal = TextView(this)
                    textViewProvCountPostal.text = "${address.provinceCode}, ${address.country}, ${address.postalCode}"
                    addressLinearLayout.addView(textViewProvCountPostal)

                    cardView.addView(addressLinearLayout)
                    frameLayout.addView(cardView)

                    addressesRadioGroup.addView(frameLayout)

                    frameLayout.setOnClickListener {
                        // Clear background color for all layouts
                        for (i in 0 until addressesRadioGroup.childCount) {
                            val layout = addressesRadioGroup.getChildAt(i) as FrameLayout
                            val containerLayout = layout.getChildAt(0) as CardView
                            containerLayout.setBackgroundResource(R.drawable.radio_button_bg_selector)
                        }

                        // Set yellow background for the selected
                        cardView.setBackgroundColor(resources.getColor(R.color.pizza_color))
                        Log.d("selected Address", address.deliverAddress)
                    }
                }
            }
        }.addOnFailureListener {  }
    }
}