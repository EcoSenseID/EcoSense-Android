package com.ecosense.android.featRecognition.presentation.recognition

import com.ecosense.android.core.util.Resource
import com.ecosense.android.core.util.UIText
import com.ecosense.android.featRecognition.data.repository.FakeRecognitionRepository
import com.ecosense.android.util.MainCoroutineRule
import com.ecosense.android.util.TestFaker
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class RecognitionViewModelTest {

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Test
    fun `getState, default state`() = runBlocking {
        val recognitionViewModel = RecognitionViewModel(
            recognitionRepository = FakeRecognitionRepository()
        )

        assertThat(recognitionViewModel.state.value).isEqualTo(RecognitionState.defaultValue)
    }

    @Test
    fun `Saving is unsuccessful, correct state`() = runBlocking {
        val recognitionViewModel = RecognitionViewModel(
            recognitionRepository = FakeRecognitionRepository(
                fakeSaveRecognisableResult = Resource.Error(UIText.DynamicString(TestFaker.getLorem()))
            )
        )

        recognitionViewModel.onSaveResult()

        assertThat(recognitionViewModel.state.value.isSavingResult).isFalse()
    }
}