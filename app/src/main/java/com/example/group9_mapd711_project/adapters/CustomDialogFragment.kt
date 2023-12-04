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
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class CustomDialogFragment : BottomSheetDialogFragment() {

    companion object {
        fun newInstance(name: String, address: String, rating: Double, ratingCount: Int, isOpen: Boolean): CustomDialogFragment {
            val fragment = CustomDialogFragment()
            val args = Bundle()
            args.putString("name", name)
            args.putString("address", address)
            args.putDouble("rating", rating)
            args.putInt("ratingCount", ratingCount)
            args.putBoolean("isOpen", isOpen)
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

        // Retrieve data from arguments
        val name = arguments?.getString("name", "") ?: ""
        val address = arguments?.getString("address", "") ?: ""
        val rating = arguments?.getDouble("rating", 0.0) ?: 0.0
        val ratingCount = arguments?.getInt("ratingCount", 0) ?: 0
        val isOpen = arguments?.getBoolean("isOpen", false) ?: false

        //initialize the components to use in the dialog
        val restNameText = view.findViewById<TextView>(R.id.restaurantName)
        val restVicinityText = view.findViewById<TextView>(R.id.restaurantVicinity)
        val restRatingBar = view.findViewById<RatingBar>(R.id.restaurantRatingBar)
        val restRatingsText = view.findViewById<TextView>(R.id.restaurantRatingsCount)
        val restOpenText = view.findViewById<TextView>(R.id.restaurantStatus)

        // Set data to views in the dialog
        restNameText.text = name
        restVicinityText.text = address
        restRatingBar.rating = rating.toFloat()
        restRatingsText.text = "($ratingCount)"
        restOpenText.text = if (isOpen) "OPENED" else "CLOSED"

        // Optionally, you can also change the text color based on isOpen
        val textColor = if (isOpen) R.color.openColor else R.color.closedColor
        restOpenText.setTextColor(ContextCompat.getColor(requireContext(), textColor))

        // Handle button click
        view.findViewById<Button>(R.id.placeOrderButton).setOnClickListener {
            // Add your navigation logic here
            // For example, navigate to another activity
            val intent = Intent(requireContext(), PizzaTypeActivity::class.java)

            intent.putExtra("selected_rest_name",name)
            intent.putExtra("selected_rest_address",address)

            startActivity(intent)

            // Dismiss the dialog
            dismiss()
        }
    }
}
