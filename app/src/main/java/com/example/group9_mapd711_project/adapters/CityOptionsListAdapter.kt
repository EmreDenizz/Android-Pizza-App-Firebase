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
import android.widget.ImageView
import android.widget.TextView
import com.example.group9_mapd711_project.R
import com.example.group9_mapd711_project.models.CityOption

internal class CityOptionsGridViewAdapter(
    private val citiesList: List<CityOption>,
    private val context: Context
) :
    BaseAdapter() {

    private var layoutInflater: LayoutInflater? = null
    private lateinit var cityName: TextView
    private lateinit var cityCountry: TextView
    private lateinit var cityImage: ImageView

    override fun getCount(): Int {
        return citiesList.size
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
            convertView = layoutInflater!!.inflate(R.layout.individual_city_from_list, null)
        }
        cityImage = convertView!!.findViewById(R.id.cityImage)
        cityName = convertView!!.findViewById(R.id.cityName)
        cityCountry = convertView!!.findViewById(R.id.cityCountry)

        cityImage.setImageResource(citiesList[position].cityImage)
        cityName.text = citiesList[position].cityName
        cityCountry.text = citiesList[position].cityCountry

        return convertView
    }
}