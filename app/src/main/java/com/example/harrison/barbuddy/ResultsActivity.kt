package com.example.harrison.barbuddy

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.example.harrison.barbuddy.adapter.CocktailAdapter
import com.example.harrison.barbuddy.data.Cocktail
import com.example.harrison.barbuddy.network.DrinkAPI
import kotlinx.android.synthetic.main.activity_results.*
import kotlinx.android.synthetic.main.app_bar_results.*
import kotlinx.android.synthetic.main.content_results.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ResultsActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, SearchDialog.SearchHandler {

    private lateinit var cocktailAdapter : CocktailAdapter
    private val HOST_URL = "https://www.thecocktaildb.com/"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_results)
        setSupportActionBar(toolbar)


        val retrofit = Retrofit.Builder()
                .baseUrl(HOST_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        val drinkAPI = retrofit.create(DrinkAPI::class.java)

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)
        initRecyclerView()
    }


    private fun initRecyclerView() {
        Thread {
            var cocktailList = mutableListOf<Cocktail>()
            intent.getStringArrayExtra("cocktails").forEach {
                cocktailList.add(Cocktail(it, intent.getStringExtra(it)))
            }
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
