package com.example.mealprep

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.room.Room
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

//        mealDao = AppDatabase.getDatabase(this).mealDao()
//        ingredientDao = AppDatabase.getDatabase(this).ingredientDao()
        val db = Room.databaseBuilder(this,AppDatabase::class.java, "mealDatabase").allowMainThreadQueries().build()
        val mealDao = db.mealDao()
        val ingredientDao = db.ingredientDao()


        searchIngBtn.setOnClickListener {
            val intent = Intent(this,SearchMealsIngredientActivity::class.java)
            startActivity(intent)
        }
//        addDbBtn.setOnClickListener {
//            Toast.makeText(this, "Data Saved!", Toast.LENGTH_SHORT).show()
//
//            runBlocking {
//                launch {
//                    mealDao.deleteAll()
//                    ingredientDao.deleteAll()
//                    val mealId = Random.nextInt(1,100)
//                    val meal = Meal(id = mealId,
//                        Meal = "Sweet and Sour Pork",
//                        DrinkAlternate = null ,
//                        Category = "Pork",
//                        Area = "Chinese",
//                        Instuctions ="Preparation\r\n1. Crack the egg into a bowl. Separate the egg white and yolk.\r\n\r\nSweet and Sour Pork\r\n2. Slice the pork tenderloin into ips.\r\n\r\n3. Prepare the marinade using a pinch of salt, one teaspoon of starch, two teaspoons of light soy sauce, and an egg white.\r\n\r\n4. Marinade the pork ips for about 20 minutes.\r\n\r\n5. Put the remaining starch in a bowl. Add some water and vinegar to make a starchy sauce.\r\n\r\nSweet and Sour Pork\r\nCooking Inuctions\r\n1. Pour the cooking oil into a wok and heat to 190\u00b0C (375\u00b0F). Add the marinated pork ips and fry them until they turn brown. Remove the cooked pork from the wok and place on a plate.\r\n\r\n2. Leave some oil in the wok. Put the tomato sauce and white sugar into the wok, and heat until the oil and sauce are fully combined.\r\n\r\n3. Add some water to the wok and thoroughly heat the sweet and sour sauce before adding the pork ips to it.\r\n\r\n4. Pour in the starchy sauce. Stir-fry all the ingredients until the pork and sauce are thoroughly mixed together.\r\n\r\n5. Serve on a plate and add some coriander for decoration." ,
//                        MealThumb = "https://www.themealdb.com/images/media/meals/1529442316.jpg",
//                        Tags = "Sweet",
//                        Youtube = "https://www.youtube.com/watch?v=mdaBIhgEAMo",
//                        Source = null,
//                        ImageSource = null,
//                        CreativeCommonsConfirmed = null,
//                        dateModified = null
//                    )
//                    mealDao.insertMeal(meal)
//
//                    val ingredients = listOf(
//                        Ingredient(ingredientName = "Rice", measure = "White", mealId = mealId),
//                        Ingredient(ingredientName = "Onion", measure = "1", mealId = mealId),
//                        Ingredient(ingredientName = "Lime", measure = "1", mealId = mealId),
//                        Ingredient(ingredientName = "Garlic Clove", measure = "3", mealId = mealId),
//                        Ingredient(ingredientName = "Cucumber", measure = "1", mealId = mealId),
//                        Ingredient(ingredientName = "Carrots", measure = "3 oz ", mealId = mealId),
//                        Ingredient(ingredientName = "Ground Beef", measure = "1 lb", mealId = mealId),
//                        Ingredient(ingredientName = "Soy Sauce", measure = "2 oz ", mealId = mealId)
//                    )
//
//                    for (i in ingredients){
//                        ingredientDao.insertIngredient(i)
//                    }
//                    val mealWithIngredients = mealDao.getMealWithIngredients(mealId)
//
//                    val mealName = mealWithIngredients.meal.Meal
//                    val ingredientList = mealWithIngredients.ingredients.joinToString("\n") {
//                        "${it.ingredientName} - ${it.measure}"
//                    }
//
//                    val mealDetails = "Meal: $mealName\n\nIngredients:\n$ingredientList"
//                    Log.d("MainActivity", mealDetails)
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
//                    }
//                }
//            }
//        }
    }

}