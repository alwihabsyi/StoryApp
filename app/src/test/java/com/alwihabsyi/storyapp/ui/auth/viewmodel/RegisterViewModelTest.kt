package com.alwihabsyi.storyapp.ui.auth.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.alwihabsyi.storyapp.data.Repository
import com.alwihabsyi.storyapp.data.Result
import com.alwihabsyi.storyapp.data.UserModel
import com.alwihabsyi.storyapp.utils.getOrAwaitValue
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class RegisterViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var repository: Repository

    private lateinit var registerViewModel: RegisterViewModel

    @Before
    fun setUp() {
        registerViewModel = RegisterViewModel(repository)
    }

    @Test
    fun `when register Should Not Null and return success`() {
        val response = MediatorLiveData<Result<UserModel>>()

        val name = "nama"
        val email = "fake@email.com"
        val password = "password"

        val userModel = UserModel(email, "null", false)
        response.value = Result.Success(userModel)

        Mockito.`when`(repository.register(name, email, password)).thenReturn(response)

        val actualResponse = registerViewModel.register(name, email, password).getOrAwaitValue()
        Mockito.verify(repository).register(name, email, password)
        assertNotNull(actualResponse)
        assertTrue(actualResponse is Result.Success)
    }

    @Test
    fun `when network Error Should Return Error`() {
        val expectedLoginResponse = MutableLiveData<Result<UserModel>>()
        expectedLoginResponse.value = Result.Error("network error")

        val name = "nama"
        val email = "fake@email.com"
        val password = "password"

        Mockito.`when`(repository.register(name, email, password)).thenReturn(expectedLoginResponse)

        val actualResponse = registerViewModel.register(name, email, password).getOrAwaitValue()
        Mockito.verify(repository).register(name, email, password)
        assertNotNull(actualResponse)
        assertTrue(actualResponse is Result.Error)
    }
}