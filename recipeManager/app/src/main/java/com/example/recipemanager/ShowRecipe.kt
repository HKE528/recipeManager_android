package com.example.recipemanager

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.show_recipe.*
import kotlinx.android.synthetic.main.toolbar.*
import kotlinx.android.synthetic.main.toolbar.view.*

class ShowRecipe : AppCompatActivity() {
    private lateinit var name : String
    private lateinit var recipe : RecipeDTO

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.show_recipe)

        name = intent.getStringExtra("name").toString()
        recipe = DataIO().loadRecipe(name)

        txt_show_ingredient.text = recipe.ingredient?.let { makeString(it) }
        txt_show_recipe.text = recipe.recipe?.let { makeString(it) }

        initToolbar()
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

        tv_title.text = name
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }

        return super.onOptionsItemSelected(item)
    }
}