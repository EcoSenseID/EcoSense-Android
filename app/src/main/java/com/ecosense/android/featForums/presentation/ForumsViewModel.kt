package com.ecosense.android.featForums.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ecosense.android.core.domain.repository.AuthRepository
import com.ecosense.android.core.util.UIText
import com.ecosense.android.featForums.domain.model.Story
import com.ecosense.android.featForums.domain.repository.ForumsRepository
import com.ecosense.android.featForums.presentation.model.StoriesState
import com.ecosense.android.featForums.presentation.model.StoryComposerState
import com.ecosense.android.featForums.presentation.model.toPresentation
import com.ecosense.android.featForums.presentation.paginator.DefaultPaginator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ForumsViewModel @Inject constructor(
    private val forumsRepository: ForumsRepository,
    private val authRepository: AuthRepository,
) : ViewModel() {

    var storiesState by mutableStateOf(StoriesState.defaultValue)
        private set

    var storyComposerState by mutableStateOf(StoryComposerState.defaultValue)
        private set

    private val paginator = DefaultPaginator(
        initialKey = storiesState.page,
        getNextKey = { storiesState.page + 1 },
        onRequest = { nextPage -> forumsRepository.getStories(nextPage, 20) },
        onLoadUpdated = { isLoading -> storiesState = storiesState.copy(isLoading = isLoading) },
        onError = { message: UIText? -> storiesState = storiesState.copy(error = message) },
        onSuccess = { items: List<Story>?, newKey: Int ->
            val newStories = items?.map { it.toPresentation() }
            storiesState = storiesState.copy(
                stories = storiesState.stories + (newStories ?: emptyList()),
                page = newKey,
                endReached = items?.isEmpty() ?: false,
            )
        }
    )

    init {
        loadNextItems()
        viewModelScope.launch {
            storyComposerState = storyComposerState.copy(
                profilePictureUrl = authRepository.getCurrentUser()?.photoUrl,
            )
        }
    }

    fun loadNextItems() {
        viewModelScope.launch {
            paginator.loadNextItems()
        }
    }

    fun onComposerCaptionChange(value: String) {
        storyComposerState = storyComposerState.copy(caption = value)
    }
}