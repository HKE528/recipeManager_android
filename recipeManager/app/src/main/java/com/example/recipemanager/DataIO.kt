package com.example.recipemanager

import androidx.appcompat.app.AppCompatActivity

class DataIO(private val recipeDTO: RecipeDTO) : AppCompatActivity() {
    private val pref = this.getPreferences(0)

    private fun saveData() {
        val editor = pref.edit()

        editor.putString("img", recipeDTO.img)
                .putString("name", recipeDTO.name)
                .putString("category", recipeDTO.category)
                .putString("ingredient", recipeDTO.ingredient)
                .putString("recipe", recipeDTO.recipe)
                .apply()
    }

    private fun loadData() : RecipeDTO{
        return RecipeDTO(
                img = pref.getString("img", null),
                name = pref.getString("name", null),
                category = pref.getString("category", null),
                ingredient = pref.getString("ingredient", null),
                recipe = pref.getString("recipe", null)
        )
    }
}