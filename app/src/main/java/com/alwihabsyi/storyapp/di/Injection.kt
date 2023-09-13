package com.alwihabsyi.storyapp.di

import android.content.Context
import com.alwihabsyi.storyapp.data.Repository
import com.alwihabsyi.storyapp.data.remote.ApiConfig
import com.alwihabsyi.storyapp.data.user.UserPreferences
import com.alwihabsyi.storyapp.data.user.dataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideRepository(context: Context): Repository {
        val pref = UserPreferences.getInstance(context.dataStore)
        val user = runBlocking { pref.getSession().first() }
        val apiService = ApiConfig.getApiService(user.token!!)
        return Repository.getInstance(pref, apiService)
    }
}