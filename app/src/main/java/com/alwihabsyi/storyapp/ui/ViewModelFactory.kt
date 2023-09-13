package com.alwihabsyi.storyapp.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.alwihabsyi.storyapp.data.Repository
import com.alwihabsyi.storyapp.di.Injection
import com.alwihabsyi.storyapp.ui.auth.viewmodel.IntroductionViewModel
import com.alwihabsyi.storyapp.ui.auth.viewmodel.LoginViewModel
import com.alwihabsyi.storyapp.ui.auth.viewmodel.RegisterViewModel
import com.alwihabsyi.storyapp.ui.detail.DetailViewModel
import com.alwihabsyi.storyapp.ui.main.MainViewModel
import com.alwihabsyi.storyapp.ui.story.StoryViewModel

class ViewModelFactory(private val repository: Repository) : ViewModelProvider.NewInstanceFactory()  {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(repository) as T
            }
            modelClass.isAssignableFrom(IntroductionViewModel::class.java) -> {
                IntroductionViewModel(repository) as T
            }
            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> {
                RegisterViewModel(repository) as T
            }
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(repository) as T
            }
            modelClass.isAssignableFrom(DetailViewModel::class.java) -> {
                DetailViewModel(repository) as T
            }
            modelClass.isAssignableFrom(StoryViewModel::class.java) -> {
                StoryViewModel(repository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        @JvmStatic
        fun getInstance(context: Context): ViewModelFactory {
            if (INSTANCE == null) {
                synchronized(ViewModelFactory::class.java) {
                    INSTANCE = ViewModelFactory(Injection.provideRepository(context))
                }
            }
            return INSTANCE as ViewModelFactory
        }
    }
}