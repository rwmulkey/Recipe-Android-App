package com.example.recipes.models

import android.app.Application
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.icu.util.Measure
import android.provider.BaseColumns
import androidx.lifecycle.AndroidViewModel
import com.example.recipes.models.SQL.ContractObject
import com.example.recipes.models.SQL.SQLHelper

/*
    The MealPlan ViewModel keeps track of all mealPlan data. This object is in charge of database operations relating to
    mealPlans. The currentPlan, currentDay, and currentMeal vars are used to select a plan that can be referenced from
    different fragments and views.
 */
class MealPlanViewModel(application: Application) : AndroidViewModel(application){
    var currentPlan:  MealPlan
    var allPlans = mutableListOf<MealPlan>()
    var currentDay : Int? = null
    var currentMeal : Int? = null

    init {
        val dbHelper = SQLHelper(getApplication())
        val db = dbHelper.readableDatabase

        val recipeViewModel = RecipeViewModel(getApplication())

        currentPlan = MealPlan()
        getMealPlans(db)
        if(currentPlan.id == -1L) currentPlan = newMealPlan(db)

        getShoppingEntriesFor(currentPlan, db)

        db.close()
    }

    //get all mealplans from DB
    fun getMealPlans(db: SQLiteDatabase){

        val cursor = db.rawQuery("SELECT * FROM ${ContractObject.Week_Plan_Table.TABLE_NAME}", null)

        with(cursor){
            while(moveToNext()){
                var mealPlan = MealPlan()
                mealPlan.id = getLong(getColumnIndexOrThrow(BaseColumns._ID))
                getRecipesFor(mealPlan, db)
                currentPlan = mealPlan
            }
        }
    }

    //Create new MealPlan and save to DB
    private fun newMealPlan(db: SQLiteDatabase): MealPlan{
        var mealPlan = MealPlan()
        val current = java.util.Calendar.getInstance()

        val values = ContentValues().apply {
            put(ContractObject.Week_Plan_Table.COLUMN_NAME_DATE, current.toString())
        }

        mealPlan.id =  db.insert(
            ContractObject.Week_Plan_Table.TABLE_NAME,
            null,
            values
        )
        return mealPlan
    }

    //Get all recipes for a MealPlan from a DB
    private fun getRecipesFor(mealPlan: MealPlan, db: SQLiteDatabase){
        val projection = arrayOf(
            ContractObject.Recipe_For_Plan_Table.COLUMN_NAME_DAY_NUM,
            ContractObject.Recipe_For_Plan_Table.COLUMN_NAME_MEAL_NUM,
            ContractObject.Recipe_For_Plan_Table.COLUMN_NAME_RECIPE_FK)
        val selection = "${ContractObject.Recipe_For_Plan_Table.COLUMN_NAME_WEEK_PLAN_FK} = ?"
        val selectionArgs = arrayOf(mealPlan.id.toString())

        val cursor = db.query(
            ContractObject.Recipe_For_Plan_Table.TABLE_NAME,
            projection,
            selection,
            selectionArgs,
            null,
            null,
            null,
            null
        )

        with(cursor){
            while(moveToNext()){
                val meal = getInt(getColumnIndexOrThrow(ContractObject.Recipe_For_Plan_Table.COLUMN_NAME_MEAL_NUM))
                val day = getInt(getColumnIndexOrThrow(ContractObject.Recipe_For_Plan_Table.COLUMN_NAME_DAY_NUM))
                val recipeID = getLong(getColumnIndexOrThrow(ContractObject.Recipe_For_Plan_Table.COLUMN_NAME_RECIPE_FK))
                mealPlan.setRecipe(day, RecipeViewModel.getRecipeByID(recipeID), meal)
            }
        }
    }

    fun setRecipe(recipe: Recipe, day: Int, meal: Int, context: Context){
        val dbHelper = SQLHelper(context)
        val db = dbHelper.readableDatabase

        if(currentPlan.days.get(day).getMeal(meal) != null) {
            for(ingredient in recipe.ingredients) subtractOrRemoveShoppingEntry(ingredient, currentPlan.id, db)
        }

        currentPlan.setRecipe(day, recipe, meal)

        updateOrSaveRecipeFor(recipe.id, day, meal, currentPlan.id, db)
        addOrUpdateShoppingEntryTable(recipe, currentPlan.id, db)
        db.close()
        dbHelper.close()
    }

    private fun updateOrSaveRecipeFor(recipeId: Long, day: Int, meal: Int, currentPlanId: Long, db: SQLiteDatabase){
        val projection = arrayOf(BaseColumns._ID)
        val selection = "${ContractObject.Recipe_For_Plan_Table.COLUMN_NAME_WEEK_PLAN_FK} = ?" +
                " and ${ContractObject.Recipe_For_Plan_Table.COLUMN_NAME_MEAL_NUM} = ?" +
                " and ${ContractObject.Recipe_For_Plan_Table.COLUMN_NAME_DAY_NUM} = ?"
        val selection_args = arrayOf(currentPlanId.toString(), meal.toString(), day.toString())

        val cursor = db.query(
            ContractObject.Recipe_For_Plan_Table.TABLE_NAME,
            projection,
            selection,
            selection_args,
            null,
            null,
            null
        )

        with(cursor){
            while(moveToNext()){
                //there exists a recipe plan entry, update it
                updateRecipeFor(recipeId, getLong(getColumnIndexOrThrow(BaseColumns._ID)), db)
            }
        }

        //No recipe for entry matches therefore create a new one.
        saveRecipeFor(recipeId, day, meal, currentPlanId, db)
    }

    private fun updateRecipeFor(recipeId: Long, recipeForId: Long, db: SQLiteDatabase){
        val values = ContentValues().apply {
            put(ContractObject.Recipe_For_Plan_Table.COLUMN_NAME_RECIPE_FK, recipeId)
        }

        val selection = "${BaseColumns._ID} = ?"
        val selectionArgs = arrayOf(recipeForId.toString())

        db.update(
            ContractObject.Recipe_For_Plan_Table.TABLE_NAME,
            values,
            selection,
            selectionArgs
        )
    }

    private fun saveRecipeFor(recipeId: Long, day: Int, meal: Int, weekPlanId: Long, db: SQLiteDatabase){
        val values = ContentValues().apply {
            put(ContractObject.Recipe_For_Plan_Table.COLUMN_NAME_RECIPE_FK, recipeId)
            put(ContractObject.Recipe_For_Plan_Table.COLUMN_NAME_DAY_NUM, day)
            put(ContractObject.Recipe_For_Plan_Table.COLUMN_NAME_MEAL_NUM, meal)
            put(ContractObject.Recipe_For_Plan_Table.COLUMN_NAME_WEEK_PLAN_FK, weekPlanId)
        }

        db.insert(ContractObject.Recipe_For_Plan_Table.TABLE_NAME, null, values)
    }

    private fun getShoppingEntriesFor(currentPlan: MealPlan, db: SQLiteDatabase){
        val cursor = getShoppingEntryCursor(db, currentPlan.id)
        with(cursor) {
            while (moveToNext()) {
                val name = getString(getColumnIndexOrThrow(ContractObject.Ingredient_Table.COLUMN_NAME_NAME))
                val amount = getFloat(getColumnIndexOrThrow(ContractObject.Shopping_Entry_For_Plan_Table.COLUMN_NAME_AMOUNT))
                val measurement = getString(getColumnIndexOrThrow(ContractObject.Shopping_Entry_For_Plan_Table.COLUMN_NAME_MEASUREMENT))
                currentPlan.shopList.addOrUpdateList(Ingredients(name, "", Measurement.valueOf(measurement), amount))
            }
        }
    }

    private fun addOrUpdateShoppingEntryTable(recipe: Recipe, currentPlanId: Long, db: SQLiteDatabase){
        for(ingredient in recipe.ingredients) {
            val cursor = getShoppingEntryCursor(db, currentPlanId)
            var isNew = false
            with(cursor) {
                while (moveToNext()) {
                    val name = getString(getColumnIndexOrThrow(ContractObject.Ingredient_Table.COLUMN_NAME_NAME))
                    if(ingredient.name == name){
                        val amount = getFloat(getColumnIndexOrThrow(ContractObject.Shopping_Entry_For_Plan_Table.COLUMN_NAME_AMOUNT))
                        val measurement = getString(getColumnIndexOrThrow(ContractObject.Shopping_Entry_For_Plan_Table.COLUMN_NAME_MEASUREMENT))
                        val id = getLong(getColumnIndexOrThrow(BaseColumns._ID))
                        val newAmount = ingredient.amount + Measurement.convert(ingredient.measurement, Measurement.valueOf(measurement), amount)
                        updateShoppingEntry(id, newAmount, measurement, db)
                        isNew = true
                        break
                    }
                }
            }
            if(isNew){
                //add new shopping entry to DB table
                addShoppingEntry(ingredient, currentPlanId, db)
            }
        }
    }

    private fun getShoppingEntryCursor(db: SQLiteDatabase, currentPlanId: Long): Cursor{
        val selectRawQuery = "Select i.${ContractObject.Ingredient_Table.COLUMN_NAME_NAME}," +
                " se.${ContractObject.Shopping_Entry_For_Plan_Table.COLUMN_NAME_AMOUNT}," +
                " se.${ContractObject.Shopping_Entry_For_Plan_Table.COLUMN_NAME_MEASUREMENT}," +
                " se.${BaseColumns._ID}" +
                " from ${ContractObject.Ingredient_Table.TABLE_NAME} i inner join" +
                " ${ContractObject.Shopping_Entry_For_Plan_Table.TABLE_NAME} se" +
                " on i.${BaseColumns._ID} = se.${ContractObject.Shopping_Entry_For_Plan_Table.COLUMN_NAME_INGREDIENT_FOREIGN_KEY}" +
                " where se.${ContractObject.Shopping_Entry_For_Plan_Table.COLUMN_NAME_WEEK_PLAN_FOREIGN_KEY} = ?"
        val selectionArgs = arrayOf(currentPlanId.toString())

        return db.rawQuery(selectRawQuery, selectionArgs)
    }

    private fun updateShoppingEntry(id: Long, amount: Float, measurement: String, db: SQLiteDatabase){
        val values = ContentValues().apply {
            put(ContractObject.Shopping_Entry_For_Plan_Table.COLUMN_NAME_AMOUNT, amount)
        }

        val selection = "${BaseColumns._ID} = ?"
        val selectionArgs = arrayOf(id.toString())

        db.update(
            ContractObject.Shopping_Entry_For_Plan_Table.TABLE_NAME,
            values,
            selection,
            selectionArgs
        )
    }

    private fun addShoppingEntry(ingredient: Ingredients, currentPlanId: Long, db: SQLiteDatabase){
        val ingredientId = RecipeViewModel.addIfNotExistsIngredientDB(ingredient, db)

        val values = ContentValues().apply {
            put(ContractObject.Shopping_Entry_For_Plan_Table.COLUMN_NAME_INGREDIENT_FOREIGN_KEY, ingredientId)
            put(ContractObject.Shopping_Entry_For_Plan_Table.COLUMN_NAME_WEEK_PLAN_FOREIGN_KEY, currentPlanId)
            put(ContractObject.Shopping_Entry_For_Plan_Table.COLUMN_NAME_MEASUREMENT, ingredient.measurement.name)
            put(ContractObject.Shopping_Entry_For_Plan_Table.COLUMN_NAME_AMOUNT, ingredient.amount)
        }

        db.insert(ContractObject.Shopping_Entry_For_Plan_Table.TABLE_NAME, null, values)
    }

    private fun subtractOrRemoveShoppingEntry(ingredient: Ingredients, currentPlanId: Long, db: SQLiteDatabase) {
        val cursor = getShoppingEntryCursor(db, currentPlanId)

        with(cursor) {
            while (moveToNext()) {
                val id = getLong(getColumnIndexOrThrow(BaseColumns._ID))
                val measurement =
                    Measurement.valueOf(getString(getColumnIndexOrThrow(ContractObject.Ingredient_Table.COLUMN_NAME_NAME)))
                val OGAmount =
                    getFloat(getColumnIndexOrThrow(ContractObject.Shopping_Entry_For_Plan_Table.COLUMN_NAME_AMOUNT))
                val newAmount = OGAmount - Measurement.convert(ingredient.measurement, measurement, ingredient.amount)
                if (newAmount > 0) {
                    updateShoppingEntry(id, newAmount, measurement.name, db)
                } else {
                    removeShoppingEntry(id, db)
                }
            }
        }
    }

    private fun removeShoppingEntry(shoppingEntryId: Long, db: SQLiteDatabase){
        val selection = "${BaseColumns._ID} LIKE ?"
        val selectionArgs = arrayOf(shoppingEntryId.toString())
        db.delete(ContractObject.Shopping_Entry_For_Plan_Table.TABLE_NAME, selection, selectionArgs)
    }
}