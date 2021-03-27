package com.example.recipemanager

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import kotlinx.android.synthetic.main.recipe_layout.view.*

class ListViewAdapter(private val items : ArrayList<RecipeDTO>) : BaseAdapter() {
    override fun getCount(): Int = items.size

    override fun getItem(position: Int): RecipeDTO = items[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, view: View?, parent: ViewGroup?): View {
        var convertView = view
        if (convertView == null)
            convertView = LayoutInflater.from(parent?.context).inflate(R.layout.recipe_layout, parent, false)

        val item : RecipeDTO = items[position]
        //convertView!!.img_recipe.setImageDrawable(item.img)
        convertView!!.txt_recipe_name.text = item.name
        convertView!!.txt_recipe_category.text = item.category

        return convertView
    }
}