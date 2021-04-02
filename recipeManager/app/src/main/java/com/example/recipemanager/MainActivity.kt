package com.example.recipemanager

import android.app.AlertDialog
import android.app.TaskStackBuilder
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import androidx.core.view.GravityCompat
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.add_recipe.*
import kotlinx.android.synthetic.main.drawer_layout.*
import kotlinx.android.synthetic.main.recipe_list_view.*

class MainActivity : AppCompatActivity() {
    private lateinit var recipes : ArrayList<RecipeDTO>

    private val UPDATE_OK = 300
    private val ADD_OK = 200

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.drawer_layout)

        initApp()
        registerForContextMenu(recipe_list)

        btn_plus.setOnClickListener {
            val pagerIntent = Intent(this, AddViewPager::class.java)
            //startActivity(pagerIntent)
            startActivityForResult(pagerIntent, ADD_OK)
        }

        recipe_list.setOnItemClickListener{ parent, view, position, id ->
            val clickedItem : String? = recipes[position].name

            val myIntent = Intent(this, ShowRecipe::class.java)
            myIntent.putExtra("name", clickedItem)
            startActivityForResult(myIntent, UPDATE_OK)
        }

        recipe_list.setOnItemLongClickListener { parent, view, position, id ->
            val clickedItem : String? = recipes[position].name


            val popupMenu : PopupMenu = PopupMenu(this, view)
            menuInflater.inflate(R.menu.long_click_menu, popupMenu.menu)

            popupMenu.setOnMenuItemClickListener {
                when (it?.itemId) {
                    R.id.action_update ->
                        Toast.makeText(applicationContext, clickedItem + " 수정", Toast.LENGTH_SHORT).show()
                    
                    R.id.action_delete ->
                        Toast.makeText(applicationContext, clickedItem + " 삭제", Toast.LENGTH_SHORT).show()
                }

                super.onContextItemSelected(it)
            }

            popupMenu.show()

            true
        }
    }

    private fun initApp()
    {
        initToolbar()
        initList()
    }

    fun initList()
    {
        recipes = DataIO().loadALL()
        recipe_list.adapter = ListViewAdapter(recipes)
    }

    private fun initToolbar() {
        setSupportActionBar(layout_toolbar as androidx.appcompat.widget.Toolbar)
        supportActionBar?.setDisplayShowCustomEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_action_open_nav)
    }
    
/*    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
        //리스트뷰에 컨텍스트 메뉴 추가
        menuInflater.inflate(R.menu.long_click_menu, menu)

        super.onCreateContextMenu(menu, v, menuInfo)
    }*/

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        //인텐트 이벤트 처리
        if(resultCode == RESULT_OK){
            when (requestCode) {
                UPDATE_OK -> initList()
                ADD_OK -> initList()
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


