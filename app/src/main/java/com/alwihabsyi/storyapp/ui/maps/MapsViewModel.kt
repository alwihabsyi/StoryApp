package com.alwihabsyi.storyapp.ui.maps

import androidx.lifecycle.ViewModel
import com.alwihabsyi.storyapp.data.Repository

class MapsViewModel(
    private val repository: Repository
) : ViewModel() {

    suspend fun getStoryWithLocation(token: String) = repository.getStoriesWithLocation(token)

}