package com.example.recipemanager

import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.load.engine.Resource
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.add_recipe.*
import kotlinx.android.synthetic.main.add_recipe_1.*
import kotlinx.android.synthetic.main.add_recipe_2.*
import kotlinx.android.synthetic.main.recipe_layout.*
import kotlinx.coroutines.delay
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.lang.Exception

class AddPageFragment2 : Fragment() {
    private val sharedViewModel : SharedViewModel by activityViewModels()
    private lateinit var recipeDTO: RecipeDTO

    private var isUpdate = false

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.add_recipe_2, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedViewModel.selected.observe(viewLifecycleOwner, Observer { item ->
            recipeDTO = item
            
            recipeDTO.recipe?.let {
                setEditText()
                isUpdate = true
            }
        })

        btn_complete.setOnClickListener{
            if(recipeDTO.name != "") {

                recipeDTO.recipe = et_add_recipe.text.toString()

                DataIO().saveRecipe(recipeDTO)

                recipeDTO.img?.let {
                    saveBitmapToJpeg(it)
                }

                if(isUpdate)    (activity as AddViewPager).AddOK("수정")
                else            (activity as AddViewPager).AddOK("등록")
            }
            else {
                Toast.makeText(context, "이름을 입력하세요", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun saveBitmapToJpeg(strImg : String) {
        val fileName = "${recipeDTO.name}.jpg"
        val bitmap : Bitmap = MediaStore.Images.Media.getBitmap(context?.contentResolver, strImg.toUri())
        val w : Int = bitmap.width * 190 / bitmap.height
        val resizedBitmap : Bitmap = Bitmap.createScaledBitmap(bitmap, w, 190, true)

        val tempFIle = File(activity?.cacheDir, fileName)
        
        try {
            tempFIle.createNewFile()
            val out = FileOutputStream(tempFIle)
            resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
            out.close()
            Log.i("io", "이미지 저장 성공")
        } catch (e : Exception) {
            e.stackTrace
            Log.i("io", "이미지 저장 실패")
        }
    }

    private fun setEditText() {
        et_add_recipe.setText(recipeDTO.recipe)
    }
}
