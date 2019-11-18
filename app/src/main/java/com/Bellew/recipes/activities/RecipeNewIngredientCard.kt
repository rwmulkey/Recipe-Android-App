package com.Bellew.recipes.activities

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.EditText
import android.widget.Spinner
import androidx.recyclerview.widget.RecyclerView
import com.Bellew.recipes.R
import com.Bellew.recipes.models.Ingredients
import com.Bellew.recipes.models.Measurement


class RecipeNewIngredientCard(itemView: View): RecyclerView.ViewHolder(itemView) {
    val amount = itemView.findViewById<EditText>(R.id.ingredient_amount)
    val name = itemView.findViewById<EditText>(R.id.ingredient_name)
    val measurement = itemView.findViewById<Spinner>(R.id.ingredient_measurement)
    public var ingredientForID: Long = -1
    var nameTextWatcher: TextWatcher? = null
    var amountTextWatcher: TextWatcher? = null

    fun bind(ingredient: Ingredients?){
        if(ingredient != null){
            setBindings(ingredient)
            amount.setText(ingredient.amount.toString())
            name.setText(ingredient.name)
            measurement.setSelection(ingredient.measurement.arrayIndex)
            ingredientForID = ingredient.ingredientForID
        }
    }

    /*
        Much like the RecipeNewDirectionCard.setBindings function we need to persist any changes to the editable text
        fields. We also need to keep references to the TextWatchers so we can remove them if this card is used again.
     */
    fun setBindings(ingredient: Ingredients){
        if(nameTextWatcher != null){
            name.removeTextChangedListener(nameTextWatcher)
        }
        if(amountTextWatcher != null){
            amount.removeTextChangedListener(amountTextWatcher)
        }

        nameTextWatcher = object : TextWatcher {

            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                ingredient.name = s.toString()
            }
        }

        amountTextWatcher = object : TextWatcher {

            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                val value = s.toString().toFloatOrNull()
                ingredient.amount = if (value == null) -1f; else value
            }
        }

        amount.addTextChangedListener(amountTextWatcher)

        name.addTextChangedListener(nameTextWatcher)

        measurement.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (view != null) ingredient.measurement =
                    Measurement.valueOf(view.resources.getStringArray(R.array.measurement_array_list)[position])
            }

        }
    }
}