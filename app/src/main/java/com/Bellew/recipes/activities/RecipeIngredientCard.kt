package com.Bellew.recipes.activities

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.Bellew.recipes.R
import com.Bellew.recipes.models.Ingredients

class RecipeIngredientCard(itemView: View): RecyclerView.ViewHolder(itemView) {
    private val name = itemView.findViewById<TextView>(R.id.recipe_single_ingredient_name)
    private val calories = itemView.findViewById<TextView>(R.id.recipe_single_ingredient_calories)
    private val amount = itemView.findViewById<TextView>(R.id.recipe_single_ingredient_amount)

    fun bind(ingredient: Ingredients){
        name.setText(ingredient.name)
        amount.setText(ingredient.amount.toString() + " " + ingredient.measurement.name)
        //calories.setText("5")
    }
}