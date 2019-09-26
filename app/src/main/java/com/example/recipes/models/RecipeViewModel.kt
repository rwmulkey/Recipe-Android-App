package com.example.recipes.models

import android.app.Application
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.provider.BaseColumns
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.recipes.models.SQL.ContractObject
import com.example.recipes.models.SQL.SQLHelper

class RecipeViewModel(application: Application) : AndroidViewModel(application) {
    var selected = MutableLiveData<Recipe?>()

    init {
        recipeList.value = arrayListOf()
        getRecipesFromDB(getApplication())
    }

    companion object{
        var recipeList = MutableLiveData<ArrayList<Recipe>>()

        fun getRecipeByID(id: Long): Recipe?{
            if(recipeList.value == null) return null
            for(recipe in recipeList.value!!){
                if(recipe.id == id) return recipe
            }
            return null
        }

        fun addIfNotExistsIngredientDB(ingredient: Ingredients, db: SQLiteDatabase): Long{
            val projection = arrayOf(BaseColumns._ID)
            val selection = "${ContractObject.Ingredient_Table.COLUMN_NAME_NAME} = ?"
            val selection_args = arrayOf(ingredient.name)

            val cursor = db.query(
                ContractObject.Ingredient_Table.TABLE_NAME,
                projection,
                selection,
                selection_args,
                null,
                null,
                null
            )

            with(cursor){
                while(moveToNext()){
                    return getLong(getColumnIndexOrThrow(BaseColumns._ID))
                }
            }

            //No ingredient matches therefore we insert a new one.

            val values = ContentValues().apply {
                put(ContractObject.Ingredient_Table.COLUMN_NAME_NAME, ingredient.name)
            }

            return db.insert(ContractObject.Ingredient_Table.TABLE_NAME, null, values)
        }
    }

    fun addRecipe(recipe: Recipe, context: Context){
        val dbHelper = SQLHelper(context)
        val db = dbHelper.writableDatabase

        val recipeID = addRecipeDB(recipe, db)

        for(ingredient in recipe.ingredients){
            ingredient.ingredientForID = addIngredientForDB(addIfNotExistsIngredientDB(ingredient, db), recipeID, ingredient.amount, ingredient.measurement.name, db)
        }

        for((index, step)  in recipe.steps.withIndex()){
            addStepForDB(step, recipeID, db, index)
        }

        db.close()

        recipeList.value!!.add(recipe)
    }

    private fun addRecipeDB(recipe: Recipe, db: SQLiteDatabase): Long{
        val values = ContentValues().apply {
            put(ContractObject.Recipe_Table.COLUMN_NAME_NAME, recipe.name)
        }
        return db.insert(ContractObject.Recipe_Table.TABLE_NAME, null, values)
    }

    private fun addIngredientForDB(ingredientID: Long, recipeID: Long, amount: Float, measurement: String, db: SQLiteDatabase): Long{
        val values = ContentValues().apply {
            put(ContractObject.Ingredients_For_Recipe_Table.COLUMN_NAME_INGREDIENT_FOREIGN_KEY, ingredientID)
            put(ContractObject.Ingredients_For_Recipe_Table.COLUMN_NAME_RECIPE_FOREIGN_KEY, recipeID)
            put(ContractObject.Ingredients_For_Recipe_Table.COLUMN_NAME_AMOUNT, amount)
            put(ContractObject.Ingredients_For_Recipe_Table.COLUMN_NAME_MEASUREMENT, measurement)
        }

        return db.insert(ContractObject.Ingredients_For_Recipe_Table.TABLE_NAME, null, values)
    }

    private fun addStepForDB(step: String, recipeID: Long, db: SQLiteDatabase, stepNum: Int): Long{
        val values = ContentValues().apply {
            put(ContractObject.Steps_Table.COLUMN_NAME_RECIPE_FOREIGN_KEY, recipeID)
            put(ContractObject.Steps_Table.COLUMN_NAME_STEP_DIRECTIONS, step)
            put(ContractObject.Steps_Table.COLUMN_NAME_STEP_NUM, stepNum)
        }

        return db.insert(ContractObject.Steps_Table.TABLE_NAME, null, values)
    }

    fun updateRecipe(recipe: Recipe, context: Context){
        val dbHelper = SQLHelper(context)
        val db = dbHelper.writableDatabase

        val values = ContentValues().apply {
            put(ContractObject.Recipe_Table.COLUMN_NAME_NAME, recipe.name)
        }

        val selection = "${BaseColumns._ID} = ?"
        val selectionArgs = arrayOf(recipe.id.toString())

        db.update(
            ContractObject.Recipe_Table.TABLE_NAME,
            values,
            selection,
            selectionArgs
        )

        removeIngredientsFor(recipe, db)

        for(ingredient in recipe.ingredients) {
            addIngredientForDB(addIfNotExistsIngredientDB(ingredient, db), recipe.id, ingredient.amount, ingredient.measurement.name, db)
        }
        for((index, step) in recipe.steps.withIndex()){
            updateStepsForRecipe(recipe, step, index, db)
        }
    }

    private fun updateStepsForRecipe(recipe: Recipe, step: String, stepNum: Int, db: SQLiteDatabase){
        val values = ContentValues().apply {
            put(ContractObject.Steps_Table.COLUMN_NAME_STEP_DIRECTIONS, step)
        }

        val selection = "${ContractObject.Steps_Table.COLUMN_NAME_RECIPE_FOREIGN_KEY} = ? and ${ContractObject.Steps_Table.COLUMN_NAME_STEP_NUM} = ?"
        val selectionArgs = arrayOf(recipe.id.toString(), stepNum.toString())

        val count = db.update(
            ContractObject.Steps_Table.TABLE_NAME,
            values,
            selection,
            selectionArgs
        )

        if(count <= 0) addStepForDB(step, recipe.id, db, stepNum)
    }

    private fun getRecipesFromDB(context: Context){
        val dbHelper = SQLHelper(context)
        val db = dbHelper.readableDatabase

        val projection = arrayOf(BaseColumns._ID, ContractObject.Recipe_Table.COLUMN_NAME_NAME)

        val cursor = db.query(
            ContractObject.Recipe_Table.TABLE_NAME,
            projection,
            null,
            null,
            null,
            null,
            null,
            null
        )

        with(cursor){
            while(moveToNext()){
                val id = getLong(getColumnIndexOrThrow(BaseColumns._ID))
                val name = getString(getColumnIndexOrThrow(ContractObject.Recipe_Table.COLUMN_NAME_NAME))
                var recipe = Recipe(id)
                recipe.name = name
                getIngredientsForRecipeDB(db, id, recipe)
                getStepsForRecipe(db, id, recipe)
                recipeList.value!!.add(recipe)
            }
        }
    }

    private fun getIngredientsForRecipeDB(db: SQLiteDatabase, recipeID: Long, recipe: Recipe){
        val selectRawQuery = "Select i.${ContractObject.Ingredient_Table.COLUMN_NAME_NAME}," +
                " if.${ContractObject.Ingredients_For_Recipe_Table.COLUMN_NAME_AMOUNT}," +
                " if.${ContractObject.Ingredients_For_Recipe_Table.COLUMN_NAME_MEASUREMENT}," +
                " if.${BaseColumns._ID}" +
                " from ${ContractObject.Ingredient_Table.TABLE_NAME} i inner join" +
                " ${ContractObject.Ingredients_For_Recipe_Table.TABLE_NAME} if" +
                " on i.${BaseColumns._ID} = if.${ContractObject.Ingredients_For_Recipe_Table.COLUMN_NAME_INGREDIENT_FOREIGN_KEY}" +
                " where if.${ContractObject.Ingredients_For_Recipe_Table.COLUMN_NAME_RECIPE_FOREIGN_KEY} = ?"
        val selection_args = arrayOf(recipeID.toString())

        val cursor = db.rawQuery(selectRawQuery, selection_args)
        with(cursor){
            while(moveToNext()){
                var ingredient = Ingredients()
                ingredient.ingredientForID = getLong(getColumnIndexOrThrow(BaseColumns._ID))
                ingredient.name = getString(getColumnIndexOrThrow(ContractObject.Ingredient_Table.COLUMN_NAME_NAME))
                ingredient.amount = getFloat(getColumnIndexOrThrow(ContractObject.Ingredients_For_Recipe_Table.COLUMN_NAME_AMOUNT))
                ingredient.measurement = Measurement.valueOf(getString(getColumnIndexOrThrow(ContractObject.Ingredients_For_Recipe_Table.COLUMN_NAME_MEASUREMENT)))
                recipe.ingredients.add(ingredient)
            }
        }
    }

    private fun getStepsForRecipe(db: SQLiteDatabase, recipeID: Long, recipe: Recipe){
        val projection = arrayOf(ContractObject.Steps_Table.COLUMN_NAME_STEP_DIRECTIONS, ContractObject.Steps_Table.COLUMN_NAME_STEP_NUM)
        val selection = "${ContractObject.Steps_Table.COLUMN_NAME_RECIPE_FOREIGN_KEY} = ?"
        val selection_args = arrayOf(recipeID.toString())
        val orderBy = "${ContractObject.Steps_Table.COLUMN_NAME_STEP_NUM} ASC"

        val cursor = db.query(
            ContractObject.Steps_Table.TABLE_NAME,
            projection,
            selection,
            selection_args,
            null,
            null,
            orderBy,
            null
        )

        with(cursor){
            while(moveToNext()){
                val step = getString(getColumnIndexOrThrow(ContractObject.Steps_Table.COLUMN_NAME_STEP_DIRECTIONS))
                val stepNum = getInt(getColumnIndexOrThrow(ContractObject.Steps_Table.COLUMN_NAME_STEP_NUM))
                recipe.steps.add(stepNum, step)
            }
        }
    }

    private fun removeIngredientsFor(recipe: Recipe, db: SQLiteDatabase){
        val selection = "${ContractObject.Ingredients_For_Recipe_Table.COLUMN_NAME_RECIPE_FOREIGN_KEY} LIKE ?"
        val selectionArgs = arrayOf(recipe.id.toString())

        db.delete(ContractObject.Ingredients_For_Recipe_Table.TABLE_NAME, selection, selectionArgs)
    }

    fun getRecipeByID(id: Long): Recipe?{
        if(recipeList.value == null) return null
        for(recipe in recipeList.value!!){
            if(recipe.id == id) return recipe
        }
        return null
    }

}