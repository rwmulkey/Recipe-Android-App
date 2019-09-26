package com.example.recipes.models

import android.util.Log

class MealPlan {
    var days: MutableList<dayPlan>
    var shopList: ShoppingList
    var id: Long = -1

    constructor(){
        days = mutableListOf<dayPlan>(dayPlan(), dayPlan(), dayPlan(), dayPlan(), dayPlan(), dayPlan(), dayPlan())
        shopList = ShoppingList()
    }

    fun setRecipe(day: Int, recipe: Recipe?, meal: Int){
        if(recipe == null) return
        if(day >= 7){
            Log.w("MealPlan", "Invalid day.")
            return
        }

        if(days.get(day).getMeal(meal) != null){
            shopList.removeMultipleFromList(days.get(day).getMeal(meal)!!.ingredients)
        }

        shopList.addMultipleToList(recipe.ingredients)

        days.get(day).setMeal(recipe, meal)
    }

    class dayPlan{
        var breakfast: Recipe? = null
        var lunch: Recipe? = null
        var dinner: Recipe? = null
        var snack: Recipe? = null

        fun setMeal(recipe: Recipe, meal: Int){
            if(meal >= 4){
                Log.w("dayPlan", "Invalid Meal." )
                return
            }
            when(meal){
                0 -> breakfast = recipe
                1 -> lunch = recipe
                2 -> dinner = recipe
                3 -> snack = recipe
            }
        }

        fun getMeal(meal: Int): Recipe?{
            return when(meal){
                0 -> breakfast
                1 -> lunch
                2 -> dinner
                3 -> snack
                else -> null
            }
        }
    }
}