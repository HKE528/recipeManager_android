package com.example.recipemanager

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.add_recipe.*
import kotlinx.android.synthetic.main.add_recipe_1.*
import kotlinx.android.synthetic.main.add_recipe_2.*
import kotlinx.android.synthetic.main.recipe_layout.*
import java.nio.channels.Selector

class AddPageFragment1 : Fragment() {
    private val sharedViewModel : SharedViewModel by activityViewModels()
    private lateinit var recipeDTO: RecipeDTO

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.add_recipe_1, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btn_next.setOnClickListener(){item ->
            setDTO()
            Toast.makeText(context, "다음", Toast.LENGTH_SHORT).show()
            sharedViewModel.select(recipeDTO)
        }
    }

    private fun setDTO() {
        recipeDTO = RecipeDTO(
                img = null,
                name = et_add_recipe_name.text.toString(),
                category = et_add_recipe_category.text.toString(),
                ingredient = et_add_recipe_material.text.toString(),
                recipe = null
        )
    }
}
