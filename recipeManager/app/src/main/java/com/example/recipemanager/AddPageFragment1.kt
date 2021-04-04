package com.example.recipemanager

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import kotlinx.android.synthetic.main.add_recipe.*
import kotlinx.android.synthetic.main.add_recipe_1.*
import kotlinx.android.synthetic.main.add_recipe_2.*
import kotlinx.android.synthetic.main.recipe_layout.*
import java.nio.channels.Selector
import kotlin.math.log

class AddPageFragment1(val name : String? = null) : Fragment() {
    private val sharedViewModel : SharedViewModel by activityViewModels()
    private var recipeDTO: RecipeDTO = RecipeDTO()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.add_recipe_1, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        name?.let { setEditText() }

        btn_next.setOnClickListener(){item ->
            setDTO()

            sharedViewModel.select(recipeDTO)

            (activity as AddViewPager).view_pager_add_recipe.currentItem = 1
        }
    }

    private fun setDTO() {
        with(recipeDTO) {
            name = et_add_recipe_name.text.toString()
            //category = et_add_recipe_category.text.toString()
            category = if (et_add_recipe_category.text.toString() == "") "미분류" else et_add_recipe_category.text.toString()
            ingredient = et_add_recipe_material.text.toString()
        }
    }

    private fun setEditText() {
        recipeDTO = DataIO().loadRecipe(name!!)

        //img_add_recipe.setText(recipeDTO.name)
        et_add_recipe_name.setText(recipeDTO.name)
        et_add_recipe_category.setText(recipeDTO.category)
        et_add_recipe_material.setText(recipeDTO.ingredient)
    }
}
