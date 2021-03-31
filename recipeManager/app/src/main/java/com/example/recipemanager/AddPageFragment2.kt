package com.example.recipemanager

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.add_recipe.*
import kotlinx.android.synthetic.main.add_recipe_2.*
import kotlinx.android.synthetic.main.recipe_layout.*
import kotlinx.coroutines.delay

class AddPageFragment2 : Fragment() {
    private val sharedViewModel : SharedViewModel by activityViewModels()
    private lateinit var recipeDTO: RecipeDTO

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.add_recipe_2, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedViewModel.selected.observe(viewLifecycleOwner, Observer { item ->
            recipeDTO = item
        })

        btn_complete.setOnClickListener{
            if(recipeDTO.name != "") {

                recipeDTO.recipe = et_add_recipe.text.toString()

                DataIO().saveRecipe(recipeDTO)

                (activity as AddViewPager).AddOK()
            }
            else {
                Toast.makeText(context, "이름을 입력하세요", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
