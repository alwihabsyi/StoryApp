package com.alwihabsyi.storyapp.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.alwihabsyi.storyapp.data.local.StoryDatabase
import com.alwihabsyi.storyapp.data.local.StoryRemoteMediator
import com.alwihabsyi.storyapp.data.remote.ApiService
import com.alwihabsyi.storyapp.data.remote.ListStory
import com.alwihabsyi.storyapp.data.remote.PostResponse
import com.google.gson.Gson
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response

class Repository private constructor(
    private val apiService: ApiService,
    private val database: StoryDatabase
) {
    private val result = MediatorLiveData<Result<UserModel>>()
    private val storyWithLocationResult = MediatorLiveData<Result<List<ListStory>>>()
    private val uploadResult = MediatorLiveData<Result<String>>()

    fun register(name: String, email: String, password: String): LiveData<Result<UserModel>> {
        result.value = Result.Loading

        val client = apiService.register(name, email, password)
        client.enqueue(object : Callback<PostResponse> {
            override fun onResponse(call: Call<PostResponse>, response: Response<PostResponse>) {
                try {
                    if (response.isSuccessful) {
                        val user = UserModel(
                            email,
                            "null",
                            false
                        )
                        result.value = Result.Success(user)
                    } else {
                        throw HttpException(response)
                    }
                } catch (e: HttpException) {
                    val jsonInString = e.response()?.errorBody()?.string()
                    val errorBody = Gson().fromJson(jsonInString, PostResponse::class.java)
                    result.value = Result.Error(errorBody.message)
                }
            }

            override fun onFailure(call: Call<PostResponse>, t: Throwable) {
                result.value = Result.Error(t.message.toString())
            }

        })

        return result
    }

    fun login(email: String, password: String): LiveData<Result<String>> {
        uploadResult.value = Result.Loading

        val client = apiService.login(email, password)
        client.enqueue(object : Callback<PostResponse> {
            override fun onResponse(call: Call<PostResponse>, response: Response<PostResponse>) {
                try {
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        if (responseBody != null) {
                            uploadResult.value = Result.Success(responseBody.loginResult!!.token!!)
                        }
                    } else {
                        throw HttpException(response)
                    }
                } catch (e: HttpException) {
                    val jsonInString = e.response()?.errorBody()?.string()
                    val errorBody = Gson().fromJson(jsonInString, PostResponse::class.java)
                    uploadResult.value = Result.Error(errorBody.message)
                }
            }

            override fun onFailure(call: Call<PostResponse>, t: Throwable) {
                uploadResult.value = Result.Error(t.message.toString())
            }

        })

        return uploadResult
    }

    @OptIn(ExperimentalPagingApi::class)
    fun getAllUser(token: String): LiveData<PagingData<ListStory>> {
        val finalToken = "Bearer $token"
        return Pager(
            config = PagingConfig(pageSize = 5),
            remoteMediator = StoryRemoteMediator(database, apiService, finalToken),
            pagingSourceFactory = {
                database.storyDao().getAllStory()
            }
        ).liveData
    }

    suspend fun getStoriesWithLocation(token: String): LiveData<Result<List<ListStory>>> {
        storyWithLocationResult.value = Result.Loading
        val finalToken = "Bearer $token"
        try {
            val client = apiService.getStoriesWIthLocation(finalToken)
            if (client.isSuccessful) {
                val listStory = client.body()?.listStory
                storyWithLocationResult.value = Result.Success(listStory!!)
            } else {
                throw HttpException(client)
            }
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, PostResponse::class.java)
            val errorMessage = errorBody.message
            storyWithLocationResult.value = Result.Error(errorMessage)
        }

        return storyWithLocationResult
    }

    fun uploadData(
        token: String,
        file: MultipartBody.Part,
        description: RequestBody
    ): LiveData<Result<String>> {
        uploadResult.value = Result.Loading
        val finalToken = "Bearer $token"
        val client = apiService.uploadImage(finalToken, file, description)
        client.enqueue(object : Callback<PostResponse> {
            override fun onResponse(call: Call<PostResponse>, response: Response<PostResponse>) {
                try {
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        if (responseBody != null && !responseBody.error) {
                            uploadResult.value = Result.Success(responseBody.message)
                        }
                    } else {
                        throw HttpException(response)
                    }
                } catch (e: HttpException) {
                    val jsonInString = e.response()?.errorBody()?.string()
                    val errorBody = Gson().fromJson(jsonInString, PostResponse::class.java)
                    val errorMessage = errorBody.message
                    uploadResult.value = Result.Error(errorMessage)
                }
            }

            override fun onFailure(call: Call<PostResponse>, t: Throwable) {
                uploadResult.value = Result.Error(t.message.toString())
            }

        })

        return uploadResult

    }

    companion object {
        private var instance: Repository? = null
        fun getInstance(
            apiService: ApiService,
            database: StoryDatabase
        ): Repository =
            instance ?: synchronized(this) {
                instance ?: Repository(apiService, database)
            }.also { instance = it }
    }

}