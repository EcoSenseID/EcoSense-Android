package com.ecosense.android.featAuth.presentation.resetPassword

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
class ResetPasswordViewModelTest {

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Test
    fun `getState, default state`() = runBlocking {
        val fakeAuthRepository = FakeAuthRepository()
        val resetPasswordViewModel = ResetPasswordViewModel(fakeAuthRepository)
        assertThat(resetPasswordViewModel.state.value).isEqualTo(ResetPasswordScreenState.defaultValue)
    }

    @Test
    fun `onEmailValueChange, state with correct email value`() = runBlocking {
        val fakeAuthRepository = FakeAuthRepository()
        val resetPasswordViewModel = ResetPasswordViewModel(fakeAuthRepository)

        assertThat(resetPasswordViewModel.state.value.email)
            .isEqualTo(ResetPasswordScreenState.defaultValue.email)

        for (i in 1..10) {
            val inputValue = TestFaker.getLorem(length = i * 100)
            resetPasswordViewModel.onEmailValueChange(value = inputValue)
            assertThat(resetPasswordViewModel.state.value.email).isEqualTo(inputValue)
        }
    }

    @Test
    fun `ResetPassword is in progress, correct state`() = runBlocking {
        val fakeAuthRepository = FakeAuthRepository(
            fakeSendPasswordResetEmailResult = Resource.Loading()
        )
        val resetPasswordViewModel = ResetPasswordViewModel(fakeAuthRepository)

        resetPasswordViewModel.onSendInstructionClick()

        assertThat(resetPasswordViewModel.state.value.isLoading).isTrue()
    }

    @Test
    fun `ResetPassword is successful, correct state`() = runBlocking {
        val fakeAuthRepository = FakeAuthRepository(
            fakeSendPasswordResetEmailResult = Resource.Success(Unit)
        )
        val resetPasswordViewModel = ResetPasswordViewModel(fakeAuthRepository)

        resetPasswordViewModel.onSendInstructionClick()

        assertThat(resetPasswordViewModel.state.value.isLoading).isFalse()
    }

    @Test
    fun `ResetPassword is unsuccessful, correct state`() = runBlocking {
        val expectedUiText = UIText.DynamicString(TestFaker.getLorem())
        val fakeAuthRepository = FakeAuthRepository(
            fakeSendPasswordResetEmailResult = Resource.Error(expectedUiText)
        )
        val resetPasswordViewModel = ResetPasswordViewModel(fakeAuthRepository)

        resetPasswordViewModel.onSendInstructionClick()

        assertThat(resetPasswordViewModel.state.value.isLoading).isFalse()
        resetPasswordViewModel.eventFlow.test {
            val event = awaitItem()
            assertThat(event).isInstanceOf(UIEvent.ShowSnackbar::class.java)
            assertThat((event as UIEvent.ShowSnackbar).uiText).isEqualTo(expectedUiText)
        }
    }
}