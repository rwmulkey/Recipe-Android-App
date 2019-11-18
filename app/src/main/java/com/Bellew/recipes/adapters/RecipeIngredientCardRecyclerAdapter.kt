package com.Bellew.recipes.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.Bellew.recipes.R
import com.Bellew.recipes.activities.RecipeIngredientCard
import com.Bellew.recipes.models.Ingredients

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