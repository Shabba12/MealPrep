package com.example.mealprep.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "ingredient",
    foreignKeys = [ForeignKey(
        entity = Meal::class,
        parentColumns = ["id"],
        childColumns = ["mealId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class Ingredient(
    @PrimaryKey (autoGenerate = true) val id: Int = 0,
    val name: String?,
    val measure: String?,
    val mealId: Int?
)