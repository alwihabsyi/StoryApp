package com.alwihabsyi.storyapp.ui.story

import androidx.lifecycle.ViewModel
import com.alwihabsyi.storyapp.data.Repository
import okhttp3.MultipartBody
import okhttp3.RequestBody

class StoryViewModel(
    private val repository: Repository
): ViewModel() {

    fun uploadImage(file: MultipartBody.Part, description: RequestBody) =
        repository.uploadData(file, description)

}