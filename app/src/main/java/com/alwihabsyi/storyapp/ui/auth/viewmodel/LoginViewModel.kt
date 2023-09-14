package com.alwihabsyi.storyapp.ui.auth.viewmodel

import androidx.lifecycle.ViewModel
import com.alwihabsyi.storyapp.data.Repository

class LoginViewModel(
    private val repository: Repository
) : ViewModel() {

    fun login(email: String, password: String) = repository.login(email, password)

}