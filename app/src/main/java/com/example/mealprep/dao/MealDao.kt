package com.example.mealprep.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.mealprep.entities.Meal

@Dao
interface MealDao {
    @Query("SELECT * FROM meal")
    fun getAllMeals(): List<Meal>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMeal(meal: Meal)
}