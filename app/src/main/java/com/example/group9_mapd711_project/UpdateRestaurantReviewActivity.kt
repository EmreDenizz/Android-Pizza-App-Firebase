/**
 * @author Emre Deniz
 * @date Jan 17, 2024
 */

package com.example.group9_mapd711_project

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.group9_mapd711_project.databinding.ActivityUpdateRestaurantReviewBinding
import com.example.group9_mapd711_project.models.Order
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject

class UpdateRestaurantReviewActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUpdateRestaurantReviewBinding
    private lateinit var firestore: FirebaseFirestore
    private lateinit var orderInView:Order
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityUpdateRestaurantReviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pref = applicationContext.getSharedPreferences("Build_Pizza_Order", 0) //initialize an instance of shared preference

        firestore = FirebaseFirestore.getInstance()

        val orderInViewRef = firestore.collection("orders").document(pref.getString("order_reference_id","No order selected")!!)
        orderInViewRef.get().addOnSuccessListener {
            if (it.exists()){
                orderInView = it.toObject<Order>()!!

                binding.singleOrderDeliveryAddressTag.text = orderInView.deliveryAddress.customerAddressTag
                binding.singleOrderRestaurantAddress.text = orderInView.restaurantAddress.restaurantAddress
                binding.singleOrderRestaurantName.text = orderInView.restaurantAddress.restaurantName
                binding.singleOrderPizzaName.text = orderInView.productInfo.productName
                binding.singleOrderDeliveryAddress.text = orderInView.deliveryAddress.deliveryAddress

                binding.reviewEditText.setText(orderInView.customerReview)
            }
        }.addOnFailureListener {  }

        binding.submitReviewButton.setOnClickListener {
            val customerReview = binding.reviewEditText.text.trim().toString()

            if (customerReview.isNotEmpty()){
                orderInViewRef.update(mapOf("customerReview" to customerReview)).addOnSuccessListener {
                    val intent = Intent(this, CustomerViewSingleOrderActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                    startActivity(intent)
                    finish()
                }.addOnFailureListener {  }
            }
            else{
                binding.reviewEditText.error = "Please give a review!!"
            }
        }

        binding.goBackButton.setOnClickListener {
            val intent = Intent(this, CustomerViewSingleOrderActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
            finish()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()

        // Create intent for the list of all orders activity
        val intent = Intent(this, CustomerViewSingleOrderActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        startActivity(intent)
        finish()
    }
}