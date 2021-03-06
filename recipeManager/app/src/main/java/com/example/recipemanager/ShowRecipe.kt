package com.example.recipemanager

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.show_recipe.*
import kotlinx.android.synthetic.main.toolbar.*
import kotlinx.android.synthetic.main.toolbar.view.*
import java.io.FileNotFoundException
import java.lang.Exception

class ShowRecipe : AppCompatActivity() {
    private lateinit var name : String
    private lateinit var recipe : RecipeDTO
    private var isUpdate = false
    private var img : Bitmap? = null

    private val UPDATE_OK = 300

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.show_recipe)

        initView()

        initToolbar()
    }
    private fun exitActivity() {
        setResult(RESULT_OK)
        finish()
    }

    private fun initView() {
        name = intent.getStringExtra("name").toString()
        recipe = DataIO().loadRecipe(name)

        txt_show_ingredient.text = recipe.ingredient?.let { makeString(it) }
        txt_show_recipe.text = recipe.recipe?.let { makeString(it) }

        val imgPath = "$cacheDir/$name.jpg"
        img = DataIO().loadImage(imgPath)

        img?.let {
            img_show.setImageBitmap(it)
        }
    }

    private fun makeString(text : String) : String {
        val input = text.split("\n")
        var output : String = ""

        for ((i, str) in input.withIndex()) {
            output += ((i+1).toString() + ". " + str + "\n" )
        }

        return output
    }

    private fun initToolbar() {
        setSupportActionBar(show_recipe_name as androidx.appcompat.widget.Toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_action_open_nav)

        tv_title.text = name
    }

    private fun delete() {
        val dlg: AlertDialog.Builder = AlertDialog.Builder(this)
                .setMessage("????????? ?????????????????????????")
                .setPositiveButton("???") {_, _ ->
                    DataIO().deleteRecipe(cacheDir.toString(), name)

                    Toast.makeText(applicationContext, "????????? ?????????????????????.", Toast.LENGTH_SHORT).show()

                    exitActivity()
                }
                .setNegativeButton("?????????", null)

        dlg.show()
    }

    private fun update() {
        val dlg: AlertDialog.Builder = AlertDialog.Builder(this)
                .setMessage("?????????????????????????")
                .setPositiveButton("???") {_, _ ->

                    val pagerIntent = Intent(this, AddViewPager::class.java)
                    pagerIntent.putExtra("name", name)
                    startActivityForResult(pagerIntent, UPDATE_OK)

                }
                .setNegativeButton("?????????", null)

        dlg.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        //????????? ????????? ??????
        if(resultCode == RESULT_OK){
            when (requestCode) {
                UPDATE_OK -> {
                    initView()
                    isUpdate = true
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
            
            R.id.action_delete -> delete()
            
            R.id.action_update -> update()
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.show_toolbar_menu, menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onBackPressed() {
        if(isUpdate) {
            exitActivity()
        }

        super.onBackPressed()
    }
}