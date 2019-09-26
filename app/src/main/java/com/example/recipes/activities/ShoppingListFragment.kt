package com.example.recipes.activities

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recipes.R
import com.example.recipes.adapters.ShoppingListRecyclerAdapter
import com.example.recipes.models.Ingredients
import com.example.recipes.models.MealPlanViewModel
import kotlinx.android.synthetic.main.fragment_shopping_list.*

/*
    The ShoppingList Fragment displays all the ingredients that need to be bought as well as the ingredients already
    owned. Each ingredient has a checkbox that when toggles moves the ingredient between owned and needed.
 */
class ShoppingListFragment : Fragment() {
    private lateinit var mealPlanModel: MealPlanViewModel
    private lateinit var neededListAdapter: ShoppingListRecyclerAdapter
    private lateinit var ownedListAdapter: ShoppingListRecyclerAdapter
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_shopping_list, container, false)

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)

        mealPlanModel = activity?.run {
            ViewModelProviders.of(this)[MealPlanViewModel::class.java]
        } ?: throw Exception("Invalid Activity")

        val neededListObserver = Observer<MutableList<Ingredients>>{ newList: MutableList<Ingredients> ->
            updateNeededList()
        }
        val ownedListObserver = Observer<MutableList<Ingredients>>{ newList: MutableList<Ingredients> ->
            updateNeededList()
        }

        mealPlanModel.currentPlan.shopList.neededList.observe(this, neededListObserver)
        mealPlanModel.currentPlan.shopList.ownedList.observe(this, ownedListObserver)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val checkedListener = { checked: Boolean, ingredient: Ingredients -> checkIngredient(checked, ingredient)}

        neededListAdapter = ShoppingListRecyclerAdapter(false, checkedListener)
        ownedListAdapter = ShoppingListRecyclerAdapter(true, checkedListener)

        neededListAdapter.currentResults = mealPlanModel.currentPlan.shopList.neededList.value!!
        ownedListAdapter.currentResults = mealPlanModel.currentPlan.shopList.ownedList.value!!

        shopping_list_needed.adapter = neededListAdapter
        shopping_list_needed.layoutManager = LinearLayoutManager(context)

        shopping_list_not_needed.adapter = ownedListAdapter
        shopping_list_not_needed.layoutManager = LinearLayoutManager(context)
    }

    companion object {
        fun newInstance(): ShoppingListFragment = ShoppingListFragment()
    }

    /*
        Let the adapters know that the data has changed and it needs to refresh
     */
    private fun updateNeededList(){
        neededListAdapter.notifyDataSetChanged()
    }

    private fun updateOwnedList(){
        ownedListAdapter.notifyDataSetChanged()
    }

    /*
        Move the ingredient between lists and refresh adapters
     */
    private fun checkIngredient(checked: Boolean, ingredients: Ingredients){
        if(checked) {
            mealPlanModel.currentPlan.shopList.moveToOwned(ingredients)
        } else {
            mealPlanModel.currentPlan.shopList.moveToNeeded(ingredients)
        }
        refreshAdapters()
    }

    private fun refreshAdapters(){
        shopping_list_needed.post{
            neededListAdapter.notifyDataSetChanged()
            ownedListAdapter.notifyDataSetChanged()
        }
    }
}
