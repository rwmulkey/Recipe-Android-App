package com.example.recipes.models

import androidx.lifecycle.MutableLiveData

class ShoppingList {
    var ownedList : MutableLiveData<MutableList<Ingredients>> = MutableLiveData(mutableListOf())
    var neededList : MutableLiveData<MutableList<Ingredients>> = MutableLiveData(mutableListOf())

    constructor(){    }

    fun addMultipleToList(ingredients: List<Ingredients>){
        for(new in ingredients){
            addOrUpdateList(new)
        }
    }

    fun addOrUpdateList(new: Ingredients){
        //if already needed update amount
        for(old in neededList.value!!){
            if(old.name == new.name){
                old.addToAmount(new.measurement, new.amount)
                return
            }
        }

        //if already owned. update amount and move to needed list
        for(old in ownedList.value!!){
            if(old.name == new.name){
                old.addToAmount(new.measurement, new.amount)
                neededList.value!!.add(old)
                ownedList.value!!.remove(old)
                return
            }
        }

        //else add to needed list
        neededList.value!!.add(Ingredients(new.name, new.type, new.measurement, new.amount))
    }

    fun removeFromList(new: Ingredients){
        for(old in neededList.value!!){
            if(old.name == new.name){
                old.removeFromAmount(new.measurement, new.amount)
                if(old.amount <= 0f){
                    neededList.value!!.remove(old)
                }
                return
            }
        }

        for(old in ownedList.value!!){
            if(old.name == new.name){
                old.removeFromAmount(new.measurement, new.amount)
                if(old.amount <= 0f) ownedList.value!!.remove(old)
                return
            }
        }
    }

    fun removeMultipleFromList(removeList: List<Ingredients>){
        for(remove in removeList){
            removeFromList(remove)
        }
    }

    fun moveToNeeded(ingredient: Ingredients){
        ownedList.value!!.remove(ingredient)
        neededList.value!!.add(ingredient)
    }

    fun moveToOwned(ingredient: Ingredients){
        neededList.value!!.remove(ingredient)
        ownedList.value!!.add(ingredient)
    }
}