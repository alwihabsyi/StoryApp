package com.alwihabsyi.storyapp.ui.detail

import androidx.lifecycle.ViewModel
import com.alwihabsyi.storyapp.data.Repository

class DetailViewModel(
    private val repository: Repository
): ViewModel() {

    fun getUserDetail(token: String, id: String) = repository.getDetailUser(token, id)

}