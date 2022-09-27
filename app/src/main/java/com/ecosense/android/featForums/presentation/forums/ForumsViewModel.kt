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
) : ViewModel() {

    var feedState by mutableStateOf(StoriesFeedState.defaultValue)
        private set

    private val feedPaginator = DefaultPaginator(initialKey = feedState.page,
        getNextKey = { feedState.page + 1 },
        onRequest = { nextPage -> forumsRepository.getStories(nextPage, 20) },
        onLoadUpdated = { isLoading -> feedState = feedState.copy(isLoading = isLoading) },
        onError = { message: UIText? -> feedState = feedState.copy(errorMessage = message) },
        onSuccess = { items: List<Story>?, newKey: Int ->
            val newStories = items?.map { it.toPresentation() }
            feedState = feedState.copy(
                stories = feedState.stories + (newStories ?: emptyList()),
                page = newKey,
                isEndReached = items?.isEmpty() ?: false,
            )
        })

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
            /* TODO */
        }
    }
}