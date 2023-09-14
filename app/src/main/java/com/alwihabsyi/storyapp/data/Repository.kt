package com.alwihabsyi.storyapp.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.alwihabsyi.storyapp.data.remote.ApiService
import com.alwihabsyi.storyapp.data.remote.ListStory
import com.alwihabsyi.storyapp.data.remote.PostResponse
import com.alwihabsyi.storyapp.data.user.UserModel
import com.google.gson.Gson
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response

class Repository private constructor(
    private val apiService: ApiService
) {
    private val result = MediatorLiveData<Result<UserModel>>()
    private val listStoryResult = MediatorLiveData<Result<List<ListStory>>>()
    private val detailResult = MediatorLiveData<Result<ListStory>>()
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
                        if (responseBody != null){
//                            val userResponse = UserModel(
//                                email,
//                                token = responseBody.loginResult!!.token.toString(),
//                                isLogin = true
//                            )
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

    fun getAllUser(token: String): LiveData<Result<List<ListStory>>> {
        listStoryResult.value = Result.Loading
        val finalToken = "Bearer $token"
        val client = apiService.getAllStories(finalToken)
        client.enqueue(object : Callback<PostResponse> {
            override fun onResponse(call: Call<PostResponse>, response: Response<PostResponse>) {
                try {
                    if (response.isSuccessful) {
                        val story = response.body()?.listStory as List<ListStory>
                        listStoryResult.value = Result.Success(story)
                    } else {
                        throw HttpException(response)
                    }
                } catch (e: HttpException) {
                    val jsonInString = e.response()?.errorBody()?.string()
                    val errorBody = Gson().fromJson(jsonInString, PostResponse::class.java)
                    val errorMessage = errorBody.message
                    listStoryResult.value = Result.Error(errorMessage)
                }
            }

            override fun onFailure(call: Call<PostResponse>, t: Throwable) {
                listStoryResult.value = Result.Error(t.message.toString())
            }
        })

        return listStoryResult
    }

    fun getDetailUser(token: String,id: String): LiveData<Result<ListStory>> {
        detailResult.value = Result.Loading
        val finalToken = "Bearer $token"
        val client = apiService.getStoryDetail(finalToken, id)
        client.enqueue(object : Callback<PostResponse> {
            override fun onResponse(call: Call<PostResponse>, response: Response<PostResponse>) {
                try {
                    if (response.isSuccessful) {
                        val story = response.body()?.story as ListStory
                        detailResult.value = Result.Success(story)
                    } else {
                        throw HttpException(response)
                    }
                } catch (e: HttpException) {
                    val jsonInString = e.response()?.errorBody()?.string()
                    val errorBody = Gson().fromJson(jsonInString, PostResponse::class.java)
                    val errorMessage = errorBody.message
                    detailResult.value = Result.Error(errorMessage)
                }
            }

            override fun onFailure(call: Call<PostResponse>, t: Throwable) {
                detailResult.value = Result.Error(t.message.toString())
            }

        })

        return detailResult
    }

    fun uploadData(token: String, file: MultipartBody.Part, description: RequestBody): LiveData<Result<String>> {
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
            apiService: ApiService
        ): Repository =
            instance ?: synchronized(this) {
                instance ?: Repository(apiService)
            }.also { instance = it }
    }

}