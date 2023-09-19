package com.alwihabsyi.storyapp.utils

import com.alwihabsyi.storyapp.data.remote.ListStory
import com.alwihabsyi.storyapp.data.remote.LoginResult
import com.alwihabsyi.storyapp.data.remote.PostResponse
import com.alwihabsyi.storyapp.data.remote.StoriesResponse

object DataDummy {
    fun generateDummyStories(): StoriesResponse {
        val listStory = ArrayList<ListStory>()
        for (i in 1..20) {
            val story = ListStory(
                id = "id_$i",
                name = "Name $i",
                description = "Description $i",
                photoUrl = "https://akcdn.detik.net.id/visual/2020/02/14/066810fd-b6a9-451d-a7ff-11876abf22e2_169.jpeg?w=650",
                createdAt = "2022-02-22T22:22:22Z",
                lat = i.toDouble() * 10,
                lon = i.toDouble() * 10
            )
            listStory.add(story)
        }

        return StoriesResponse(
            error = false,
            message = "Stories fetched successfully",
            listStory = listStory
        )
    }

    fun generateNullDummyStories(): StoriesResponse {
        val listStory = ArrayList<ListStory>()

        return StoriesResponse(
            error = false,
            message = "Stories fetched successfully",
            listStory = listStory
        )
    }

    fun generateDummyLoginResponse(): PostResponse {
        return PostResponse(
            error = false,
            message = "success",
            loginResult = LoginResult(
                userId = "user-yj5pc_LARC_AgK61",
                name = "Arif Faizin",
                token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJ1c2VyLXlqNXBjX0xBUkNfQWdLNjEiLCJpYXQiOjE2NDE3OTk5NDl9.flEMaQ7zsdYkxuyGbiXjEDXO8kuDTcI__3UjCwt6R_I"
            )
        )
    }
}