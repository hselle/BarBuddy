package com.example.harrison.barbuddy.data

import android.arch.persistence.room.*

@Dao
interface IngredientDAO {

    @Query("SELECT * FROM ingredients")
    fun findAllIngredients(): List<Ingredient>

    @Insert
    fun insertIngredient(item: Ingredient) : Long

    @Delete
    fun deleteIngredient(item: Ingredient)

    @Update
    fun updateIngredient(item: Ingredient)

}