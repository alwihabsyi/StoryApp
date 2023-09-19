package com.alwihabsyi.storyapp.di

import android.content.Context
import com.alwihabsyi.storyapp.data.Repository
import com.alwihabsyi.storyapp.data.local.StoryDatabase
import com.alwihabsyi.storyapp.data.remote.ApiConfig

object Injection {
    fun provideRepository(context: Context): Repository {
        val apiService = ApiConfig.getApiService()
        val database = StoryDatabase.getDatabase(context)
        return Repository.getInstance(apiService, database)
    }

}