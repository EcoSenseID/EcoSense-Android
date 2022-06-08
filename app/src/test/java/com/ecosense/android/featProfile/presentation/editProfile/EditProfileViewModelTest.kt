package com.ecosense.android.featProfile.presentation.editProfile

import app.cash.turbine.test
import com.ecosense.android.R
import com.ecosense.android.core.data.repository.FakeAuthRepository
import com.ecosense.android.core.presentation.util.UIEvent
import com.ecosense.android.core.util.Resource
import com.ecosense.android.core.util.UIText
import com.ecosense.android.featProfile.data.repository.FakeProfileRepository
import com.ecosense.android.util.MainCoroutineRule
import com.ecosense.android.util.TestFaker
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
@OptIn(ExperimentalCoroutinesApi::class)
class EditProfileViewModelTest {
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Test
    fun `getState, default state`() = runBlocking {
        val editProfileViewModel = EditProfileViewModel(
            authRepository = FakeAuthRepository(),
            profileRepository = FakeProfileRepository(),
        )
        assertThat(editProfileViewModel.state.value).isEqualTo(EditProfileScreenState.defaultValue)
    }

    @Test
    fun `onDisplayNameChange, state with correct display name value`() = runBlocking {
        val editProfileViewModel = EditProfileViewModel(
            authRepository = FakeAuthRepository(),
            profileRepository = FakeProfileRepository(),
        )

        assertThat(editProfileViewModel.state.value.displayName)
            .isEqualTo(EditProfileScreenState.defaultValue.displayName)

        for (i in 1..10) {
            val inputValue = TestFaker.getLorem(length = i * 100)
            editProfileViewModel.onDisplayNameChange(value = inputValue)
            assertThat(editProfileViewModel.state.value.displayName).isEqualTo(inputValue)
        }
    }

    @Test
    fun `Saving is in progress, correct state`() = runBlocking {
        val editProfileViewModel = EditProfileViewModel(
            authRepository = FakeAuthRepository(),
            profileRepository = FakeProfileRepository(fakeUpdateProfileResult = Resource.Loading()),
        )

        editProfileViewModel.onSaveClick()

        assertThat(editProfileViewModel.state.value.isSavingProfileLoading).isTrue()
        editProfileViewModel.eventFlow.test {
            assertThat(awaitItem()).isInstanceOf(UIEvent.HideKeyboard::class.java)
        }
    }

    @Test
    fun `Saving is successful, correct state`() = runBlocking {
        val editProfileViewModel = EditProfileViewModel(
            authRepository = FakeAuthRepository(),
            profileRepository = FakeProfileRepository(
                fakeUpdateProfileResult = Resource.Success(Unit)
            ),
        )

        editProfileViewModel.onSaveClick()

        assertThat(editProfileViewModel.state.value.isSavingProfileLoading).isFalse()
        editProfileViewModel.eventFlow.test {
            val event = awaitItem()
            val expectedUiText = UIText.StringResource(R.string.sm_changes_saved)
            assertThat(event).isInstanceOf(UIEvent.ShowSnackbar::class.java)
            assertThat((event as UIEvent.ShowSnackbar).uiText).isEqualTo(expectedUiText)
        }
    }

    @Test
    fun `Saving is unsuccessful, correct state`() = runBlocking {
        val expectedUiText = UIText.DynamicString(TestFaker.getLorem())
        val editProfileViewModel = EditProfileViewModel(
            authRepository = FakeAuthRepository(),
            profileRepository = FakeProfileRepository(
                fakeUpdateProfileResult = Resource.Error(expectedUiText)
            ),
        )

        editProfileViewModel.onSaveClick()

        assertThat(editProfileViewModel.state.value.isSavingProfileLoading).isFalse()
        editProfileViewModel.eventFlow.test {
            val event = awaitItem()
            assertThat(event).isInstanceOf(UIEvent.ShowSnackbar::class.java)
            assertThat((event as UIEvent.ShowSnackbar).uiText).isEqualTo(expectedUiText)
        }
    }

    @Test
    fun `Sending verification email is in progress, correct state`() = runBlocking {
        val editProfileViewModel = EditProfileViewModel(
            authRepository = FakeAuthRepository(),
            profileRepository = FakeProfileRepository(fakeSendEmailVerificationResult = Resource.Loading()),
        )

        editProfileViewModel.onSendEmailVerificationClick()

        assertThat(editProfileViewModel.state.value.isEmailVerificationLoading).isTrue()
        editProfileViewModel.eventFlow.test {
            assertThat(awaitItem()).isInstanceOf(UIEvent.HideKeyboard::class.java)
        }
    }

    @Test
    fun `Sending verification email is successful, correct state`() = runBlocking {
        val editProfileViewModel = EditProfileViewModel(
            authRepository = FakeAuthRepository(),
            profileRepository = FakeProfileRepository(
                fakeSendEmailVerificationResult = Resource.Success(Unit)
            ),
        )

        editProfileViewModel.onSendEmailVerificationClick()

        assertThat(editProfileViewModel.state.value.isEmailVerificationLoading).isFalse()
        editProfileViewModel.eventFlow.test {
            val event = awaitItem()
            val expectedUiText = UIText.StringResource(R.string.sm_email_verification_sent)
            assertThat(event).isInstanceOf(UIEvent.ShowSnackbar::class.java)
            assertThat((event as UIEvent.ShowSnackbar).uiText).isEqualTo(expectedUiText)
        }
    }

    @Test
    fun `Sending verification email is unsuccessful, correct state`() = runBlocking {
        val expectedUiText = UIText.DynamicString(TestFaker.getLorem())
        val editProfileViewModel = EditProfileViewModel(
            authRepository = FakeAuthRepository(),
            profileRepository = FakeProfileRepository(
                fakeSendEmailVerificationResult = Resource.Error(expectedUiText)
            ),
        )

        editProfileViewModel.onSendEmailVerificationClick()

        assertThat(editProfileViewModel.state.value.isEmailVerificationLoading).isFalse()
        editProfileViewModel.eventFlow.test {
            val event = awaitItem()
            assertThat(event).isInstanceOf(UIEvent.ShowSnackbar::class.java)
            assertThat((event as UIEvent.ShowSnackbar).uiText).isEqualTo(expectedUiText)
        }
    }

    @Test
    fun `onImagePicked, correct state`() {
        val expectedUri = TestFaker.validPhotoUri

        val editProfileViewModel = EditProfileViewModel(
            authRepository = FakeAuthRepository(),
            profileRepository = FakeProfileRepository(),
        )

        editProfileViewModel.onImagePicked(expectedUri)
        assertThat(editProfileViewModel.state.value.photoUrl).isEqualTo(expectedUri.toString())
    }
}