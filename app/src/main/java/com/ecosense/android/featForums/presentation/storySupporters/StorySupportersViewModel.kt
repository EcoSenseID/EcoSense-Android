package com.ecosense.android.featForums.presentation.storySupporters

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ecosense.android.R
import com.ecosense.android.core.util.Resource
import com.ecosense.android.core.util.UIText
import com.ecosense.android.featForums.domain.model.Supporter
import com.ecosense.android.featForums.domain.repository.ForumsRepository
import com.ecosense.android.featForums.presentation.forums.model.StorySupportersState
import com.ecosense.android.featForums.presentation.model.StoryPresentation
import com.ecosense.android.featForums.presentation.model.toPresentation
import com.ecosense.android.featForums.presentation.paginator.DefaultPaginator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StorySupportersViewModel @Inject constructor(
    private val forumsRepository: ForumsRepository,
) : ViewModel() {

    private var storyId: Int? = null

    var state by mutableStateOf(StorySupportersState.defaultValue)
        private set

    private val paginator = DefaultPaginator(initialKey = state.page,
        getNextKey = { state.page + 1 },
        onRequest = { nextPage: Int ->
            storyId?.let { id ->
                forumsRepository.getSupporters(
                    storyId = id,
                    page = nextPage,
                    size = 20,
                )
            } ?: Resource.Error(uiText = UIText.StringResource(R.string.em_comments))
        },
        onLoadUpdated = { isLoading -> state = state.copy(isLoading = isLoading) },
        onError = { message: UIText? -> state = state.copy(errorMessage = message) },
        onSuccess = { items: List<Supporter>?, newKey: Int ->
            val newItems = items?.map { it.toPresentation() }
            state = state.copy(
                supporters = state.supporters + (newItems ?: emptyList()),
                page = newKey,
                isEndReached = items?.isEmpty() ?: false,
            )
        })

    fun setStoryId(storyId: Int) {
        this.storyId = storyId
        onLoadNextCommentsFeed()
    }

    fun onLoadNextCommentsFeed() {
        viewModelScope.launch {
            paginator.loadNextItems()
        }
    }
}