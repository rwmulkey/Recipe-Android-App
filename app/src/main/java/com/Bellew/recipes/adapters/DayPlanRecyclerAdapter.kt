package com.Bellew.recipes.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.Bellew.recipes.R
import com.Bellew.recipes.activities.DayPlanCard
import com.Bellew.recipes.models.MealPlan
import com.Bellew.recipes.models.Recipe

/*
    The DayPlan RecyclerAdapter simply displays the current meal plan meals. It also passes through two onclick
    listeners to the DayPlan Card that define onclick of the recipe name and the edit button. These functions are defined
    in the MealPlan Fragment
 */
class DayPlanRecyclerAdapter(val editOnClickListener: (Int, Int) -> Unit, val viewOnClickListener: (Recipe) -> Unit) : RecyclerView.Adapter<DayPlanCard>() {
    var currentResults: MealPlan = MealPlan()

    override fun getItemCount(): Int {
        return currentResults.days.size
    }

    override fun onBindViewHolder(holder: DayPlanCard, position: Int) {
        holder.bind(currentResults.days[position], position, editOnClickListener, viewOnClickListener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayPlanCard {
        var cardItem = LayoutInflater.from(parent.context).inflate(R.layout.dayplan_card_item, parent, false)

        return DayPlanCard(cardItem)
    }
}