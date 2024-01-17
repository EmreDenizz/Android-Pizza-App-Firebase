/**
 * @author Emre Deniz
 * @date Jan 17, 2024
 */

package com.example.group9_mapd711_project

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.group9_mapd711_project.adapters.OrderListAdapter
import com.example.group9_mapd711_project.databinding.ActivityCustomerViewAllOrdersBinding
import com.example.group9_mapd711_project.models.Order
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject

class CustomerViewAllOrdersActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCustomerViewAllOrdersBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var orderListAdapter: OrderListAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCustomerViewAllOrdersBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firestore = FirebaseFirestore.getInstance()
        firebaseAuth = FirebaseAuth.getInstance()

        orderListAdapter = OrderListAdapter(emptyList()) { orderId ->
            val pref = applicationContext.getSharedPreferences("Build_Pizza_Order", 0) //initialize an instance of shared preference
            val editor = pref.edit()

            editor.putString("order_reference_id",orderId)
            editor.commit()

            startActivity(Intent(this, CustomerViewSingleOrderActivity::class.java))
        }

        binding.allOrdersRecycleView.layoutManager = LinearLayoutManager(this)
        binding.allOrdersRecycleView.adapter = orderListAdapter

        val allMyOrdersRef = firestore.collection("orders").whereEqualTo("deliveryAddress.customerID",firebaseAuth.currentUser!!.uid).get()
        allMyOrdersRef.addOnSuccessListener {
            if (it.isEmpty){

            }
            else{
                val allOrders = mutableListOf<Order>()

                for (orderDoc in it.documents){
                    val singleOrder = orderDoc.toObject<Order>()!!
                    singleOrder.orderID = orderDoc.id
                    allOrders.add(singleOrder)
                }
                orderListAdapter.updateData(allOrders)
            }
        }.addOnFailureListener {  }

        binding.goBackButton.setOnClickListener {
            val intent = Intent(this, CustomerProfileViewActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()  // Optional: Close the current activity
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()

        // Create intent for the list of all orders activity
        val intent = Intent(this, CustomerProfileViewActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        startActivity(intent)
        finish()
    }
}