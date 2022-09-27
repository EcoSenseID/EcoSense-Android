package com.ecosense.android.featForums.presentation.storyDetail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ecosense.android.R
import com.ecosense.android.core.domain.repository.AuthRepository
import com.ecosense.android.core.util.Resource
import com.ecosense.android.core.util.UIText
import com.ecosense.android.featForums.domain.model.Reply
import com.ecosense.android.featForums.domain.repository.ForumsRepository
import com.ecosense.android.featForums.presentation.model.StoryPresentation
import com.ecosense.android.featForums.presentation.paginator.DefaultPaginator
import com.ecosense.android.featForums.presentation.storyDetail.model.RepliesFeedState
import com.ecosense.android.featForums.presentation.storyDetail.model.ReplyComposerState
import com.ecosense.android.featForums.presentation.storyDetail.model.toPresentation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StoryDetailViewModel @Inject constructor(
    private val forumsRepository: ForumsRepository,
    private val authRepository: AuthRepository,
) : ViewModel() {

    private var storyId: Int? = null

    var replyComposerState by mutableStateOf(ReplyComposerState.defaultValue)
        private set

    var repliesState by mutableStateOf(RepliesFeedState.defaultValue)
        private set

    private val paginator = DefaultPaginator(initialKey = repliesState.page,
        getNextKey = { repliesState.page + 1 },
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
            repliesState = repliesState.copy(isLoading = isLoading)
        },
        onError = { message: UIText? ->
            repliesState = repliesState.copy(errorMessage = message)
        },
        onSuccess = { items: List<Reply>?, newKey: Int ->
            val newComments = items?.map { it.toPresentation() }
            repliesState = repliesState.copy(
                replies = repliesState.replies + (newComments ?: emptyList()),
                page = newKey,
                isEndReached = items?.isEmpty() ?: false,
            )
        })

    init {
        viewModelScope.launch {
            authRepository.getCurrentUser()?.let {
                replyComposerState = replyComposerState.copy(
                    avatarUrl = it.photoUrl,
                )
            }
        }
    }

    fun setStory(story: StoryPresentation) {
        storyId = story.id
        onLoadNextCommentsFeed()
    }

    private var onLoadNextCommentsFeedJob: Job? = null
    fun onLoadNextCommentsFeed() {
        onLoadNextCommentsFeedJob?.cancel()
        onLoadNextCommentsFeedJob = viewModelScope.launch {
            paginator.loadNextItems()
        }
    }

    private var onChangeReplyComposerCaptionJob: Job? = null
    fun onChangeReplyComposerCaption(value: String) {
        onChangeReplyComposerCaptionJob?.cancel()
        onChangeReplyComposerCaptionJob = viewModelScope.launch {
            replyComposerState = replyComposerState.copy(
                caption = value,
            )
        }
    }
}