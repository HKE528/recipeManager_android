package com.example.recipemanager

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class App : Application() {

    companion object {
        lateinit var prefs : DataSharedPreferences
    }

    override fun onCreate() {
        prefs = DataSharedPreferences(applicationContext)
        super.onCreate()
    }
}

class DataSharedPreferences(context: Context) {
    private val fileName = "recipe_data"
    private val prefs : SharedPreferences = context.getSharedPreferences(fileName, 0)

    fun setData(key : String, value : String?) {
        prefs.edit().putString(key, value).apply()
    }

    fun getData(key : String): String? {
        return prefs.getString(key, null).toString()
    }

    fun getALL() : MutableCollection<out Any?> {
        return prefs.all.values
    }

    fun deleteData(key : String) {
        prefs.edit().remove(key).apply()
    }
}

class DataIO() {
    //private val makeGson = GsonBuilder().create()
    //private val typeToken : TypeToken<RecipeDTO> = object : TypeToken<RecipeDTO>() {}

    fun saveRecipe(recipeDTO: RecipeDTO) {
        //val dataJson = makeGson.toJson(recipeDTO, typeToken.type)
        //App.prefs.setData(recipeDTO.name.toString(), dataJson)

        val json = Json.encodeToString(recipeDTO)
        App.prefs.setData(recipeDTO.name.toString(), json)
    }

    fun loadRecipe(name : String) : RecipeDTO{
        val dataJson = App.prefs.getData(name)

        //return makeGson.fromJson(dataJson, RecipeDTO::class.java)
        return Json.decodeFromString<RecipeDTO> (dataJson.toString())
    }

    fun loadALL() : ArrayList<RecipeDTO> {
        val datas : ArrayList<RecipeDTO> = ArrayList()
        val col : MutableCollection<out Any?> = App.prefs.getALL()
        val it : Iterator<Any?> = col.iterator()

        while (it.hasNext()) {
            val value = it.next().toString()
            val dto = Json.decodeFromString<RecipeDTO> (value)

            datas.add(dto)
        }

        return datas
    }

    fun deleteRecipe(name : String) {
        App.prefs.deleteData(name)
    }
}