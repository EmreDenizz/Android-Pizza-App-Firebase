/**
 * @author Emre Deniz
 * @date Jan 17, 2024
 */

package com.example.group9_mapd711_project.models

class PizzaTopping (
    val toppingName: String,
    val toppingPrice: Double,
){
    var pizzaToppingID:String = ""

    constructor():this("",0.0)
}