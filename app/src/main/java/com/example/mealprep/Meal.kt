package com.example.mealprep

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey



@Entity(tableName = "meals")
data class Meal(
        @PrimaryKey
        val id: Int = 0,
        val Meal: String,
        val DrinkAlternate: String?,
        val Category: String,
        val Area: String,
        val Instuctions: String,
        val MealThumb: String,
        val Tags: String,
        val Youtube: String,
        val Source: String?,
        val ImageSource: String?,
        val CreativeCommonsConfirmed: String?,
        val dateModified: String?
)