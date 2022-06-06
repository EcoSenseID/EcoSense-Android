package com.ecosense.android.core.presentation

import app.cash.turbine.test
import com.ecosense.android.core.data.repository.FakeAuthRepository
import com.ecosense.android.util.MainCoroutineRule
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModelTest {

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Test
    fun `User is logged in, isLoggedIn is true`() = runBlocking {
        val fakeAuthRepository = FakeAuthRepository(fakeIsLoggedIn = true)
        val mainViewModel = MainViewModel(fakeAuthRepository)
        mainViewModel.isLoggedIn.test {
            assertThat(awaitItem()).isTrue()
        }
    }

    @Test
    fun `User is logged out, isLoggedIn is false`() = runBlocking {
        val fakeAuthRepository = FakeAuthRepository(fakeIsLoggedIn = false)
        val mainViewModel = MainViewModel(fakeAuthRepository)
        mainViewModel.isLoggedIn.test {
            assertThat(awaitItem()).isFalse()
        }
    }
}