package com.Bellew.recipes.models

import androidx.lifecycle.ViewModel
import com.Bellew.recipes.activities.ShoppingListFragment

class ShoppingListViewModel: ViewModel() {
    var currentList: ShoppingListFragment? = null
    var lists: MutableList<ShoppingListFragment> = mutableListOf()


}