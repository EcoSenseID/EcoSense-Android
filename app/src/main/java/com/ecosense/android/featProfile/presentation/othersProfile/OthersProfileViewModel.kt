package com.ecosense.android.featProfile.presentation.othersProfile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ecosense.android.core.presentation.util.UIEvent
import com.ecosense.android.core.util.Resource
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
class OthersProfileViewModel @Inject constructor(
    private val profileRepository: ProfileRepository,
//    private val forumsRepository: ForumsRepository,
) : ViewModel() {

    var profile by mutableStateOf(OthersProfilePresentation.defaultValue)
        private set

    var isRefreshing by mutableStateOf(false)
        private set
//
//    private val _recentStories = mutableStateListOf<StoryPresentation>()
//    val recentStories: List<StoryPresentation> = _recentStories

    private val _eventFlow = Channel<UIEvent>()
    val eventFlow = _eventFlow.receiveAsFlow()

    fun setUserId(userId: Int) {
        profile = profile.copy(userId = userId)
        onRefreshProfile()
    }

    private var onRefreshProfileJob: Job? = null
    fun onRefreshProfile() {
        onRefreshProfileJob?.cancel()
        onRefreshProfileJob = viewModelScope.launch {
            profileRepository.getOthersProfile(
                userId = profile.userId
            ).onEach { result ->
                when (result) {
                    is Resource.Error -> {
                        isRefreshing = false
                        result.uiText?.let { _eventFlow.send(UIEvent.ShowSnackbar(it)) }
                    }

                    is Resource.Loading -> isRefreshing = true

                    is Resource.Success -> {
                        isRefreshing = false
                        result.data?.let { profile = it.toPresentation() }
//                        result.data?.recentStories?.map { it.toPresentation() }?.let {
//                            _recentStories.clear()
//                            _recentStories.addAll(it)
//                        }
                    }
                }
            }.launchIn(this)
        }
    }
//
//    private var onClickSupportJob: Job? = null
//    fun onClickSupport(storyId: Int) {
//        onClickSupportJob?.cancel()
//        onClickSupportJob = viewModelScope.launch {
//            val storyIndex = recentStories.indexOfFirst { it.id == storyId }
//            if (storyIndex == -1) return@launch
//
//            val oldStory = recentStories[storyIndex]
//
//            (if (oldStory.isSupported) forumsRepository.postUnsupportStory(storyId = storyId)
//            else forumsRepository.postSupportStory(storyId = storyId)).onEach { result ->
//                when (result) {
//                    is Resource.Error -> {
//                        _recentStories[storyIndex] = oldStory.copy(isLoadingSupport = false)
//                        result.uiText?.let { _eventFlow.send(UIEvent.ShowSnackbar(it)) }
//                    }
//                    is Resource.Loading -> {
//                        _recentStories[storyIndex] = oldStory.copy(isLoadingSupport = true)
//                    }
//                    is Resource.Success -> {
//                        _recentStories[storyIndex] = oldStory.copy(isLoadingSupport = false)
//                        _recentStories[storyIndex] = oldStory.copy(
//                            supportersCount = if (oldStory.isSupported) oldStory.supportersCount - 1
//                            else oldStory.supportersCount + 1,
//                            isSupported = !oldStory.isSupported,
//                        )
//                    }
//                }
//            }.launchIn(this)
//        }
//    }
}