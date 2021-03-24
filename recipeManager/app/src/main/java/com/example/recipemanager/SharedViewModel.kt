package com.example.recipemanager

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {
    val selected = MutableLiveData<RecipeDTO>()

    fun select(item: RecipeDTO) {
        selected.value = item
    }
}