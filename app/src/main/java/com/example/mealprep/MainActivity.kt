package com.example.mealprep

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.room.Room
import com.example.mealprep.entities.Ingredient
import com.example.mealprep.entities.Meal

import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.json.JSONObject
import kotlin.random.Random
import kotlin.random.nextInt

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val addDbBtn =findViewById<AppCompatButton>(R.id.addDbBtn)
        val searchIngBtn = findViewById<AppCompatButton>(R.id.searchIngBtn)



        searchIngBtn.setOnClickListener {
            val intent = Intent(this,SearchMealsIngredientActivity::class.java)
            startActivity(intent)
        }
        addDbBtn.setOnClickListener {
            Toast.makeText(this, "Data Saved!", Toast.LENGTH_SHORT).show()
            val db = AppDatabase.getDatabase(context = applicationContext)

            runBlocking {
                launch {


                    val meal = Meal(1111,"Spaghetti Bolognese", "Pasta")
                    db.mealDao().insertMeal(meal)

                    val ingredients = listOf(
                        Ingredient(name = "Spaghetti", measure = "200g", mealId = meal.id),
                        Ingredient(name = "Beef mince", measure = "500g", mealId = meal.id),
                        Ingredient(name = "Onion", measure = "1", mealId = meal.id),
                        Ingredient(name = "Garlic", measure = "2 cloves", mealId = meal.id),
                        Ingredient(name = "Tomato paste", measure = "2 tbsp", mealId = meal.id),
                        Ingredient(name = "Canned tomatoes", measure = "400g", mealId = meal.id),
                        Ingredient(name = "Olive oil", measure = "2 tbsp", mealId = meal.id),
                        Ingredient(name = "Salt", measure = "To taste", mealId = meal.id),
                        Ingredient(name = "Black pepper", measure = "To taste", mealId = meal.id)
                    )

                    ingredients.forEach{
                        ingredient -> db.ingredientDao().insertIngredient(ingredient)
                    }

//                     //Get the JSON string from the file
//                    val jsonString = application.assets.open("data.json").bufferedReader().use { it.readText() }
//
//                    // Parse the JSON string into a JSONObject
//                    val jsonObject = JSONObject(jsonString)
//
//                    val mealArray = jsonObject.getJSONArray("Meals")
//
//                    var count = 0
//                    for (i in 0 until mealArray.length()) {
//                        val mealObject = mealArray.getJSONObject(i)
////                        println(mealObject.getString("Meal"))
//                        val Meal = mealObject.getString("Meal")
////                        println(mealObject.getString("DrinkAlternate"))
//                        val DrinkAlternate = mealObject.getString("DrinkAlternate")
////                        println(mealObject.getString("Category"))
//                        val Category = mealObject.getString("Category")
////                        println(mealObject.getString("Area"))
//                        val Area = mealObject.getString("Area")
////                        println(mealObject.getString("Instructions"))
//                        val Instructions = mealObject.getString("Instructions")
////                        println(mealObject.getString("MealThumb"))
//                        val MealThumb = mealObject.getString("MealThumb")
////                        println(mealObject.getString("Tags"))
//                        val Tags = mealObject.getString("Tags")
////                        println(mealObject.getString("Youtube"))
//                        val Youtube = mealObject.getString("Youtube")
//                        val Source = mealObject.getString("Source")
//                        val ImageSource = mealObject.getString("ImageSource")
//                        val CreativeCommonsConfirmed = mealObject.getString("CreativeCommonsConfirmed")
//                        val dateModified = mealObject.getString("dateModified")
//
//                        val meal = Meal(Meal = Meal, DrinkAlternate = DrinkAlternate, Category = Category, Area = Area, Instuctions = Instructions, MealThumb = MealThumb, Tags = Tags, CreativeCommonsConfirmed = CreativeCommonsConfirmed, dateModified = dateModified, ImageSource = ImageSource, Youtube = Youtube, Source = Source)
//                        val mealId = mealDao.insertMeal(meal).toInt()
//
//                        val ingredients:MutableList<Ingredient> = mutableListOf()
//
//                        for(key in mealObject.keys()){
//                            if (key.contains("Ingredient")) {
//                                count++ // Increment the counter
//                            }
//                        }
//                        println(count)
//                        for (i in 1 until count+1){
//                            val ingredient = mealObject.getString("Ingredient${i}")
//                            val measure = mealObject.getString("strMeasure${i}")
////                            println("$ingredient - $measure")
//                            ingredients.add(Ingredient(ingredientName = ingredient, measure = measure, mealId = mealId))
//                        }
//                        for (i in ingredients){
//                            ingredientDao.insertIngredient(i)
//                        }
//                        count = 0
//
//                        println("---------------------------------------------------------------")
//                        println("---------------------------------------------------------------")
                    }
                }
            }
        }
}