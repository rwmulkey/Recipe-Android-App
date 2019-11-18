package com.Bellew.recipes.activities

import android.graphics.Color
import android.view.View
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.Bellew.recipes.R
import com.Bellew.recipes.models.Ingredients

/*
    The ShoppingListIngredient Card displays a single ingredient on the shopping list. The listener for on checkbox
    toggle is passed as an argument from the adapter and is defined in the ShoppingListFragment class
 */
class ShoppingListIngredientCard(itemView: View): RecyclerView.ViewHolder(itemView) {
    private var checkBox = itemView.findViewById<CheckBox>(R.id.shopping_list_card_checkbox)
    private var label = itemView.findViewById<TextView>(R.id.shopping_list_card_ingredient)

    fun bind(ingredient: Ingredients, checked: Boolean, checkedListener: (Boolean, Ingredients) -> Unit){
        checkBox.setChecked(checked)
        checkBox.setOnClickListener(View.OnClickListener {
            checkedListener(checkBox.isChecked, ingredient)
        })
        label.text = ingredient.amount.toString() + " " + ingredient.measurement.name + " " + ingredient.name

        if(!checked) label.setTextColor(Color.parseColor("#000000"))
        else label.setTextColor(Color.parseColor("#808080"))
    }
}