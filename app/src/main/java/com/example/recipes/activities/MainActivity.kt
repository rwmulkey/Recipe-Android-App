package com.example.recipes.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.ActionBar
import androidx.fragment.app.Fragment
import com.example.recipes.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {


    private val bottomNavigationSelectedItemListener = BottomNavigationView.OnNavigationItemSelectedListener { selection ->
        when (selection.itemId){
            R.id.Recipes -> {
                val recipeFragment = RecipesFragment.newInstance()
                openFragment(recipeFragment)
                return@OnNavigationItemSelectedListener true
            }

            R.id.MealPlan -> {
                val mealPlanFragment = MealPlanFragment.newInstance()
                openFragment(mealPlanFragment)
                return@OnNavigationItemSelectedListener true
            }

            R.id.ShoppingList -> {
                val shoppingListFragment = ShoppingListFragment.newInstance()
                openFragment(shoppingListFragment)
                return@OnNavigationItemSelectedListener true
            }

//            R.id.Account -> {
//                val accountFragment = Account.newInstance()
//                openFragment(accountFragment)
//                return@OnNavigationItemSelectedListener true
//            }
        }
        false
    }

    //creates and opens a fragment to put in the "container" frameLayout
    private fun openFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE)

        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)


        setContentView(R.layout.activity_main)

        //add bottom navigation menu
        val bottomNavigation: BottomNavigationView = findViewById(R.id.navigationView)
        bottomNavigation.setOnNavigationItemSelectedListener(bottomNavigationSelectedItemListener)
        bottomNavigation.setSelectedItemId(R.id.MealPlan)

        //default fragment
        val mealPlanFragment = MealPlanFragment.newInstance()
        openFragment(mealPlanFragment)
    }
}
