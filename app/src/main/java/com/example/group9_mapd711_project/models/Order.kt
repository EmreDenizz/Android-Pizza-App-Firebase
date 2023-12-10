package com.example.group9_mapd711_project.models

import com.google.firebase.Timestamp
import java.util.Date

class Order(
    val productInfo: ProductInfo,
    val restaurantAddress: RestaurantAddress,
    val deliveryAddress: DeliveryAddress,
    val orderInfo: OrderInfo,
    val customerReview:String,
){
    var orderID:String  = ""

    constructor():this(
        ProductInfo("","","","", mutableListOf()),
        RestaurantAddress(0.0,0.0,"","","",0.0,0),
        DeliveryAddress("","","","","","","","","",""),
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
    val restaurantRating:Double,
    val restaurantRatingsCount:Int,
)

class ProductInfo(
    val productID:String,
    val productName:String,
    val productCategory:String,
    val productSize:String,
    val productToppings:List<String>,
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
    val customerID:String,
    val customerFullNames:String,
    val customerEmailAddress:String,
    val customerPhoneNumber:String,
    val customerAddressTag:String,
    val deliveryPostalCode: String,
    val deliveryCity: String,
    val deliveryProvince: String,
    val deliveryCountry: String,
    val deliveryAddress: String,
)