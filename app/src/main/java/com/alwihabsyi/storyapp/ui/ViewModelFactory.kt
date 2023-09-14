package com.alwihabsyi.storyapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.alwihabsyi.storyapp.di.Injection
import com.alwihabsyi.storyapp.ui.auth.viewmodel.LoginViewModel
import com.alwihabsyi.storyapp.ui.auth.viewmodel.RegisterViewModel
import com.alwihabsyi.storyapp.ui.detail.DetailViewModel
import com.alwihabsyi.storyapp.ui.main.MainViewModel
import com.alwihabsyi.storyapp.ui.story.StoryViewModel

class ViewModelFactory : ViewModelProvider.NewInstanceFactory()  {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(Injection.provideRepository()) as T
            }
            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> {
                RegisterViewModel(Injection.provideRepository()) as T
            }
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(Injection.provideRepository()) as T
            }
            modelClass.isAssignableFrom(DetailViewModel::class.java) -> {
                DetailViewModel(Injection.provideRepository()) as T
            }
            modelClass.isAssignableFrom(StoryViewModel::class.java) -> {
                StoryViewModel(Injection.provideRepository()) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }
}