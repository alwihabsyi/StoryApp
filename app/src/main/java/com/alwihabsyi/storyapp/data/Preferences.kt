package com.alwihabsyi.storyapp.data

import android.content.Context
import android.content.SharedPreferences
import com.alwihabsyi.storyapp.utils.Constants.SESSION
import com.alwihabsyi.storyapp.utils.Constants.TOKEN

object Preferences {

    fun init(context: Context, name: String): SharedPreferences {
        return context.getSharedPreferences(name, Context.MODE_PRIVATE)
    }

    private fun preferenceEditor(context: Context): SharedPreferences.Editor {
        val sharedPref = context.getSharedPreferences(SESSION, Context.MODE_PRIVATE)
        return sharedPref.edit()
    }

    fun saveToken(token: String, context: Context) {
        val editor = preferenceEditor(context)
        editor.putString(TOKEN, token)
        editor.apply()
    }

    fun logOut(context: Context) {
        val editor = preferenceEditor(context)
        editor.remove(TOKEN)
        editor.remove("status")
        editor.apply()
    }

}