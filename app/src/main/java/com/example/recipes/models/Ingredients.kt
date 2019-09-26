package com.example.recipes.models

class Ingredients {
    var ingredientForID: Long = -1
    var name: String
    var type: String
    var measurement: Measurement
    var amount: Float

    constructor(_name: String, _type: String, _measurement: Measurement, _amount: Float){
        name = _name
        type = _type
        measurement = _measurement
        amount = _amount
    }

    constructor(){
        name = "N/A"
        type = "N/A"
        measurement = Measurement.OUNCE
        amount = -1f
    }

    fun convert(newMeasurement: Measurement){
        amount = Measurement.convert(measurement, newMeasurement, amount)
        measurement = newMeasurement
    }

    fun addToAmount(newMeasurement: Measurement, newAmount: Float){
        amount += Measurement.convert(newMeasurement, measurement, newAmount)
    }

    fun removeFromAmount(newMeasurement: Measurement, newAmount: Float){
        amount -= Measurement.convert(newMeasurement, measurement, newAmount)
    }

    override fun toString(): String{
        return amount.toString() + " " + measurement.toString() + "(s)"
    }
}