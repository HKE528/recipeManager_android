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
}

class DataIO(private val recipeDTO: RecipeDTO) {
    private val makeGson = GsonBuilder().create()
    private val typeToken : TypeToken<RecipeDTO> = object : TypeToken<RecipeDTO>() {}


    fun saveRecipe() {
        //val dataJson = makeGson.toJson(recipeDTO, typeToken.type)
        //App.prefs.setData(recipeDTO.name.toString(), dataJson)

        val json = Json.encodeToString(recipeDTO)
        App.prefs.setData(recipeDTO.name.toString(), json)
    }

    fun loadRecipe() : RecipeDTO{
        val dataJson = App.prefs.getData(recipeDTO.name.toString())

        //return makeGson.fromJson(dataJson, RecipeDTO::class.java)
        return Json.decodeFromString<RecipeDTO> (dataJson.toString())
    }
}