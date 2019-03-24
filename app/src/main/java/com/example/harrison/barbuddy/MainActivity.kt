package com.example.harrison.barbuddy

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.Menu
import android.view.MenuItem
import android.util.Log
import android.widget.Toast
import com.example.harrison.barbuddy.adapter.IngredientAdapter
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
import kotlinx.android.synthetic.main.dialog_search.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, AddIngredientDialog.IngredientHandler, SearchDialog.SearchHandler {

    companion object {
        val KEY_ACTIVITY_START = "KEY_ACTIVITY_START"
    }

    private lateinit var ingredientAdapter : IngredientAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            showAddIngredientDialog()
        }
        showMeTheDrinks.setOnClickListener{
            var intent = Intent(this, ResultsActivity::class.java)
            Thread{
                val ingredientList = AppDatabase.getInstance(
                        this@MainActivity
                ).ingredientDAO().findAllIngredients()
                intent.putExtra("ingredients", ingredientList.toTypedArray())
                startActivity(intent)
            }.start()

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
            val ingredientList = AppDatabase.getInstance(
                    this@MainActivity
            ).ingredientDAO().findAllIngredients()

            ingredientAdapter = IngredientAdapter(
                    this@MainActivity,
                    ingredientList
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
    fun showSearchDialog() {
        SearchDialog().show(supportFragmentManager,
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
            R.id.nav_ingredients -> {
                Toast.makeText(this@MainActivity, "This'r alreddy yur greediens", Toast.LENGTH_LONG).show()
            }
            R.id.nav_Search -> {
                showSearchDialog()
            }
            R.id.nav_about -> {
                var intent = Intent(this, AboutActivity::class.java)
                startActivity(intent)
            }

        }
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
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
    override fun newSearch(drinkName: String) {
        var intentSearch= Intent()
        intentSearch.setClass(this@MainActivity, CocktailsDetailsActivity::class.java)

        intentSearch.putExtra(KEY_ACTIVITY_START, drinkName)

        startActivity(intentSearch)
    }

}
