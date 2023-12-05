package com.example.group9_mapd711_project

import android.content.Intent
import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.GridView
import android.widget.Toast
import com.example.group9_mapd711_project.adapters.CityOptionsGridViewAdapter
import com.example.group9_mapd711_project.databinding.ActivityCitySelectorBinding
import com.example.group9_mapd711_project.models.CityOption
import com.example.group9_mapd711_project.models.Customer
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject

class CitySelectorActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCitySelectorBinding
    private lateinit var possibleCityOptions:List<CityOption>
    private lateinit var firestore: FirebaseFirestore
    private lateinit var currentCustomer: Customer
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var cityOptionsGrid:GridView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCitySelectorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        val customerSnap = firestore.collection("customers").document(firebaseAuth.currentUser!!.uid)
        customerSnap.get().addOnSuccessListener {
            if (it.exists()){
                currentCustomer = it.toObject<Customer>()!!

                binding.greetingTextWithName.text = "Hello ${currentCustomer.lastName}"
            }
        }.addOnFailureListener {  }

        cityOptionsGrid = binding.citiesGridView

        binding.customerProfileRedirectButton.setOnClickListener {
            startActivity(Intent(this,CustomerProfileViewActivity::class.java))
        }

        possibleCityOptions = arrayListOf(
            CityOption(cityName = "Downtown", cityCountry = "Ontario, CA", cityImage = R.drawable.city1),
            CityOption(cityName = "Scarborough", cityCountry = "Ontario, CA", cityImage = R.drawable.city2),
            CityOption(cityName = "Calgary", cityCountry = "Alberta, CA", cityImage = R.drawable.city3),
            CityOption(cityName = "North York", cityCountry = "Ontario, CA", cityImage = R.drawable.city4),
            CityOption(cityName = "Saskatoon", cityCountry = "Saskatchewan, CA", cityImage = R.drawable.city5),
            CityOption(cityName = "Vancouver", cityCountry = "British Columbia, CA", cityImage = R.drawable.city6),
            CityOption(cityName = "Montreal", cityCountry = "Quebec, CA", cityImage = R.drawable.city7),
            CityOption(cityName = "Mississauga", cityCountry = "Ontario, CA", cityImage = R.drawable.dundas),
            CityOption(cityName = "Lethbridge", cityCountry = "Alberta, CA", cityImage = R.drawable.city1),
            CityOption(cityName = "London", cityCountry = "Ontario, CA", cityImage = R.drawable.city2),
            CityOption(cityName = "New York", cityCountry = "New York, USA", cityImage = R.drawable.city3),
        )

        val cityOptionsAdapter = CityOptionsGridViewAdapter(citiesList = possibleCityOptions, context = this)
        cityOptionsGrid.adapter = cityOptionsAdapter

        cityOptionsGrid.setOnItemClickListener { _, _, position, id ->
            val selectedCity = possibleCityOptions[position]
            val searchTerm = "${selectedCity.cityName}, ${selectedCity.cityCountry}"

            // Define intent
            val intent = Intent(this,PizzaRestaurantsMapActivity::class.java)

            // Pass selected city to intent
            intent.putExtra("selected_city",searchTerm)
            intent.putExtra("selected_city_image",possibleCityOptions[position].cityImage)

            // Get longtitude and latitude values of selected city
            val geocoder = Geocoder(this)
            val results = geocoder.getFromLocationName(searchTerm, 1)
            if (results != null) {
                if (results.isNotEmpty()) {
                    val location = results[0]

                    val latitude = location.latitude
                    val longitude = location.longitude

                    // Pass latitude and longitude to intent
                    intent.putExtra("latitude",latitude)
                    intent.putExtra("longitude",longitude)

                    startActivity(intent)
                }
                else{
                    Toast.makeText(
                        applicationContext, "No cities found in $searchTerm",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            else{
                Toast.makeText(
                    applicationContext, "No results for $searchTerm",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}