package com.ecosense.android.featForums.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ecosense.android.core.util.UIText
import com.ecosense.android.featForums.domain.model.Story
import com.ecosense.android.featForums.domain.repository.ForumsRepository
import com.ecosense.android.featForums.presentation.paginator.DefaultPaginator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ForumsViewModel @Inject constructor(
    private val forumsRepository: ForumsRepository
) : ViewModel() {

    var storiesState by mutableStateOf(StoriesState.defaultValue)
        private set

    private val paginator = DefaultPaginator(
        initialKey = storiesState.page,
        getNextKey = { storiesState.page + 1 },
        onRequest = { nextPage -> forumsRepository.getStories(nextPage, 20) },
        onLoadUpdated = { isLoading -> storiesState = storiesState.copy(isLoading = isLoading) },
        onError = { message: UIText? -> storiesState = storiesState.copy(error = message) },
        onSuccess = { items: List<Story>?, newKey: Int ->
            storiesState = storiesState.copy(
                stories = storiesState.stories + (items ?: emptyList()),
                page = newKey,
                endReached = items?.isEmpty() ?: false,
            )
        }
    )

    init {
        loadNextItems()
    }

    fun loadNextItems() {
        viewModelScope.launch {
            paginator.loadNextItems()
        }
    }
}