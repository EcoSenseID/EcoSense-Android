package com.ecosense.android.featForums.presentation.storyDetail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ecosense.android.R
import com.ecosense.android.core.util.Resource
import com.ecosense.android.core.util.UIText
import com.ecosense.android.featForums.domain.model.Comment
import com.ecosense.android.featForums.domain.repository.ForumsRepository
import com.ecosense.android.featForums.presentation.model.StoryPresentation
import com.ecosense.android.featForums.presentation.paginator.DefaultPaginator
import com.ecosense.android.featForums.presentation.storyDetail.model.CommentsFeedState
import com.ecosense.android.featForums.presentation.storyDetail.model.toPresentation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StoryDetailViewModel @Inject constructor(
    private val forumsRepository: ForumsRepository,
) : ViewModel() {

    private var storyId: Int? = null

    var commentsFeedState by mutableStateOf(CommentsFeedState.defaultValue)
        private set

    private val paginator = DefaultPaginator(
        initialKey = commentsFeedState.page,
        getNextKey = { commentsFeedState.page + 1 },
        onRequest = { nextPage: Int ->
            storyId?.let { id ->
                forumsRepository.getComments(
                    storyId = id,
                    page = nextPage,
                    size = 20,
                )
            } ?: Resource.Error(uiText = UIText.StringResource(R.string.em_comments))
        },
        onLoadUpdated = { isLoading ->
            commentsFeedState = commentsFeedState.copy(isLoading = isLoading)
        },
        onError = { message: UIText? ->
            commentsFeedState = commentsFeedState.copy(errorMessage = message)
        },
        onSuccess = { items: List<Comment>?, newKey: Int ->
            val newComments = items?.map { it.toPresentation() }
            commentsFeedState = commentsFeedState.copy(
                comments = commentsFeedState.comments + (newComments ?: emptyList()),
                page = newKey,
                isEndReached = items?.isEmpty() ?: false,
            )
        }
    )

    fun setStory(story: StoryPresentation) {
        storyId = story.id
        onLoadNextCommentsFeed()
    }

    fun onLoadNextCommentsFeed() {
        viewModelScope.launch {
            paginator.loadNextItems()
        }
    }
}