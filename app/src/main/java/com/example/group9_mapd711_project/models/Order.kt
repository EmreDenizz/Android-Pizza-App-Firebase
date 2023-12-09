package com.example.group9_mapd711_project.models

import com.google.firebase.Timestamp
import java.util.Date

class Order(
    val customerID:String,
    val pizzaID: String,
    val pizzaToppings: MutableList<String>,
    val restaurantAddress: RestaurantAddress,
    val deliveryAddress: DeliveryAddress,
    val orderInfo: OrderInfo,
    val customerReview:String,
){
    var orderID:String  = ""

    constructor():this("","",
        mutableListOf<String>(),
        RestaurantAddress(0.0,0.0,"",""),
        DeliveryAddress("","",""),
        OrderInfo(0,0.0,com.google.firebase.Timestamp(Date()),false,com.google.firebase.Timestamp(Date())),
        "",
    )
}

class RestaurantAddress(
    val restaurantLatitude:Double,
    val restaurantLongitude: Double,
    val restaurantName: String,
    val restaurantAddress: String,
)

class OrderInfo(
    val unitsCount: Int,
    val totalOrderPrice: Double,
    val orderSubmitDate: Timestamp,
    val orderDelivered: Boolean,
    val orderDeliverDate: Timestamp,
)

class DeliveryAddress(
    val deliveryPostalCode: String,
    val deliveryCityCountry: String,
    val deliveryAddress: String,
)