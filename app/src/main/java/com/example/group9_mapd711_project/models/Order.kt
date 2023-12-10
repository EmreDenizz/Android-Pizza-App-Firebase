package com.example.group9_mapd711_project.models

import com.google.firebase.Timestamp
import java.util.Date

class Order(
    val customerID:String,
    val pizzaID: String,
    val pizzaSize: String,
    val pizzaToppings: List<String>,
    val restaurantAddress: RestaurantAddress,
    val deliveryAddress: DeliveryAddress,
    val orderInfo: OrderInfo,
    val customerReview:String,
){
    var orderID:String  = ""

    constructor():this("","","",
        mutableListOf<String>(),
        RestaurantAddress(0.0,0.0,"","",""),
        DeliveryAddress("","","","",""),
        OrderInfo(0,0.0,com.google.firebase.Timestamp(Date()),false,false,com.google.firebase.Timestamp(Date()),0.0,0.0),
        "",
    )
}

class RestaurantAddress(
    val restaurantLatitude:Double,
    val restaurantLongitude: Double,
    val restaurantName: String,
    val restaurantAddress: String,
    val restaurantCityCountry:String,
)

class OrderInfo(
    val unitsCount: Int,
    val totalOrderPrice: Double,
    val orderSubmitDate: Timestamp,
    val orderDelivered: Boolean,
    val orderDiscounted:Boolean,
    val orderDeliverDate: Timestamp,
    val tipPercent: Double,
    val tipAmount: Double
)

class DeliveryAddress(
    val deliveryPostalCode: String,
    val deliveryCity: String,
    val deliveryProvince: String,
    val deliveryCountry: String,
    val deliveryAddress: String,
)