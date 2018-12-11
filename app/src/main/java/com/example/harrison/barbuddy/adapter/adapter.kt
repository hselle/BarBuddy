package com.example.harrison.barbuddy.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.harrison.barbuddy.MainActivity
import com.example.harrison.barbuddy.R
import com.example.harrison.barbuddy.data.AppDatabase
import com.example.harrison.barbuddy.data.Ingredient
import com.example.harrison.barbuddy.touch.TouchHelperAdapter
import kotlinx.android.synthetic.main.inventory_row.view.*
import java.util.*

class Adapter : RecyclerView.Adapter<Adapter.ViewHolder>, TouchHelperAdapter {


    var ingredients = mutableListOf<Ingredient>()

    val context : Context

    constructor(context: Context, todoList: List<Ingredient>) : super() {
        this.context = context
        this.ingredients.addAll(todoList)
    }

    constructor(context: Context) : super() {
        this.context = context
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(
                R.layout.inventory_row, parent, false
        )
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return ingredients.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val ingredient = ingredients[position]

        holder.tvName.text = ingredient.ingredientName

        holder.btnDelete.setOnClickListener {
            deleteIngredient(holder.adapterPosition)
        }
    }

    private fun deleteIngredient(adapterPosition: Int) {
        Thread {
            AppDatabase.getInstance(
                    context).ingredientDAO().deleteIngredient(ingredients[adapterPosition])

            ingredients.removeAt(adapterPosition)

            (context as MainActivity).runOnUiThread {
                notifyItemRemoved(adapterPosition)
            }
        }.start()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        val tvName = itemView.tvName
        val btnDelete = itemView.floatingActionButton2
    }


    fun addIngredient(ingredient: Ingredient) {
        ingredients.add(0, ingredient)
        //notifyDataSetChanged()
        notifyItemInserted(0)
    }

    override fun onDismissed(position: Int) {
        deleteIngredient(position)
    }

    override fun onItemMoved(fromPosition: Int, toPosition: Int) {
        Collections.swap(ingredients, fromPosition, toPosition)
        notifyItemMoved(fromPosition, toPosition)
    }

    fun updateTodo(item: Ingredient, editIndex: Int) {
        ingredients[editIndex] = item
        notifyItemChanged(editIndex)
    }

}