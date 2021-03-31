package com.example.recipemanager

import android.app.AlertDialog
import android.app.TaskStackBuilder
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.core.view.GravityCompat
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.add_recipe.*
import kotlinx.android.synthetic.main.drawer_layout.*
import kotlinx.android.synthetic.main.recipe_list_view.*

class MainActivity : AppCompatActivity() {
    //private var isVisibleAddView = false
    //private val recipes : ArrayList<RecipeDTO> =  DataIO().loadALL()
    private lateinit var recipes : ArrayList<RecipeDTO>

    private val DELETE_OK = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.drawer_layout)

        initApp()

        btn_plus.setOnClickListener {
            //view_pager_add_recipe.currentItem = 0
            //visibleAddView()

            val pagerIntent = Intent(this, AddViewPager::class.java)
            startActivity(pagerIntent)
        }

        recipe_list.setOnItemClickListener{ parent, view, position, id ->
            val clickedItem : String? = recipes[position].name

            val myIntent = Intent(this, ShowRecipe::class.java)
            myIntent.putExtra("name", clickedItem)
            //startActivity(myIntent)
            startActivityForResult(myIntent, DELETE_OK)
        }
    }

    private fun initApp()
    {
        initToolbar()
        //initAddPager()
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

/*    fun invisibleAddView()
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
    }*/

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode == RESULT_OK){
            when (requestCode) {
                DELETE_OK -> initList()
            }
        }
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
        
        menuInflater.inflate(R.menu.main_toolbar_actions, menu)
        
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


