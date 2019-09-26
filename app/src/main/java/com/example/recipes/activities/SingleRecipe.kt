package com.example.recipes.activities

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recipes.R
import com.example.recipes.adapters.RecipeDirectionCardRecyclerAdapter
import com.example.recipes.adapters.RecipeIngredientCardRecyclerAdapter
import com.example.recipes.models.RecipeViewModel
import kotlinx.android.synthetic.main.fragment_single_recipe.*

/*
    The SingleRecipe Fragment is a simple class that displays the recipe in an easy to read format for the user and
    allows them to open the EditRecipe Fragment to change anything.
 */
class SingleRecipe : Fragment() {
    //Models
    private lateinit var recipeModel: RecipeViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_single_recipe, container, false)

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)

        recipeModel = activity?.run {
            ViewModelProviders.of(this)[RecipeViewModel::class.java]
        } ?: throw Exception("Invalid Activity")

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        single_recipe_name.setText(recipeModel.selected.value!!.name)

        //Ingredients List
        var ingredientAdapter = RecipeIngredientCardRecyclerAdapter()
        ingredientAdapter.currentResults = recipeModel.selected.value!!.ingredients

        single_recipe_ingredients_recycler_view.adapter = ingredientAdapter
        single_recipe_ingredients_recycler_view.layoutManager = LinearLayoutManager(context)

        //Directions List
        var directionsAdapter = RecipeDirectionCardRecyclerAdapter()
        directionsAdapter.currentResults = recipeModel.selected.value!!.steps

        single_recipe_directions_recycler_view.adapter = directionsAdapter
        single_recipe_directions_recycler_view.layoutManager = LinearLayoutManager(context)

        single_recipe_edit.setOnClickListener { editRecipe() }
    }

    companion object {
        fun newInstance(): SingleRecipe = SingleRecipe()
    }

    fun editRecipe(){
        val editRecipeFragment = EditRecipe()
        val fragmentTransaction = fragmentManager!!.beginTransaction()
        fragmentTransaction.replace(R.id.container, editRecipeFragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

}
