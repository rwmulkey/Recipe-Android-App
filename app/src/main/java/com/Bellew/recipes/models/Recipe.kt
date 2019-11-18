package com.Bellew.recipes.models

class Recipe {
    var id: Long
    var name: String
    var genre: String
    var steps =  mutableListOf<String>()
    var ingredients = mutableListOf<Ingredients>()

    constructor(_id: Long){
        name = String()
        genre = String()
        id = _id
    }

    constructor(_id: Long, _name: String, _genre: String, _steps: MutableList<String> = mutableListOf<String>(), _ingredients: MutableList<Ingredients> = mutableListOf<Ingredients>()){
        name = _name
        genre = _genre
        steps = _steps
        ingredients = _ingredients
        id = _id
    }

    fun multiplyRecipe(multiplier: Float){
        for(ingredient in ingredients){
            ingredient.amount *= multiplier
        }
    }

    fun convertIngredient(index: Int, measurement: Measurement){
        if(index < 0 || index >= ingredients.size) throw Exception("illegal ingredient access")

        ingredients[index].convert(measurement)
    }

    fun printRecipe(){
        for(step in steps){
            println(parseStep(step))
        }
    }

    /*
        A step can contain a substring of "$X" where X is the ingredient number

        This function replaces the substring with the amount and the name of the ingredient
        in order to make following the recipe simpler and account for recipe changes
     */
    fun parseStep(step: String): String{
        var tempStep = step
        while(tempStep.contains("$")){
            var ingredientNumber: Int = tempStep[tempStep.indexOf("$") +1].toInt() -48
            if(ingredientNumber >= ingredients.size || ingredientNumber < 0) return ""
            tempStep = tempStep.replace("$" + ingredientNumber, ingredients[ingredientNumber].toString())
        }
        return tempStep
    }

    /*
        return a step iterator that may be used for an app that might display only one step at a time or something like that
        This makes a copy of the step list in order to ensure that if the recipe is changed later this iterator will not be affected
     */
    fun getStepsIterator(): Iterator<String>{
        var tempSteps: MutableList<String> = arrayListOf()
        var i = 0
        for(step in steps){
            tempSteps.add(i, parseStep(step))
            i++
        }
        return tempSteps.iterator()
    }

    fun getRecipeIngredientsClone(): MutableList<Ingredients>{
        var newList = mutableListOf<Ingredients>()
        for(ing in ingredients){
            newList.add(Ingredients(ing.name, ing.type, ing.measurement, ing.amount))
        }
        return newList
    }

    fun getRecipeStepsClone(): MutableList<String>{
        var newList = mutableListOf<String>()
        for(st in steps){
            newList.add(String(st.toCharArray()))
        }
        return newList
    }
}