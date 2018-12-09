package com.example.harrison.barbuddy

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.harrison.barbuddy.apidata.DetailResult
import com.example.harrison.barbuddy.apidata.DrinkResult
import com.example.harrison.barbuddy.apidata.Drinks734794428
import com.example.harrison.barbuddy.network.DrinkAPI
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    private val HOST_URL = "https://www.thecocktaildb.com/"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val drinkList = listOf("Galliano", "Ginger ale", "Ice")
        val retrofit = Retrofit.Builder()
                .baseUrl(HOST_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        val drinkAPI = retrofit.create(DrinkAPI::class.java)

        val drinkIds = getDrinkIds(drinkList, drinkAPI)
        val drinkDetailsList = getDrinkDetailsList(drinkIds, drinkAPI)
        val drinkDict = makeDrinkDictionary(drinkDetailsList)
        var makeableDrinkList = mutableListOf<String>()
        drinkDict.keys.forEach { key ->
            if(checkAllIngredientsInDrink(drinkDict, key, drinkList)){
                makeableDrinkList.add(key)
            }
        }
        Log.w("Debgg", makeableDrinkList.toString())
    }

    private fun checkAllIngredientsInDrink(drinkDict: HashMap<String, List<String>>, key: String, drinkList: List<String>): Boolean {
        drinkDict.get(key)!!.forEach { ingredient ->
            if (!drinkList.contains(ingredient)) {
                return false
            }
        }
        return true
    }


    fun getDrinkIds(drinkList: List<String>, drinkAPI: DrinkAPI ): MutableList<String>{
        var drinkIdList = mutableListOf<String>()
        drinkList.forEach{
            val drinkCall = drinkAPI.getDrinks(it)
            drinkCall.enqueue(object : Callback<DrinkResult> {
                override fun onFailure(call: Call<DrinkResult>, t: Throwable) {
                    tvResult.text = t.message
                }

                override fun onResponse(call: Call<DrinkResult>, response: Response<DrinkResult>) {
                    val drinkResult = response.body()
                    drinkResult?.drinks?.forEach { i ->
                        drinkIdList.add(i.idDrink!!.toString())
                    }
                }
            })
        }
        return drinkIdList
    }
    fun getAllIngredients(drink: Drinks734794428): MutableList<String> {
        var allIngredietns = mutableListOf<String>()
        allIngredietns.add(drink.strIngredient1!!)
        allIngredietns.add(drink.strIngredient2!!)
        allIngredietns.add(drink.strIngredient3!!)
        allIngredietns.add(drink.strIngredient4!!)
        allIngredietns.add(drink.strIngredient5!!)
        allIngredietns.add(drink.strIngredient6!!)
        allIngredietns.add(drink.strIngredient7!!)
        allIngredietns.add(drink.strIngredient8!!)
        allIngredietns.add(drink.strIngredient9!!)
        allIngredietns.add(drink.strIngredient10!!)
        allIngredietns.add(drink.strIngredient11!!)
        allIngredietns.add(drink.strIngredient12!!)
        allIngredietns.add(drink.strIngredient13!!)
        allIngredietns.add(drink.strIngredient14!!)
        allIngredietns.add(drink.strIngredient15!!)
        return allIngredietns

    }

    private fun makeDrinkDictionary(drinkDetailsList: List<DetailResult>): HashMap<String, List<String>> {
        var drinkIngredientDict = hashMapOf<String, List<String>>()
        drinkDetailsList.forEach {
            it.drinks?.forEach {
                drinkIngredientDict.put(it.strDrink!!, getAllIngredients(it))
            }
        }
        return drinkIngredientDict
    }

    private fun getDrinkDetailsList(drinkIds: MutableList<String>, drinkAPI: DrinkAPI): List<DetailResult> {
        var drinkDetailsList = mutableListOf<DetailResult>()
        drinkIds.forEach {
            val drinkDetailsCall = drinkAPI.getDetailsById(it)
            drinkDetailsCall.enqueue(object : Callback<DetailResult> {
                override fun onFailure(call: Call<DetailResult>, t: Throwable) {
                    tvResult.text = t.message
                    Log.w("API_ERROR", "Api error in details call")
                }

                override fun onResponse(call: Call<DetailResult>, response: Response<DetailResult>) {
                    drinkDetailsList.add(response.body()!!)
                }
            })
        }
        return drinkDetailsList
    }
}
