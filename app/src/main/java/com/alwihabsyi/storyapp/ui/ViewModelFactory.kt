package com.alwihabsyi.storyapp.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.alwihabsyi.storyapp.di.Injection
import com.alwihabsyi.storyapp.ui.auth.viewmodel.LoginViewModel
import com.alwihabsyi.storyapp.ui.auth.viewmodel.RegisterViewModel
import com.alwihabsyi.storyapp.ui.main.MainViewModel
import com.alwihabsyi.storyapp.ui.maps.MapsViewModel
import com.alwihabsyi.storyapp.ui.story.StoryViewModel

class ViewModelFactory(private val context: Context) : ViewModelProvider.NewInstanceFactory()  {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(Injection.provideRepository(context)) as T
            }
            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> {
                RegisterViewModel(Injection.provideRepository(context)) as T
            }
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(Injection.provideRepository(context)) as T
            }
            modelClass.isAssignableFrom(StoryViewModel::class.java) -> {
                StoryViewModel(Injection.provideRepository(context)) as T
            }
            modelClass.isAssignableFrom(MapsViewModel::class.java) -> {
                MapsViewModel(Injection.provideRepository(context)) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }
}