package com.example.recipemanager

import android.content.Context
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import kotlinx.android.synthetic.main.recipe_layout.view.*

class ListViewAdapter(private val context: Context? = null, val resourceId : Int? = null, private val items : ArrayList<RecipeDTO>) : BaseAdapter() {
    private lateinit var selected : BooleanArray
    private var img : Bitmap? = null

    init {
        context?.let {
            selected  = BooleanArray(items.size) { false }
        }
    }

    override fun getCount(): Int = items.size

    override fun getItem(position: Int): RecipeDTO = items[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, view: View?, parent: ViewGroup?): View {
        var convertView = view
        if (convertView == null)
            convertView = LayoutInflater.from(parent?.context).inflate(R.layout.recipe_layout, parent, false)

        val item : RecipeDTO = items[position]
        val imgPath = "${context?.cacheDir}/${item.name}.jpg"

        img = DataIO().loadImage(imgPath)
        img?.let { convertView!!.img_recipe.setImageBitmap(it) }
        convertView!!.txt_recipe_name.text = item.name
        convertView!!.txt_recipe_category.text = item.category

        return convertView
    }

    fun setSelectedCount() : Int {
        return selected.count() { true }
    }
}