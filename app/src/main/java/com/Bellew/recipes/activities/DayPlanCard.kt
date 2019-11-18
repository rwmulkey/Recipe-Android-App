package com.Bellew.recipes.activities

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.Bellew.recipes.R
import com.Bellew.recipes.models.MealPlan
import com.Bellew.recipes.models.Recipe

class DayPlanCard(itemView: View): RecyclerView.ViewHolder(itemView) {
    private var dayLabel = itemView.findViewById<TextView>(R.id.day_plan_day)

    private var bLabel = itemView.findViewById<TextView>(R.id.day_plan_breakfast_title)
    private var bCalories = itemView.findViewById<TextView>(R.id.day_plan_breakfast_calories)
    private var bEdit = itemView.findViewById<ImageView>(R.id.day_plan_breakfast_change)
    private var lLabel = itemView.findViewById<TextView>(R.id.day_plan_lunch_title)
    private var lCalories = itemView.findViewById<TextView>(R.id.day_plan_lunch_calories)
    private var lEdit = itemView.findViewById<ImageView>(R.id.day_plan_lunch_change)
    private var dLabel = itemView.findViewById<TextView>(R.id.day_plan_dinner_title)
    private var dCalories = itemView.findViewById<TextView>(R.id.day_plan_dinner_calories)
    private var dEdit = itemView.findViewById<ImageView>(R.id.day_plan_dinner_change)
    private var sLabel = itemView.findViewById<TextView>(R.id.day_plan_snack_title)
    private var sCalories = itemView.findViewById<TextView>(R.id.day_plan_snack_calories)
    private var sEdit = itemView.findViewById<ImageView>(R.id.day_plan_snack_change)

    fun bind(plan: MealPlan.dayPlan, day: Int, editListener: (Int, Int) -> Unit, viewOnClickListener: (Recipe) -> Unit){
        var dayString = "Monday"
        when(day){
            0 -> dayString = "Sunday"
            1 -> dayString = "Monday"
            2 -> dayString = "Tuesday"
            3 -> dayString = "Wednesday"
            4 -> dayString = "Thursday"
            5 -> dayString = "Friday"
            6 -> dayString = "Saturday"
        }

        dayLabel.text = dayString

        bEdit.setOnClickListener{editListener(day, 0)}
        lEdit.setOnClickListener{editListener(day, 1)}
        dEdit.setOnClickListener{editListener(day, 2)}
        sEdit.setOnClickListener{editListener(day, 3)}

        bLabel.setOnClickListener{if(plan.breakfast != null) viewOnClickListener(plan.breakfast!!) else editListener(day, 0)}
        lLabel.setOnClickListener{if(plan.lunch != null) viewOnClickListener(plan.lunch!!) else editListener(day, 1)}
        dLabel.setOnClickListener{if(plan.dinner != null) viewOnClickListener(plan.dinner!!) else editListener(day, 2)}
        sLabel.setOnClickListener{if(plan.snack != null) viewOnClickListener(plan.snack!!) else editListener(day, 3)}


        if(plan.breakfast != null) bLabel.text = "Breakfast: " + plan.breakfast!!.name
        else bLabel.text = "Breakfast: "
        if(plan.lunch != null) lLabel.text = "Lunch: " + plan.lunch!!.name
        else lLabel.text = "Lunch: "
        if(plan.dinner != null) dLabel.text = "Dinner: " + plan.dinner!!.name
        else dLabel.text = "Dinner: "
        if(plan.snack != null) sLabel.text = "Snack: " + plan.snack!!.name
        else sLabel.text = "Snack: "
    }
}