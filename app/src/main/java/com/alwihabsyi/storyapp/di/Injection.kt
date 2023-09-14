package com.alwihabsyi.storyapp.di

import com.alwihabsyi.storyapp.data.Repository
import com.alwihabsyi.storyapp.data.remote.ApiConfig

object Injection {
    fun provideRepository(): Repository {
        val apiService = ApiConfig.getApiService()
        return Repository.getInstance(apiService)
    }

}