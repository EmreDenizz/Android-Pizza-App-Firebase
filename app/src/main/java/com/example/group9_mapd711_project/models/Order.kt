/**
 * @Group 9
 * @author Muindo Gituku (301372521)
 * @author Emre Deniz (301371047)
 * @author Nkemjika Obi (301275091)
 * @date Dec 11, 2023
 * @description: Android Project
 */

package com.example.group9_mapd711_project.models

import com.google.firebase.Timestamp
import java.io.Serializable
import java.util.Date

class Order : Serializable {
    val productInfo: ProductInfo
    val restaurantAddress: RestaurantAddress
    val deliveryAddress: DeliveryAddress
    val orderInfo: OrderInfo
    val customerReview: String
    var orderID: String = ""

    constructor(
        productInfo: ProductInfo,
        restaurantAddress: RestaurantAddress,
        deliveryAddress: DeliveryAddress,
        orderInfo: OrderInfo,
        customerReview: String
    ) {
        this.productInfo = productInfo
        this.restaurantAddress = restaurantAddress
        this.deliveryAddress = deliveryAddress
        this.orderInfo = orderInfo
        this.customerReview = customerReview
    }

    constructor(
        productInfo: ProductInfo,
        restaurantAddress: RestaurantAddress,
        deliveryAddress: DeliveryAddress,
        orderInfo: OrderInfo,
        customerReview: String,
        orderID: String
    ) : this(productInfo, restaurantAddress, deliveryAddress, orderInfo, customerReview) {
        this.orderID = orderID
    }

    // No-argument constructor for deserialization
    constructor() : this(
        ProductInfo(),
        RestaurantAddress(),
        DeliveryAddress(),
        OrderInfo(),
        ""
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
){
    constructor():this(0.0,0.0,"","","",0.0,0)
}

class ProductInfo(
    val productID:String,
    val productName:String,
    val productCategory:String,
    val productSize:String,
    val productToppings:List<String>,
){
    constructor():this("","","","", emptyList())
}

class OrderInfo(
    val unitsCount: Int,
    val totalOrderPrice: Double,
    val orderSubmitDate: Timestamp,
    val orderDelivered: Boolean,
    val orderDiscounted:Boolean,
    val orderDeliverDate: Timestamp,
    val tipPercent: Double,
    val tipAmount: Double
){
    constructor():this(0,0.0,com.google.firebase.Timestamp(Date()),false,false,com.google.firebase.Timestamp(Date()),0.0,0.0)
}

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
){
    constructor():this("","","","","","","","","","")
}