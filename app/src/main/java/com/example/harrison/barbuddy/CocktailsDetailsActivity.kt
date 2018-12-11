package com.example.harrison.barbuddy

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.harrison.barbuddy.apidata.DetailResult
import com.example.harrison.barbuddy.apidata.Drinks734794428
import com.example.harrison.barbuddy.network.DrinkAPI
import kotlinx.android.synthetic.main.activity_cocktails_details.*
import kotlinx.android.synthetic.main.cocktail_details_row.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CocktailsDetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cocktails_details)
        val HOST_URL = "https://www.thecocktaildb.com/"
        val cocktailName = "Moscow Mule"
//        if (intent.hasExtra(MainActivity.CocktailDeatailName)) {
//            val cocktailName = intent.getStringExtra(MainActivity.CocktailDetailName)
//        }

        val retrofit = Retrofit.Builder()
                .baseUrl(HOST_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        val drinkAPI = retrofit.create(DrinkAPI::class.java)

        val drinkCall = drinkAPI.getDetailsByName(cocktailName)
        drinkCall.enqueue(object : Callback<DetailResult> {
            override fun onFailure(call: Call<DetailResult>, t: Throwable) {
                Log.w("Debgg", "Fail in CocktailsActivity" + t.message)
            }

            override fun onResponse(call: Call<DetailResult>, response: Response<DetailResult>) {
                val detailResult = response.body()
                val drink = detailResult?.drinks?.get(0)
                Log.w("Debgg", "SUCESS")

                tvName.text = drink?.strDrink
                val ingredientsList = getIngredientsAndAmounts(drink!!)
                ingredientsList.forEach {ingredient ->
                    val viewIngredient = layoutInflater.inflate(
                            R.layout.cocktail_details_row, null, false
                    )
                    viewIngredient.tvIngredientName.text = ingredient[0]
                    viewIngredient.tvAmount.text = ingredient[1]

                    cocks.addView(viewIngredient)
                }

            }
        })
    }
    fun getIngredientsAndAmounts(drink: Drinks734794428): MutableList<MutableList<String>> {
        var allIngredietns: MutableList<MutableList<String>> = arrayListOf()
        if (drink.strIngredient1 != "") {
            allIngredietns[0].add(drink.strIngredient1.toString())
            allIngredietns[1].add(drink.strMeasure1.toString())
        }
        if (drink.strIngredient2 != "") {
            allIngredietns[0].add(drink.strIngredient2.toString())
            allIngredietns[1].add(drink.strMeasure2.toString())
        }
        if (drink.strIngredient3 != "") {
            allIngredietns[0].add(drink.strIngredient3.toString())
            allIngredietns[1].add(drink.strMeasure3.toString())
        }
        if (drink.strIngredient4 != "") {
            allIngredietns[0].add(drink.strIngredient4.toString())
            allIngredietns[1].add(drink.strMeasure4.toString())
        }
        if (drink.strIngredient5 != "") {
            allIngredietns[0].add(drink.strIngredient5.toString())
            allIngredietns[1].add(drink.strMeasure5.toString())
        }
        if (drink.strIngredient6 != "") {
            allIngredietns[0].add(drink.strIngredient6.toString())
            allIngredietns[1].add(drink.strMeasure1.toString())
        }
        if (drink.strIngredient6 != "") {
            allIngredietns[0].add(drink.strIngredient6.toString())
            allIngredietns[1].add(drink.strMeasure6.toString())
        }
        if (drink.strIngredient7 != "") {
            allIngredietns[0].add(drink.strIngredient7.toString())
            allIngredietns[1].add(drink.strMeasure7.toString())
        }
        if (drink.strIngredient8 != "") {
            allIngredietns[0].add(drink.strIngredient8.toString())
            allIngredietns[1].add(drink.strMeasure8.toString())
        }
        if (drink.strIngredient9 != "") {
            allIngredietns[0].add(drink.strIngredient9.toString())
            allIngredietns[1].add(drink.strMeasure9.toString())
        }
        if (drink.strIngredient10 != "") {
            allIngredietns[0].add(drink.strIngredient10.toString())
            allIngredietns[1].add(drink.strMeasure10.toString())
        }
        if (drink.strIngredient11 != "") {
            allIngredietns[0].add(drink.strIngredient11.toString())
            allIngredietns[1].add(drink.strMeasure11.toString())
        }
        if (drink.strIngredient12 != "") {
            allIngredietns[0].add(drink.strIngredient12.toString())
            allIngredietns[1].add(drink.strMeasure12.toString())
        }
        if (drink.strIngredient13 != "") {
            allIngredietns[0].add(drink.strIngredient13.toString())
            allIngredietns[1].add(drink.strMeasure13.toString())
        }
        if (drink.strIngredient14 != "") {
            allIngredietns[0].add(drink.strIngredient14.toString())
            allIngredietns[1].add(drink.strMeasure14.toString())
        }
        if (drink.strIngredient15 != "") {
            allIngredietns[0].add(drink.strIngredient15.toString())
            allIngredietns[1].add(drink.strMeasure15.toString())
        }

        return allIngredietns

    }
}

