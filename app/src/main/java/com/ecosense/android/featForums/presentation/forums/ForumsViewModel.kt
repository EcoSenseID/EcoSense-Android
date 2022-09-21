package com.ecosense.android.featForums.presentation.forums

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ecosense.android.core.domain.repository.AuthRepository
import com.ecosense.android.core.util.UIText
import com.ecosense.android.featForums.domain.model.Story
import com.ecosense.android.featForums.domain.repository.ForumsRepository
import com.ecosense.android.featForums.presentation.forums.model.StoriesFeedState
import com.ecosense.android.featForums.presentation.forums.model.StoryComposerState
import com.ecosense.android.featForums.presentation.forums.model.SupportersDialogState
import com.ecosense.android.featForums.presentation.model.StoryPresentation
import com.ecosense.android.featForums.presentation.model.toPresentation
import com.ecosense.android.featForums.presentation.paginator.DefaultPaginator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ForumsViewModel @Inject constructor(
    private val forumsRepository: ForumsRepository,
    private val authRepository: AuthRepository,
) : ViewModel() {

    var storiesFeedState by mutableStateOf(StoriesFeedState.defaultValue)
        private set

    var storyComposerState by mutableStateOf(StoryComposerState.defaultValue)
        private set

    var supportersDialogState by mutableStateOf(SupportersDialogState.defaultValue)
        private set

    private val storiesPaginator = DefaultPaginator(initialKey = storiesFeedState.page,
        getNextKey = { storiesFeedState.page + 1 },
        onRequest = { nextPage -> forumsRepository.getStories(nextPage, 20) },
        onLoadUpdated = { isLoading ->
            storiesFeedState = storiesFeedState.copy(isLoading = isLoading)
        },
        onError = { message: UIText? ->
            storiesFeedState = storiesFeedState.copy(errorMessage = message)
        },
        onSuccess = { items: List<Story>?, newKey: Int ->
            val newStories = items?.map { it.toPresentation() }
            storiesFeedState = storiesFeedState.copy(
                stories = storiesFeedState.stories + (newStories ?: emptyList()),
                page = newKey,
                isEndReached = items?.isEmpty() ?: false,
            )
        })

    init {
        onLoadNextStoriesFeed()
        viewModelScope.launch {
            storyComposerState = storyComposerState.copy(
                profilePictureUrl = authRepository.getCurrentUser()?.photoUrl,
            )
        }
    }

    private var onLoadNextStoriesFeedJob: Job? = null
    fun onLoadNextStoriesFeed() {
        onLoadNextStoriesFeedJob?.cancel()
        onLoadNextStoriesFeedJob = viewModelScope.launch {
            storiesPaginator.loadNextItems()
        }
    }

    private var onComposerCaptionChangeJob: Job? = null
    fun onComposerCaptionChange(value: String) {
        onComposerCaptionChangeJob?.cancel()
        onComposerCaptionChangeJob = viewModelScope.launch {
            storyComposerState = storyComposerState.copy(caption = value)
        }
    }

    private var onClickSupportJob: Job? = null
    fun onClickSupport(story: StoryPresentation) {
        onClickSupportJob?.cancel()
        onClickSupportJob = viewModelScope.launch {
            /* TODO */
        }
    }

    private var onClickSupportersJob: Job? = null
    fun onClickSupporters(story: StoryPresentation) {
        onClickSupportersJob?.cancel()
        onClickSupportersJob = viewModelScope.launch {
            supportersDialogState = supportersDialogState.copy(
                isVisible = true,
                storyId = story.id,
            )
        }
    }

    private var onDismissSupportersDialogRequestJob: Job? = null
    fun onDismissSupportersDialogRequest() {
        onDismissSupportersDialogRequestJob?.cancel()
        onDismissSupportersDialogRequestJob = viewModelScope.launch {
            supportersDialogState = supportersDialogState.copy(
                isVisible = false,
                storyId = null,
            )
        }
    }
}