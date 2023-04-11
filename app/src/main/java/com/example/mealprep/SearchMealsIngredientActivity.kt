package com.example.mealprep

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_meals_ingredient)

        displayMeals = findViewById<TextView>(R.id.displayMealsView)
        val retrieveMeals = findViewById<Button>(R.id.retrieveBtn)
        val saveDbBtn = findViewById<Button>(R.id.saveMealsBtn)
        val ingredientView = findViewById<EditText>(R.id.ingredientView)


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