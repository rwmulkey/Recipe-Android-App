package com.Bellew.recipes.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.Bellew.recipes.R
import com.Bellew.recipes.activities.RecipeNewDirectionCard

/*
    The RecipeNewDirectionCard RecyclerAdapter creates cards with editable fields for creating or editing direcitons for
    for a recipe.
 */
class RecipeNewDirectionCardRecyclerAdapter : RecyclerView.Adapter<RecipeNewDirectionCard>() {
    var currentResults: MutableList<String> = ArrayList()

    override fun getItemCount(): Int {
        return currentResults.size
    }

    override fun onBindViewHolder(holder: RecipeNewDirectionCard, position: Int) {
        var page = currentResults[position]
        holder.bind(currentResults, position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeNewDirectionCard {
        var cardItem = LayoutInflater.from(parent.context).inflate(R.layout.recipe_new_direction, parent, false)
        return RecipeNewDirectionCard(cardItem)
    }
}