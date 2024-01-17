/**
 * @author Emre Deniz
 * @date Jan 17, 2024
 */

package com.example.group9_mapd711_project.models

class Pizza(
    val pizzaName: String,
    val pizzaCategory: String,
    val smallPrice: Double,
    val mediumPrice: Double,
    val largePrice: Double,
    val deluxePrice: Double,
){
    var pizzaID: String = ""

    constructor():this("","",0.0,0.0,0.0,0.0)
}