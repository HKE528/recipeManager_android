package com.example.recipemanager

import android.app.AlertDialog
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import kotlinx.android.synthetic.main.add_recipe.*
import kotlinx.android.synthetic.main.recipe_layout.*
import kotlinx.android.synthetic.main.show_recipe.*
import kotlinx.android.synthetic.main.toolbar.*

class AddViewPager : AppCompatActivity() {
    private var name : String? = null
    private lateinit var recipe : RecipeDTO

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_recipe)

        initActivity()

        name = intent.getStringExtra("name")
        name?.let { updateRecipe() }
    }

    private fun updateRecipe() {
        tv_title.text = "레시피 수정"
        recipe = DataIO().loadRecipe(name!!)
    }

    fun AddOK() {

        Toast.makeText(applicationContext, " 등록 완료", Toast.LENGTH_SHORT).show()

        setResult(RESULT_OK)

        finish()
    }

    private fun initActivity(){
        tv_title.text = "레시피 추가"

        val pagerAdapter = PagerAdapter(supportFragmentManager)
        view_pager_add_recipe.adapter = pagerAdapter
    }

    override fun onBackPressed() {
        when (view_pager_add_recipe.currentItem) {

            0 -> showPopupIsExit()

            else -> view_pager_add_recipe.currentItem -= 1
        }

    }

    private fun showPopupIsExit()
    {
        val dlg: AlertDialog.Builder = AlertDialog.Builder(this)
                .setMessage("정말로 레시피 추가를 종료하시겠습니까?")
                .setPositiveButton("네") {_, _ ->
                    finish()
                }
                .setNegativeButton("아니오", null)

        dlg.show()
    }

    private inner class PagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        override fun getItem(position: Int): Fragment {
            return when(position) {
                0 -> AddPageFragment1()

                else -> AddPageFragment2()
            }
        }

        override fun getCount(): Int = 2
    }
}