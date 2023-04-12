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

        val addDbBtn = findViewById<AppCompatButton>(R.id.addDbBtn)
        val searchIngBtn = findViewById<AppCompatButton>(R.id.searchIngBtn)
        val searchMealBtn = findViewById<Button>(R.id.searchMealsBtn)

        searchMealBtn.setOnClickListener {
            val intent = Intent(this, SearchForMealsActivity::class.java)
            startActivity(intent)
        }



        searchIngBtn.setOnClickListener {
            val intent = Intent(this, SearchMealsIngredientActivity::class.java)
            startActivity(intent)
        }
        addDbBtn.setOnClickListener {
            runBlocking {
                launch {

                    val db = AppDatabase.getDatabase(context = applicationContext)
                    db.clearAllTables()
//                    val meal = Meal(1111, "Spaghetti Bolognese", "Pasta")
//                    db.mealDao().insertMeal(meal)
//
//                    val ingredients = listOf(
//                        Ingredient(name = "Spaghetti", measure = "200g", mealId = meal.id),
//                        Ingredient(name = "Beef mince", measure = "500g", mealId = meal.id),
//                        Ingredient(name = "Onion", measure = "1", mealId = meal.id),
//                        Ingredient(name = "Garlic", measure = "2 cloves", mealId = meal.id),
//                        Ingredient(name = "Tomato paste", measure = "2 tbsp", mealId = meal.id),
//                        Ingredient(name = "Canned tomatoes", measure = "400g", mealId = meal.id),
//                        Ingredient(name = "Olive oil", measure = "2 tbsp", mealId = meal.id),
//                        Ingredient(name = "Salt", measure = "To taste", mealId = meal.id),
//                        Ingredient(name = "Black pepper", measure = "To taste", mealId = meal.id)
//                    )
//
//                    ingredients.forEach { ingredient ->
//                        db.ingredientDao().insertIngredient(ingredient)
//                    }
//
//                    val meal2 = Meal(2222, "Spaghetti", "Pasta")
//                    db.mealDao().insertMeal(meal2)
//
//                    val ingredients2 = listOf(
//                        Ingredient(name = "2Spaghetti", measure = "100g", mealId = meal2.id),
//                        Ingredient(name = "2Beef mince", measure = "100g", mealId = meal2.id),
//                        Ingredient(name = "2Onion", measure = "1", mealId = meal2.id),
//                        Ingredient(name = "2Garlic", measure = "2 cloves", mealId = meal2.id),
//                        Ingredient(name = "2Tomato paste", measure = "2 tbsp", mealId = meal2.id),
//                        Ingredient(name = "2Canned tomatoes", measure = "100g", mealId = meal2.id),
//                        Ingredient(name = "2Olive oil", measure = "2 tbsp", mealId = meal2.id),
//                        Ingredient(name = "2Salt", measure = "To taste", mealId = meal2.id),
//                        Ingredient(name = "2Black pepper", measure = "To taste", mealId = meal2.id)
//                    )
//
//                    ingredients2.forEach { ingredient ->
//                        db.ingredientDao().insertIngredient(ingredient)
//                    }
//                    val allMeals = db.mealDao().getAllMeals()
//
//                    allMeals.forEach { meal ->
//                        println("${meal.mealName} (${meal.category}):")
//                        val ingredients = db.ingredientDao().getIngredientsForMeal(meal.id)
//                        ingredients.forEach { ingredient ->
//                            println("- ${ingredient.name}: ${ingredient.measure}")
//                        }
//                    }


                     //Get the JSON string from the file
                    val jsonString = application.assets.open("data.json").bufferedReader().use { it.readText() }

                    // Parse the JSON string into a JSONObject
                    val jsonObject = JSONObject(jsonString)

                    val mealArray = jsonObject.getJSONArray("Meals")

                    var count = 0
                    for (i in 0 until mealArray.length()) {
                        val mealObject = mealArray.getJSONObject(i)
//                        println(mealObject.getString("Meal"))
                        val MealId = mealObject.getString("mealId").toInt()
                        val Meal = mealObject.getString("Meal")
//                        println(mealObject.getString("DrinkAlternate"))
                        val DrinkAlternate = mealObject.getString("DrinkAlternate")
//                        println(mealObject.getString("Category"))
                        val Category = mealObject.getString("Category")
//                        println(mealObject.getString("Area"))
                        val Area = mealObject.getString("Area")
//                        println(mealObject.getString("Instructions"))
                        val Instructions = mealObject.getString("Instructions")
//                        println(mealObject.getString("MealThumb"))
                        val MealThumb = mealObject.getString("MealThumb")
//                        println(mealObject.getString("Tags"))
                        val Tags = mealObject.getString("Tags")
//                        println(mealObject.getString("Youtube"))
                        val Youtube = mealObject.getString("Youtube")
                        val Source = mealObject.getString("Source")
                        val ImageSource = mealObject.getString("ImageSource")
                        val CreativeCommonsConfirmed =
                            mealObject.getString("CreativeCommonsConfirmed")
                        val dateModified = mealObject.getString("dateModified")

                        val meal = Meal(
                            id = MealId,
                            mealName = Meal,
                            drinkAlternate = DrinkAlternate,
                            category = Category,
                            area = Area,
                            instruction = Instructions,
                            mealThumb = MealThumb,
                            tags = Tags,
                            youtube = Youtube,
                            source = Source,
                            imageSource = ImageSource,
                            creativeCommonsConfirmed = CreativeCommonsConfirmed,
                            dateModified = dateModified
                        )
                        db.mealDao().insertMeal(meal)


                        val ingredients: MutableList<Ingredient> = mutableListOf()

                        for (key in mealObject.keys()) {
                            if (key.contains("Ingredient")) {
                                count++ // Increment the counter
                            }
                        }
                        println(count)
                        for (i in 1 until count + 1) {
                            val ingredient = mealObject.getString("Ingredient${i}")
                            val measure = mealObject.getString("strMeasure${i}")
//                            println("$ingredient - $measure")
                            ingredients.add(
                                Ingredient(
                                    name = ingredient,
                                    measure = measure,
                                    mealId = meal.id
                                )
                            )
                        }
                        for (i in ingredients) {
                            db.ingredientDao().insertIngredient(i)
                        }
                        count = 0

                        println("---------------------------------------------------------------")
                        println("---------------------------------------------------------------")
                    }
//                     //Get all meals from the database
//                    val allMeals = db.mealDao().getAllMeals()
//                    allMeals.forEach { meal ->
//                        println("${meal.name} (${meal.category}):")
//                        val ingredients = db.ingredientDao().getIngredientsForMeal(meal.id)
//                        ingredients.forEach { ingredient ->
//                            println("- ${ingredient.name}: ${ingredient.measure}")
//                        }
//                    }
//
//                    db.close()
                }
            }
        }
    }
}
