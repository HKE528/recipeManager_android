package com.example.recipemanager

data class RecipeDTO(
        var img: String?,
        var name: String?,
        var category: String? = "미분류",
        var ingredient: String?,
        var recipe: String?
)