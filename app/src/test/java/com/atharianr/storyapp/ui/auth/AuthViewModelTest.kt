package com.atharianr.storyapp.ui.auth

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.atharianr.storyapp.data.repository.AuthRepository
import com.atharianr.storyapp.data.source.remote.request.LoginRequest
import com.atharianr.storyapp.data.source.remote.request.RegisterRequest
import com.atharianr.storyapp.data.source.remote.response.AuthResponse
import com.atharianr.storyapp.data.source.remote.response.vo.ApiResponse
import com.atharianr.storyapp.data.source.remote.response.vo.StatusResponse
import com.atharianr.storyapp.utils.DataDummy
import com.atharianr.storyapp.utils.LiveDataTestUtil.getOrAwaitValue
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class AuthViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var authRepository: AuthRepository
    private lateinit var authViewModel: AuthViewModel
    private val dummyAuth = DataDummy.generateAuthData()
    private val loginRequest = LoginRequest("a@c.com", "123456")
    private val registerRequest = RegisterRequest("Rian", "a@c.com", "123456")

    @Before
    fun setUp() {
        authViewModel = AuthViewModel(authRepository)
    }

    @Test
    fun `when Login Response Data Should Not Null and Return Success`() = runTest {
        val expectedLogin = MutableLiveData<ApiResponse<AuthResponse>>()
        expectedLogin.value = ApiResponse.success(dummyAuth)

        Mockito.`when`(authRepository.login(loginRequest)).thenReturn(expectedLogin)

        val actualLogin = authViewModel.login(loginRequest).getOrAwaitValue()
        Mockito.verify(authRepository).login(loginRequest)

        Assert.assertNotNull(actualLogin)
        Assert.assertTrue(actualLogin.status == StatusResponse.SUCCESS)
        Assert.assertEquals(dummyAuth, actualLogin.body)
    }

    @Test
    fun `when Login Network Error, Response Data Should Not Null and Return Error`() = runTest {
        val expectedLogin = MutableLiveData<ApiResponse<AuthResponse>>()
        expectedLogin.value = ApiResponse.error("Error")

        Mockito.`when`(authRepository.login(loginRequest)).thenReturn(expectedLogin)

        val actualLogin = authViewModel.login(loginRequest).getOrAwaitValue()
        Mockito.verify(authRepository).login(loginRequest)

        Assert.assertNotNull(actualLogin)
        Assert.assertTrue(actualLogin.status == StatusResponse.ERROR)
    }

    @Test
    fun `when Register Response Data Should Not Null and Return Success`() = runTest {
        val expectedRegister = MutableLiveData<ApiResponse<AuthResponse>>()
        expectedRegister.value = ApiResponse.success(dummyAuth)

        Mockito.`when`(authRepository.register(registerRequest)).thenReturn(expectedRegister)

        val actualRegister = authViewModel.register(registerRequest).getOrAwaitValue()
        Mockito.verify(authRepository).register(registerRequest)

        Assert.assertNotNull(actualRegister)
        Assert.assertTrue(actualRegister.status == StatusResponse.SUCCESS)
        Assert.assertEquals(dummyAuth, actualRegister.body)
    }

    @Test
    fun `when Register Network Error, Response Data Should Not Null and Return Error`() = runTest {
        val expectedRegister = MutableLiveData<ApiResponse<AuthResponse>>()
        expectedRegister.value = ApiResponse.error("Error")

        Mockito.`when`(authRepository.register(registerRequest)).thenReturn(expectedRegister)

        val actualRegister = authViewModel.register(registerRequest).getOrAwaitValue()
        Mockito.verify(authRepository).register(registerRequest)

        Assert.assertNotNull(actualRegister)
        Assert.assertTrue(actualRegister.status == StatusResponse.ERROR)
    }
}