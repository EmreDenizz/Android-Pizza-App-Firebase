/**
 * @author Emre Deniz
 * @date Jan 17, 2024
 */

package com.example.group9_mapd711_project

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.group9_mapd711_project.databinding.ActivityCustomerViewSingleOrderBinding
import com.example.group9_mapd711_project.models.Order
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import java.text.SimpleDateFormat

class CustomerViewSingleOrderActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCustomerViewSingleOrderBinding
    private lateinit var firestore: FirebaseFirestore
    private lateinit var orderInView: Order
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCustomerViewSingleOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pref = applicationContext.getSharedPreferences("Build_Pizza_Order", 0) //initialize an instance of shared preference
        val orderInViewRef = pref.getString("order_reference_id","No Order Selected")!!

        firestore = FirebaseFirestore.getInstance()
        val orderDocRef = firestore.collection("orders").document(orderInViewRef)
        orderDocRef.get().addOnSuccessListener {
            if (it.exists()){
                orderInView = it.toObject<Order>()!!

                binding.orderPizzaName.text = orderInView.productInfo.productName
                binding.orderPizzaCategory.text = orderInView.productInfo.productCategory
                binding.orderQuantityText.text = "${orderInView.orderInfo.unitsCount} box(es)"
                binding.orderToppingsCountText.text = "${orderInView.productInfo.productToppings.size} toppings"
                binding.orderPizzaSizeText.text = "${orderInView.productInfo.productSize} Pizza"
                binding.orderTotalPrice.text = "$ ${String.format("%.2f",orderInView.orderInfo.totalOrderPrice)}"

                binding.orderRestaurantName.text = orderInView.restaurantAddress.restaurantName
                binding.orderRestaurantAddress.text = orderInView.restaurantAddress.restaurantAddress
                binding.orderRestaurantLocation.text = orderInView.restaurantAddress.restaurantCityCountry
                binding.orderRestaurantRatingBar.rating = orderInView.restaurantAddress.restaurantRating.toFloat()
                binding.orderRestaurantRatingCount.text = "(${orderInView.restaurantAddress.restaurantRatingsCount})"

                binding.orderReviewedText.text = if (orderInView.customerReview.isEmpty()) "Please give feedback on the service" else "Thanks for your feedback!!"
                binding.orderReceivedText.text = if (orderInView.orderInfo.orderDelivered) "Delivered on ${SimpleDateFormat("YYYY/MM/DD hh:mm").format(orderInView.orderInfo.orderDeliverDate.seconds * 1000L)}" else "Delivery is underway to your location"
                binding.orderPlacedTimeText.text = "Submitted on ${SimpleDateFormat("YYYY/MM/DD hh:mm").format(orderInView.orderInfo.orderSubmitDate.seconds * 1000L)}"
                binding.deliveryImage.setImageResource(if (orderInView.orderInfo.orderDelivered) R.drawable.success else R.drawable.delivery)

                binding.orderDeliveryAddressTag.text = orderInView.deliveryAddress.customerAddressTag
                binding.orderDeliveryAddress.text = "${orderInView.deliveryAddress.deliveryAddress}, ${orderInView.deliveryAddress.deliveryCity}"
                binding.orderDeliveryLocation.text = "${orderInView.deliveryAddress.deliveryProvince}, ${orderInView.deliveryAddress.deliveryCountry} ${orderInView.deliveryAddress.deliveryPostalCode}"

                binding.orderSubmittedReviewText.text = if (orderInView.customerReview.isEmpty()) "No review submitted fo this restaurant. Click the button below to submit a review about our service..." else orderInView.customerReview
            }
        }.addOnFailureListener {  }

        binding.updateReviewButton.setOnClickListener {
            startActivity(Intent(this, UpdateRestaurantReviewActivity::class.java))
        }

        binding.goBackButton.setOnClickListener {
            val intent = Intent(this, CustomerViewAllOrdersActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
            finish()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()

        // Create intent for the list of all orders activity
        val intent = Intent(this, CustomerViewAllOrdersActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        startActivity(intent)
        finish()
    }
}