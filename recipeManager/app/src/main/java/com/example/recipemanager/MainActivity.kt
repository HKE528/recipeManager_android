package com.example.recipemanager

import android.app.AlertDialog
import android.app.TaskStackBuilder
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.core.view.GravityCompat
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.add_recipe.*
import kotlinx.android.synthetic.main.drawer_layout.*
import kotlinx.android.synthetic.main.drawer_view.*

class MainActivity : AppCompatActivity() {

    var addRecipeViewList: ArrayList<View> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.drawer_layout)

        initToolbar()
        //initAddRecipeView()

        btn_plus.setOnClickListener {

        }
    }

    private fun initAddRecipeView()
    {
        addRecipeViewList.add(layoutInflater.inflate(R.layout.add_recipe_1, null))
        addRecipeViewList.add(layoutInflater.inflate(R.layout.add_recipe_2, null))

        view_pager_add_recipe.adapter = ViewPagerAdapter(addRecipeViewList)
    }

    private fun initToolbar() {
        setSupportActionBar(layout_toolbar as androidx.appcompat.widget.Toolbar)
        supportActionBar?.setDisplayShowCustomEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_action_open_nav)
    }

    private fun showPopupIsExit(title : String? = null, text: String = "내용")
    {
        val dlg: AlertDialog.Builder = AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(text)
                .setPositiveButton("네") {_, _ ->
                    finish()
                }
                .setNegativeButton("아니오", null)

        dlg.show()
    }

    //뒤로가기 처리
    override fun onBackPressed() {
        if(main_layout_drawer.isDrawerOpen(GravityCompat.START)){
            main_layout_drawer.closeDrawers()
        }
        else {
            super.onBackPressed()
        }
    }
    
    override fun onCreateNavigateUpTaskStack(builder: TaskStackBuilder?) {
        
        super.onCreateNavigateUpTaskStack(builder)
    }
    
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        
        menuInflater.inflate(R.menu.toolbar_actions, menu)
        
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        //툴바 엑션 처리
        when (item.itemId) {
            //네비게이션 드로어 열기
            android.R.id.home -> {
                main_layout_drawer.openDrawer(GravityCompat.START)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}


