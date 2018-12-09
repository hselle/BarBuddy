package com.example.harrison.barbuddy.data

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "ingredients")
data class Ingredient(
        @PrimaryKey(autoGenerate = true) var ingredientId: Long?,
        @ColumnInfo(name = "ingredientName") var ingredientName: String
) : Serializable