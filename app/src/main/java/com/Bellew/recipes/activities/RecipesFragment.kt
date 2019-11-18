package com.Bellew.recipes.activities

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.Bellew.recipes.R
import com.Bellew.recipes.adapters.RecipeCardRecyclerAdapter
import com.Bellew.recipes.models.MealPlanViewModel
import com.Bellew.recipes.models.Recipe
import com.Bellew.recipes.models.RecipeViewModel
import kotlinx.android.synthetic.main.fragment_recipes.*

class RecipesFragment : Fragment() {
    private lateinit var recipeModel: RecipeViewModel
    private lateinit var mealPlanModel: MealPlanViewModel
    private lateinit var adapter: RecipeCardRecyclerAdapter

    var getResult : Boolean = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_recipes, container, false)

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)

        if(arguments != null) getResult = arguments!!.getBoolean("getResult")
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if(!getResult) adapter = RecipeCardRecyclerAdapter{ recipe : Recipe? -> openEditRecipe(recipe) }
        else adapter = RecipeCardRecyclerAdapter { recipe: Recipe? -> returnRecipe(recipe) }

        recipeModel = activity?.run {
            ViewModelProviders.of(this)[RecipeViewModel::class.java]
        } ?: throw Exception("Invalid Activity")
        mealPlanModel = activity?.run {
            ViewModelProviders.of(this)[MealPlanViewModel::class.java]
        } ?: throw Exception("Invalid Activity")

        adapter.currentResults = RecipeViewModel.Companion.recipeList.value!!

        recipe_recycler_view.adapter = adapter
        recipe_recycler_view.layoutManager = LinearLayoutManager(context)
        add_recipe.setOnClickListener { createNewRecipe() }

        RecipeViewModel.Companion.recipeList.observe(this, Observer { recipeListChange() })
    }

    companion object {
        /*
            The Recipes Fragment can be called 2 different ways. Normally, or as opened from the bottomNav, and for
            selection. If opened normally we want the onclick of a recipe to open the SingleRecipe View. If opened for
            selection we want to assign the onclick of a recipe to assign the recipe to corresponding day and meal of
            the meal plan. This difference is directed by the "getResult" boolean.
         */
        fun newInstance(day: Int, meal: Int): RecipesFragment {
            val fragment = RecipesFragment()
            val args = Bundle()
            args.putInt("day", day)
            args.putInt("meal", meal)
            args.putBoolean("getResult", true)
            fragment.setArguments(args)
            return fragment
        }

        fun newInstance(): RecipesFragment = RecipesFragment()
    }

    /*
        Open a SingleRecipe Fragment and display the recipe
     */
    fun openEditRecipe(recipe: Recipe?){
        recipeModel.selected.value = recipe

        val recipeFragment = SingleRecipe()
        openFragment(recipeFragment)
    }

    /*
        Set the mealPlanModel meal and day selection to the recipe.
     */
    fun returnRecipe(recipe: Recipe?){
        if(mealPlanModel.currentDay != null && mealPlanModel.currentMeal != null && recipe != null) {
            mealPlanModel.setRecipe(recipe, mealPlanModel.currentDay!!, mealPlanModel.currentMeal!!, context!!)
        }

        getFragmentManager()!!.popBackStack()
    }

    private fun createNewRecipe(){
        val newRecipeFragment = EditRecipe()
        recipeModel.selected.value = null

        openFragment(newRecipeFragment)
    }


    private fun openFragment(fragment: Fragment) {
        val fragmentTransaction = fragmentManager!!.beginTransaction()
        fragmentTransaction.replace(R.id.container, fragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

    private fun recipeListChange(){
        adapter.notifyDataSetChanged()
    }

}
