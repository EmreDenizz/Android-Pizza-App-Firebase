package com.example.group9_mapd711_project.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.group9_mapd711_project.R
import com.example.group9_mapd711_project.models.CityOption
import com.example.group9_mapd711_project.models.Pizza
import kotlin.random.Random

internal class PizzaOptionsListAdapter(
    private val pizzasList: List<Pizza>,
    private val pizzasImagesList: List<Int>,
    private val context: Context
) :
    BaseAdapter() {

    private var layoutInflater: LayoutInflater? = null
    private lateinit var pizzaName: TextView
    private lateinit var pizzaCategory: TextView
    private lateinit var pizzastartPrice: TextView
    private lateinit var pizzaImage: ImageView

    override fun getCount(): Int {
        return pizzasList.size
    }

    override fun getItem(position: Int): Any? {
        return null
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        var convertView = convertView

        if (layoutInflater == null) {
            layoutInflater =
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        }

        if (convertView == null) {
            convertView = layoutInflater!!.inflate(R.layout.single_pizza_layout_activity, null)
        }
        pizzaImage = convertView!!.findViewById(R.id.pizzaImage)
        pizzaName = convertView!!.findViewById(R.id.singlePizzaName)
        pizzaCategory = convertView!!.findViewById(R.id.singlePizzaCategory)
        pizzastartPrice = convertView!!.findViewById(R.id.singlePizzaPrice)

        pizzaImage.setImageResource(pizzasImagesList[Random.nextInt(pizzasImagesList.size)])
        pizzaName.text = pizzasList[position].pizzaName
        pizzaCategory.text = pizzasList[position].pizzaCategory
        pizzastartPrice.text = pizzasList[position].smallPrice.toString()

        return convertView
    }
}