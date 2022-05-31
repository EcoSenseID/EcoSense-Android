package com.ecosense.android.featAuth.presentation.login

import app.cash.turbine.test
import com.ecosense.android.core.data.repository.FakeAuthRepository
import com.ecosense.android.core.presentation.util.UIEvent
import com.ecosense.android.core.util.Resource
import com.ecosense.android.core.util.UIText
import com.ecosense.android.util.MainCoroutineRule
import com.ecosense.android.util.TestFaker
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Test
    fun `getState, default state`() = runBlocking {
        val fakeAuthRepository = FakeAuthRepository()
        val loginViewModel = LoginViewModel(fakeAuthRepository)
        assertThat(loginViewModel.state.value).isEqualTo(LoginScreenState.defaultValue)
    }

    @Test
    fun `onEmailValueChange, state with correct email value`() = runBlocking {
        val fakeAuthRepository = FakeAuthRepository()
        val loginViewModel = LoginViewModel(fakeAuthRepository)

        assertThat(loginViewModel.state.value.email).isEqualTo(LoginScreenState.defaultValue.email)

        for (i in 1..10) {
            val inputValue = TestFaker.getLorem(length = i * 100)
            loginViewModel.onEmailValueChange(value = inputValue)
            assertThat(loginViewModel.state.value.email).isEqualTo(inputValue)
        }
    }

    @Test
    fun `onPasswordValueChange, state with correct password value`() = runBlocking {
        val fakeAuthRepository = FakeAuthRepository()
        val loginViewModel = LoginViewModel(fakeAuthRepository)

        assertThat(loginViewModel.state.value.password).isEqualTo(LoginScreenState.defaultValue.password)

        for (i in 1..10) {
            val inputValue = TestFaker.getLorem(length = i * 100)
            loginViewModel.onPasswordValueChange(value = inputValue)
            assertThat(loginViewModel.state.value.password).isEqualTo(inputValue)
        }
    }

    @Test
    fun onTogglePasswordVisibility() = runBlocking {
        val fakeAuthRepository = FakeAuthRepository()
        val loginViewModel = LoginViewModel(fakeAuthRepository)

        val defaultValue = LoginScreenState.defaultValue.isPasswordVisible
        assertThat(loginViewModel.state.value.isPasswordVisible)
            .isEqualTo(defaultValue)

        for (i in 1..10) {
            loginViewModel.onTogglePasswordVisibility()
            val expectedValue = if (i % 2 == 0) defaultValue else !defaultValue
            assertThat(loginViewModel.state.value.isPasswordVisible).isEqualTo(expectedValue)
        }
    }

    @Test
    fun `LoginWithEmail is in progress, correct state`() = runBlocking {
        val fakeAuthRepository = FakeAuthRepository(
            fakeLoginWithEmailResult = Resource.Loading()
        )
        val loginViewModel = LoginViewModel(fakeAuthRepository)

        loginViewModel.onLoginWithEmailClick()

        assertThat(loginViewModel.state.value.isLoadingEmailLogin).isTrue()
    }

    @Test
    fun `LoginWithEmail is successful, correct state`() = runBlocking {
        val fakeAuthRepository = FakeAuthRepository(
            fakeLoginWithEmailResult = Resource.Success(Unit)
        )
        val loginViewModel = LoginViewModel(fakeAuthRepository)

        loginViewModel.onLoginWithEmailClick()

        assertThat(loginViewModel.state.value.isLoadingEmailLogin).isFalse()
    }

    @Test
    fun `LoginWithEmail is unsuccessful, correct state`() = runBlocking {
        val expectedUiText = UIText.DynamicString(TestFaker.getLorem())
        val fakeAuthRepository = FakeAuthRepository(
            fakeLoginWithEmailResult = Resource.Error(expectedUiText)
        )
        val loginViewModel = LoginViewModel(fakeAuthRepository)

        loginViewModel.onLoginWithEmailClick()

        assertThat(loginViewModel.state.value.isLoadingEmailLogin).isFalse()
        loginViewModel.eventFlow.test {
            val event = awaitItem()
            assertThat(event).isInstanceOf(UIEvent.ShowSnackbar::class.java)
            assertThat((event as UIEvent.ShowSnackbar).uiText).isEqualTo(expectedUiText)
        }
    }

    @Test
    fun `GoogleSignIn is in progress, correct state`() = runBlocking {
        val fakeAuthRepository = FakeAuthRepository(
            fakeLoginWithGoogleResult = Resource.Loading()
        )
        val loginViewModel = LoginViewModel(fakeAuthRepository)

        loginViewModel.onGoogleSignInResult(TestFaker.validIdToken)

        assertThat(loginViewModel.state.value.isLoadingGoogleLogin).isTrue()
    }

    @Test
    fun `GoogleSignIn is successful, correct state`() = runBlocking {
        val fakeAuthRepository = FakeAuthRepository(
            fakeLoginWithGoogleResult = Resource.Success(Unit)
        )
        val loginViewModel = LoginViewModel(fakeAuthRepository)

        loginViewModel.onGoogleSignInResult(TestFaker.validIdToken)

        assertThat(loginViewModel.state.value.isLoadingGoogleLogin).isFalse()
    }

    @Test
    fun `GoogleSignIn is unsuccessful, correct state`() = runBlocking {
        val expectedUiText = UIText.DynamicString(TestFaker.getLorem())
        val fakeAuthRepository = FakeAuthRepository(
            fakeLoginWithGoogleResult = Resource.Error(expectedUiText)
        )
        val loginViewModel = LoginViewModel(fakeAuthRepository)

        loginViewModel.onGoogleSignInResult(TestFaker.validIdToken)

        assertThat(loginViewModel.state.value.isLoadingGoogleLogin).isFalse()
        loginViewModel.eventFlow.test {
            val event = awaitItem()
            assertThat(event).isInstanceOf(UIEvent.ShowSnackbar::class.java)
            assertThat((event as UIEvent.ShowSnackbar).uiText).isEqualTo(expectedUiText)
        }
    }
}