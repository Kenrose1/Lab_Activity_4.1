package com.example.recipemanagementapp.model

/** The two row types shown in the single RecyclerView. */
sealed interface RecipeListItem {
    data class FlavorHeader(val flavor: Flavor) : RecipeListItem
    data class RecipeRow(val recipe: Recipe, val flavor: Flavor) : RecipeListItem
}
