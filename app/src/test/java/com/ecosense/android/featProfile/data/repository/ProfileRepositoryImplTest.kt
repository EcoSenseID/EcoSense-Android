package com.ecosense.android.featProfile.data.repository

import app.cash.turbine.test
import com.ecosense.android.R
import com.ecosense.android.core.domain.api.FakeAuthApi
import com.ecosense.android.core.domain.api.FakeCloudStorageApi
import com.ecosense.android.core.util.Resource
import com.ecosense.android.core.util.UIText
import com.ecosense.android.featProfile.data.api.FakeProfileApi
import com.ecosense.android.featProfile.data.model.ContributionsDto
import com.ecosense.android.util.TestFaker
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class ProfileRepositoryImplTest {

    @Test
    fun `getContributions, emit Loading first`() = runBlocking {
        val profileRepository = ProfileRepositoryImpl(
            authApi = FakeAuthApi(),
            profileApi = FakeProfileApi(),
            cloudStorageApi = FakeCloudStorageApi(),
        )

        profileRepository.getContributions().test {
            assertThat(awaitItem()).isInstanceOf(Resource.Loading::class.java)
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `getContributions with error and message, emit Error with correct message`() = runBlocking {
        val expectedMessage = TestFaker.getLorem()
        val expectedUiText = UIText.DynamicString(expectedMessage)

        val profileRepository = ProfileRepositoryImpl(
            authApi = FakeAuthApi(),
            profileApi = FakeProfileApi(
                contributionsDto = ContributionsDto(
                    error = true,
                    message = expectedMessage,
                    experiences = null,
                    completedCampaigns = null
                )
            ),
            cloudStorageApi = FakeCloudStorageApi(),
        )


        profileRepository.getContributions().test {
            assertThat(awaitItem()).isInstanceOf(Resource.Loading::class.java)
            val errorResult = awaitItem()
            assertThat(errorResult).isInstanceOf(Resource.Error::class.java)
            assertThat(errorResult.uiText).isEqualTo(expectedUiText)
            awaitComplete()
        }
    }

    @Test
    fun `getContributions with error and no message, emit Error with correct message`() =
        runBlocking {
            val expectedUiText = UIText.StringResource(R.string.em_unknown)

            val profileRepository = ProfileRepositoryImpl(
                authApi = FakeAuthApi(),
                profileApi = FakeProfileApi(
                    contributionsDto = ContributionsDto(
                        error = true,
                        message = null,
                        experiences = null,
                        completedCampaigns = null
                    )
                ),
                cloudStorageApi = FakeCloudStorageApi(),
            )


            profileRepository.getContributions().test {
                assertThat(awaitItem()).isInstanceOf(Resource.Loading::class.java)
                val errorResult = awaitItem()
                assertThat(errorResult).isInstanceOf(Resource.Error::class.java)
                assertThat(errorResult.uiText).isEqualTo(expectedUiText)
                awaitComplete()
            }
        }

    @Test
    fun `updateProfile, emit Loading first`() = runBlocking {
        val profileRepository = ProfileRepositoryImpl(
            authApi = FakeAuthApi(),
            profileApi = FakeProfileApi(),
            cloudStorageApi = FakeCloudStorageApi(),
        )

        profileRepository.updateProfile(
            newDisplayName = TestFaker.validName,
            newPhotoUri = TestFaker.validPhotoUri,
        ).test {
            assertThat(awaitItem()).isInstanceOf(Resource.Loading::class.java)
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `sendEmailVerification, emit Loading first`() = runBlocking {
        val profileRepository = ProfileRepositoryImpl(
            authApi = FakeAuthApi(),
            profileApi = FakeProfileApi(),
            cloudStorageApi = FakeCloudStorageApi(),
        )

        profileRepository.sendEmailVerification().test {
            assertThat(awaitItem()).isInstanceOf(Resource.Loading::class.java)
            cancelAndConsumeRemainingEvents()
        }
    }
}