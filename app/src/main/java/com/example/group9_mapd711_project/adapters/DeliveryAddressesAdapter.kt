/**
/**
 * @author Emre Deniz
 * @date Jan 17, 2024
*/

package com.example.group9_mapd711_project.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.group9_mapd711_project.R
import com.example.group9_mapd711_project.models.Customer
import com.example.group9_mapd711_project.models.CustomerAddress

class DeliveryAddressesAdapter(private var customerAddresses: List<CustomerAddress>, private val onItemClick: (CustomerAddress) -> Unit) : RecyclerView.Adapter<DeliveryAddressesAdapter.AddressViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddressViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.single_delivery_address_in_list, parent, false)
        return AddressViewHolder(view)
    }

    override fun onBindViewHolder(holder: AddressViewHolder, position: Int) {
        val address = customerAddresses[position]
        holder.bind(address, customerAddresses) { onItemClick(customerAddresses[position]) }
    }

    override fun getItemCount(): Int {
        return customerAddresses.size
    }

    // Update the data in the adapter
    fun updateData(newList: List<CustomerAddress>) {
        customerAddresses = newList
        notifyDataSetChanged()
    }

    class AddressViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val singleAddressTag = itemView.findViewById<TextView>(R.id.singleAddressTag)
        private val singleAddress = itemView.findViewById<TextView>(R.id.singleAddress)
        private val singleAddressLocation = itemView.findViewById<TextView>(R.id.singleAddressLocation)
        private val deleteAddressButton = itemView.findViewById<Button>(R.id.deleteAddressButton)
        fun bind(address: CustomerAddress, customerAddresses: List<CustomerAddress>, onItemClick: (CustomerAddress) -> Unit) {
            singleAddressTag.text = address.addressTag
            singleAddress.text = "${address.deliverAddress}, ${address.city}"
            singleAddressLocation.text = "${address.provinceCode}, ${address.country} ${address.postalCode}"

            if (customerAddresses.size > 1){
                deleteAddressButton.visibility = View.VISIBLE
                deleteAddressButton.setOnClickListener {onItemClick}
            }
        }
    }
}