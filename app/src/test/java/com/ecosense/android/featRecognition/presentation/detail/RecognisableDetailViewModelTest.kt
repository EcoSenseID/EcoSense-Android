package com.ecosense.android.featRecognition.presentation.detail

import app.cash.turbine.test
import com.ecosense.android.R
import com.ecosense.android.core.presentation.util.UIEvent
import com.ecosense.android.core.util.Resource
import com.ecosense.android.core.util.UIText
import com.ecosense.android.featRecognition.data.repository.FakeRecognitionRepository
import com.ecosense.android.featRecognition.domain.model.RecognisableDetail
import com.ecosense.android.featRecognition.presentation.model.toParcelable
import com.ecosense.android.util.MainCoroutineRule
import com.ecosense.android.util.TestFaker
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class RecognisableDetailViewModelTest {

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Test
    fun `getState, default state`() = runBlocking {
        val recognisableDetailViewModel = RecognisableDetailViewModel(
            recognitionRepository = FakeRecognitionRepository()
        )

        assertThat(recognisableDetailViewModel.state.value)
            .isEqualTo(RecognisableDetailScreenState.defaultValue)
    }

    @Test
    fun `setDetailParcelable, correct state`() = runBlocking {
        val expectedRecognisableDetail = RecognisableDetail(
            id = 420,
            label = TestFaker.getLorem(),
            savedAt = 1654612972,
            confidencePercent = 69
        )
        val recognisableDetailViewModel = RecognisableDetailViewModel(
            recognitionRepository = FakeRecognitionRepository()
        )

        recognisableDetailViewModel.setDetailParcelable(expectedRecognisableDetail.toParcelable())
        assertThat(recognisableDetailViewModel.state.value.recognisableDetail)
            .isEqualTo(expectedRecognisableDetail)
    }

    @Test
    fun `Saving is in progress, correct state`() = runBlocking {
        val recognisableDetailViewModel = RecognisableDetailViewModel(
            recognitionRepository = FakeRecognitionRepository(
                fakeSaveRecognisableResult = Resource.Loading()
            )
        )

        recognisableDetailViewModel.onSaveClick()

        assertThat(recognisableDetailViewModel.state.value.isLoadingSaving).isTrue()
    }

    @Test
    fun `Saving is successful, correct state`() = runBlocking {
        val expectedId = 69
        val recognisableDetailViewModel = RecognisableDetailViewModel(
            recognitionRepository = FakeRecognitionRepository(
                fakeSaveRecognisableResult = Resource.Success(expectedId)
            )
        )

        recognisableDetailViewModel.onSaveClick()

        assertThat(recognisableDetailViewModel.state.value.isLoadingSaving).isFalse()
        assertThat(recognisableDetailViewModel.state.value.recognisableDetail.id).isEqualTo(expectedId)
    }

    @Test
    fun `Saving is unsuccessful, correct state`() = runBlocking {
        val expectedUiText = UIText.DynamicString(TestFaker.getLorem())
        val recognisableDetailViewModel = RecognisableDetailViewModel(
            recognitionRepository = FakeRecognitionRepository(
                fakeSaveRecognisableResult = Resource.Error(expectedUiText)
            )
        )

        recognisableDetailViewModel.onSaveClick()

        assertThat(recognisableDetailViewModel.state.value.isLoadingSaving).isFalse()
        recognisableDetailViewModel.eventFlow.test {
            val event = awaitItem()
            assertThat(event).isInstanceOf(UIEvent.ShowSnackbar::class.java)
            assertThat((event as UIEvent.ShowSnackbar).uiText).isEqualTo(expectedUiText)
        }
    }
}