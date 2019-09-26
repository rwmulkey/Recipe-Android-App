package com.example.recipes.models

enum class Measurement(val arrayIndex: Int) {
    TEASPOON(0), TABLESPOON(1), OUNCE(2), CUP(3), PINT(4), POUND(5), QUART(6),  GALLON(7);
    companion object {
        /*
        The conversion table will be a 2d array of static values used for conversion.
        This way we be able to call the conversion table like this: conversionTable[Measurement.OUNCE][Measurement.CUP]
        to get the conversion constant for ounces to cups.

        most of these conversions are volumetric however pound is a weight measurement. since a pound of butter is
        roughly equivalent to a pint of butter we will use the conversions for pints as pounds
     */
        val conversionTable: Array<FloatArray> = arrayOf(
            floatArrayOf(1f, 3f, 6f, 48f, 96f, 96f, 192f, 768f), //X to teaspoons
            floatArrayOf(1f / 3f, 1f, 2f, 16f, 32f, 32f, 64f, 256f),//X to tablespoons
            floatArrayOf(1f / 6f, 1f / 2f, 1f, 8f, 16f, 16f, 32f, 128f), //X to ounces
            floatArrayOf(1f / 48f, 1f / 16f, 1f / 8f, 1f, 2f, 2f, 4f, 16f), //X to cups
            floatArrayOf(1f / 96f, 1f / 32f, 1f / 16f, 1 / 2f, 1f, 1f, 2f, 8f), //X to pints
            floatArrayOf(1f / 96f, 1f / 32f, 1f / 16f, 1 / 2f, 1f, 1f, 2f, 8f), //X to pounds
            floatArrayOf(1f / 192f, 1f / 64f, 1f / 32f, 1 / 4f, 1f / 2f, 1f / 2f, 1f, 4f), //X to quarts
            floatArrayOf(1f / 768f, 1f / 256f, 1f / 128f, 1 / 16f, 1f / 8f, 1f / 8f, 1f / 4f, 1f) //X to gallons
        )

        public fun convert(from: Measurement, to: Measurement, amount: Float): Float {
            return amount * conversionTable[to.arrayIndex][from.arrayIndex]
        }
    }

    override fun toString(): String{
        return name
    }
}