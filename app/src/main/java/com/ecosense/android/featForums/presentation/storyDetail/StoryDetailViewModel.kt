package com.ecosense.android.featForums.presentation.storyDetail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.focus.FocusState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ecosense.android.core.domain.repository.AuthRepository
import com.ecosense.android.core.presentation.util.UIEvent
import com.ecosense.android.core.util.Resource
import com.ecosense.android.core.util.UIText
import com.ecosense.android.featForums.domain.model.Reply
import com.ecosense.android.featForums.domain.repository.ForumsRepository
import com.ecosense.android.featForums.presentation.model.StoryPresentation
import com.ecosense.android.featForums.presentation.paginator.DefaultPaginator
import com.ecosense.android.featForums.presentation.storyDetail.model.RepliesFeedState
import com.ecosense.android.featForums.presentation.storyDetail.model.ReplyComposerState
import com.ecosense.android.featForums.presentation.storyDetail.model.ReplyPresentation
import com.ecosense.android.featForums.presentation.storyDetail.model.toPresentation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StoryDetailViewModel @Inject constructor(
    private val forumsRepository: ForumsRepository,
    private val authRepository: AuthRepository,
) : ViewModel() {

    val isLoggedIn: StateFlow<Boolean?> =
        authRepository.isLoggedIn.stateIn(viewModelScope, SharingStarted.Eagerly, null)

    var storyDetail by mutableStateOf(StoryPresentation.defaultValue)
        private set

    var replyComposerState by mutableStateOf(ReplyComposerState.defaultValue)
        private set

    var repliesState by mutableStateOf(RepliesFeedState.defaultValue)
        private set

    private val _replies = mutableStateListOf<ReplyPresentation>()
    val replies: List<ReplyPresentation> = _replies

    private val _eventFlow = Channel<UIEvent>()
    val eventFlow = _eventFlow.receiveAsFlow()

    private val paginator = DefaultPaginator(initialKey = repliesState.page,
        getNextKey = { repliesState.page + 1 },
        onRequest = { nextPage: Int ->
            forumsRepository.getStoryReplies(
                storyId = storyDetail.id, // TODO: check if this is right
                page = nextPage,
                size = 20,
            )
        },
        onLoadUpdated = { isLoading ->
            repliesState = repliesState.copy(isLoading = isLoading)
        },
        onError = { message: UIText? ->
            repliesState = repliesState.copy(errorMessage = message)
        },
        onSuccess = { items: List<Reply>?, newKey: Int ->
            val newReplies = items?.map { it.toPresentation() } ?: emptyList()
            _replies.addAll(newReplies)
            repliesState = repliesState.copy(
                page = newKey,
                isEndReached = newReplies.isEmpty(),
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
        storyDetail = story
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

    private var onFocusChangeCaptionJob: Job? = null
    fun onFocusChangeCaption(focusState: FocusState) {
        onFocusChangeCaptionJob?.cancel()
        onFocusChangeCaptionJob = viewModelScope.launch {
            replyComposerState = replyComposerState.copy(
                isFocused = focusState.isFocused,
            )
        }
    }

    private var onClickSupportStoryJob: Job? = null
    fun onClickSupportStory() {
        onClickSupportReplyJob?.cancel()
        onClickSupportReplyJob = viewModelScope.launch {
            val oldStory = storyDetail.copy()

            (if (oldStory.isSupported) forumsRepository.postUnsupportStory(storyId = oldStory.id)
            else forumsRepository.postSupportStory(storyId = oldStory.id)).onEach { result ->
                when (result) {
                    is Resource.Error -> {
                        storyDetail = oldStory.copy(isLoadingSupport = false)
                        result.uiText?.let { _eventFlow.send(UIEvent.ShowSnackbar(it)) }
                    }
                    is Resource.Loading -> {
                        storyDetail = oldStory.copy(isLoadingSupport = true)
                    }
                    is Resource.Success -> {
                        storyDetail = oldStory.copy(
                            isLoadingSupport = false,
                            supportersCount = if (oldStory.isSupported) oldStory.supportersCount - 1
                            else oldStory.supportersCount + 1,
                            isSupported = !oldStory.isSupported,
                        )
                    }
                }
            }.launchIn(this)
        }
    }

    private var onClickSupportReplyJob: Job? = null
    fun onClickSupportReply(replyId: Int) {
        onClickSupportReplyJob?.cancel()
        onClickSupportReplyJob = viewModelScope.launch {
            val replyIndex = replies.indexOfFirst { it.id == replyId }
            if (replyIndex == -1) return@launch

            val oldReply = replies[replyIndex]

            (if (oldReply.isSupported) forumsRepository.postUnsupportReply(replyId = replyId)
            else forumsRepository.postSupportReply(replyId = replyId)).onEach { result ->
                when (result) {
                    is Resource.Error -> {
                        _replies[replyIndex] = oldReply.copy(isLoadingSupport = false)
                        result.uiText?.let { _eventFlow.send(UIEvent.ShowSnackbar(it)) }
                    }
                    is Resource.Loading -> {
                        _replies[replyIndex] = oldReply.copy(isLoadingSupport = true)
                    }
                    is Resource.Success -> {
                        _replies[replyIndex] = oldReply.copy(isLoadingSupport = false)
                        _replies[replyIndex] = oldReply.copy(
                            supportersCount = if (oldReply.isSupported) oldReply.supportersCount - 1
                            else oldReply.supportersCount + 1,
                            isSupported = !oldReply.isSupported,
                        )
                    }
                }
            }.launchIn(this)
        }
    }
}