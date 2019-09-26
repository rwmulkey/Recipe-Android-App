package com.example.recipes.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.recipes.R
import com.example.recipes.activities.ShoppingListIngredientCard
import com.example.recipes.models.Ingredients

/*
    The ShoppingList RecyclerAdapter creates cards that display shopping list ingredients. Passes through a clicklistener
    from the ShoppingList Fragment that defines on checkbox toggle behavior
 */
class ShoppingListRecyclerAdapter(var checked: Boolean, var checkedListener: (Boolean, Ingredients) -> Unit) : RecyclerView.Adapter<ShoppingListIngredientCard>() {
    var currentResults = mutableListOf<Ingredients>()

    override fun getItemCount(): Int {
        return currentResults.size
    }

    override fun onBindViewHolder(holder: ShoppingListIngredientCard, position: Int) {
        holder.bind(currentResults[position], checked, checkedListener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShoppingListIngredientCard {
        var cardItem = LayoutInflater.from(parent.context).inflate(R.layout.shopping_list_ingredient_card, parent, false)

        return ShoppingListIngredientCard(cardItem)
    }
}