package com.ecosense.android.featProfile.presentation.storyHistory

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ecosense.android.core.presentation.util.UIEvent
import com.ecosense.android.core.util.Resource
import com.ecosense.android.featForums.domain.repository.ForumsRepository
import com.ecosense.android.featForums.presentation.model.StoryPresentation
import com.ecosense.android.featForums.presentation.model.toPresentation
import com.ecosense.android.featProfile.domain.repository.ProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StoryHistoryViewModel @Inject constructor(
    private val profileRepository: ProfileRepository,
    private val forumsRepository: ForumsRepository,
) : ViewModel() {

    private var mUserId by mutableStateOf<Int?>(null)

    private val _stories = mutableStateListOf<StoryPresentation>()
    val stories: List<StoryPresentation> = _stories

    var isLoading by mutableStateOf(false)
        private set

    private val _eventFlow = Channel<UIEvent>()
    val eventFlow = _eventFlow.receiveAsFlow()

    private var onClickSupportJob: Job? = null
    fun onClickSupport(storyId: Int) {
        onClickSupportJob?.cancel()
        onClickSupportJob = viewModelScope.launch {
            val storyIndex = stories.indexOfFirst { it.id == storyId }
            if (storyIndex == -1) return@launch

            val oldStory = stories[storyIndex]

            (if (oldStory.isSupported) forumsRepository.postUnsupportStory(storyId = storyId)
            else forumsRepository.postSupportStory(storyId = storyId)).onEach { result ->
                when (result) {
                    is Resource.Error -> {
                        _stories[storyIndex] = oldStory.copy(isLoadingSupport = false)
                        result.uiText?.let { _eventFlow.send(UIEvent.ShowSnackbar(it)) }
                    }
                    is Resource.Loading -> {
                        _stories[storyIndex] = oldStory.copy(isLoadingSupport = true)
                    }
                    is Resource.Success -> {
                        _stories[storyIndex] = oldStory.copy(isLoadingSupport = false)
                        _stories[storyIndex] = oldStory.copy(
                            supportersCount = if (oldStory.isSupported) oldStory.supportersCount - 1
                            else oldStory.supportersCount + 1,
                            isSupported = !oldStory.isSupported,
                        )
                    }
                }
            }.launchIn(this)
        }
    }

    private var refreshJob: Job? = null
    fun refresh() {
        refreshJob?.cancel()
        refreshJob = viewModelScope.launch {
            profileRepository.getStoriesHistory(
                userId = mUserId,
            ).onEach { result ->
                when (result) {
                    is Resource.Error -> {
                        isLoading = false
                        result.uiText?.let { _eventFlow.send(UIEvent.ShowSnackbar(it)) }
                    }
                    is Resource.Loading -> isLoading = true
                    is Resource.Success -> {
                        isLoading = false
                        result.data?.let { storiesList ->
                            _stories.clear()
                            _stories.addAll(storiesList.map { it.toPresentation() })
                        }
                    }
                }
            }.launchIn(this)
        }
    }

    fun setUserId(userId: Int?) {
        mUserId = userId
        refresh()
    }
}