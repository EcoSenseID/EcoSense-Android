package com.ecosense.android.core.data.repository

import app.cash.turbine.test
import com.ecosense.android.R
import com.ecosense.android.core.domain.api.FakeAuthApi
import com.ecosense.android.core.util.Resource
import com.ecosense.android.core.util.UIText
import com.ecosense.android.util.TestFaker
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.runBlocking
import org.junit.Test

class AuthRepositoryImplTest {
    @Test
    fun `User is logged in, isLoggedIn emits true`() = runBlocking {
        val fakeAuthApi = FakeAuthApi(fakeIsLoggedIn = true)
        val authRepository = AuthRepositoryImpl(fakeAuthApi)

        authRepository.isLoggedIn.test {
            assertThat(awaitItem()).isTrue()
            awaitComplete()
        }
    }

    @Test
    fun `User is logged out, isLoggedIn emits false`() = runBlocking {
        val fakeAuthApi = FakeAuthApi(fakeIsLoggedIn = false)
        val authRepository = AuthRepositoryImpl(fakeAuthApi)

        authRepository.isLoggedIn.test {
            assertThat(awaitItem()).isFalse()
            awaitComplete()
        }
    }

    @Test
    fun `getCurrentUser, correct user`() = runBlocking {
        val expectedUser = TestFaker.user
        val fakeAuthApi = FakeAuthApi(fakeCurrentUser = expectedUser)
        val authRepository = AuthRepositoryImpl(fakeAuthApi)

        val user = authRepository.getCurrentUser()
        assertThat(user).isEqualTo(expectedUser)
    }

    @Test
    fun `Attempt loginWithEmail, emit Loading first`() = runBlocking {
        val fakeAuthApi = FakeAuthApi()
        val authRepository = AuthRepositoryImpl(fakeAuthApi)

        authRepository.loginWithEmail(
            email = TestFaker.validEmail,
            password = TestFaker.validPassword,
        ).test {
            assertThat(awaitItem()).isInstanceOf(Resource.Loading::class.java)
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `Successful loginWithEmail, emit Success`() = runBlocking {
        val fakeAuthApi = FakeAuthApi(fakeLoginWithEmailResult = Resource.Success(Unit))
        val authRepository = AuthRepositoryImpl(fakeAuthApi)

        authRepository.loginWithEmail(
            email = TestFaker.validEmail,
            password = TestFaker.validPassword,
        ).test {
            assertThat(awaitItem()).isInstanceOf(Resource.Loading::class.java)
            assertThat(awaitItem()).isInstanceOf(Resource.Success::class.java)
            awaitComplete()
        }
    }

    @Test
    fun `API error when loginWithEmail, emit Error with correct message`() = runBlocking {
        val expectedUIText = UIText.DynamicString(TestFaker.getLorem())
        val fakeAuthApi = FakeAuthApi(fakeLoginWithEmailResult = Resource.Error(expectedUIText))
        val authRepository = AuthRepositoryImpl(fakeAuthApi)

        authRepository.loginWithEmail(
            email = TestFaker.validEmail,
            password = TestFaker.validPassword,
        ).test {
            assertThat(awaitItem()).isInstanceOf(Resource.Loading::class.java)
            val errorResource = awaitItem()
            assertThat(errorResource).isInstanceOf(Resource.Error::class.java)
            assertThat(errorResource.uiText).isEqualTo(expectedUIText)
            awaitComplete()
        }
    }

    @Test
    fun `loginWithEmail with blank email, emit Error with correct message`() = runBlocking {
        val expectedUIText = UIText.StringResource(R.string.em_email_blank)
        val fakeAuthApi = FakeAuthApi()
        val authRepository = AuthRepositoryImpl(fakeAuthApi)

        authRepository.loginWithEmail(
            email = "",
            password = TestFaker.validPassword,
        ).test {
            assertThat(awaitItem()).isInstanceOf(Resource.Loading::class.java)
            val errorResource = awaitItem()
            assertThat(errorResource).isInstanceOf(Resource.Error::class.java)
            assertThat(errorResource.uiText).isEqualTo(expectedUIText)
            awaitComplete()
        }
    }

    @Test
    fun `loginWithEmail blank password, emit Error with correct message`() = runBlocking {
        val expectedUIText = UIText.StringResource(R.string.em_password_blank)
        val fakeAuthApi = FakeAuthApi()
        val authRepository = AuthRepositoryImpl(fakeAuthApi)

        authRepository.loginWithEmail(
            email = TestFaker.validEmail,
            password = "",
        ).test {
            assertThat(awaitItem()).isInstanceOf(Resource.Loading::class.java)
            val errorResource = awaitItem()
            assertThat(errorResource).isInstanceOf(Resource.Error::class.java)
            assertThat(errorResource.uiText).isEqualTo(expectedUIText)
            awaitComplete()
        }
    }

    @Test
    fun `loginWithEmail invalid email, emit Error with correct message`() = runBlocking {
        val expectedUIText = UIText.StringResource(R.string.em_invalid_email)
        val fakeAuthApi = FakeAuthApi()
        val authRepository = AuthRepositoryImpl(fakeAuthApi)

        authRepository.loginWithEmail(
            email = TestFaker.invalidEmail,
            password = TestFaker.validPassword,
        ).test {
            assertThat(awaitItem()).isInstanceOf(Resource.Loading::class.java)
            val errorResource = awaitItem()
            assertThat(errorResource).isInstanceOf(Resource.Error::class.java)
            assertThat(errorResource.uiText).isEqualTo(expectedUIText)
            awaitComplete()
        }
    }

    @Test
    fun `Attempt loginWithGoogle, emit Loading first`() = runBlocking {
        val fakeAuthApi = FakeAuthApi()
        val authRepository = AuthRepositoryImpl(fakeAuthApi)

        authRepository.loginWithGoogle(
            idToken = TestFaker.validIdToken
        ).test {
            assertThat(awaitItem()).isInstanceOf(Resource.Loading::class.java)
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `loginWithGoogle with null idToken, emit Error with correct message`() = runBlocking {
        val expectedUIText = UIText.StringResource(R.string.em_google_sign_in)
        val fakeAuthApi = FakeAuthApi()
        val authRepository = AuthRepositoryImpl(fakeAuthApi)

        authRepository.loginWithGoogle(
            idToken = null
        ).test {
            assertThat(awaitItem()).isInstanceOf(Resource.Loading::class.java)
            val errorResource = awaitItem()
            assertThat(errorResource).isInstanceOf(Resource.Error::class.java)
            assertThat(errorResource.uiText).isEqualTo(expectedUIText)
            awaitComplete()
        }
    }

    @Test
    fun `loginWithGoogle with blank idToken, emit Error with correct message`() = runBlocking {
        val expectedUIText = UIText.StringResource(R.string.em_google_sign_in)
        val fakeAuthApi = FakeAuthApi()
        val authRepository = AuthRepositoryImpl(fakeAuthApi)

        authRepository.loginWithGoogle(
            idToken = ""
        ).test {
            assertThat(awaitItem()).isInstanceOf(Resource.Loading::class.java)
            val errorResource = awaitItem()
            assertThat(errorResource).isInstanceOf(Resource.Error::class.java)
            assertThat(errorResource.uiText).isEqualTo(expectedUIText)
            awaitComplete()
        }
    }

    @Test
    fun `API error when loginWithGoogle, emit Error with correct message`() = runBlocking {
        val expectedUIText = UIText.DynamicString(TestFaker.getLorem())
        val fakeAuthApi = FakeAuthApi(fakeLoginWithGoogleResult = Resource.Error(expectedUIText))
        val authRepository = AuthRepositoryImpl(fakeAuthApi)

        authRepository.loginWithGoogle(
            idToken = TestFaker.validIdToken
        ).test {
            assertThat(awaitItem()).isInstanceOf(Resource.Loading::class.java)
            val errorResource = awaitItem()
            assertThat(errorResource).isInstanceOf(Resource.Error::class.java)
            assertThat(errorResource.uiText).isEqualTo(expectedUIText)
            awaitComplete()
        }
    }

    @Test
    fun `Successful loginWithGoogle, emit Success`() = runBlocking {
        val fakeAuthApi = FakeAuthApi(fakeLoginWithGoogleResult = Resource.Success(Unit))
        val authRepository = AuthRepositoryImpl(fakeAuthApi)

        authRepository.loginWithGoogle(
            idToken = TestFaker.validIdToken
        ).test {
            assertThat(awaitItem()).isInstanceOf(Resource.Loading::class.java)
            val errorResource = awaitItem()
            assertThat(errorResource).isInstanceOf(Resource.Success::class.java)
            awaitComplete()
        }
    }


    @Test
    fun `Attempt registerWithEmail, emit Loading first`() = runBlocking {
        val fakeAuthApi = FakeAuthApi()
        val authRepository = AuthRepositoryImpl(fakeAuthApi)

        val fakePassword = TestFaker.validPassword
        authRepository.registerWithEmail(
            name = TestFaker.validName,
            email = TestFaker.validEmail,
            password = fakePassword,
            repeatedPassword = fakePassword,
        ).test {
            assertThat(awaitItem()).isInstanceOf(Resource.Loading::class.java)
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `Successful registerWithEmail, emit Success`() = runBlocking {
        val fakeAuthApi = FakeAuthApi(
            fakeRegisterWithEmailResult = Resource.Success(Unit),
            fakeUpdateProfileResult = Resource.Success(Unit)
        )
        val authRepository = AuthRepositoryImpl(fakeAuthApi)

        val fakePassword = TestFaker.validPassword
        authRepository.registerWithEmail(
            name = TestFaker.validName,
            email = TestFaker.validEmail,
            password = fakePassword,
            repeatedPassword = fakePassword,
        ).test {
            assertThat(awaitItem()).isInstanceOf(Resource.Loading::class.java)
            assertThat(awaitItem()).isInstanceOf(Resource.Success::class.java)
            awaitComplete()
        }
    }

    @Test
    fun `API error when registerWithEmail, emit Error with correct message`() = runBlocking {
        val expectedUIText = UIText.DynamicString(TestFaker.getLorem())
        val fakeAuthApi = FakeAuthApi(fakeRegisterWithEmailResult = Resource.Error(expectedUIText))
        val authRepository = AuthRepositoryImpl(fakeAuthApi)

        val fakePassword = TestFaker.validPassword
        authRepository.registerWithEmail(
            name = TestFaker.validName,
            email = TestFaker.validEmail,
            password = fakePassword,
            repeatedPassword = fakePassword,
        ).test {
            assertThat(awaitItem()).isInstanceOf(Resource.Loading::class.java)
            val errorResource = awaitItem()
            assertThat(errorResource).isInstanceOf(Resource.Error::class.java)
            assertThat(errorResource.uiText).isEqualTo(expectedUIText)
            awaitComplete()
        }
    }

    @Test
    fun `registerWithEmail with blank name, emit Error with correct message`() = runBlocking {
        val expectedUIText = UIText.StringResource(R.string.em_name_blank)
        val fakeAuthApi = FakeAuthApi()
        val authRepository = AuthRepositoryImpl(fakeAuthApi)

        val fakePassword = TestFaker.validPassword
        authRepository.registerWithEmail(
            name = "",
            email = TestFaker.validEmail,
            password = fakePassword,
            repeatedPassword = fakePassword,
        ).test {
            assertThat(awaitItem()).isInstanceOf(Resource.Loading::class.java)
            val errorResource = awaitItem()
            assertThat(errorResource).isInstanceOf(Resource.Error::class.java)
            assertThat(errorResource.uiText).isEqualTo(expectedUIText)
            awaitComplete()
        }
    }

    @Test
    fun `registerWithEmail with blank email, emit Error with correct message`() = runBlocking {
        val expectedUIText = UIText.StringResource(R.string.em_email_blank)
        val fakeAuthApi = FakeAuthApi()
        val authRepository = AuthRepositoryImpl(fakeAuthApi)

        val fakePassword = TestFaker.validPassword
        authRepository.registerWithEmail(
            name = TestFaker.validName,
            email = "",
            password = fakePassword,
            repeatedPassword = fakePassword,
        ).test {
            assertThat(awaitItem()).isInstanceOf(Resource.Loading::class.java)
            val errorResource = awaitItem()
            assertThat(errorResource).isInstanceOf(Resource.Error::class.java)
            assertThat(errorResource.uiText).isEqualTo(expectedUIText)
            awaitComplete()
        }
    }

    @Test
    fun `registerWithEmail with invalid email, emit Error with correct message`() = runBlocking {
        val expectedUIText = UIText.StringResource(R.string.em_invalid_email)
        val fakeAuthApi = FakeAuthApi()
        val authRepository = AuthRepositoryImpl(fakeAuthApi)

        val fakePassword = TestFaker.validPassword
        authRepository.registerWithEmail(
            name = TestFaker.validName,
            email = TestFaker.invalidEmail,
            password = fakePassword,
            repeatedPassword = fakePassword,
        ).test {
            assertThat(awaitItem()).isInstanceOf(Resource.Loading::class.java)
            val errorResource = awaitItem()
            assertThat(errorResource).isInstanceOf(Resource.Error::class.java)
            assertThat(errorResource.uiText).isEqualTo(expectedUIText)
            awaitComplete()
        }
    }

    @Test
    fun `registerWithEmail blank password, emit Error with correct message`() = runBlocking {
        val expectedUIText = UIText.StringResource(R.string.em_password_blank)
        val fakeAuthApi = FakeAuthApi()
        val authRepository = AuthRepositoryImpl(fakeAuthApi)

        authRepository.registerWithEmail(
            name = TestFaker.validName,
            email = TestFaker.validEmail,
            password = "",
            repeatedPassword = TestFaker.validPassword,
        ).test {
            assertThat(awaitItem()).isInstanceOf(Resource.Loading::class.java)
            val errorResource = awaitItem()
            assertThat(errorResource).isInstanceOf(Resource.Error::class.java)
            assertThat(errorResource.uiText).isEqualTo(expectedUIText)
            awaitComplete()
        }
    }

    @Test
    fun `registerWithEmail password shorter than min len requirement, emit Error with correct message`() =
        runBlocking {
            val expectedUIText = UIText.StringResource(R.string.em_register_password_too_short)
            val fakeAuthApi = FakeAuthApi()
            val authRepository = AuthRepositoryImpl(fakeAuthApi)

            val fakePassword = TestFaker.getLorem(
                length = AuthRepositoryImpl.MINIMUM_PASSWORD_LENGTH - 1
            )
            authRepository.registerWithEmail(
                name = TestFaker.validName,
                email = TestFaker.validEmail,
                password = fakePassword,
                repeatedPassword = fakePassword,
            ).test {
                assertThat(awaitItem()).isInstanceOf(Resource.Loading::class.java)
                val errorResource = awaitItem()
                assertThat(errorResource).isInstanceOf(Resource.Error::class.java)
                assertThat(errorResource.uiText).isEqualTo(expectedUIText)
                awaitComplete()
            }
        }

    @Test
    fun `registerWithEmail blank repeatPassword, emit Error with correct message`() = runBlocking {
        val expectedUIText = UIText.StringResource(R.string.em_repeat_password_blank)
        val fakeAuthApi = FakeAuthApi()
        val authRepository = AuthRepositoryImpl(fakeAuthApi)

        authRepository.registerWithEmail(
            name = TestFaker.validName,
            email = TestFaker.validEmail,
            password = TestFaker.validPassword,
            repeatedPassword = "",
        ).test {
            assertThat(awaitItem()).isInstanceOf(Resource.Loading::class.java)
            val errorResource = awaitItem()
            assertThat(errorResource).isInstanceOf(Resource.Error::class.java)
            assertThat(errorResource.uiText).isEqualTo(expectedUIText)
            awaitComplete()
        }
    }

    @Test
    fun `registerWithEmail password doesn't watch with repeatPassword, emit Error with correct message`() =
        runBlocking {
            val expectedUIText = UIText.StringResource(R.string.em_password_not_match)
            val fakeAuthApi = FakeAuthApi()
            val authRepository = AuthRepositoryImpl(fakeAuthApi)

            val fakePassword = TestFaker.validPassword
            authRepository.registerWithEmail(
                name = TestFaker.validName,
                email = TestFaker.validEmail,
                password = fakePassword,
                repeatedPassword = fakePassword.plus("00"),
            ).test {
                assertThat(awaitItem()).isInstanceOf(Resource.Loading::class.java)
                val errorResource = awaitItem()
                assertThat(errorResource).isInstanceOf(Resource.Error::class.java)
                assertThat(errorResource.uiText).isEqualTo(expectedUIText)
                awaitComplete()
            }
        }


    @Test
    fun `Attempt sendPasswordResetEmail, emit Loading first`() = runBlocking {
        val fakeAuthApi = FakeAuthApi()
        val authRepository = AuthRepositoryImpl(fakeAuthApi)

        authRepository.sendPasswordResetEmail(
            email = TestFaker.validEmail,
        ).test {
            assertThat(awaitItem()).isInstanceOf(Resource.Loading::class.java)
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `Successful sendPasswordResetEmail, emit Success`() = runBlocking {
        val fakeAuthApi = FakeAuthApi(fakeSendPasswordResetEmailResult = Resource.Success(Unit))
        val authRepository = AuthRepositoryImpl(fakeAuthApi)

        authRepository.sendPasswordResetEmail(
            email = TestFaker.validEmail,
        ).test {
            assertThat(awaitItem()).isInstanceOf(Resource.Loading::class.java)
            assertThat(awaitItem()).isInstanceOf(Resource.Success::class.java)
            awaitComplete()
        }
    }

    @Test
    fun `API error when sendPasswordResetEmail, emit Error with correct message`() = runBlocking {
        val expectedUIText = UIText.DynamicString(TestFaker.getLorem())
        val fakeAuthApi =
            FakeAuthApi(fakeSendPasswordResetEmailResult = Resource.Error(expectedUIText))
        val authRepository = AuthRepositoryImpl(fakeAuthApi)

        authRepository.sendPasswordResetEmail(
            email = TestFaker.validEmail,
        ).test {
            assertThat(awaitItem()).isInstanceOf(Resource.Loading::class.java)
            val errorResource = awaitItem()
            assertThat(errorResource).isInstanceOf(Resource.Error::class.java)
            assertThat(errorResource.uiText).isEqualTo(expectedUIText)
            awaitComplete()
        }
    }

    @Test
    fun `sendPasswordResetEmail with blank email, emit Error with correct message`() = runBlocking {
        val expectedUIText = UIText.StringResource(R.string.em_email_blank)
        val fakeAuthApi = FakeAuthApi()
        val authRepository = AuthRepositoryImpl(fakeAuthApi)

        authRepository.sendPasswordResetEmail(
            email = "",
        ).test {
            assertThat(awaitItem()).isInstanceOf(Resource.Loading::class.java)
            val errorResource = awaitItem()
            assertThat(errorResource).isInstanceOf(Resource.Error::class.java)
            assertThat(errorResource.uiText).isEqualTo(expectedUIText)
            awaitComplete()
        }
    }

    @Test
    fun `sendPasswordResetEmail invalid email, emit Error with correct message`() = runBlocking {
        val expectedUIText = UIText.StringResource(R.string.em_invalid_email)
        val fakeAuthApi = FakeAuthApi()
        val authRepository = AuthRepositoryImpl(fakeAuthApi)

        authRepository.sendPasswordResetEmail(
            email = TestFaker.invalidEmail,
        ).test {
            assertThat(awaitItem()).isInstanceOf(Resource.Loading::class.java)
            val errorResource = awaitItem()
            assertThat(errorResource).isInstanceOf(Resource.Error::class.java)
            assertThat(errorResource.uiText).isEqualTo(expectedUIText)
            awaitComplete()
        }
    }

    @Test
    fun `Attempt logout, isLoggedIn emits false`() = runBlocking {
        val fakeAuthApi = FakeAuthApi(fakeIsLoggedIn = true)
        val authRepository = AuthRepositoryImpl(fakeAuthApi)

        authRepository.logout()
        authRepository.isLoggedIn.test {
            assertThat(awaitItem()).isFalse()
            awaitComplete()
        }
    }
}