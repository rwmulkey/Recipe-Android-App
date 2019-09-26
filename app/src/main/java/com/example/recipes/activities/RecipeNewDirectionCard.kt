package com.example.recipes.activities

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.recipes.R
import java.text.FieldPosition

class RecipeNewDirectionCard(itemView: View): RecyclerView.ViewHolder(itemView) {
    private val label = itemView.findViewById<TextView>(R.id.recipe_new_direction_step_label)
    val step = itemView.findViewById<EditText>(R.id.recipe_new_direction_step)
    var stepTextWatcher: TextWatcher? = null
    lateinit var results: MutableList<String>

    fun bind(_result: MutableList<String>, stepNum: Int){
        results = _result
        setBindings(stepNum)
        label.setText("Step " + (stepNum+1).toString() + ":")
        step.setText(results[stepNum])
    }

    /*
        We need the recipe_new_direction_step editText values to be persisted on change. By adding a TextWatcher we can
        save the values back to the results list. However, this being a recyclerView, this card could very well be
        reused for a different direction later on. Therefore, we need to keep a reference to the TextWatcher so we can
        remove it if this happens.
     */
    fun setBindings(position: Int){
        if(stepTextWatcher != null) step.removeTextChangedListener(stepTextWatcher)

        stepTextWatcher = object : TextWatcher {

            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                results[position] = s.toString()
            }
        }

        step.addTextChangedListener(stepTextWatcher)
    }
}