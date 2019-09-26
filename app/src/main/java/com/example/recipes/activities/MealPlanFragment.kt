package com.example.recipes.activities

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recipes.R
import com.example.recipes.adapters.DayPlanRecyclerAdapter
import com.example.recipes.models.MealPlanViewModel
import com.example.recipes.models.Recipe
import com.example.recipes.models.RecipeViewModel
import kotlinx.android.synthetic.main.fragment_meal_plan.*

/*
    The MealPlan Fragment shows all planned meals for the week. The user can click a recipe to view the ingredients and
    directions in the SingleRecipe Fragment or click the edit page which brings up the Recipes Fragment to select a
    recipe to add to that meal.
 */
class MealPlanFragment : Fragment() {
    lateinit var mealPlanModel: MealPlanViewModel
    lateinit var recipeModel : RecipeViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_meal_plan, container, false)

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        mealPlanModel = activity?.run {
            ViewModelProviders.of(this)[MealPlanViewModel::class.java]
        } ?: throw Exception("Invalid Activity")
        recipeModel = activity?.run {
            ViewModelProviders.of(this)[RecipeViewModel::class.java]
        } ?: throw Exception("Invalid Activity")

        var adapter = DayPlanRecyclerAdapter({day: Int, meal: Int -> getRecipeFromFragment(day, meal)}, {recipe: Recipe -> openRecipe(recipe)})

        adapter.currentResults = mealPlanModel.currentPlan

        meal_plan_recycler_view.adapter = adapter
        meal_plan_recycler_view.layoutManager = LinearLayoutManager(context)
    }

    /*
        Sets mealPlanModel day and meal to the corresponding values and opens the recipeFragment. The recipefragment will use this
        information to set the mealplan correctly when a recipe is chosen. This is to be called when the an edit button
        for a meal is clicked.
     */
    fun getRecipeFromFragment(day: Int, meal: Int){
        mealPlanModel.currentDay = day
        mealPlanModel.currentMeal = meal

        val recipeFragment = RecipesFragment.newInstance(day, meal)
        openFragment(recipeFragment)
    }

    /*
        Sets the recipeModel selected to the selected recipe and opens the SingleRecipe Fragment. The fragment uses
        the selected recipe to display. This is meant to be called when a recipe is clicked
     */
    fun openRecipe(recipe: Recipe){
        recipeModel.selected.value = recipe

        val recipeFragment = SingleRecipe()
        openFragment(recipeFragment)
    }

    private fun openFragment(fragment: Fragment) {
        val fragmentTransaction = fragmentManager!!.beginTransaction()
        fragmentTransaction.replace(R.id.container, fragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

    companion object {
        fun newInstance(): MealPlanFragment = MealPlanFragment()
    }
}
