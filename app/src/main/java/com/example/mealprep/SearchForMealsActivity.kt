package com.example.mealprep

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.mealprep.entities.Meal

class SearchForMealsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_for_meals)

        val mealChar = findViewById<EditText>(R.id.mealChar)
        val searchMealBtn = findViewById<Button>(R.id.searchMealBtn)
        val retrieveSearchByChar = findViewById<TextView>(R.id.retrieveSearchByChar)

        searchMealBtn.setOnClickListener {
            if (mealChar.toString() ==""){
                Toast.makeText(this,"Enter Text!",Toast.LENGTH_SHORT).show()
            }else{
                val allMeal = java.lang.StringBuilder()
                val db = AppDatabase.getDatabase(context = applicationContext)
                // Search for meals or ingredients based on partial string matches
                val query = mealChar.text.toString().trim() // example search query
                val printedMeals = HashSet<Meal>()

                // Search for meals or ingredients containing the query string
                val mealsWithMatchingName = db.mealDao().getMealsWithNameMatching(query)
                mealsWithMatchingName.forEach { meal ->
                    if (printedMeals.add(meal)) {
                        allMeal.append("Found meal with matching name: ${meal.mealName}\n")
                        allMeal.append("idMeal: ${meal.id}\nstrMeal: ${meal.mealName}\nstrDrinkAlternate: ${meal.drinkAlternate}\nstrCategory: ${meal.category}\nstrArea: ${meal.area}\nstrInstructions: ${meal.instruction}\nstrMealThumb: ${meal.mealThumb}\nstrTags: ${meal.tags}\nstrYoutube: ${meal.youtube}")
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

                val ingredientsWithMatchingName = db.ingredientDao().getIngredientsWithNameMatching(query)
                ingredientsWithMatchingName.forEach { ingredient ->
                    val meal = db.mealDao().getMealById(ingredient.mealId)
                    if (printedMeals.add(meal)) {
                        allMeal.append("Found meal with matching ingredient: ${meal.mealName}\n")
                        allMeal.append("idMeal: ${meal.id}\nstrMeal: ${meal.mealName}\nstrDrinkAlternate: ${meal.drinkAlternate}\nstrCategory: ${meal.category}\nstrArea: ${meal.area}\nstrInstructions: ${meal.instruction}\nstrMealThumb: ${meal.mealThumb}\nstrTags: ${meal.tags}\nstrYoutube: ${meal.youtube}")
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
                retrieveSearchByChar.setText(allMeal)

            }
        }
    }
}