package com.example.harrison.barbuddy


import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.widget.EditText
import kotlinx.android.synthetic.main.dialog_search.view.*
import java.lang.RuntimeException
import java.util.*

class SearchDialog : DialogFragment() {

    interface SearchHandler {
        fun newSearch(drinkName: String)
    }

    private lateinit var searchHandler: SearchHandler

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        if (context is SearchHandler) {
            searchHandler = context
        } else {
            throw RuntimeException(
                    "The activity does not implement the SearchHandler interface")
        }
    }

    private lateinit var etDrinkName: EditText

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())

        builder.setTitle("New Search")

        val rootView = requireActivity().layoutInflater.inflate(
                R.layout.dialog_search, null
        )
        etDrinkName = rootView.etDrinkName
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
            if (etDrinkName.text.isNotEmpty()) {
                handleSearch()
                dialog.dismiss()
            } else {
                etDrinkName.error = "This field can not be empty"
            }
        }
    }

    private fun handleSearch() {
        searchHandler.newSearch(
                etDrinkName.text.toString()
        )
    }
}
