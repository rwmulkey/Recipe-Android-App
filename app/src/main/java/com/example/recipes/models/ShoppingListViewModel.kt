package com.example.recipes.models

import androidx.lifecycle.ViewModel
import com.example.recipes.activities.ShoppingListFragment

class ShoppingListViewModel: ViewModel() {
    var currentList: ShoppingListFragment? = null
    var lists: MutableList<ShoppingListFragment> = mutableListOf()


}