package com.alwihabsyi.storyapp.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alwihabsyi.storyapp.data.Repository
import kotlinx.coroutines.launch

class MainViewModel(
    private val repository: Repository
): ViewModel() {

    fun getAllStories() = repository.getAllUser()

    fun logOut() = viewModelScope.launch { repository.logout() }
}