package com.alwihabsyi.storyapp.ui.auth.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.alwihabsyi.storyapp.data.Repository
import com.alwihabsyi.storyapp.data.user.UserModel

class IntroductionViewModel(private val repository: Repository) : ViewModel()  {

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

}