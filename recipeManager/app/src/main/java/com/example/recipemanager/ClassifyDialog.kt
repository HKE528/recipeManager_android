package com.example.recipemanager

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
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

    /*fun show(items : ArrayList<String>) {

        val dlg = Dialog(context)

        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dlg.setContentView(R.layout.layout_select_category)

        dlg.show()

        Log.i("test", items.toString())

        //dlg.spinner_category.adapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items)

        dlg.btn_ok.setOnClickListener {
            dlg.dismiss()
        }

        dlg.btn_cancel.setOnClickListener {
            dlg.dismiss()
        }
    }*/

    fun show(items : ArrayList<String>) {
        val spinner = view.findViewById<Spinner>(R.id.spinner_category)

        spinner.adapter = ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, items)
        spinner.setSelection(0)

        builder.setView(view)

        builder.setPositiveButton("확인", null)
        builder.setNegativeButton("취소", null)

        val dlg = builder.create()
        dlg.show()
    }
}