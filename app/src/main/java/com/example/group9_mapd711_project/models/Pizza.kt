/**
 * @Group 9
 * @author Muindo Gituku (301372521)
 * @author Emre Deniz (301371047)
 * @author Nkemjika Obi (301275091)
 * @date Dec 11, 2023
 * @description: Android Project
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