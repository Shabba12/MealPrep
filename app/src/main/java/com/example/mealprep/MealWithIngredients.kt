package com.example.mealprep

import androidx.room.Embedded
import androidx.room.Relation

data class MealWithIngredients(
    @Embedded val meal: Meal,
    @Relation(
        parentColumn = "id",
        entityColumn = "mealId"
    )
    val ingredients: List<Ingredient>
)
