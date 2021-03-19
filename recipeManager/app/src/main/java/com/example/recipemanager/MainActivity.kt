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
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import android.widget.Toast
import androidx.core.view.GravityCompat
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.add_recipe.*
import kotlinx.android.synthetic.main.add_recipe_1.*
import kotlinx.android.synthetic.main.drawer_layout.*
import kotlinx.android.synthetic.main.drawer_view.*

class MainActivity : AppCompatActivity() {

    var addRecipeViewList: ArrayList<View> = ArrayList()
    var isVisibleAddView = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.drawer_layout)

        initToolbar()
        initAddRecipeView()

        btn_plus.setOnClickListener {
            layout_add_recipe.visibility = View.VISIBLE
            btn_plus.visibility = View.INVISIBLE
            isVisibleAddView = true
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

    private fun showPopupIsExit()
    {
        val dlg: AlertDialog.Builder = AlertDialog.Builder(this)
                .setMessage("정말로 레시피 추가를 종료하시겠습니까?")
                .setPositiveButton("네") {_, _ ->
                    isVisibleAddView = false
                    layout_add_recipe.visibility = View.INVISIBLE
                    btn_plus.visibility = View.VISIBLE
                }
                .setNegativeButton("아니오", null)

        dlg.show()
    }

    //뒤로가기 처리
    override fun onBackPressed() {
        if(main_layout_drawer.isDrawerOpen(GravityCompat.START)){
            main_layout_drawer.closeDrawers()
        }
        else if(isVisibleAddView){
            showPopupIsExit()
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


