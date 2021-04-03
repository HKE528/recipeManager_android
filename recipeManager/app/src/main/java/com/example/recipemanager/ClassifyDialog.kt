package com.example.recipemanager

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.util.Log
import android.view.LayoutInflater
import android.view.Window
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.layout_select_category.*

class ClassifyDialog(private val context: Context) : AppCompatActivity() {

    val builder = AlertDialog.Builder(context)
    val inflater = context.getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
    val view = inflater.inflate(R.layout.layout_select_category, null)

    fun show(items : ArrayList<String>) {
        val spinner = view.findViewById<Spinner>(R.id.spinner_category)

        spinner.adapter = ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, items)
        spinner.setSelection(0)

        builder.setView(view)
                .setNegativeButton("취소", null)
                .setPositiveButton("확인") { _ , _ ->
                    Log.i("spinner", spinner.selectedItem.toString())
                }


        val dlg = builder.create()
        dlg.show()
    }
}