package com.example.group9_mapd711_project.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.group9_mapd711_project.R
import com.example.group9_mapd711_project.models.Order
import java.text.SimpleDateFormat

class OrderListAdapter(private var ordersList: List<Order>, private val onItemClick: (String) -> Unit) : RecyclerView.Adapter<OrderListAdapter.OrderViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.single_order_on_list_activity, parent, false)
        return OrderViewHolder(view)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val order = ordersList[position]
        holder.bind(order)
        holder.itemView.setOnClickListener { onItemClick(order.orderID) }
    }

    override fun getItemCount(): Int {
        return ordersList.size
    }

    // Update the data in the adapter
    fun updateData(newData: List<Order>) {
        ordersList = newData
        notifyDataSetChanged()
    }

    class OrderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val singleOrderPizzaName = itemView.findViewById<TextView>(R.id.singleOrderPizzaName)
        private val singleOrderRestaurantName = itemView.findViewById<TextView>(R.id.singleOrderRestaurantName)
        private val singleOrderRestaurantAddress = itemView.findViewById<TextView>(R.id.singleOrderRestaurantAddress)
        private val singleOrderDeliveryAddressTag = itemView.findViewById<TextView>(R.id.singleOrderDeliveryAddressTag)
        private val singleOrderDeliveryAddress = itemView.findViewById<TextView>(R.id.singleOrderDeliveryAddress)
        private val deliveryStatusText = itemView.findViewById<TextView>(R.id.deliveryStatusText)

        private val simpleDateFormat = SimpleDateFormat("YYYY/MM/DD hh:mm")
        fun bind(order: Order) {
            deliveryStatusText.text = if (order.orderInfo.orderDelivered) "Delivered on ${simpleDateFormat.format(order.orderInfo.orderDeliverDate.seconds * 1000L)}" else "Delivery is in progress... arriving soon!!"
            singleOrderDeliveryAddressTag.text = order.deliveryAddress.customerAddressTag
            singleOrderRestaurantAddress.text = order.restaurantAddress.restaurantAddress
            singleOrderRestaurantName.text = order.restaurantAddress.restaurantName
            singleOrderPizzaName.text = order.productInfo.productName
            singleOrderDeliveryAddress.text = order.deliveryAddress.deliveryAddress
        }
    }
}