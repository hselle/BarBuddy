package com.example.harrison.barbuddy

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.view.View
import android.widget.EditText
import com.example.harrison.barbuddy.data.Ingredient
import kotlinx.android.synthetic.main.dialog_ingredients.*
import kotlinx.android.synthetic.main.dialog_ingredients.view.*
import java.lang.RuntimeException
import java.util.*

class AddIngredientDialog : DialogFragment() {

    interface IngredientHandler {
        fun ingredientCreated(item: Ingredient)
    }

    private lateinit var ingredientHandler: IngredientHandler

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        if (context is IngredientHandler) {
            ingredientHandler = context
        } else {
            throw RuntimeException(
                    "The activity does not implement the TodoHandlerInterface")
        }
    }

    private lateinit var etIngredientName: EditText

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())

        builder.setTitle("New ingredient")

        val rootView = requireActivity().layoutInflater.inflate(
                R.layout.dialog_ingredients, null
        )
        //etTodoDate = rootView.findViewById(R.id.etTodoText)
        etIngredientName = rootView.etName
        builder.setView(rootView)


        builder.setPositiveButton("OK") { dialog, which ->
            // empty
        }

        return builder.create()
    }


    override fun onResume() {
        super.onResume()

        val positiveButton = (dialog as AlertDialog).getButton(Dialog.BUTTON_POSITIVE)
        positiveButton.setOnClickListener {
            if (etIngredientName.text.isNotEmpty()) {
                handleIngredientCreate()
                dialog.dismiss()
            } else {
                etIngredientName.error = "This field can not be empty"
            }
        }
    }

    private fun handleIngredientCreate() {
        ingredientHandler.ingredientCreated(
                Ingredient(
                        null,
                        etIngredientName.text.toString().toLowerCase().capitalize()
                )
        )
    }
}
