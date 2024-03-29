package com.ecosense.android.featForums.presentation.forums

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ecosense.android.core.domain.model.Story
import com.ecosense.android.core.domain.repository.AuthRepository
import com.ecosense.android.core.presentation.util.UIEvent
import com.ecosense.android.core.util.Resource
import com.ecosense.android.core.util.UIText
import com.ecosense.android.featForums.domain.repository.ForumsRepository
import com.ecosense.android.featForums.presentation.forums.model.StoriesFeedState
import com.ecosense.android.featForums.presentation.model.StoryPresentation
import com.ecosense.android.featForums.presentation.model.toPresentation
import com.ecosense.android.featForums.presentation.paginator.DefaultPaginator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import logcat.logcat
import javax.inject.Inject

@HiltViewModel
class ForumsViewModel @Inject constructor(
    private val forumsRepository: ForumsRepository,
    authRepository: AuthRepository,
) : ViewModel() {

    val isLoggedIn: StateFlow<Boolean?> =
        authRepository.isLoggedIn.stateIn(viewModelScope, SharingStarted.Eagerly, null)

    private val _stories = mutableStateListOf<StoryPresentation>()
    val stories: List<StoryPresentation> = _stories

    var feedState by mutableStateOf(StoriesFeedState.defaultValue)
        private set

    private val _eventFlow = Channel<UIEvent>()
    val eventFlow = _eventFlow.receiveAsFlow()

    var isRefreshingFeed by mutableStateOf(false)
        private set

    private val feedPaginator = DefaultPaginator(
        initialKey = feedState.page,
        getNextKey = { feedState.page + 1 },
        onRequest = { nextPage ->
            feedState = feedState.copy(errorMessage = null)
            forumsRepository.getStories(nextPage, 20)
        },
        onLoadUpdated = { isLoading -> feedState = feedState.copy(isLoading = isLoading) },
        onError = { message: UIText? ->
            feedState = feedState.copy(
                isLoading = false,
                errorMessage = message,
            )
            if (isRefreshingFeed) isRefreshingFeed = false
        },
        onSuccess = { items: List<Story>?, newKey: Int ->
            val newStories = items?.map { it.toPresentation() } ?: emptyList()
            if (isRefreshingFeed) {
                _stories.clear()
                isRefreshingFeed = false
            }
            _stories.addAll(newStories)
            feedState = feedState.copy(
                page = newKey,
                isEndReached = newStories.isEmpty(),
                errorMessage = null,
            )
        },
    )

    init {
        onLoadNextStoriesFeed()
    }

    private var onLoadNextStoriesFeedJob: Job? = null
    fun onLoadNextStoriesFeed() {
        onLoadNextStoriesFeedJob?.cancel()
        onLoadNextStoriesFeedJob = viewModelScope.launch {
            feedPaginator.loadNextItems()
        }
    }

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

    fun refreshStoriesFeed() {
        isRefreshingFeed = true
        feedState = StoriesFeedState.defaultValue
        feedPaginator.reset()
        onLoadNextStoriesFeed()
    }
}