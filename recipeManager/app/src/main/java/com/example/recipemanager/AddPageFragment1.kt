package com.example.recipemanager

import android.app.Activity
import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
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
import kotlinx.coroutines.processNextEventInCurrentThread
import java.io.InputStream
import java.lang.Exception
import java.nio.channels.Selector
import java.security.cert.CertPath
import kotlin.math.log

class AddPageFragment1(val name : String? = null) : Fragment() {
    private val sharedViewModel : SharedViewModel by activityViewModels()

    private var recipeDTO: RecipeDTO = RecipeDTO()
    private var imageUri: Uri? = null

    private val LOAD_IMG = 1

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

        img_add_recipe.setOnClickListener {
            //갤러리 오픈
            openGallery()
        }

        name?.let { setEditText() }

        btn_next.setOnClickListener(){item ->
            setDTO()

            sharedViewModel.select(recipeDTO)

            (activity as AddViewPager).view_pager_add_recipe.currentItem = 1
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode == RESULT_OK) {

            when (requestCode) {
                LOAD_IMG -> {
                    imageUri = data?.data

                    try {
                        val bitmap = MediaStore.Images.Media.getBitmap(activity?.contentResolver, imageUri)
                        img_add_recipe.setImageBitmap(bitmap)

                    } catch (e: Exception) {
                        e.printStackTrace()
                        Toast.makeText(context, "이미지 로드 실패", Toast.LENGTH_SHORT).show()
                    }

                }
            }

        }

    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(intent, LOAD_IMG)
    }

    private fun setDTO() {
        with(recipeDTO) {
            img = imageUri?.toString()
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
