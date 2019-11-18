package com.Bellew.recipes.activities

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.Bellew.recipes.R

class RecipeDirectionCard(itemView: View): RecyclerView.ViewHolder(itemView) {
    private val label = itemView.findViewById<TextView>(R.id.recipe_single_direction_step_label)
    private val step = itemView.findViewById<TextView>(R.id.recipe_single_direction_step)

    fun bind(_step: String, stepNum: Int){
        label.setText("Step " + (stepNum+1).toString() + ":")
        step.setText(_step)
    }
}