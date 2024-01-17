/**
 * @author Emre Deniz
 * @date Jan 17, 2024
 */

package com.example.group9_mapd711_project

import android.content.Intent
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
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.view.marginStart
import com.example.group9_mapd711_project.databinding.ActivityConfirmOrderAddressBinding
import com.example.group9_mapd711_project.models.Customer
import com.example.group9_mapd711_project.models.CustomerAddress
import com.example.group9_mapd711_project.models.DeliveryAddress
import com.example.group9_mapd711_project.models.Order
import com.example.group9_mapd711_project.models.OrderInfo
import com.example.group9_mapd711_project.models.PizzaTopping
import com.example.group9_mapd711_project.models.ProductInfo
import com.example.group9_mapd711_project.models.RestaurantAddress
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import java.util.Date

class ConfirmOrderAddressActivity : AppCompatActivity() {
    private lateinit var binding: ActivityConfirmOrderAddressBinding
    private lateinit var currentCustomer: Customer
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var addressesRadioGroup: RadioGroup
    private lateinit var orderToSubmit: Order

    private var selectedDeliveryAddress: CustomerAddress? = null
    private var tipPercent = 15.0
    private var tipAmount = 0.0
    private var totalOrderCharge = 0.0
    private var discountClaimed = false

    private val discountCode = "DIY2023"
    private var taxesAmount = 0.0
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
        binding.orderInitialPriceText.text = "$ ${pref.getString("order_total_price"," No Pizza Selected ")}"

        binding.orderAndUnitsText.text = "${pref.getString("selected_pizza_name","No Pizza Selected")} x ${pref.getString("order_units_count","0")} box(es)"

        binding.orderQuantityText.text = "${pref.getString("order_units_count","0")} boxes"
        binding.orderToppingsCountText.text = "${pref.getString("order_toppings_list","No Order Submitted")!!.split(",").size} toppings"
        binding.orderPizzaSizeText.text = "${pref.getString("selected_pizza_size", "No Pizza Selected")} Pizza"

        binding.selectedPizzaRestaurantName.text = pref.getString("selected_restaurant_name","No Restaurant Selected")
        binding.selectedPizzaRestaurantAddress.text = pref.getString("selected_restaurant_address","No Restaurant Selected")
        binding.selectedPizzaRestaurantRatingBar.rating = pref.getString("selected_restaurant_rating","No Ratings")!!.toFloat()
        binding.selectedPizzaRestaurantRatingCount.text = "(${pref.getString("selected_restaurant_rating_count","No Ratings")})"

        tipAmount = (15 * pref.getString("order_total_price","No Pizza Selected")!!.toDouble())/100
        taxesAmount = (16 * pref.getString("order_total_price","No Pizza Selected")!!.toDouble())/100
        totalOrderCharge = tipAmount + pref.getString("order_total_price","No Pizza Selected")!!.toDouble() + taxesAmount

        //Initial calculation
        updateUIWithCost(discounted = discountClaimed, taxAmount = taxesAmount, initialAmount = pref.getString("order_total_price","No Pizza Selected")!!.toDouble(), tipAmount = tipAmount)

        firebaseAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()
        addressesRadioGroup = binding.addressesRadioGroup

        binding.tipsRadioGrp.setOnCheckedChangeListener { _, checkedId ->
            tipPercent = when(checkedId){
                R.id.radio15-> 15.0
                R.id.radio20-> 20.0
                R.id.radio25-> 25.0
                R.id.radio30-> 30.0
                R.id.noTipRadio->0.0
                else -> 15.0
            }

            tipAmount = (tipPercent * pref.getString("order_total_price","No Pizza Selected")!!.toDouble())/100

            updateUIWithCost(discounted = discountClaimed, taxAmount = taxesAmount, initialAmount = pref.getString("order_total_price","No Pizza Selected")!!.toDouble(), tipAmount = tipAmount)
        }

        binding.redeemPromoButton.setOnClickListener {
            val promoCodeText = binding.promoCodeEditText.text.trim().toString()

            if (promoCodeText.isNotEmpty()&&promoCodeText==discountCode&&!discountClaimed){
                discountClaimed = true
                updateUIWithCost(discounted = discountClaimed, taxAmount = taxesAmount, initialAmount = pref.getString("order_total_price","No Pizza Selected")!!.toDouble(), tipAmount = tipAmount)
                binding.promoCodeEditText.setText("")
                binding.promoCodeEditText.clearFocus()

                Toast.makeText(this,"Order Discounted", Toast.LENGTH_SHORT).show()
            }
            else{
                if (promoCodeText.isNotEmpty()&&promoCodeText!=discountCode&&!discountClaimed){
                    Toast.makeText(this,"Invalid Promo Code",Toast.LENGTH_SHORT).show()
                    binding.promoCodeEditText.error = "Invalid Promo Code"
                }
                else if (promoCodeText.isEmpty()){
                    Toast.makeText(this,"No Promo Code provided", Toast.LENGTH_SHORT).show()
                    binding.promoCodeEditText.error = "No Promo Code"
                }
                else if (discountClaimed){
                    Toast.makeText(this,"Promo Code already claimed", Toast.LENGTH_SHORT).show()
                    binding.promoCodeEditText.error = "Promo Code applied"
                }
            }
        }

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

                        selectedDeliveryAddress = address
                        Log.d("selected Address", selectedDeliveryAddress!!.deliverAddress)
                    }
                }
            }
        }.addOnFailureListener {  }

        binding.placeCompleteOrderButton.setOnClickListener {
            if (selectedDeliveryAddress != null){
                orderToSubmit = Order(
                    productInfo = ProductInfo(
                        productID = pref.getString("selected_pizza_ID","No Pizza Selected")!!,
                        productToppings = pref.getString("order_toppings_list","No Order Submitted")!!.split(","),
                        productCategory = pref.getString("selected_pizza_category","No Pizza Selected")!!,
                        productName = pref.getString("selected_pizza_name","No Pizza Selected")!!,
                        productSize = pref.getString("selected_pizza_size","No Pizza Selected")!!,
                    ),
                    restaurantAddress = RestaurantAddress(
                        restaurantLatitude = pref.getString("selected_restaurant_latitude","No Pizza Selected")!!.toDouble(),
                        restaurantLongitude = pref.getString("selected_restaurant_longitude","No Pizza Selected")!!.toDouble(),
                        restaurantName = pref.getString("selected_restaurant_name","No Pizza Selected")!!,
                        restaurantAddress = pref.getString("selected_restaurant_address","No Pizza Selected")!!,
                        restaurantCityCountry = pref.getString("selected_city","No Pizza Selected")!!,
                        restaurantRating = pref.getString("selected_restaurant_rating","No Pizza Selected")!!.toDouble(),
                        restaurantRatingsCount = pref.getString("selected_restaurant_rating_count","No Pizza Selected")!!.toInt(),
                    ),
                    deliveryAddress = DeliveryAddress(
                        deliveryPostalCode = selectedDeliveryAddress!!.postalCode,
                        deliveryCity = selectedDeliveryAddress!!.city,
                        deliveryProvince = selectedDeliveryAddress!!.provinceCode,
                        deliveryCountry = selectedDeliveryAddress!!.country,
                        deliveryAddress = selectedDeliveryAddress!!.deliverAddress,
                        customerAddressTag = selectedDeliveryAddress!!.addressTag,
                        customerID = pref.getString("current_customer_uid","No Pizza Selected")!!,
                        customerEmailAddress = pref.getString("current_customer_email","No Pizza Selected")!!,
                        customerFullNames = pref.getString("current_customer_names","No Pizza Selected")!!,
                        customerPhoneNumber = pref.getString("current_customer_phone","No Pizza Selected")!!,
                    ),
                    orderInfo = OrderInfo(
                        unitsCount = pref.getString("order_units_count","0")!!.toInt(),
                        totalOrderPrice = totalOrderCharge,
                        orderSubmitDate = com.google.firebase.Timestamp(Date()),
                        orderDelivered = false,
                        orderDiscounted = discountClaimed,
                        orderDeliverDate = com.google.firebase.Timestamp(Date(2000,0,1,0,0,0)),
                        tipPercent = tipPercent,
                        tipAmount = tipAmount,
                    ),
                    customerReview = "",
                )

                firestore.collection("orders").add(orderToSubmit).addOnSuccessListener {
                    editor.putString("order_discounted",discountClaimed.toString())
                    editor.putString("order_final_charge",totalOrderCharge.toString())
                    editor.putString("order_tip_percent",tipPercent.toString())
                    editor.putString("order_tip_amount",tipAmount.toString())
                    editor.putString("order_taxes_amount",taxesAmount.toString())
                    editor.putString("order_delivery_address",selectedDeliveryAddress!!.deliverAddress)
                    editor.putString("order_delivery_country",selectedDeliveryAddress!!.country)
                    editor.putString("order_delivery_province",selectedDeliveryAddress!!.provinceCode)
                    editor.putString("order_delivery_city",selectedDeliveryAddress!!.city)
                    editor.putString("order_delivery_postal_code",selectedDeliveryAddress!!.postalCode)
                    editor.putString("order_reference_id",it.id)
                    editor.commit()

                    startActivity(Intent(this, ConfirmActivity::class.java))
                }.addOnFailureListener {  }
            }
            else{
                Toast.makeText(this,"Set a delivery address to proceed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateUIWithCost(discounted: Boolean, taxAmount: Double, initialAmount:Double, tipAmount:Double,) {
        totalOrderCharge = if (discounted) (initialAmount+tipAmount+taxAmount)-5.76 else initialAmount+tipAmount+taxAmount

        binding.totalToPayText.text = "$ ${String.format("%.2f",totalOrderCharge)}"
        binding.taxesAmountText.text = "16% VAT ($ ${String.format("%.2f",taxAmount)})"
        binding.promoCodeDiscountText.text = if (discounted) "- $ 5.76" else "No Discount"
        binding.serverTipAmountText.text = "$tipPercent% ($ ${String.format("%.2f",tipAmount)})"
        binding.placeCompleteOrderButton.text = "PAY $ ${String.format("%.2f",totalOrderCharge)}"
    }
}