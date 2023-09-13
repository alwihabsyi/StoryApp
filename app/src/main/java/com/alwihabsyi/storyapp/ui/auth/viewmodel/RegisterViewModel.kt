package com.alwihabsyi.storyapp.ui.auth.viewmodel

import androidx.lifecycle.ViewModel
import com.alwihabsyi.storyapp.data.Repository

class RegisterViewModel(
    private val repository: Repository
): ViewModel() {

    fun register(name: String, email: String, password: String) = repository.register(name, email, password)

}