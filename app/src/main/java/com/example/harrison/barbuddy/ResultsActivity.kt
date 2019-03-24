package com.example.harrison.barbuddy

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.example.harrison.barbuddy.adapter.CocktailAdapter
import com.example.harrison.barbuddy.adapter.IngredientAdapter
import com.example.harrison.barbuddy.apidata.DetailResult
import com.example.harrison.barbuddy.apidata.DrinkResult
import com.example.harrison.barbuddy.apidata.Drinks734794428
import com.example.harrison.barbuddy.data.AppDatabase
import com.example.harrison.barbuddy.data.Cocktail
import com.example.harrison.barbuddy.data.Ingredient
import com.example.harrison.barbuddy.network.DrinkAPI
import kotlinx.android.synthetic.main.activity_results.*
import kotlinx.android.synthetic.main.app_bar_results.*
import kotlinx.android.synthetic.main.content_results.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ResultsActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, SearchDialog.SearchHandler {

    private lateinit var cocktailAdapter : CocktailAdapter
    private val HOST_URL = "https://www.thecocktaildb.com/"
    private var DRINKDETAILSLIST = mutableListOf<Drinks734794428>()
    private var DRINK_DICT = hashMapOf<String, List<String>>()
    private var URL_DICT = hashMapOf<String, String>()
    private var INGREDIENTS = listOf<Ingredient>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_results)
        setSupportActionBar(toolbar)

        runMakeableDrinksThread()

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
        nav_view.setNavigationItemSelectedListener(this)

        initRecyclerView()
    }

    fun runMakeableDrinksThread() {
        Thread {
            INGREDIENTS = AppDatabase.getInstance(
                    this@ResultsActivity
            ).ingredientDAO().findAllIngredients()
            createDrinkDict(INGREDIENTS)
        }.start()
    }

    fun stringifyIngredients(ingredientList: List<Ingredient>): String{
        var ingredientString = ""
        ingredientList.forEach {
            ingredientString = ingredientString.plus(it.ingredientName + ", ")
        }
        Log.w("Ingredient String: ", ingredientString)
        return ingredientString
    }

    fun createDrinkDict(ingredientList: List<Ingredient>) {
        val retrofit = Retrofit.Builder()
                .baseUrl(HOST_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        val drinkAPI = retrofit.create(DrinkAPI::class.java)
        ingredientList.forEach {
            addToDictPipeline(it.ingredientName, drinkAPI)
        }
    }

    fun addToDictPipeline(ingredient: String, drinkAPI: DrinkAPI) {
        val drinkCall = drinkAPI.getDrinks(ingredient)
        drinkCall.enqueue(object : Callback<DrinkResult> {
            override fun onFailure(call: Call<DrinkResult>, t: Throwable) {
                Log.w("API-Debug", "Fail in addToDictPipeline" + t.message)
                Toast.makeText(this@ResultsActivity, "cocktaildb gave no response: " + t.message, Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<DrinkResult>, response: Response<DrinkResult>) {
                val drinkResult = response.body()
                Log.w("API-Debug", "Got a response from cocktailDB: $drinkResult")

                drinkResult?.drinks?.forEach { i ->
                    Log.w("API-Debug", "Found Drink by Ingrediet " + i.idDrink)
                    drinkToDetailResultPipe(i.idDrink!!, drinkAPI)
                }
            }
        })
    }

    fun drinkToDetailResultPipe(drink: String, drinkAPI: DrinkAPI) {
        val drinkCall = drinkAPI.getDetailsById(drink)
        drinkCall.enqueue(object : Callback<DetailResult> {
            override fun onFailure(call: Call<DetailResult>, t: Throwable) {
                Log.w("API-Debug", "Fail in drinkToDetailResultPipe" + t.message)
                Toast.makeText(this@ResultsActivity, "cocktaildb gave no response: " + t.message, Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<DetailResult>, response: Response<DetailResult>) {
                val detailResult = response.body()
                Log.w("API-Debug", "Got a response from cocktailDB: $detailResult")

                detailResult?.drinks?.forEach { drink ->
                    Log.w("API-Debug", "Found Drink by Id" + drink.strDrink)
                    makeDictEntryPipe(drink)
                    makeDrinkUrlEntry(drink)
                }
                getMakeableDrinks()
            }
        })
    }

    fun makeDrinkUrlEntry(drink : Drinks734794428) {
        URL_DICT[drink.strDrink.toString()] = drink.strDrinkThumb.toString()
    }

    fun makeDictEntryPipe(drink: Drinks734794428) {
        DRINK_DICT[drink.strDrink.toString()] = getAllIngredients(drink)
    }

    fun getMakeableDrinks(){
        Log.w("ALL DRINKS:", DRINK_DICT.toString())
        DRINK_DICT.keys.forEach { key ->
            if (checkAllIngredientsInDrink(DRINK_DICT, key, stringifyIngredients(INGREDIENTS))) {
                cocktailAdapter.addCocktail(Cocktail(key, URL_DICT[key]))
            }
        }
    }
    private fun getDrinkDetailsByDrinkId(drinkIds: List<String>, drinkAPI: DrinkAPI) {
        if (drinkIds.isNotEmpty()) {
            getDrinkDetails(drinkIds, drinkAPI, drinkIds[0])
        }
    }

    fun getDrinkDetails(drinkIds: List<String>, drinkAPI: DrinkAPI, id: String) {
        val drinkCall = drinkAPI.getDetailsById(id)
        drinkCall.enqueue(object : Callback<DetailResult> {

            override fun onFailure(call: Call<DetailResult>, t: Throwable) {
                Log.w("API-Debug", "Fail in getDrinkDetails" + t.message)
                Toast.makeText(this@ResultsActivity, "cocktaildb gave no response: " + t.message, Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<DetailResult>, response: Response<DetailResult>) {
                val detailResult = response.body()
                Log.w("API-Debug", "Got a response from cocktailDB: $detailResult")

                detailResult?.drinks?.forEach { i ->
                    Log.w("API-Debug", "Found Drink by Id" + i.strDrink)
                    DRINKDETAILSLIST.add(i)
                }
                getDrinkDetailsByDrinkId(drinkIds.slice(1 until drinkIds.size), drinkAPI)
            }
        })
    }

    private fun checkAllIngredientsInDrink(drinkDict: HashMap<String, List<String>>, key: String, ingredientList: String): Boolean {
        drinkDict[key]!!.forEach { ingredient ->
            if (!ingredientList.contains(ingredient)) {
                return false
            }
        }

        return true
    }

    fun getAllIngredients(drink: Drinks734794428): MutableList<String> {
        var allIngredients = mutableListOf<String>()
        if (drink.strIngredient1 != "") {
            allIngredients.add(drink.strIngredient1.toString())
        }
        if (drink.strIngredient2 != "") {
            allIngredients.add(drink.strIngredient2.toString())
        }
        if (drink.strIngredient3 != "") {
            allIngredients.add(drink.strIngredient3.toString())
        }
        if (drink.strIngredient4 != "") {
            allIngredients.add(drink.strIngredient4.toString())
        }
        if (drink.strIngredient5 != "") {
            allIngredients.add(drink.strIngredient5.toString())
        }
        if (drink.strIngredient6 != "") {
            allIngredients.add(drink.strIngredient6.toString())
        }
        if (drink.strIngredient7 != "") {
            allIngredients.add(drink.strIngredient7.toString())
        }
        if (drink.strIngredient8 != "") {
            allIngredients.add(drink.strIngredient8.toString())
        }
        if (drink.strIngredient9 != "") {
            allIngredients.add(drink.strIngredient9.toString())
        }
        if (drink.strIngredient10 != "") {
            allIngredients.add(drink.strIngredient10.toString())
        }
        if (drink.strIngredient11 != "") {
            allIngredients.add(drink.strIngredient11.toString())
        }
        if (drink.strIngredient12 != "") {
            allIngredients.add(drink.strIngredient12.toString())
        }
        if (drink.strIngredient13 != "") {
            allIngredients.add(drink.strIngredient13.toString())
        }
        if (drink.strIngredient14 != "") {
            allIngredients.add(drink.strIngredient14.toString())
        }
        if (drink.strIngredient15 != "") {
            allIngredients.add(drink.strIngredient15.toString())
        }
        return allIngredients

    }


    private fun initRecyclerView() {
        Thread {
            var cocktailList = mutableListOf<Cocktail>()
            cocktailAdapter = CocktailAdapter(
                    this@ResultsActivity,
                     cocktailList
            )
            runOnUiThread {
                rvCocktails.adapter = cocktailAdapter
            }
        }.start()
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_settings -> return true
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_ingredients -> {
                val intentMain = Intent()
                intentMain.setClass(this@ResultsActivity, MainActivity::class.java)
                startActivity(intentMain)
            }
            R.id.nav_Search -> {
                showSearchDialog()
            }
            R.id.nav_about -> {
                var intent: Intent = Intent(this, AboutActivity::class.java)
                startActivity(intent)
            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
    fun showSearchDialog() {
        SearchDialog().show(supportFragmentManager,
                "TAG_CREATE")
    }
    override fun newSearch(drinkName: String) {
        var intentSearch= Intent()
        intentSearch.setClass(this@ResultsActivity, CocktailsDetailsActivity::class.java)

        intentSearch.putExtra(MainActivity.KEY_ACTIVITY_START, drinkName)

        startActivity(intentSearch)
    }
}
