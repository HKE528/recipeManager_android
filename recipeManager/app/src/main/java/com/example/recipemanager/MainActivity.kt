package com.example.recipemanager

import android.app.AlertDialog
import android.app.TaskStackBuilder
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.text.TextUtils
import android.util.Log
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.view.GravityCompat
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.add_recipe.*
import kotlinx.android.synthetic.main.drawer_layout.*
import kotlinx.android.synthetic.main.layout_select_category.view.*
import kotlinx.android.synthetic.main.recipe_list_view.*
import kotlinx.android.synthetic.main.toolbar.*
import java.io.File
import java.lang.Exception
import java.nio.file.Files
import java.util.jar.Manifest
import kotlin.properties.Delegates

class MainActivity : AppCompatActivity() {
    private lateinit var recipes: ArrayList<RecipeDTO>
    private lateinit var categorySet: MutableSet<String>
    private var backPressedTime: Long = 0
    private var isMultiChoice: Boolean = false

    private val UPDATE_OK = 300
    private val ADD_OK = 200
    private val REQ_PREMISSION = 10

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.drawer_layout)

        initApp()
        registerForContextMenu(recipe_list)

        btn_plus.setOnClickListener {
            val pagerIntent = Intent(this, AddViewPager::class.java)
            startActivityForResult(pagerIntent, ADD_OK)
        }


        recipe_list.setOnItemClickListener { parent, view, position, id ->

            if(!isMultiChoice) {
                val clickedItem: String? = recipes[position].name

                val myIntent = Intent(this, ShowRecipe::class.java)
                myIntent.putExtra("name", clickedItem)
                startActivityForResult(myIntent, UPDATE_OK)
            }

        }

        recipe_list.setOnItemLongClickListener { parent, view, position, id ->

            val clickedItem: String? = recipes[position].name

            val popupMenu: PopupMenu = PopupMenu(this, view)
            menuInflater.inflate(R.menu.long_click_menu, popupMenu.menu)

            popupMenu.setOnMenuItemClickListener {
                when (it?.itemId) {
                    R.id.action_update -> update(clickedItem!!)

                    R.id.action_delete -> delete(clickedItem!!)

                }

                super.onContextItemSelected(it)
            }

            if (!isMultiChoice)  popupMenu.show()

            true
        }
    }

    private fun update(name: String) {
        val dlg: AlertDialog.Builder = AlertDialog.Builder(this)
                .setMessage("?????????????????????????")
                .setPositiveButton("???") { _, _ ->

                    val pagerIntent = Intent(this, AddViewPager::class.java)
                    pagerIntent.putExtra("name", name)
                    startActivityForResult(pagerIntent, UPDATE_OK)

                }
                .setNegativeButton("?????????", null)

        dlg.show()
    }

    private fun delete(name: String) {
        val dlg: AlertDialog.Builder = AlertDialog.Builder(this)
                .setMessage("????????? ?????????????????????????")
                .setPositiveButton("???") { _, _ ->
                    DataIO().deleteRecipe(cacheDir.toString(), name)

                    Toast.makeText(applicationContext, "????????? ?????????????????????.", Toast.LENGTH_SHORT).show()

                    initList()
                }
                .setNegativeButton("?????????", null)

        dlg.show()

    }

    private fun initApp() {
        //?????? ??????
        val permissions = arrayOf(
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.CAMERA
        )

        var rejectedPermissions = ArrayList<String>()

        permissions.forEach {
            if (checkSelfPermission(it) != PackageManager.PERMISSION_GRANTED) {
                rejectedPermissions.add(it)
            }
        }

        if(rejectedPermissions.isNotEmpty()) {
            val array = arrayOfNulls<String>(rejectedPermissions.size)

            requestPermissions(rejectedPermissions.toArray(array), REQ_PREMISSION)
        } else {
            //Toast.makeText(this, "?????? ?????? ??????", Toast.LENGTH_SHORT).show()
            ActivityCompat.requestPermissions(this, permissions, REQ_PREMISSION)
            //makeDir()
        }

        initToolbar()
        initList()
    }

    fun initList() {
        tv_title.text = "ALL"

        recipes = DataIO().loadALL()

        categorySet = mutableSetOf<String>("ALL", "?????????")
        recipes.forEach { categorySet.add(it.category.toString()) }

        //recipe_list.adapter = ListViewAdapter(recipes)
        refreshList(recipes)
    }

    fun refreshList(list: ArrayList<RecipeDTO>) {
        recipe_list.adapter = ListViewAdapter(context = this, items = list)
    }

    private fun initToolbar() {
        setSupportActionBar(layout_toolbar as androidx.appcompat.widget.Toolbar)
        supportActionBar?.setDisplayShowCustomEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_action_open_nav)
    }

    private fun appClose() {
        if (System.currentTimeMillis() > backPressedTime + 2000) {
            backPressedTime = System.currentTimeMillis()
            Toast.makeText(this, "?????? ??? ???????????? ???????????????.", Toast.LENGTH_SHORT).show()
        } else {
            super.onBackPressed()
        }
    }

    private fun makeDir() {
        val root = Environment.getExternalStorageDirectory().absolutePath
        val dirName = "/RecipeManager/Image/"
        
        val dir = File(root + dirName)

        try {
            if (!dir.exists()) {
                if (dir.mkdirs()) {
                    Log.i("mkdir", "?????? ????????? ??????")
                } else {
                    Log.i("mkdir", "?????? ????????? ??????")
                    finish()
                }
            }
        } catch (e : Exception) {
            e.printStackTrace()
        }
    }

    //?????? ?????? 
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            REQ_PREMISSION -> {
                if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //makeDir()
                } else {
                    finish()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        //????????? ????????? ??????
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                UPDATE_OK -> initList()
                ADD_OK -> initList()
            }
        }
    }

    //???????????? ??????
    override fun onBackPressed() {
        if (main_layout_drawer.isDrawerOpen(GravityCompat.START)) {
            main_layout_drawer.closeDrawers()
        } else {
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
        //?????? ?????? ??????
        when (item.itemId) {
            //??????????????? ????????? ??????
            android.R.id.home -> {
                main_layout_drawer.openDrawer(GravityCompat.START)
            }

            //?????? ??????
            R.id.multiply_delete -> {
                multiChoice()
            }
            //?????? ?????? ??????
            R.id.classify_category -> {
                val dView = layoutInflater.inflate(R.layout.layout_select_category, null)
                val sp: Spinner = dView.spinner_category

                sp.adapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, categorySet.toCollection(ArrayList<String>()))

                AlertDialog.Builder(this)
                        .setView(dView)
                        .setTitle("?????? ??????")
                        .setNegativeButton("??????", null)
                        .setPositiveButton("??????") { _, _ ->
                            classifyList(sp.selectedItem.toString())
                        }
                        .show()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun classifyList(category: CharSequence) {
        if (category == "ALL") {
            initList()
        } else {
            tv_title.text = category

            val list = ArrayList<RecipeDTO>()

            recipes.forEach {
                if (it.category == category) {
                    list.add(it)
                }
            }

            refreshList(list)
        }
    }

    private fun multiChoice() {
        /*isMultiChoice = true
        recipe_list.choiceMode = ListView.CHOICE_MODE_MULTIPLE
        recipe_list.adapter = ListViewAdapter*/

        Toast.makeText(applicationContext, "?????? ?????? ?????????..", Toast.LENGTH_SHORT).show()
    }
}


