package com.example.recipes.activities

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.recipes.R
import com.example.recipes.models.Recipe

class RecipeCardHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    private val nameView = itemView.findViewById<TextView>(R.id.recipe_name)

    fun bind(recipe: Recipe, listener: (Recipe) -> Unit){
        this.setOnClickListener(recipe, listener)

        nameView.text = recipe.name
    }

    fun setOnClickListener(recipe: Recipe, listener: (Recipe) -> Unit){
        itemView.setOnClickListener{listener(recipe)}
    }

}