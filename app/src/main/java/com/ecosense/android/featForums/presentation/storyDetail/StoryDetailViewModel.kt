package com.ecosense.android.featForums.presentation.storyDetail

import android.net.Uri
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
import com.ecosense.android.featForums.presentation.model.toPresentation
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

    var isLoadingStoryDetail by mutableStateOf(false)
        private set

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

    var isRefreshing by mutableStateOf(false)
        private set

    private val paginator = DefaultPaginator(
        initialKey = repliesState.page,
        getNextKey = { repliesState.page + 1 },
        onRequest = { nextPage: Int ->
            forumsRepository.getStoryReplies(
                storyId = storyDetail.id,
                page = nextPage,
                size = 20,
            )
        },
        onLoadUpdated = { isLoading -> repliesState = repliesState.copy(isLoading = isLoading) },
        onError = { message: UIText? ->
            repliesState = repliesState.copy(errorMessage = message)
            if (isRefreshing) isRefreshing = false
            onLoadNextRepliesFeed()
        },
        onSuccess = { items: List<Reply>?, newKey: Int ->
            val newReplies = items?.map { it.toPresentation() } ?: emptyList()
            if (isRefreshing) {
                _replies.clear()
                isRefreshing = false
            }
            _replies.addAll(newReplies)
            repliesState = repliesState.copy(
                page = newKey,
                isEndReached = newReplies.isEmpty(),
            )
        },
    )

    init {
        viewModelScope.launch {
            authRepository.getCurrentUser()?.let {
                replyComposerState = replyComposerState.copy(avatarUrl = it.photoUrl)
            }
        }
    }

    private var onLoadNextCommentsFeedJob: Job? = null
    fun onLoadNextRepliesFeed() {
        onLoadNextCommentsFeedJob?.cancel()
        onLoadNextCommentsFeedJob = viewModelScope.launch {
            paginator.loadNextItems()
        }
    }

    private var onChangeReplyComposerCaptionJob: Job? = null
    fun onChangeReplyComposerCaption(value: String) {
        onChangeReplyComposerCaptionJob?.cancel()
        onChangeReplyComposerCaptionJob = viewModelScope.launch {
            replyComposerState = replyComposerState.copy(caption = value)
        }
    }

    private var onClickSendReplyJob: Job? = null
    fun onClickSendReply() {
        onClickSendReplyJob?.cancel()
        onClickSendReplyJob = viewModelScope.launch {
            _eventFlow.send(UIEvent.HideKeyboard)

            forumsRepository.postNewReply(storyId = storyDetail.id,
                caption = replyComposerState.caption,
                attachedPhoto = replyComposerState.attachedPhotoUri?.let {
                    forumsRepository.findJpegByUri(it)
                }).onEach { result ->
                when (result) {
                    is Resource.Error -> {
                        replyComposerState = replyComposerState.copy(isUploading = false)
                        result.uiText?.let { _eventFlow.send(UIEvent.ShowSnackbar(it)) }
                    }

                    is Resource.Loading -> {
                        replyComposerState = replyComposerState.copy(isUploading = true)
                    }

                    is Resource.Success -> {
                        replyComposerState = ReplyComposerState.defaultValue
                        onRefresh()
                    }
                }
            }.launchIn(this)
        }
    }

    fun onRefresh() {
        isRefreshing = true
        repliesState = RepliesFeedState.defaultValue
        paginator.reset()
        onLoadNextRepliesFeed()
        onLoadStoryDetail()
    }

    fun onImagePicked(uri: Uri?) {
        uri?.let { replyComposerState = replyComposerState.copy(attachedPhotoUri = it) }
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
        onClickSupportStoryJob?.cancel()
        onClickSupportStoryJob = viewModelScope.launch {
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

    fun setStoryId(storyId: Int) {
        storyDetail = storyDetail.copy(id = storyId)
        onRefresh()
    }

    private var onLoadStoryDetailJob: Job? = null
    private fun onLoadStoryDetail() {
        onLoadStoryDetailJob?.cancel()
        onLoadStoryDetailJob = viewModelScope.launch {
            forumsRepository.getStoryDetail(storyDetail.id).onEach { result ->
                when (result) {
                    is Resource.Error -> {
                        isLoadingStoryDetail = false
                        result.uiText?.let { _eventFlow.send(UIEvent.ShowSnackbar(it)) }
                        onLoadStoryDetail()
                    }
                    is Resource.Loading -> isLoadingStoryDetail = true
                    is Resource.Success -> {
                        isLoadingStoryDetail = false
                        result.data?.let { storyDetail = it.toPresentation() }
                    }
                }
            }.launchIn(this)
        }
    }
}