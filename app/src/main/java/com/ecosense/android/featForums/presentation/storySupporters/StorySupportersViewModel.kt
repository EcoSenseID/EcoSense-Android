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

    var isRefreshing by mutableStateOf(false)
        private set

    var state by mutableStateOf(StorySupportersState.defaultValue)
        private set

    private val paginator = DefaultPaginator(
        initialKey = state.page,
        getNextKey = { state.page + 1 },
        onRequest = { nextPage: Int ->
            state = state.copy(errorMessage = null)
            storyId?.let { id ->
                forumsRepository.getStorySupporters(
                    storyId = id,
                    page = nextPage,
                    size = 20,
                )
            } ?: Resource.Error(uiText = UIText.StringResource(R.string.em_comments))
        },
        onLoadUpdated = { isLoading -> state = state.copy(isLoading = isLoading) },
        onError = { message: UIText? ->
            state = state.copy(
                isLoading = false,
                errorMessage = message,
            )
            if (isRefreshing) isRefreshing = false
        },
        onSuccess = { items: List<Supporter>?, newKey: Int ->
            val newItems = items?.map { it.toPresentation() }

            if (isRefreshing) {
                state = state.copy(supporters = emptyList())
                isRefreshing = false
            }

            state = state.copy(
                supporters = state.supporters + (newItems ?: emptyList()),
                page = newKey,
                isEndReached = items?.isEmpty() ?: false,
                errorMessage = null,
            )
        },
    )

    fun setStoryId(storyId: Int) {
        this.storyId = storyId
        onLoadNextSupporters()
    }

    fun onLoadNextSupporters() {
        viewModelScope.launch {
            paginator.loadNextItems()
        }
    }

    fun onRefresh() {
        isRefreshing = true
        state = StorySupportersState.defaultValue
        paginator.reset()
        onLoadNextSupporters()
    }
}