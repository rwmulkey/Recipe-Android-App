package com.Bellew.recipes.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.Bellew.recipes.R
import com.Bellew.recipes.activities.RecipeDirectionCard

/*
    The RecipeDirectionCard RecyclerAdapter creates recipeDirectionCards to display direction information for a recipe
 */
class RecipeDirectionCardRecyclerAdapter : RecyclerView.Adapter<RecipeDirectionCard>() {
    var currentResults: MutableList<String> = ArrayList()

    override fun getItemCount(): Int {
        return currentResults.size
    }

    override fun onBindViewHolder(holder: RecipeDirectionCard, position: Int) {
        var page = currentResults[position]
        holder.bind(page, position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeDirectionCard {
        var cardItem = LayoutInflater.from(parent.context).inflate(R.layout.recipe_single_direction, parent, false)

        return RecipeDirectionCard(cardItem)
    }
}