package com.example.harrison.barbuddy.network

import com.example.harrison.barbuddy.apidata.DrinkResult
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


// URL: https://api.exchangeratesapi.io/latest?base=USD
// HOST: https://api.exchangeratesapi.io
//
// PATH: /latest
//
// QUERY param separator: ?
// QUERY params: base

interface DrinkAPI {
    @GET("/api/json/v1/1/filter.php")
    fun getDrinks(@Query("i") base: String) : Call<DrinkResult>
    @GET("/api/json/v1/1/lookup.php")
    fun getDetailsById(@Query("i") base: String) : Call<DrinkResult>
}