/**
 * @author Emre Deniz
 * @date Jan 17, 2024
 */

package com.example.group9_mapd711_project

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.example.group9_mapd711_project.adapters.CustomDialogFragment

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.group9_mapd711_project.databinding.ActivityPizzaRestaurantsMapBinding
import com.example.group9_mapd711_project.models.Place
import com.example.group9_mapd711_project.models.PlacesApiService
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.Marker
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class PizzaRestaurantsMapActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityPizzaRestaurantsMapBinding

    // Define place longitude and latitude values
    private var townLatitude = 0.0
    private var townLongitude = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPizzaRestaurantsMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pref = applicationContext.getSharedPreferences("Build_Pizza_Order", 0) //initialize an instance of shared preference

        binding.backFAB2.setOnClickListener {
            finish()
        }

        townLatitude = pref.getString("selected_city_latitude", "0.00")!!.toDouble()
        townLongitude = pref.getString("selected_city_longitude", "0.00")!!.toDouble()

        binding.selectedCityNameText.text = pref.getString("selected_city", "No Selected City")
        binding.selectedCityImage2.setImageResource(pref.getString("selected_city_image", "R.drawable.dundas")!!.toInt())

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker to selected town
        val selectedTownPin = LatLng(townLatitude, townLongitude)
        mMap.addMarker(MarkerOptions().position(selectedTownPin).title(intent.getStringExtra("selected_city")))

        // Set default zoom on map
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(selectedTownPin, 13f))

        // Set a custom info window adapter to handle marker clicks
        mMap.setInfoWindowAdapter(CustomInfoWindowAdapter())

        // Set a custom click listener for markers
        mMap.setOnMarkerClickListener { marker ->
            // Handle marker click here
            val selectedMarker = marker.position
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(CameraPosition(selectedMarker,15f,10f,15f)))

            showCustomDialog(marker.tag as Place)
            true
        }

        // Call fetch Nearby Pizza Restaurants and draw function
        fetchNearbyPizzaRestaurants()
    }

    private fun fetchNearbyPizzaRestaurants(){
        // Google Places API key
        val apiKey = "AIzaSyDBQOf0tvMNiecFD8A5UQ-0JmHW8BL6JAA"

        // Coordinates of the location
        val latitude = townLatitude
        val longitude = townLongitude
        val location = "$latitude,$longitude"

        // Radius in meters (2 kilometer)
        val radius = 2000

        // Name of places to filter
        val name = "pizza"

        // Retrofit setup
        val retrofit = Retrofit.Builder()
            .baseUrl("https://maps.googleapis.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val placesApiService = retrofit.create(PlacesApiService::class.java)

        lifecycleScope.launch {
            try {
                // Make API call to get nearby restaurants
                val response = placesApiService.getNearbyPlaces(
                    location, radius, name, apiKey
                )
                val places = response.results

                // Add markers for each nearby restaurants
                for (place in places) {
                    val latLng = LatLng(place.geometry.location.lat, place.geometry.location.lng)
                    val markerOptions = MarkerOptions()
                        .position(latLng)
                        .title(place.name)
                        .snippet(place.vicinity)
                        .infoWindowAnchor(1.0F, 2.0F)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.pizza_icon_small))

                    val newMarker = mMap.addMarker(markerOptions)
                    if (newMarker != null) {
                        newMarker.tag = place
                    }
                    Log.d("places",place.toString())
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun showCustomDialog(place: Place) {
        val dialog = CustomDialogFragment.newInstance(place)
        dialog.show(supportFragmentManager, "custom_dialog")
    }

    inner class CustomInfoWindowAdapter : GoogleMap.InfoWindowAdapter {
        override fun getInfoWindow(marker: Marker): View? {
            // Return null to use the default info window
            return null
        }

        override fun getInfoContents(marker: Marker): View {
            // Return null to use the default info window
            return View(applicationContext)
        }
    }
}