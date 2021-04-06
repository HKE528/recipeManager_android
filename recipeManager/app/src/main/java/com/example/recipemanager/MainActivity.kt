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
                .setMessage("수정하시겠습니까?")
                .setPositiveButton("네") { _, _ ->

                    val pagerIntent = Intent(this, AddViewPager::class.java)
                    pagerIntent.putExtra("name", name)
                    startActivityForResult(pagerIntent, UPDATE_OK)

                }
                .setNegativeButton("아니오", null)

        dlg.show()
    }

    private fun delete(name: String) {
        val dlg: AlertDialog.Builder = AlertDialog.Builder(this)
                .setMessage("정말로 삭제하시겠습니까?")
                .setPositiveButton("네") { _, _ ->
                    DataIO().deleteRecipe(name)

                    Toast.makeText(applicationContext, "삭제가 완료되었습니다.", Toast.LENGTH_SHORT).show()

                    initList()
                }
                .setNegativeButton("아니오", null)

        dlg.show()

    }

    private fun initApp() {
        //권한 요청
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
            Toast.makeText(this, "권한 모두 허용", Toast.LENGTH_SHORT).show()
            ActivityCompat.requestPermissions(this, permissions, REQ_PREMISSION)
            //makeDir()
        }

        initToolbar()
        initList()
    }

    fun initList() {
        tv_title.text = "ALL"

        recipes = DataIO().loadALL()

        categorySet = mutableSetOf<String>("ALL", "미분류")
        recipes.forEach { categorySet.add(it.category.toString()) }

        //recipe_list.adapter = ListViewAdapter(recipes)
        refreshList(recipes)
    }

    fun refreshList(list: ArrayList<RecipeDTO>) {
        recipe_list.adapter = ListViewAdapter(items = list)
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
            Toast.makeText(this, "한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT).show()
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
                    Log.i("mkdir", "폴더 만들기 성공")
                } else {
                    Log.i("mkdir", "폴더 만들기 실패")
                    finish()
                }
            }
        } catch (e : Exception) {
            e.printStackTrace()
        }
    }

    //권한 요청 
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

        //인텐트 이벤트 처리
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                UPDATE_OK -> initList()
                ADD_OK -> initList()
            }
        }
    }

    //뒤로가기 처리
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
        //툴바 엑션 처리
        when (item.itemId) {
            //네비게이션 드로어 열기
            android.R.id.home -> {
                main_layout_drawer.openDrawer(GravityCompat.START)
            }

            //다중 삭제
            R.id.multiply_delete -> {
                multiChoice()
            }
            //툴바 분류 보기
            R.id.classify_category -> {
                val dView = layoutInflater.inflate(R.layout.layout_select_category, null)
                val sp: Spinner = dView.spinner_category

                sp.adapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, categorySet.toCollection(ArrayList<String>()))

                AlertDialog.Builder(this)
                        .setView(dView)
                        .setTitle("분류 선택")
                        .setNegativeButton("취소", null)
                        .setPositiveButton("확인") { _, _ ->
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

        Toast.makeText(applicationContext, "다중 삭제 공사중..", Toast.LENGTH_SHORT).show()
    }
}


