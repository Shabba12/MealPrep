package com.example.mealprep

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface IngredientDao {
    @Query("SELECT * FROM ingredients WHERE mealId = :mealId")
    fun getIngredientsForMeal(mealId: Int): List<Ingredient>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertIngredient(ingredient: Ingredient): Long

    @Query("DELETE FROM ingredients")
    suspend fun deleteAll()
}