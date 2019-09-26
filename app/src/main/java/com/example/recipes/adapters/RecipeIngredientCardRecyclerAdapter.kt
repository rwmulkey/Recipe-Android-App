package com.example.recipes.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.recipes.R
import com.example.recipes.activities.RecipeIngredientCard
import com.example.recipes.models.Ingredients

/*
    The RecipeIngredientCard RecyclerAdapter creates RecipeIngredientCards that display the ingredient information for a
    recipe.
 */
class RecipeIngredientCardRecyclerAdapter : RecyclerView.Adapter<RecipeIngredientCard>() {
    var currentResults: MutableList<Ingredients> = ArrayList()

    override fun getItemCount(): Int {
        return currentResults.size
    }

    override fun onBindViewHolder(holder: RecipeIngredientCard, position: Int) {
        var page = currentResults[position]
        holder.bind(page)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeIngredientCard {
        var cardItem = LayoutInflater.from(parent.context).inflate(R.layout.recipe_single_ingredient, parent, false)

        return RecipeIngredientCard(cardItem)
    }
}