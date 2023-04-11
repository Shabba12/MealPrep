package com.example.mealprep.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.mealprep.entities.Ingredient

@Dao
interface IngredientDao {
    @Query("SELECT * FROM ingredient WHERE mealId = :mealId")
    fun getIngredientsForMeal(mealId: Int): List<Ingredient>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertIngredient(ingredient: Ingredient):Long
}