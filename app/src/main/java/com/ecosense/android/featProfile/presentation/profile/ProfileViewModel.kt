package com.ecosense.android.featProfile.presentation.profile

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ecosense.android.core.domain.repository.AuthRepository
import com.ecosense.android.core.presentation.util.UIEvent
import com.ecosense.android.core.util.Resource
import com.ecosense.android.featForums.domain.repository.ForumsRepository
import com.ecosense.android.featForums.presentation.model.StoryPresentation
import com.ecosense.android.featForums.presentation.model.toPresentation
import com.ecosense.android.featProfile.domain.repository.ProfileRepository
import com.ecosense.android.featProfile.presentation.model.toPresentation
import com.ecosense.android.featProfile.presentation.profile.model.ProfileScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val profileRepository: ProfileRepository,
    private val forumsRepository: ForumsRepository,
) : ViewModel() {

    val isLoggedIn: StateFlow<Boolean?> = authRepository.isLoggedIn
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    private val _state = mutableStateOf(ProfileScreenState.defaultValue)
    val state: State<ProfileScreenState> = _state

    private val _recentStories = mutableStateListOf<StoryPresentation>()
    val recentStories: List<StoryPresentation> = _recentStories

    private val _eventFlow = Channel<UIEvent>()
    val eventFlow = _eventFlow.receiveAsFlow()

    init {
        onRefreshProfile()
    }

    private var onRefreshProfileJob: Job? = null
    fun onRefreshProfile() {
        onRefreshProfileJob?.cancel()
        onRefreshProfileJob = viewModelScope.launch {
            val user = authRepository.getCurrentUser()
            user?.let { _state.value = state.value.copy(user = it) }

            profileRepository.getProfile().onEach { result ->
                when (result) {
                    is Resource.Error -> {
                        _state.value = state.value.copy(isLoading = false)
                        result.uiText?.let { _eventFlow.send(UIEvent.ShowSnackbar(it)) }
                    }

                    is Resource.Loading -> {
                        _state.value = state.value.copy(
                            isLoading = true,
                            totalEcoPoints = result.data?.totalEcoPoints
                                ?: state.value.totalEcoPoints,
                            recentCampaigns = result.data?.recentCampaigns?.map { it.toPresentation() }
                                ?: state.value.recentCampaigns,
                        )
                        result.data?.recentStories?.map { it.toPresentation() }?.let {
                            _recentStories.clear()
                            _recentStories.addAll(it)
                        }
                    }

                    is Resource.Success -> {
                        _state.value = state.value.copy(
                            isLoading = false,
                            totalEcoPoints = result.data?.totalEcoPoints
                                ?: state.value.totalEcoPoints,
                            recentCampaigns = result.data?.recentCampaigns?.map { it.toPresentation() }
                                ?: state.value.recentCampaigns,
                        )

                        result.data?.recentStories?.map { it.toPresentation() }?.let {
                            _recentStories.clear()
                            _recentStories.addAll(it)
                        }
                    }
                }
            }.launchIn(this)
        }
    }

    private var onClickSupportJob: Job? = null
    fun onClickSupport(storyId: Int) {
        onClickSupportJob?.cancel()
        onClickSupportJob = viewModelScope.launch {
            val storyIndex = recentStories.indexOfFirst { it.id == storyId }
            if (storyIndex == -1) return@launch

            val oldStory = recentStories[storyIndex]

            (if (oldStory.isSupported) forumsRepository.postUnsupportStory(storyId = storyId)
            else forumsRepository.postSupportStory(storyId = storyId)).onEach { result ->
                when (result) {
                    is Resource.Error -> {
                        _recentStories[storyIndex] = oldStory.copy(isLoadingSupport = false)
                        result.uiText?.let { _eventFlow.send(UIEvent.ShowSnackbar(it)) }
                    }
                    is Resource.Loading -> {
                        _recentStories[storyIndex] = oldStory.copy(isLoadingSupport = true)
                    }
                    is Resource.Success -> {
                        _recentStories[storyIndex] = oldStory.copy(isLoadingSupport = false)
                        _recentStories[storyIndex] = oldStory.copy(
                            supportersCount = if (oldStory.isSupported) oldStory.supportersCount - 1
                            else oldStory.supportersCount + 1,
                            isSupported = !oldStory.isSupported,
                        )
                    }
                }
            }.launchIn(this)
        }
    }

    private var setExpandDropdownMenuJob: Job? = null
    fun setExpandDropdownMenu(visible: Boolean) {
        setExpandDropdownMenuJob = viewModelScope.launch {
            _state.value = state.value.copy(
                isDropdownMenuExpanded = visible
            )
        }
    }
}