package com.example.group9_mapd711_project.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.group9_mapd711_project.R
import com.example.group9_mapd711_project.models.Pizza

class PizzaOptionsListAdapter(private var pizzasList: List<Pizza>, private val onItemClick: (String) -> Unit) : RecyclerView.Adapter<PizzaOptionsListAdapter.PizzaViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PizzaViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.single_pizza_layout_activity, parent, false)
        return PizzaViewHolder(view)
    }

    override fun onBindViewHolder(holder: PizzaViewHolder, position: Int) {
        val pizza = pizzasList[position]
        holder.bind(pizza)
        holder.itemView.setOnClickListener { onItemClick(pizza.pizzaID) }
    }

    override fun getItemCount(): Int {
        return pizzasList.size
    }

    // Update the data in the adapter
    fun updateData(newData: List<Pizza>) {
        pizzasList = newData
        notifyDataSetChanged()
    }

    class PizzaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val pizzaNameTextView: TextView = itemView.findViewById(R.id.singlePizzaName)
        private val priceTextView: TextView = itemView.findViewById(R.id.singlePizzaPrice)
        private val categoryTextView: TextView = itemView.findViewById(R.id.singlePizzaCategory)

        fun bind(pizza: Pizza) {
            pizzaNameTextView.text = pizza.pizzaName
            priceTextView.text = pizza.smallPrice.toString()
            categoryTextView.text = pizza.pizzaCategory
        }
    }
}