package com.example.mealprep

import androidx.room.*

@Dao
interface MealDao {
    @Query("SELECT * FROM meals")
    fun getAllMeals(): List<Meal>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMeal(meal: Meal): Long

    @Query("DELETE FROM meals")
    suspend fun deleteAll()

    @Transaction
    @Query("SELECT * FROM meals WHERE id = :mealId")
    fun getMealWithIngredients(mealId: Int): MealWithIngredients

}