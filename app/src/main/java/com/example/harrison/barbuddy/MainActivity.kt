package com.example.harrison.barbuddy

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.design.widget.NavigationView
import android.support.v4.widget.DrawerLayout
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.Menu
import android.view.MenuItem
import android.util.Log
import android.widget.Toast
import com.example.harrison.barbuddy.adapter.Adapter
import com.example.harrison.barbuddy.apidata.DetailResult
import com.example.harrison.barbuddy.apidata.DrinkResult
import com.example.harrison.barbuddy.apidata.Drinks734794428
import com.example.harrison.barbuddy.data.AppDatabase
import com.example.harrison.barbuddy.data.Ingredient
import com.example.harrison.barbuddy.network.DrinkAPI
import com.example.harrison.barbuddy.touch.TouchHelperCallback
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, AddIngredientDialog.IngredientHandler {
    private val HOST_URL = "https://www.thecocktaildb.com/"
    private var DRINKIDLIST = mutableListOf<String>()
    private var INGREDIENT_LIST = mutableListOf<String>()
    private var DRINKDETAILSLIST = mutableListOf<Drinks734794428>()
    private var DRINK_DICT = hashMapOf<String, List<String>>()

    private lateinit var ingredientAdapter : Adapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        INGREDIENT_LIST.add("Wild Turkey")
        INGREDIENT_LIST.add("Amaretto")
        INGREDIENT_LIST.add("Scotch")

        INGREDIENT_LIST.add("Pineapple juice")


        val retrofit = Retrofit.Builder()
                .baseUrl(HOST_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        val drinkAPI = retrofit.create(DrinkAPI::class.java)

//        btnRates.setOnClickListener {
//            val makeables = getMakeableDrinks()
//            Log.w("Hi im daisy", makeables.toString())
//            Toast.makeText(this@MainActivity, "BUTTON RECIEVED", Toast.LENGTH_LONG).show()
//            tvResult.text = makeables.toString()
//        }
//        Thread {
//            createDrinkDict(INGREDIENT_LIST, drinkAPI)
//        }.start()

        fab.setOnClickListener { view ->
            showAddIngredientDialog()
        }

        initRecyclerView()


        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)
    }

    private fun initRecyclerView() {
        Thread {
            val todoList = AppDatabase.getInstance(
                    this@MainActivity
            ).ingredientDAO().findAllIngredients()

            ingredientAdapter = Adapter(
                    this@MainActivity,
                    todoList
            )

            runOnUiThread {
                rvInventory.adapter = ingredientAdapter

                val callback = TouchHelperCallback(ingredientAdapter)
                val touchHelper = ItemTouchHelper(callback)
                touchHelper.attachToRecyclerView(rvInventory)
            }
        }.start()
    }


    private fun showAddIngredientDialog() {
        AddIngredientDialog().show(supportFragmentManager,
                "TAG_CREATE")
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
        // about
        // what can I make...
        // search tab
        when (item.itemId) {
            R.id.nav_make -> {
                // Handle the camera action
            }
            R.id.nav_ingredients -> {

            }
            R.id.nav_Search -> {

            }
            R.id.nav_about -> {
                var intent: Intent = Intent(this, AboutActivity::class.java)
                startActivity(intent)
            }

        }
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }


    private fun createDrinkDict(ingredientList: List<String>, drinkAPI: DrinkAPI) {
        ingredientList.forEach {
            addToDictPipeline(it, drinkAPI)
        }
    }

    fun addToDictPipeline(ingredient: String, drinkAPI: DrinkAPI) {
        val drinkCall = drinkAPI.getDrinks(ingredient)
        drinkCall.enqueue(object : Callback<DrinkResult> {
            override fun onFailure(call: Call<DrinkResult>, t: Throwable) {
                Log.w("Debgg", "Fail" + t.message)
            }

            override fun onResponse(call: Call<DrinkResult>, response: Response<DrinkResult>) {
                val drinkResult = response.body()
                Log.w("Debgg", "SUCESS")

                drinkResult?.drinks?.forEach { i ->
                    Log.w("Debgg", "Found Drink by Ingrediet " + i.idDrink)
                    drinkToDetailResultPipe(i.idDrink!!, drinkAPI)
                }
            }
        })
    }

    fun drinkToDetailResultPipe(drink: String, drinkAPI: DrinkAPI) {
        val drinkCall = drinkAPI.getDetailsById(drink)
        drinkCall.enqueue(object : Callback<DetailResult> {
            override fun onFailure(call: Call<DetailResult>, t: Throwable) {
                Log.w("Debgg", "Fail" + t.message)

            }

            override fun onResponse(call: Call<DetailResult>, response: Response<DetailResult>) {
                val detailResult = response.body()
                Log.w("Debgg", "SUCESS")

                detailResult?.drinks?.forEach { drink ->
                    Log.w("Debgg", "Found Drink by Id" + drink.strDrink)
                    makeDictEntryPipe(drink)
                }
            }
        })
    }

    fun makeDictEntryPipe(drink: Drinks734794428) {
        DRINK_DICT[drink.strDrink.toString()] = getAllIngredients(drink)
        Log.w("Createddbg", "Made drink Dickt entry: " + DRINK_DICT[drink.strDrink.toString()])
    }

    fun getMakeableDrinks(): List<String> {
        val makeableDrinkList = mutableListOf<String>()

        DRINK_DICT.keys.forEach { key ->
            if (checkAllIngredientsInDrink(DRINK_DICT, key, INGREDIENT_LIST)) {
                makeableDrinkList.add(key)
            }
        }
        return makeableDrinkList
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
                Log.w("Debgg", "Fail" + t.message)
                //tvResult.text = t.message
            }

            override fun onResponse(call: Call<DetailResult>, response: Response<DetailResult>) {
                val detailResult = response.body()
                Log.w("Debgg", "SUCESS")

                detailResult?.drinks?.forEach { i ->
                    Log.w("Debgg", "Found Drink by Id" + i.strDrink)
                    DRINKDETAILSLIST.add(i)
                }
                getDrinkDetailsByDrinkId(drinkIds.slice(1 until drinkIds.size), drinkAPI)
            }
        })
    }

    private fun checkAllIngredientsInDrink(drinkDict: HashMap<String, List<String>>, key: String, drinkList: List<String>): Boolean {
        drinkDict[key]!!.forEach { ingredient ->
            if (!drinkList.contains(ingredient)) {
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

    override fun ingredientCreated(item: Ingredient) {
        Thread {
            val ingredientId = AppDatabase.getInstance(
                    this@MainActivity).ingredientDAO().insertIngredient(item)

            item.ingredientId = ingredientId

            runOnUiThread {
                ingredientAdapter.addIngredient(item)

            }
        }.start()
    }

}
