package com.example.mealprep

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [Meal::class, Ingredient::class], version = 2)
abstract class AppDatabase: RoomDatabase() {
    abstract fun mealDao(): MealDao
    abstract fun ingredientDao(): IngredientDao


}