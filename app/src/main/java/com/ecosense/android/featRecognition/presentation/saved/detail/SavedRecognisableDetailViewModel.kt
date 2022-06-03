package com.ecosense.android.featRecognition.presentation.saved.detail

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ecosense.android.featRecognition.domain.model.SavedRecognisable
import com.ecosense.android.featRecognition.domain.repository.RecognitionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SavedRecognisableDetailViewModel @Inject constructor(
    private val recognitionRepository: RecognitionRepository
) : ViewModel() {
    private val savedRecognisableId: MutableState<Int?> = mutableStateOf(null)

    private val savedRecognisable = mutableStateOf(
        SavedRecognisable(
            id = null,
            label = "",
            confidencePercent = 0,
            timeInMillis = 8989898,
            symptoms = "",
            treatment = "",
            preventiveMeasure = ""
        )
    )

    val state: State<SavedRecognisable> = savedRecognisable

    fun setResultId(id: Int) {
        savedRecognisableId.value = id

        viewModelScope.launch {
            val result = recognitionRepository.getSavedRecognisable(id)
            if (result != null) {
                savedRecognisable.value = result
            }
        }
    }
}