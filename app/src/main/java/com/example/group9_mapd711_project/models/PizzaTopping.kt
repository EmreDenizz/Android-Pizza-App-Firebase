/**
 * @Group 9
 * @author Muindo Gituku (301372521)
 * @author Emre Deniz (301371047)
 * @author Nkemjika Obi (301275091)
 * @date Dec 11, 2023
 * @description: Android Project
 */

package com.example.group9_mapd711_project.models

class PizzaTopping (
    val toppingName: String,
    val toppingPrice: Double,
){
    var pizzaToppingID:String = ""

    constructor():this("",0.0)
}