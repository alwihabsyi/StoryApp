package com.alwihabsyi.storyapp.ui.main

import androidx.lifecycle.ViewModel
import com.alwihabsyi.storyapp.data.Repository

class MainViewModel(
    private val repository: Repository
): ViewModel() {

    fun getAllStories(token: String) = repository.getAllUser(token)

}