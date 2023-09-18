package com.alwihabsyi.storyapp.ui.auth.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.alwihabsyi.storyapp.data.Repository
import com.alwihabsyi.storyapp.utils.DataDummy
import com.alwihabsyi.storyapp.data.Result
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
class LoginViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var repository: Repository

    private lateinit var loginViewModel: LoginViewModel
    private val dummyLoginResponse = DataDummy.generateDummyLoginResponse()

    @Before
    fun setUp() {
        loginViewModel = LoginViewModel(repository)
    }

    @Test
    fun `when login Should Not Null and return success`() {
        val response = MediatorLiveData<Result<String>>()
        response.value = Result.Success(dummyLoginResponse.loginResult!!.token!!)

        val email = "fake@email.com"
        val password = "password"

        Mockito.`when`(repository.login(email, password)).thenReturn(response)

        val actualResponse = loginViewModel.login(email, password).getOrAwaitValue()
        Mockito.verify(repository).login(email, password)
        assertNotNull(actualResponse)
        assertTrue(actualResponse is Result.Success)
    }

    @Test
    fun `when network Error Should Return Error`() {
        val expectedLoginResponse = MutableLiveData<Result<String>>()
        expectedLoginResponse.value = Result.Error("network error")

        val email = "fake@email.com"
        val password = "password"

        Mockito.`when`(repository.login(email, password)).thenReturn(expectedLoginResponse)

        val actualResponse = loginViewModel.login(email, password).getOrAwaitValue()
        Mockito.verify(repository).login(email, password)
        assertNotNull(actualResponse)
        assertTrue(actualResponse is Result.Error)
    }

}