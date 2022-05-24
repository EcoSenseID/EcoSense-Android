package com.ecosense.android.featProfile.presentation.editProfile

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ecosense.android.R
import com.ecosense.android.core.domain.repository.AuthRepository
import com.ecosense.android.core.presentation.util.UIEvent
import com.ecosense.android.core.util.Resource
import com.ecosense.android.core.util.UIText
import com.ecosense.android.featProfile.domain.repository.ProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val profileRepository: ProfileRepository
) : ViewModel() {

    private val _state = mutableStateOf(EditProfileScreenState.defaultValue)
    val state: State<EditProfileScreenState> = _state

    private val _initState = mutableStateOf(EditProfileScreenState.defaultValue)

    private val _eventFlow = Channel<UIEvent>()
    val eventFlow = _eventFlow.receiveAsFlow()

    init {
        viewModelScope.launch {
            authRepository.getCurrentUser().collect { user ->
                _state.value = state.value.copy(
                    uid = user?.uid,
                    displayName = user?.displayName,
                    email = user?.email,
                    photoUrl = user?.photoUrl,
                    isEmailVerified = user?.isEmailVerified
                )

                _initState.value = state.value
            }
        }
    }

    fun onDisplayNameChange(value: String) {
        _state.value = state.value.copy(displayName = value)
    }

    private var onSaveClickJob: Job? = null
    fun onSaveClick() {
        onSaveClickJob?.cancel()
        onSaveClickJob = viewModelScope.launch {
            val newDisplayName = if (_initState.value.displayName != state.value.displayName)
                state.value.displayName else null

            val newPhotoUrl = if (_initState.value.photoUrl != state.value.photoUrl)
                state.value.photoUrl else null

            profileRepository.updateProfile(
                displayName = newDisplayName,
                photoUrl = newPhotoUrl
            ).onEach { result ->
                when (result) {
                    is Resource.Error -> {
                        _state.value = state.value.copy(isSavingProfileLoading = false)
                        result.uiText?.let { _eventFlow.send(UIEvent.ShowSnackbar(it)) }
                    }
                    is Resource.Loading -> {
                        _state.value = state.value.copy(isSavingProfileLoading = true)
                        _eventFlow.send(UIEvent.HideKeyboard)
                    }
                    is Resource.Success -> {
                        _state.value = state.value.copy(isSavingProfileLoading = false)
                        val uiText = UIText.StringResource(R.string.sm_changes_saved)
                        _eventFlow.send(UIEvent.ShowSnackbar(uiText))
                    }
                }
            }.launchIn(this)
        }
    }

    fun onChangeProfilePictureClick() {
        // TODO: implement image picker
        val newPhotoUrl = "https://i.ibb.co/QXBQfXg/1067981407.jpg"

        _state.value = state.value.copy(photoUrl = newPhotoUrl)
    }

    private var onSendEmailVerificationClickJob: Job? = null
    fun onSendEmailVerificationClick() {
        onSendEmailVerificationClickJob?.cancel()
        onSendEmailVerificationClickJob = viewModelScope.launch {
            profileRepository.sendEmailVerification().onEach { result ->
                when (result) {
                    is Resource.Error -> {
                        _state.value = state.value.copy(isEmailVerificationLoading = false)
                        result.uiText?.let { _eventFlow.send(UIEvent.ShowSnackbar(it)) }
                    }
                    is Resource.Loading -> {
                        _state.value = state.value.copy(isEmailVerificationLoading = true)
                        _eventFlow.send(UIEvent.HideKeyboard)
                    }
                    is Resource.Success -> {
                        _state.value = state.value.copy(isEmailVerificationLoading = false)
                        val uiText = UIText.StringResource(R.string.sm_email_verification_sent)
                        _eventFlow.send(UIEvent.ShowSnackbar(uiText))
                    }
                }
            }.launchIn(this)
        }
    }
}