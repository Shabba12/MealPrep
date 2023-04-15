package com.example.mealprep

import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ImageSpan
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.mealprep.entities.Meal
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.StringBuilder
import java.net.HttpURLConnection
import java.net.URL

class SearchForMealsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_for_meals)

        val mealChar = findViewById<EditText>(R.id.mealChar)
        val searchMealBtn = findViewById<Button>(R.id.searchMealDbBtn)
        val searchForMealWebBtn = findViewById<Button>(R.id.searchForMealWebBtn)
        val retrieveSearchByChar = findViewById<TextView>(R.id.retrieveSearchByChar)

        searchForMealWebBtn.setOnClickListener {
            if (mealChar.toString() ==""){
                Toast.makeText(this,"Enter Text!",Toast.LENGTH_SHORT).show()
            }else{
                val allMeal = StringBuilder()
                val searchByMeal: String = "https://www.themealdb.com/api/json/v1/1/search.php?s="
                val mealArray = getRequest(searchByMeal,mealChar.text.toString().trim())
                if (mealArray != null) {
                    for (i in 0 until mealArray.length()){
                        val mealObj = mealArray.getJSONObject(i)
                        val keysIterator: Iterator<String> = mealObj?.keys() as Iterator<String>

                        while (keysIterator.hasNext()) {
                            val key = keysIterator.next()
                            val value: String = mealObj.getString(key)
                            allMeal.append("$key: $value\n")
                        }
                        allMeal.append("\n")

                    }
                    retrieveSearchByChar.setText(allMeal)


                }
            }
        }

        searchMealBtn.setOnClickListener {
            if (mealChar.toString() ==""){
                Toast.makeText(this,"Enter Text!",Toast.LENGTH_SHORT).show()
            }else{
                Thread{
                    try {
                        val allMeal = SpannableStringBuilder()
                        val db = AppDatabase.getDatabase(context = applicationContext)
                        // Search for meals or ingredients based on partial string matches
                        val query = mealChar.text.toString().trim() // example search query
                        val printedMeals = HashSet<Meal>()

                        // Search for meals or ingredients containing the query string
                        val mealsWithMatchingName = db.mealDao().getMealsWithNameMatching(query)
                        mealsWithMatchingName.forEach { meal ->
                            if (printedMeals.add(meal)) {
                                allMeal.append("Found meal with matching name: ${meal.mealName}\n")
                                val imageUrl = URL(meal.mealThumb)
                                val inputStream = imageUrl.openConnection().getInputStream()
                                val bitmap = BitmapFactory.decodeStream(inputStream)
                                val drawable = BitmapDrawable(resources, bitmap)
                                drawable.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
                                allMeal.setSpan(ImageSpan(drawable), allMeal.length - 1, allMeal.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                                allMeal.append("\n")


                                allMeal.append("idMeal: ${meal.id}\nstrMeal: ${meal.mealName}\nstrDrinkAlternate: ${meal.drinkAlternate}\nstrCategory: ${meal.category}\nstrArea: ${meal.area}\nstrInstructions: ${meal.instruction}\nstrMealThumb: ${meal.mealThumb}\nstrTags: ${meal.tags}\nstrYoutube: ${meal.youtube}\n")
                                val ingredients = db.ingredientDao().getIngredientsForMeal(meal.id)
                                var count = 1
                                ingredients.forEach { ingredient ->
                                    allMeal.append("strIngredient$count: ${ingredient.name}\nstrMeasure$count: ${ingredient.measure}\n")
                                    count++
                                }
                                allMeal.append("strSource: ${meal.source}\nstrImageSource: ${meal.imageSource}\nstrCreativeCommonsConfirmed: ${meal.creativeCommonsConfirmed}\ndateModified: ${meal.dateModified}\n")
                                allMeal.append("\n\n")
                            }

                        }

                        val ingredientsWithMatchingName = db.ingredientDao().getIngredientsWithNameMatching(query)
                        ingredientsWithMatchingName.forEach { ingredient ->
                            val meal = db.mealDao().getMealById(ingredient.mealId)
                            if (printedMeals.add(meal)) {
                                allMeal.append("Found meal with matching ingredient: ${meal.mealName}\n")
                                allMeal.append("idMeal: ${meal.id}\nstrMeal: ${meal.mealName}\nstrDrinkAlternate: ${meal.drinkAlternate}\nstrCategory: ${meal.category}\nstrArea: ${meal.area}\nstrInstructions: ${meal.instruction}\nstrMealThumb: ${meal.mealThumb}\nstrTags: ${meal.tags}\nstrYoutube: ${meal.youtube}\n")
                                val ingredients = db.ingredientDao().getIngredientsForMeal(meal.id)
                                var count = 1
                                ingredients.forEach { ingredient ->
//                            println("- ${ingredient.name}: ${ingredient.measure}")
                                    allMeal.append("strIngredient$count: ${ingredient.name}\nstrMeasure$count: ${ingredient.measure}\n")
                                    count++
                                }
                                allMeal.append("strSource: ${meal.source}\nstrImageSource: ${meal.imageSource}\nstrCreativeCommonsConfirmed: ${meal.creativeCommonsConfirmed}\ndateModified: ${meal.dateModified}\n")
                                allMeal.append("\n\n")
                            }

                        }
                        retrieveSearchByChar.post {
                            retrieveSearchByChar.setText(allMeal)
                        }

                    }catch (e: Exception){
                        e.printStackTrace()
                    }
                }.start()
            }
        }
    }
    private fun getRequest(refUrl: String, refName: String): JSONArray? {
        var mealsArray: JSONArray? = null
        runBlocking {
            launch {
                withContext(Dispatchers.IO){
                    try {
                        val stb = StringBuilder()
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
}