package com.ecosense.android.featForums.presentation.storyComposer

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ecosense.android.core.domain.repository.AuthRepository
import com.ecosense.android.featForums.domain.repository.ForumsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StoryComposerViewModel @Inject constructor(
    private val forumsRepository: ForumsRepository,
    private val authRepository: AuthRepository,
) : ViewModel() {

    var state by mutableStateOf(StoryComposerState.defaultValue)
        private set

    init {
        viewModelScope.launch {
            authRepository.getCurrentUser()?.let {
                state = state.copy(
                    avatarUrl = it.photoUrl,
                )
            }
        }
    }

    private var onChangeCaptionJob: Job? = null
    fun onChangeCaption(value: String) {
        onChangeCaptionJob?.cancel()
        onChangeCaptionJob = viewModelScope.launch {
            state = state.copy(
                caption = value,
            )
        }
    }

    private var onClickSendJob: Job? = null
    fun onClickSend() {
        onClickSendJob?.cancel()
        onClickSendJob = viewModelScope.launch {
            // TODO: not yet implemented
        }
    }
}
