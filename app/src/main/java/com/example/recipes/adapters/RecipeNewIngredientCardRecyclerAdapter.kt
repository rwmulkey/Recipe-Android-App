package com.example.recipes.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.recipes.R
import com.example.recipes.activities.RecipeNewIngredientCard
import com.example.recipes.models.Ingredients
import kotlinx.android.synthetic.main.recipe_new_ingredient_card.view.*

/*
    The RecipeNewIngredient RecyclerAdapter creates cards with editable fields for creating or editing ingredients for
    for a recipe. Also a spinner for selecting measurement
 */
class RecipeNewIngredientCardRecyclerAdapter : RecyclerView.Adapter<RecipeNewIngredientCard>() {
    var currentResults: MutableList<Ingredients> = ArrayList()

    override fun getItemCount(): Int {
        return currentResults.size
    }

    override fun onBindViewHolder(holder: RecipeNewIngredientCard, position: Int) {
        var page = currentResults[position]
        holder.bind(page)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeNewIngredientCard {
        var cardItem = LayoutInflater.from(parent.context).inflate(R.layout.recipe_new_ingredient_card, parent, false)
        // Create an ArrayAdapter
        val adapter = ArrayAdapter.createFromResource(cardItem.context,
            R.array.measurement_array_list, android.R.layout.simple_spinner_item)
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        // Apply the adapter to the spinner
        cardItem.ingredient_measurement.adapter = adapter
        return RecipeNewIngredientCard(cardItem)
    }
}