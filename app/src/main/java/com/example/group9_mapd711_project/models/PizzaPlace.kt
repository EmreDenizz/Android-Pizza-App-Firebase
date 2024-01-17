/**
 * @author Emre Deniz
 * @date Jan 17, 2024
 */

package com.example.group9_mapd711_project.models

import retrofit2.http.GET
import retrofit2.http.Query

data class PlaceResult(val results: List<Place>)
data class Place(
    val name: String,
    val vicinity: String,
    val formatted_address: String,
    val formatted_phone_number: String,
    val international_phone_number: String,
    val opening_hours: OpenNow,
    val rating: Double,
    val user_ratings_total: Double,
    val website: String,
    val geometry: Geometry,
    val photos: List<Photo>,
)
data class Geometry(val location: Location)

data class Photo(
    val height: Double,
    val photo_reference : String,
    val width: Double,
)

data class OpenNow(val open_now: Boolean)
data class Location(val lat: Double, val lng: Double)

interface PlacesApiService {
    @GET("maps/api/place/nearbysearch/json")
    suspend fun getNearbyPlaces(
        @Query("location") location: String,
        @Query("radius") radius: Int,
        @Query("name") name: String,
        @Query("key") apiKey: String
    ): PlaceResult
}
