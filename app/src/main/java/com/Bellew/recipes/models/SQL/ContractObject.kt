package com.Bellew.recipes.models.SQL

import android.provider.BaseColumns

/*
    Defines SQL Tables and relationships
 */
object ContractObject {
    object Ingredient_Table: BaseColumns{
        const val TABLE_NAME = "Ingredient_Table"
        const val COLUMN_NAME_NAME = "name"
        const val COLUMN_NAME_TYPE = "type"
        const val COLUMN_NAME_CALORIES = "calories"
        const val COLUMN_NAME_CALORIES_PER_AMOUNT = "calories_per_amount"
        const val COLUMN_NAME_CALORIES_PER_MEASUREMENT = "calories_per_measurement"

        const val SQL_CREATE_TABLE =
            "CREATE TABLE ${TABLE_NAME} (" +
                    "${BaseColumns._ID} INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "${COLUMN_NAME_NAME} TEXT," +
                    "${COLUMN_NAME_TYPE} TEXT," +
                    "${COLUMN_NAME_CALORIES} REAL," +
                    "${COLUMN_NAME_CALORIES_PER_AMOUNT} REAL," +
                    "${COLUMN_NAME_CALORIES_PER_MEASUREMENT} TEXT)"

        const val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS ${Ingredient_Table.TABLE_NAME}"
    }

    object Recipe_Table: BaseColumns{
        const val TABLE_NAME = "Recipe_Table"
        const val COLUMN_NAME_NAME = "name"
        const val COLUMN_NAME_SERVINGS = "servings"
        const val COLUMN_NAME_TYPE = "type"

        const val SQL_CREATE_TABLE =
            "CREATE TABLE ${TABLE_NAME}(" +
                    "${BaseColumns._ID} INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "${COLUMN_NAME_NAME} TEXT," +
                    "${COLUMN_NAME_TYPE} TEXT," +
                    "${COLUMN_NAME_SERVINGS} INTEGER)"

        const val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS ${TABLE_NAME}"
    }

    object Steps_Table: BaseColumns{
        const val TABLE_NAME = "Steps_Table"
        const val COLUMN_NAME_RECIPE_FOREIGN_KEY = "recipeFK"
        const val COLUMN_NAME_STEP_DIRECTIONS = "directions"
        const val COLUMN_NAME_STEP_NUM = "step_number"

        const val SQL_CREATE_TABLE =
            "CREATE TABLE ${TABLE_NAME}(" +
                    "${BaseColumns._ID} INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "${COLUMN_NAME_STEP_DIRECTIONS} TEXT," +
                    "${COLUMN_NAME_STEP_NUM} INTEGER," +
                    "${COLUMN_NAME_RECIPE_FOREIGN_KEY} INTEGER," +
                    "FOREIGN KEY(${COLUMN_NAME_RECIPE_FOREIGN_KEY}) REFERENCES ${Recipe_Table.TABLE_NAME}(${BaseColumns._ID}))"

        const val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS ${TABLE_NAME}"
    }

    object Ingredients_For_Recipe_Table: BaseColumns{
        const val TABLE_NAME = "ingredients_for_recipe"
        const val COLUMN_NAME_RECIPE_FOREIGN_KEY = "recipeFK"
        const val COLUMN_NAME_INGREDIENT_FOREIGN_KEY = "ingredientFK"
        const val COLUMN_NAME_AMOUNT = "amount"
        const val COLUMN_NAME_MEASUREMENT = "measurement"

        const val SQL_CREATE_TABLE =
            "CREATE TABLE ${TABLE_NAME}(" +
                    "${BaseColumns._ID} INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "${COLUMN_NAME_AMOUNT} REAL," +
                    "${COLUMN_NAME_MEASUREMENT} TEXT," +
                    "${COLUMN_NAME_RECIPE_FOREIGN_KEY} INTEGER," +
                    "${COLUMN_NAME_INGREDIENT_FOREIGN_KEY} INTEGER," +
                    "FOREIGN KEY(${COLUMN_NAME_INGREDIENT_FOREIGN_KEY}) REFERENCES ${Ingredient_Table.TABLE_NAME}(${BaseColumns._ID}), " +
                    "FOREIGN KEY(${COLUMN_NAME_RECIPE_FOREIGN_KEY}) REFERENCES ${Recipe_Table.TABLE_NAME}(${BaseColumns._ID}))"

        const val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS ${TABLE_NAME}"
    }

    object Week_Plan_Table: BaseColumns{
        const val TABLE_NAME = "week_plan"
        const val COLUMN_NAME_NAME = "name"
        const val COLUMN_NAME_DATE = "date_start"

        const val SQL_CREATE_TABLE =
            "CREATE TABLE ${TABLE_NAME}(" +
                    "${BaseColumns._ID} INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "${COLUMN_NAME_NAME} TEXT," +
                    "${COLUMN_NAME_DATE} TEXT)"

        const val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS ${TABLE_NAME}"
    }

    object Recipe_For_Plan_Table: BaseColumns{
        const val TABLE_NAME = "recipe_for_plan"
        const val COLUMN_NAME_NAME = "name"
        const val COLUMN_NAME_WEEK_PLAN_FK = "week_planFK"
        const val COLUMN_NAME_RECIPE_FK = "recipeFK"
        const val COLUMN_NAME_DAY_NUM = "day_num"
        const val COLUMN_NAME_MEAL_NUM = "meal_num"

        const val SQL_CREATE_TABLE =
            "CREATE TABLE ${TABLE_NAME}(" +
                    "${BaseColumns._ID} INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "${COLUMN_NAME_NAME} TEXT," +
                    "${COLUMN_NAME_DAY_NUM} INTEGER," +
                    "${COLUMN_NAME_MEAL_NUM} INTEGER," +
                    "${COLUMN_NAME_RECIPE_FK} INTEGER," +
                    "${COLUMN_NAME_WEEK_PLAN_FK} INTEGER," +
                    "FOREIGN KEY(${COLUMN_NAME_RECIPE_FK}) REFERENCES ${Recipe_Table.TABLE_NAME}(${BaseColumns._ID})," +
                    "FOREIGN KEY(${COLUMN_NAME_WEEK_PLAN_FK}) REFERENCES ${Week_Plan_Table.TABLE_NAME}(${BaseColumns._ID}))"

        const val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS ${TABLE_NAME}"
    }

    object Shopping_Entry_For_Plan_Table: BaseColumns{
        const val TABLE_NAME = "Shopping_Entry_Table"
        const val COLUMN_NAME_WEEK_PLAN_FOREIGN_KEY = "recipeFK"
        const val COLUMN_NAME_INGREDIENT_FOREIGN_KEY = "ingredientFK"
        const val COLUMN_NAME_AMOUNT = "amount"
        const val COLUMN_NAME_MEASUREMENT = "measurement"

        const val SQL_CREATE_TABLE =
            "CREATE TABLE ${TABLE_NAME}(" +
                    "${BaseColumns._ID} INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "${COLUMN_NAME_AMOUNT} REAL," +
                    "${COLUMN_NAME_MEASUREMENT} TEXT," +
                    "${COLUMN_NAME_WEEK_PLAN_FOREIGN_KEY} INTEGER," +
                    "${COLUMN_NAME_INGREDIENT_FOREIGN_KEY} INTEGER," +
                    "FOREIGN KEY(${COLUMN_NAME_INGREDIENT_FOREIGN_KEY}) REFERENCES ${Ingredient_Table.TABLE_NAME}(${BaseColumns._ID}), " +
                    "FOREIGN KEY(${COLUMN_NAME_WEEK_PLAN_FOREIGN_KEY}) REFERENCES ${Week_Plan_Table.TABLE_NAME}(${BaseColumns._ID}))"

        const val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS ${TABLE_NAME}"
    }
}