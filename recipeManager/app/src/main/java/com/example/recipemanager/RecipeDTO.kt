package com.example.recipemanager

import kotlinx.serialization.Serializable

@Serializable
data class RecipeDTO(
        var img: String? = null,
        var name: String? = null,
        var category: String? = "미분류",
        var ingredient: String? = null,
        var recipe: String? = null
)