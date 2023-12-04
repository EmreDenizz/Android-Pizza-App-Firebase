package com.example.group9_mapd711_project.models

class Customer(
    val firstName: String = "",
    val lastName: String = "",
    val userName: String = "",
    val emailAddress: String = "",
    val phoneNumber: String = "",
    val deliveryAddress: MutableList<CustomerAddress> = mutableListOf()
) {
    var customerID: String = ""

    // Secondary constructor
    constructor(
        firstName: String,
        lastName: String,
        userName: String,
        emailAddress: String,
        phoneNumber: String,
        deliveryAddress: MutableList<CustomerAddress>,
        customerID: String
    ) : this(firstName, lastName, userName, emailAddress, phoneNumber, deliveryAddress) {
        this.customerID = customerID
    }
}

class CustomerAddress(
    val deliverAddress:String,
    val addressTag:String,
    val city:String,
    val provinceCode:String,
    val country:String,
    val postalCode:String,
){
    constructor() : this("", "", "", "", "", "")
}