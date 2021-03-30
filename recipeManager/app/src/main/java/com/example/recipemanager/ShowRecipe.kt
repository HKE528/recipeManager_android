package com.example.recipemanager

import android.app.AlertDialog
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
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
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_action_open_nav)

        tv_title.text = name
    }

    private fun delete() {
        val dlg: AlertDialog.Builder = AlertDialog.Builder(this)
                .setMessage("정말로 삭제하시겠습니까?")
                .setPositiveButton("네") {_, _ ->
                    DataIO().deleteRecipe(name)

                    Toast.makeText(applicationContext, "삭제가 완료되었습니다.", Toast.LENGTH_SHORT).show()

                    setResult(RESULT_OK)
                    finish()
                }
                .setNegativeButton("아니오", null)

        dlg.show()


    }

    private fun update() {
        Toast.makeText(applicationContext, "수정?", Toast.LENGTH_SHORT).show()
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
    
    
}