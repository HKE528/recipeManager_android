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
import kotlin.properties.Delegates

class MainActivity : AppCompatActivity() {
    private lateinit var recipes : ArrayList<RecipeDTO>
    private var backPressedTime : Long = 0

    private val UPDATE_OK = 300
    private val ADD_OK = 200

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.drawer_layout)

        initApp()
        registerForContextMenu(recipe_list)

        btn_plus.setOnClickListener {
            val pagerIntent = Intent(this, AddViewPager::class.java)
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
                    R.id.action_update -> update(clickedItem!!)
                    
                    R.id.action_delete -> delete(clickedItem!!)

                }

                super.onContextItemSelected(it)
            }

            popupMenu.show()

            true
        }
    }
    private fun update(name : String) {
        val dlg: AlertDialog.Builder = AlertDialog.Builder(this)
                .setMessage("수정하시겠습니까?")
                .setPositiveButton("네") {_, _ ->

                    val pagerIntent = Intent(this, AddViewPager::class.java)
                    pagerIntent.putExtra("name", name)
                    startActivityForResult(pagerIntent, UPDATE_OK)

                }
                .setNegativeButton("아니오", null)

        dlg.show()
    }

    private fun delete(name : String) {
        val dlg: AlertDialog.Builder = AlertDialog.Builder(this)
                .setMessage("정말로 삭제하시겠습니까?")
                .setPositiveButton("네") {_, _ ->
                    DataIO().deleteRecipe(name)

                    Toast.makeText(applicationContext, "삭제가 완료되었습니다.", Toast.LENGTH_SHORT).show()

                    initList()
                }
                .setNegativeButton("아니오", null)

        dlg.show()

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

    private fun appClose() {
        if ( System.currentTimeMillis() > backPressedTime + 2000) {
            backPressedTime = System.currentTimeMillis()
            Toast.makeText(this, "한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT).show()
        }
        else {
            super.onBackPressed()
        }
    }

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
            appClose()
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

            R.id.multiply_delete ->
                Toast.makeText(applicationContext, "다중 삭제", Toast.LENGTH_SHORT).show()

            R.id.classify_category ->
                Toast.makeText(applicationContext, "분류별 표시", Toast.LENGTH_SHORT).show()

        }
        return super.onOptionsItemSelected(item)
    }
}


