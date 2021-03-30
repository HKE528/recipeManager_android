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
import android.widget.*
import androidx.core.view.GravityCompat
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.add_recipe.*
import kotlinx.android.synthetic.main.add_recipe_1.*
import kotlinx.android.synthetic.main.add_recipe_2.*
import kotlinx.android.synthetic.main.drawer_layout.*
import kotlinx.android.synthetic.main.drawer_view.*
import kotlinx.android.synthetic.main.recipe_list_view.*

class MainActivity : AppCompatActivity() {
    private var isVisibleAddView = false
    //private val recipes : ArrayList<RecipeDTO> =  DataIO().loadALL()
    private lateinit var recipes : ArrayList<RecipeDTO>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.drawer_layout)

        initApp()

        btn_plus.setOnClickListener {
            view_pager_add_recipe.currentItem = 0
            visibleAddView()
        }

        recipe_list.setOnItemClickListener{ parent, view, position, id ->
            val clickedItem : String? = recipes[position].name

            val myIntent = Intent(this, ShowRecipe::class.java)
            myIntent.putExtra("name", clickedItem)
            startActivity(myIntent)
        }
    }

    private fun initApp()
    {
        initToolbar()
        initAddPager()
        initList()
    }

    fun initList()
    {
        recipes = DataIO().loadALL()
        recipe_list.adapter = ListViewAdapter(recipes)
    }

    private fun initAddPager()
    {
        view_pager_add_recipe.adapter = AddPagerAdapter(supportFragmentManager)
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
                    invisibleAddView()
                }
                .setNegativeButton("아니오", null)

        dlg.show()
    }

    fun invisibleAddView()
    {
        layout_add_recipe.visibility = View.INVISIBLE
        btn_plus.visibility = View.VISIBLE
        isVisibleAddView = false
    }
    private fun visibleAddView()
    {
        layout_add_recipe.visibility = View.VISIBLE
        btn_plus.visibility = View.INVISIBLE
        isVisibleAddView = true
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


