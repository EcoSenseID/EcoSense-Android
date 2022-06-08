package com.ecosense.android.featProfile.presentation.profile

import com.ecosense.android.core.data.repository.FakeAuthRepository
import com.ecosense.android.featProfile.data.repository.FakeProfileRepository
import com.ecosense.android.featProfile.presentation.profile.model.ProfileScreenState
import com.ecosense.android.util.MainCoroutineRule
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ProfileViewModelTest {

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Test
    fun `getState, default state`() = runBlocking {
        val profileViewModel = ProfileViewModel(
            authRepository = FakeAuthRepository(),
            profileRepository = FakeProfileRepository(),
        )
        assertThat(profileViewModel.state.value).isEqualTo(ProfileScreenState.defaultValue)
    }

    @Test
    fun `setExpandDropdownMenu, correct state`() {
        val profileViewModel = ProfileViewModel(
            authRepository = FakeAuthRepository(),
            profileRepository = FakeProfileRepository(),
        )

        profileViewModel.setExpandDropdownMenu(true)
        assertThat(profileViewModel.state.value.isDropdownMenuExpanded).isTrue()

        profileViewModel.setExpandDropdownMenu(false)
        assertThat(profileViewModel.state.value.isDropdownMenuExpanded).isFalse()
    }
}