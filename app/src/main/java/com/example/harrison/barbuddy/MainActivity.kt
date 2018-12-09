package com.example.harrison.barbuddy

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.harrison.barbuddy.apidata.DrinkResult
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


        val retrofit = Retrofit.Builder()
                .baseUrl(HOST_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        val drinkAPI = retrofit.create(DrinkAPI::class.java)



        btnRates.setOnClickListener {
            val drinkCall = drinkAPI.getDrinks(etBase.text.toString())
            drinkCall.enqueue(object : Callback<DrinkResult> {

                override fun onFailure(call: Call<DrinkResult>, t: Throwable) {
                    tvResult.text = t.message
                }

                override fun onResponse(call: Call<DrinkResult>, response: Response<DrinkResult>) {
                    tvResult.text = response.body().toString()
                    val drinkResult = response.body()
                    var count: Int = 1
                    drinkResult?.drinks?.forEach { i ->
                        val drinkDescriptionCall = drinkAPI.getDetailsById(i.idDrink!!)
                        drinkDescriptionCall.enqueue(object : Callback<DrinkResult>{
                            override fun onFailure(call: Call<DrinkResult>, t: Throwable) {
                                tvResult.text = t.message
                            }

                            override fun onResponse(call: Call<DrinkResult>?, response: Response<DrinkResult>) {
                                tvResult.text = count.toString()
                                count++

                            }

                        })
                    }
            }
        })

    }


}
}
