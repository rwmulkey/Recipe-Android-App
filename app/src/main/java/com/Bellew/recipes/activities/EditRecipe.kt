package com.Bellew.recipes.activities

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.Bellew.recipes.R
import com.Bellew.recipes.adapters.RecipeNewDirectionCardRecyclerAdapter
import com.Bellew.recipes.adapters.RecipeNewIngredientCardRecyclerAdapter
import com.Bellew.recipes.models.Ingredients
import com.Bellew.recipes.models.Recipe
import com.Bellew.recipes.models.RecipeViewModel
import kotlinx.android.synthetic.main.fragment_edit_recipe.*
import kotlinx.android.synthetic.main.recipe_new_direction.view.*
import kotlinx.android.synthetic.main.recipe_new_ingredient_card.view.*

/*
    The EditRecipe Fragment is used for adding/updating ingredients and directions of a recipe as well as changing the
    name of the recipe.
 */
class EditRecipe : Fragment() {
    private lateinit var recipeModel: RecipeViewModel
    lateinit var currentRecipe : Recipe
    lateinit var ingredientAdapter : RecipeNewIngredientCardRecyclerAdapter
    lateinit var directionAdapter : RecipeNewDirectionCardRecyclerAdapter
    private var isNewRecipe : Boolean = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_edit_recipe, container, false)

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        ingredientAdapter = RecipeNewIngredientCardRecyclerAdapter()
        directionAdapter = RecipeNewDirectionCardRecyclerAdapter()

        recipeModel = activity?.run {
            ViewModelProviders.of(this)[RecipeViewModel::class.java]
        } ?: throw Exception("Invalid Activity")

        if(recipeModel.selected.value == null){
            currentRecipe = Recipe(-1)
            isNewRecipe = true
        } else {
            currentRecipe = recipeModel.selected.value!!
        }
        currentRecipe = recipeModel.selected.value ?: Recipe(-1)

        ingredientAdapter.currentResults = currentRecipe.getRecipeIngredientsClone()

        edit_recipe_ingredients.adapter = ingredientAdapter
        edit_recipe_ingredients.layoutManager = LinearLayoutManager(context)

        directionAdapter.currentResults = currentRecipe.getRecipeStepsClone()

        edit_recipe_directions.adapter = directionAdapter
        edit_recipe_directions.layoutManager = LinearLayoutManager(context)

        edit_recipe_name.setText(currentRecipe.name)

        edit_recipe_save.setOnClickListener { saveRecipe() }
        edit_recipe_cancel.setOnClickListener { cancel() }
        edit_recipe_add_ingredient.setOnClickListener { addIngredient() }
        edit_recipe_add_step.setOnClickListener { addStep() }
    }

    fun saveRecipe(){
        if(!validateRecipe()) return
        val newIngredients = ingredientAdapter.currentResults
        val newDirections = directionAdapter.currentResults

        currentRecipe.ingredients = newIngredients
        currentRecipe.steps = newDirections
        currentRecipe.name = edit_recipe_name.text.toString()

        if(isNewRecipe) {
            recipeModel.addRecipe(currentRecipe, context!!)
        } else{
            recipeModel.updateRecipe(currentRecipe, context!!)
        }

        getFragmentManager()!!.popBackStack()
    }

    fun cancel(){
        getFragmentManager()!!.popBackStack()
    }

    fun addIngredient(){
        ingredientAdapter.currentResults.add(ingredientAdapter.currentResults.size, Ingredients())
        ingredientAdapter.notifyItemChanged(ingredientAdapter.currentResults.size -1)
        edit_recipe_ingredients.scrollToPosition(ingredientAdapter.currentResults.size-1)
    }

    fun addStep(){
        directionAdapter.currentResults.add(directionAdapter.currentResults.size, "N/A")
        directionAdapter.notifyItemChanged(directionAdapter.currentResults.size -1)
        edit_recipe_directions.scrollToPosition(directionAdapter.currentResults.size-1)
    }


    fun validateRecipe(): Boolean{
        var isValid = true
        for((i, ingredient) in ingredientAdapter.currentResults.withIndex()){
            if(ingredient.amount <= 0){
                isValid = false
                edit_recipe_ingredients.scrollToPosition(i)
                edit_recipe_ingredients.get(i).ingredient_amount.setError("Must not be blank")
            }
            if( ingredient.name.equals("N/A") ) {

                isValid = false
                edit_recipe_ingredients.scrollToPosition(i)
                edit_recipe_ingredients.get(i).ingredient_name.setError("Must not be blank")
            }
        }
        for((i, direction) in directionAdapter.currentResults.withIndex()){
            if(direction.equals("N/A")){
                isValid = false
                edit_recipe_directions.scrollToPosition(i)
                edit_recipe_directions.get(i).recipe_new_direction_step.setError("Must not be blank")
            }
        }
        return isValid
    }

    companion object {
        fun newInstance(): EditRecipe = EditRecipe()
    }
}
