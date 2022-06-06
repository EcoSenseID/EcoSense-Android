package com.ecosense.android.featAuth.presentation.registration

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
class RegistrationViewModelTest {
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Test
    fun `getState, default state`() = runBlocking {
        val fakeAuthRepository = FakeAuthRepository()
        val registrationViewModel = RegistrationViewModel(fakeAuthRepository)
        assertThat(registrationViewModel.state.value)
            .isEqualTo(RegistrationScreenState.defaultValue)
    }

    @Test
    fun `onEmailValueChange, state with correct email value`() = runBlocking {
        val fakeAuthRepository = FakeAuthRepository()
        val registrationViewModel = RegistrationViewModel(fakeAuthRepository)


        assertThat(registrationViewModel.state.value.email).isEqualTo(RegistrationScreenState.defaultValue.email)

        for (i in 1..10) {
            val inputValue = TestFaker.getLorem(length = i * 100)
            registrationViewModel.onEmailValueChange(value = inputValue)
            assertThat(registrationViewModel.state.value.email).isEqualTo(inputValue)
        }
    }

    @Test
    fun `onPasswordValueChange, state with correct password value`() = runBlocking {
        val fakeAuthRepository = FakeAuthRepository()
        val registrationViewModel = RegistrationViewModel(fakeAuthRepository)

        assertThat(registrationViewModel.state.value.password)
            .isEqualTo(RegistrationScreenState.defaultValue.password)

        for (i in 1..10) {
            val inputValue = TestFaker.getLorem(length = i * 100)
            registrationViewModel.onPasswordValueChange(value = inputValue)
            assertThat(registrationViewModel.state.value.password).isEqualTo(inputValue)
        }
    }

    @Test
    fun `onRepeatedPasswordValueChange, state with correct password value`() = runBlocking {
        val fakeAuthRepository = FakeAuthRepository()
        val registrationViewModel = RegistrationViewModel(fakeAuthRepository)

        assertThat(registrationViewModel.state.value.repeatedPassword)
            .isEqualTo(RegistrationScreenState.defaultValue.repeatedPassword)

        for (i in 1..10) {
            val inputValue = TestFaker.getLorem(length = i * 100)
            registrationViewModel.onRepeatedPasswordValueChange(value = inputValue)
            assertThat(registrationViewModel.state.value.repeatedPassword).isEqualTo(inputValue)
        }
    }

    @Test
    fun `onTogglePasswordVisibility, correct state`() = runBlocking {
        val fakeAuthRepository = FakeAuthRepository()
        val registrationViewModel = RegistrationViewModel(fakeAuthRepository)

        val defaultValue = RegistrationScreenState.defaultValue.isPasswordVisible
        assertThat(registrationViewModel.state.value.isPasswordVisible)
            .isEqualTo(defaultValue)

        for (i in 1..10) {
            registrationViewModel.onTogglePasswordVisibility()
            val expectedValue = if (i % 2 == 0) defaultValue else !defaultValue
            assertThat(registrationViewModel.state.value.isPasswordVisible)
                .isEqualTo(expectedValue)
        }
    }

    @Test
    fun `onToggleRepeatedPasswordVisibility, correctState`() = runBlocking {
        val fakeAuthRepository = FakeAuthRepository()
        val registrationViewModel = RegistrationViewModel(fakeAuthRepository)

        val defaultValue = RegistrationScreenState.defaultValue.isRepeatedPasswordVisible
        assertThat(registrationViewModel.state.value.isRepeatedPasswordVisible)
            .isEqualTo(defaultValue)

        for (i in 1..10) {
            registrationViewModel.onToggleRepeatedPasswordVisibility()
            val expectedValue = if (i % 2 == 0) defaultValue else !defaultValue
            assertThat(registrationViewModel.state.value.isRepeatedPasswordVisible)
                .isEqualTo(expectedValue)
        }
    }

    @Test
    fun `RegistrationWithEmail is in progress, correct state`() = runBlocking {
        val fakeAuthRepository = FakeAuthRepository(
            fakeRegisterWithEmailResult = Resource.Loading()
        )
        val registrationViewModel = RegistrationViewModel(fakeAuthRepository)

        registrationViewModel.onRegisterClick()

        assertThat(registrationViewModel.state.value.isRegistering).isTrue()
    }

    @Test
    fun `RegistrationWithEmail is successful, correct state`() = runBlocking {
        val fakeAuthRepository = FakeAuthRepository(
            fakeRegisterWithEmailResult = Resource.Success(Unit)
        )
        val registrationViewModel = RegistrationViewModel(fakeAuthRepository)

        registrationViewModel.onRegisterClick()

        assertThat(registrationViewModel.state.value.isRegistering).isFalse()
    }

    @Test
    fun `RegistrationWithEmail is unsuccessful, correct state`() = runBlocking {
        val expectedUiText = UIText.DynamicString(TestFaker.getLorem())
        val fakeAuthRepository = FakeAuthRepository(
            fakeRegisterWithEmailResult = Resource.Error(expectedUiText)
        )
        val registrationViewModel = RegistrationViewModel(fakeAuthRepository)

        registrationViewModel.onRegisterClick()

        assertThat(registrationViewModel.state.value.isRegistering).isFalse()
        registrationViewModel.eventFlow.test {
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
        val registrationViewModel = RegistrationViewModel(fakeAuthRepository)

        registrationViewModel.onGoogleSignInResult(TestFaker.validIdToken)

        assertThat(registrationViewModel.state.value.isLoadingGoogleLogin).isTrue()
    }

    @Test
    fun `GoogleSignIn is successful, correct state`() = runBlocking {
        val fakeAuthRepository = FakeAuthRepository(
            fakeLoginWithGoogleResult = Resource.Success(Unit)
        )
        val registrationViewModel = RegistrationViewModel(fakeAuthRepository)

        registrationViewModel.onGoogleSignInResult(TestFaker.validIdToken)

        assertThat(registrationViewModel.state.value.isLoadingGoogleLogin).isFalse()
    }

    @Test
    fun `GoogleSignIn is unsuccessful, correct state`() = runBlocking {
        val expectedUiText = UIText.DynamicString(TestFaker.getLorem())
        val fakeAuthRepository = FakeAuthRepository(
            fakeLoginWithGoogleResult = Resource.Error(expectedUiText)
        )
        val registrationViewModel = RegistrationViewModel(fakeAuthRepository)

        registrationViewModel.onGoogleSignInResult(TestFaker.validIdToken)

        assertThat(registrationViewModel.state.value.isLoadingGoogleLogin).isFalse()
        registrationViewModel.eventFlow.test {
            val event = awaitItem()
            assertThat(event).isInstanceOf(UIEvent.ShowSnackbar::class.java)
            assertThat((event as UIEvent.ShowSnackbar).uiText).isEqualTo(expectedUiText)
        }
    }
}