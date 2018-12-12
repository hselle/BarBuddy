package com.example.harrison.barbuddy

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.design.widget.Snackbar
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.util.Log
import android.widget.Toast
import android.view.Menu
import android.view.MenuItem
import com.example.harrison.barbuddy.apidata.DetailResult
import com.example.harrison.barbuddy.apidata.Drinks734794428
import com.example.harrison.barbuddy.network.DrinkAPI
import kotlinx.android.synthetic.main.activity_cocktails_details.*
import kotlinx.android.synthetic.main.app_bar_activity_cocktail_details.*
import kotlinx.android.synthetic.main.cocktail_details_row.view.*
import kotlinx.android.synthetic.main.content_cocktail_details.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CocktailsDetailsActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cocktails_details)
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)


        val HOST_URL = "https://www.thecocktaildb.com/"
        val cocktailName = "Moscow Mule"
//        if (intent.hasExtra(MainActivity.CocktailDeatailName)) {
//            val cocktailName = intent.getStringExtra(MainActivity.CocktailDetailName)
//        }

        val retrofit = Retrofit.Builder()
                .baseUrl(HOST_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        getCocktailDetails(retrofit, cocktailName)
    }

    private fun getCocktailDetails(retrofit: Retrofit, cocktailName: String) {
        val drinkAPI = retrofit.create(DrinkAPI::class.java)

        val drinkCall = drinkAPI.getDetailsByName(cocktailName)
        drinkCall.enqueue(object : Callback<DetailResult> {
            override fun onFailure(call: Call<DetailResult>, t: Throwable) {
                Log.w("Debgg", "Fail in CocktailsActivity" + t.message)
                Toast.makeText(this@CocktailsDetailsActivity, "No response from api", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<DetailResult>, response: Response<DetailResult>) {
                val detailResult = response.body()
                val drink = detailResult?.drinks?.get(0)
                Log.w("Debgg", "SUCESS")

                tvName.text = drink?.strDrink ?: "ERROR"
                val ingredientsDict = getIngredientsAndAmounts(drink!!)
                ingredientsDict.keys.forEach { ingredient ->
                    val viewIngredient = layoutInflater.inflate(
                            R.layout.cocktail_details_row, null, false
                    )

                    viewIngredient.tvIngredientName.text = ingredient
                    viewIngredient.tvAmount.text = ingredientsDict[ingredient]

                    cocktails.addView(viewIngredient)
                }

            }
        })
    }

    fun getRecipe(drink: Drinks734794428): String? {
         return drink?.strInstructions
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

        }
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    fun getIngredientsAndAmounts(drink: Drinks734794428): HashMap<String, String> {
        var allIngredietns = hashMapOf<String, String>()
        if (drink.strIngredient1 != "") {
            allIngredietns[drink.strIngredient1.toString()] = drink.strMeasure1.toString()
        }
        if (drink.strIngredient2 != "") {
            allIngredietns[drink.strIngredient2.toString()] = drink.strMeasure2.toString()
        }
        if (drink.strIngredient3 != "") {
            allIngredietns[drink.strIngredient3.toString()] = drink.strMeasure3.toString()
        }
        if (drink.strIngredient4 != "") {
            allIngredietns[drink.strIngredient4.toString()] = drink.strMeasure4.toString()
        }
        if (drink.strIngredient5 != "") {
            allIngredietns[drink.strIngredient5.toString()] = drink.strMeasure5.toString()
        }
        if (drink.strIngredient6 != "") {
            allIngredietns[drink.strIngredient6.toString()] = drink.strMeasure6.toString()
        }
        if (drink.strIngredient7 != "") {
            allIngredietns[drink.strIngredient7.toString()] = drink.strMeasure7.toString()
        }
        if (drink.strIngredient8 != "") {
            allIngredietns[drink.strIngredient8.toString()] = drink.strMeasure8.toString()
        }
        if (drink.strIngredient9 != "") {
            allIngredietns[drink.strIngredient9.toString()] = drink.strMeasure9.toString()
        }
        if (drink.strIngredient10 != "") {
            allIngredietns[drink.strIngredient10.toString()] = drink.strMeasure10.toString()
        }
        if (drink.strIngredient11 != "") {
            allIngredietns[drink.strIngredient11.toString()] = drink.strMeasure11.toString()
        }
        if (drink.strIngredient12 != "") {
            allIngredietns[drink.strIngredient12.toString()] = drink.strMeasure12.toString()
        }
        if (drink.strIngredient13 != "") {
            allIngredietns[drink.strIngredient13.toString()] = drink.strMeasure13.toString()
        }
        if (drink.strIngredient14 != "") {
            allIngredietns[drink.strIngredient14.toString()] = drink.strMeasure14.toString()
        }
        if (drink.strIngredient15 != "") {
            allIngredietns[drink.strIngredient15.toString()] = drink.strMeasure15.toString()
        }

        return allIngredietns

    }
}

