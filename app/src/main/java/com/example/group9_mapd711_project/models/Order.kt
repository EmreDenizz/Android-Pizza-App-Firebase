package com.example.group9_mapd711_project.models

import com.google.firebase.Timestamp
import java.util.Date

class Order(
    val customerID:String,
    val pizzaID: String,
    val pizzaToppings: Array<Any>,
    val restaurantAddress: RestaurantAddress,
    val orderSubmitDate: Timestamp,
    val orderDelivered: Boolean,
    val orderDeliverDate: Timestamp,
    val customerReview:String,
){
    var orderID:String  = ""

    constructor():this("","",
        arrayOf<Any>(),
        RestaurantAddress(0.0,0.0,"",""),com.google.firebase.Timestamp(Date()),false,com.google.firebase.Timestamp(Date()),"",
    )
}

class RestaurantAddress(
    val restaurantLatitude:Double,
    val restaurantLongitude: Double,
    val restaurantName: String,
    val restaurantAddress: String,
)