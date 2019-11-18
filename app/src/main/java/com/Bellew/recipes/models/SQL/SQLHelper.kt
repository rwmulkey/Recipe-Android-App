package com.Bellew.recipes.models.SQL

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

/*
    creates and upgrades SQL tables.
 */
class SQLHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(ContractObject.Ingredient_Table.SQL_CREATE_TABLE)
        db.execSQL(ContractObject.Recipe_Table.SQL_CREATE_TABLE)
        db.execSQL(ContractObject.Steps_Table.SQL_CREATE_TABLE)
        db.execSQL(ContractObject.Ingredients_For_Recipe_Table.SQL_CREATE_TABLE)
        db.execSQL(ContractObject.Week_Plan_Table.SQL_CREATE_TABLE)
        db.execSQL(ContractObject.Recipe_For_Plan_Table.SQL_CREATE_TABLE)
        db.execSQL(ContractObject.Shopping_Entry_For_Plan_Table.SQL_CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, p1: Int, p2: Int) {
        db.execSQL(ContractObject.Ingredient_Table.SQL_DELETE_ENTRIES)
        db.execSQL(ContractObject.Recipe_Table.SQL_DELETE_ENTRIES)
        db.execSQL(ContractObject.Steps_Table.SQL_DELETE_ENTRIES)
        db.execSQL(ContractObject.Ingredients_For_Recipe_Table.SQL_DELETE_ENTRIES)
        db.execSQL(ContractObject.Week_Plan_Table.SQL_DELETE_ENTRIES)
        db.execSQL(ContractObject.Recipe_For_Plan_Table.SQL_DELETE_ENTRIES)
        db.execSQL(ContractObject.Shopping_Entry_For_Plan_Table.SQL_DELETE_ENTRIES)
        onCreate(db)
    }

    companion object{
        const val DATABASE_NAME = "RECIPES.db"
        const val DATABASE_VERSION = 2
    }
}