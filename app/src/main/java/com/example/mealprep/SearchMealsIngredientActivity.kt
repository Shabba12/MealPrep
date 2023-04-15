package com.example.mealprep


import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mealprep.entities.Ingredient
import com.example.mealprep.entities.Meal
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class SearchMealsIngredientActivity : AppCompatActivity() {
    private lateinit var displayMeals:TextView
    private val searchByMeal: String = "https://www.themealdb.com/api/json/v1/1/search.php?s="
    private val searchByIngredient:String ="https://www.themealdb.com/api/json/v1/1/filter.php?i="
    private lateinit var storeMealObjArray: MutableList<JSONObject?>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_meals_ingredient)

        displayMeals = findViewById(R.id.displayMealsView)
        val retrieveMeals = findViewById<Button>(R.id.retrieveBtn)
        val saveDbBtn = findViewById<Button>(R.id.saveMealsBtn)
        val ingredientView = findViewById<EditText>(R.id.ingredientView)
        storeMealObjArray = mutableListOf()

        saveDbBtn.setOnClickListener {
            runBlocking {
                launch {
                    val db = AppDatabase.getDatabase(context = applicationContext)
                    db.clearAllTables()
                    var count = 0
                    for (i in storeMealObjArray) {
                        val MealId = i?.getString("idMeal")?.toInt()
                        val Meal = i?.getString("strMeal")
//                        println(i.getString("DrinkAlternate"))
                        val DrinkAlternate = i?.getString("strDrinkAlternate")
//                        println(i.getString("Category"))
                        val Category = i?.getString("strCategory")
//                        println(i.getString("Area"))
                        val Area = i?.getString("strArea")
//                        println(i.getString("Instructions"))
                        val Instructions = i?.getString("strInstructions")
//                        println(i.getString("MealThumb"))
                        val MealThumb = i?.getString("strMealThumb")
//                        println(i.getString("Tags"))
                        val Tags = i?.getString("strTags")
//                        println(i.getString("Youtube"))
                        val Youtube = i?.getString("strYoutube")
                        val Source = i?.getString("strSource")
                        val ImageSource = i?.getString("strImageSource")
                        val CreativeCommonsConfirmed =
                            i?.getString("strCreativeCommonsConfirmed")
                        val dateModified = i?.getString("dateModified")




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

                        if (i != null) {
                            for (key in i.keys()) {
                                if (key.contains("strIngredient")) {
                                    count++ // Increment the counter
                                }
                            }
                        }
                        println(count)
                        val dupObj = i
                        for (i in 1 until count + 1) {
                            val ingredient = dupObj?.getString("strIngredient${i}")
                            val measure = dupObj?.getString("strMeasure${i}")
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
                    }
                }
            }
            Toast.makeText(this,"data saved!",Toast.LENGTH_SHORT).show()
            storeMealObjArray.clear()
        }


        retrieveMeals.setOnClickListener {
            if (ingredientView.text.toString() == ""){
                Toast.makeText(this,"Enter an ingredient!", Toast.LENGTH_SHORT).show()
            }else{
                val ingredientName = ingredientView.text.toString().trim()



                executeMeals(ingredientName)
//                getRequest(searchByIngredient,ingredientName)


            }
        }
    }




    private fun getRequest(refUrl: String, refName: String): JSONArray? {
        var mealsArray:JSONArray? = null
        runBlocking {
            launch {
                withContext(Dispatchers.IO){
                    try {
                        val stb = java.lang.StringBuilder()
                        val url_string = refUrl+refName
                        val url = URL(url_string)
                        val con: HttpURLConnection =
                            url.openConnection() as HttpURLConnection
                        val bf = BufferedReader(InputStreamReader(con.inputStream))
                        var line: String? = bf.readLine()
                        while (line != null) {
                            stb.append(line + "\n")
                            line = bf.readLine()
                        }
                        bf.close()
                        val responseString = stb.toString()
                        val json = JSONObject(responseString)
                        mealsArray = json.getJSONArray("meals")

                    }catch (e: Exception){
                        e.printStackTrace()
                    }

                }
            }
        }
        return mealsArray

    }

    private fun executeMeals(ingredientName: String) {
//        val stb = StringBuilder()
        runBlocking {
            launch {
                withContext(Dispatchers.IO) {
                    try {
//                        val stb = StringBuilder()
//                        val url_string =
//                            "https://www.themealdb.com/api/json/v1/1/filter.php?i=$ingredientName"
//                        val url = URL(url_string)
//                        val con: HttpURLConnection =
//                            url.openConnection() as HttpURLConnection
//                        val bf = BufferedReader(InputStreamReader(con.inputStream))
//                        var line: String? = bf.readLine()
//                        while (line != null) {
//                            stb.append(line + "\n")
//                            line = bf.readLine()
//                        }
//                        bf.close()
//                        val responseString = stb.toString()
//                        val json = JSONObject(responseString)
//                        val mealsArray = json.getJSONArray("meals")
                        val mealsArray = getRequest(searchByIngredient,ingredientName)
                        val allMeal = java.lang.StringBuilder()

                        if (mealsArray != null) {
                            for (i in 0 until mealsArray.length()) {
                                val mealObj = mealsArray.getJSONObject(i)
                                val mealName = mealObj.getString("strMeal")
                    //                            allMeal.append("${i + 1} $mealObj")
                    //                            println(mealName)
                    //                            allMeal.append("\n\n")

                                val singleMealArray = getRequest(searchByMeal,mealName)
                                val singleMealObj = singleMealArray?.getJSONObject(0)
                                storeMealObjArray?.add(singleMealObj)

                                val keysIterator: Iterator<String> = singleMealObj?.keys() as Iterator<String>

                                while (keysIterator.hasNext()) {
                                    val key = keysIterator.next()
                                    val value: String = singleMealObj.getString(key)
                                    allMeal.append("$key: $value\n")
                                }
                                allMeal.append("\n")
                            }
                        }
                        displayMeals.setText(allMeal)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        }
    }


}