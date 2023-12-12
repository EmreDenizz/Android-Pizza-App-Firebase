/**
 * @Group 9
 * @author Muindo Gituku (301372521)
 * @author Emre Deniz (301371047)
 * @author Nkemjika Obi (301275091)
 * @date Dec 11, 2023
 * @description: Android Project
 */

package com.example.group9_mapd711_project.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.CheckBox
import android.widget.TextView
import com.example.group9_mapd711_project.R
import com.example.group9_mapd711_project.models.PizzaTopping

internal class ToppingsOptionsAdapter (
    private val toppingsList: List<PizzaTopping>,
    private val selectedToppings:MutableList<PizzaTopping>,
    private val context: Context,
):BaseAdapter(){
    private var layoutInflater: LayoutInflater? = null
    private lateinit var toppingName: TextView
    private lateinit var toppingPrice: TextView
    private lateinit var toppingCheck: CheckBox

    override fun getCount(): Int {
        return toppingsList.size
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
            convertView = layoutInflater!!.inflate(R.layout.single_topping_layout_activity, null)
        }
        toppingName = convertView!!.findViewById(R.id.toppingNameText)
        toppingPrice = convertView!!.findViewById(R.id.toppingPriceText)
        toppingCheck = convertView!!.findViewById(R.id.toppingCheckBox)

        toppingName.text = toppingsList[position].toppingName
        toppingPrice.text = toppingsList[position].toppingPrice.toString()

        if (toppingCheck.isChecked){
            selectedToppings.add(toppingsList[position])
        }
        else{
            if (toppingsList.contains(toppingsList[position])){
                selectedToppings.remove(toppingsList[position])
            }
        }

        return convertView
    }
}