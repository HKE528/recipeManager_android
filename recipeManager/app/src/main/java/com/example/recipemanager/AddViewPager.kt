package com.example.recipemanager

import android.app.AlertDialog
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.*
import androidx.viewpager.widget.ViewPager
import kotlinx.android.synthetic.main.add_recipe.*
import kotlinx.android.synthetic.main.add_recipe_1.*
import kotlinx.android.synthetic.main.recipe_layout.*
import kotlinx.android.synthetic.main.show_recipe.*
import kotlinx.android.synthetic.main.toolbar.*

class AddViewPager : FragmentActivity() {
    private var name : String? = null
    
    private lateinit var recipe : RecipeDTO
    private lateinit var pagerAdapter : PagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_recipe)

        initActivity()

        name = intent.getStringExtra("name")
        name?.let { updateRecipe() }
    }

    private fun updateRecipe() {
        tv_title.text = "레시피 수정"
    }

    fun AddOK(text:String) {

        Toast.makeText(applicationContext, text + " 완료", Toast.LENGTH_SHORT).show()

        setResult(RESULT_OK)

        finish()
    }

    private fun initActivity(){
        tv_title.text = "레시피 추가"

        pagerAdapter = PagerAdapter(supportFragmentManager)
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
                .setMessage("정말로 작업을 종료하시겠습니까?")
                .setPositiveButton("네") {_, _ ->
                    finish()
                }
                .setNegativeButton("아니오", null)

        dlg.show()
    }

    private inner class PagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

        override fun getItem(position: Int): Fragment {
            return when(position) {
                0 -> AddPageFragment1(name)

                else -> AddPageFragment2()
            }
        }

        override fun getCount(): Int = 2
    }


}