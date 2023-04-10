package com.example.mealprep

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "ingredients",
    foreignKeys = [ForeignKey(entity = Meal::class,
    parentColumns = ["id"],
    childColumns = ["mealId"],
    onDelete = ForeignKey.CASCADE)]
)
data class Ingredient(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val ingredientName: String,
    val measure: String,
    val mealId: Int
)