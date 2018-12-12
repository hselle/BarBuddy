package com.example.harrison.barbuddy.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.harrison.barbuddy.R
import com.example.harrison.barbuddy.ResultsActivity
import com.example.harrison.barbuddy.data.Cocktail
import kotlinx.android.synthetic.main.cocktail_row.view.*
import java.util.*

class CocktailAdapter : RecyclerView.Adapter<CocktailAdapter.ViewHolder>{


    var cocktails = mutableListOf<Cocktail>()

    val context : Context

    constructor(context: Context, cocktailList: List<Cocktail>) : super() {
        this.context = context
        this.cocktails.addAll(cocktailList)
    }

    constructor(context: Context) : super() {
        this.context = context
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(
                R.layout.cocktail_row, parent, false
        )
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return cocktails.size
    }

    override fun onBindViewHolder(holder: CocktailAdapter.ViewHolder, position: Int) {
        val cocktail = cocktails[position]

        holder.tvName.text = cocktail.name
        Glide.with(context as ResultsActivity)
                .load(cocktail.imageUrl)
                .into(holder.imageView)

    }

    fun addCocktail(cocktail: Cocktail) {
        cocktails.add(0, cocktail)
    }

    private fun deleteCocktail(position: Int) {
        cocktails.removeAt(position)

    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        val tvName = itemView.tvName
        val imageView = itemView.imageView
    }

}
