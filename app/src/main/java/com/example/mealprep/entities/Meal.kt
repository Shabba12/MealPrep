package com.example.mealprep.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "meal")
data class Meal(
    @PrimaryKey val id: Int,
    val mealName: String,
//    val drinkAlternate: String?,
    val category: String
//    val area:String?,
//    val instruction:String?,
//    val mealThumb:String?,
//    val tags:String?,
//    val youtube:String?,
//    val source:String?,
//    val imageSource:String?,
//    val creativeCommonsConfirmed:String?,
//    val dateModified:String?,
)