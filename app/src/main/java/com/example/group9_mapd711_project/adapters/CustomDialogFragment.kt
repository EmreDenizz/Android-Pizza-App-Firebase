/**
 * @author Emre Deniz
 * @date Jan 17, 2024
 */

package com.example.group9_mapd711_project.adapters

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.RatingBar
import android.widget.TextView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import com.example.group9_mapd711_project.PizzaTypeActivity
import com.example.group9_mapd711_project.R
import com.example.group9_mapd711_project.models.Place
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class CustomDialogFragment : BottomSheetDialogFragment() {

    companion object {
        fun newInstance(selectedPizzaRestaurant:Place): CustomDialogFragment {
            val fragment = CustomDialogFragment()
            val args = Bundle()
            args.putString("latitude", selectedPizzaRestaurant.geometry.location.lat.toString())
            args.putString("longitude", selectedPizzaRestaurant.geometry.location.lng.toString())
            args.putString("name", selectedPizzaRestaurant.name)
            args.putString("address", selectedPizzaRestaurant.vicinity)
            args.putDouble("rating", selectedPizzaRestaurant.rating)
            args.putInt("ratingCount", selectedPizzaRestaurant.user_ratings_total.toInt())
            args.putBoolean("isOpen", selectedPizzaRestaurant.opening_hours.open_now)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.custom_map_marker_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val name = arguments?.getString("name", "") ?: ""
        val address = arguments?.getString("address", "") ?: ""
        val rating = arguments?.getDouble("rating", 0.0) ?: 0.0
        val ratingCount = arguments?.getInt("ratingCount", 0) ?: 0
        val isOpen = arguments?.getBoolean("isOpen", false) ?: false



        val restNameText = view.findViewById<TextView>(R.id.restaurantName)
        val restVicinityText = view.findViewById<TextView>(R.id.restaurantVicinity)
        val restRatingBar = view.findViewById<RatingBar>(R.id.restaurantRatingBar)
        val restRatingsText = view.findViewById<TextView>(R.id.restaurantRatingsCount)
        val restOpenText = view.findViewById<TextView>(R.id.restaurantStatus)

        restNameText.text = name
        restVicinityText.text = address
        restRatingBar.rating = rating.toFloat()
        restRatingsText.text = "($ratingCount)"
        restOpenText.text = if (isOpen) "OPENED" else "CLOSED"

        val textColor = if (isOpen) R.color.openColor else R.color.closedColor
        restOpenText.setTextColor(ContextCompat.getColor(requireContext(), textColor))

        view.findViewById<TextView>(R.id.placeOrderButton).setOnClickListener {
            val pref = requireContext().getSharedPreferences("Build_Pizza_Order", 0) //initialize an instance of shared preference
            val editor = pref.edit()

            editor.putString("selected_restaurant_latitude",arguments?.getString("latitude","0.00")?: "0.00")
            editor.putString("selected_restaurant_longitude",arguments?.getString("longitude","0.00")?: "0.00")
            editor.putString("selected_restaurant_name",name)
            editor.putString("selected_restaurant_address",address)
            editor.putString("selected_restaurant_rating",rating.toString())
            editor.putString("selected_restaurant_rating_count",ratingCount.toString())
            editor.putString("selected_restaurant_open",isOpen.toString())
            editor.commit()

            startActivity(Intent(requireContext(), PizzaTypeActivity::class.java))

            // Dismiss the dialog
            dismiss()
        }
    }
}
