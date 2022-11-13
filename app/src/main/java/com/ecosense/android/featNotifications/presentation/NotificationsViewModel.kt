package com.ecosense.android.featNotifications.presentation

import android.annotation.SuppressLint
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ecosense.android.core.domain.repository.AuthRepository
import com.ecosense.android.core.presentation.util.UIEvent
import com.ecosense.android.core.util.Resource
import com.ecosense.android.featNotifications.domain.repository.NotificationsRepository
import com.ecosense.android.featNotifications.presentation.model.NotificationPresentation
import com.ecosense.android.featNotifications.presentation.model.toPresentation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class NotificationsViewModel @Inject constructor(
    private val notificationsRepository: NotificationsRepository,
    authRepository: AuthRepository,
) : ViewModel() {

    val isLoggedIn: StateFlow<Boolean?> =
        authRepository.isLoggedIn.stateIn(viewModelScope, SharingStarted.Eagerly, null)

    var isRefreshing by mutableStateOf(false)
        private set

    val isAllNotifsEmpty
        get() = todaysNotifs.isEmpty() && yesterdaysNotifs.isEmpty() && lastWeeksNotifs.isEmpty() && olderNotifs.isEmpty()

    private val _todaysNotifs = mutableStateListOf<NotificationPresentation>()
    val todaysNotifs: List<NotificationPresentation> = _todaysNotifs

    private val _yesterdaysNotifs = mutableStateListOf<NotificationPresentation>()
    val yesterdaysNotifs: List<NotificationPresentation> = _yesterdaysNotifs

    private val _lastWeeksNotifs = mutableStateListOf<NotificationPresentation>()
    val lastWeeksNotifs: List<NotificationPresentation> = _lastWeeksNotifs

    private val _olderNotifs = mutableStateListOf<NotificationPresentation>()
    val olderNotifs: List<NotificationPresentation> = _olderNotifs

    private val _eventFlow = Channel<UIEvent>()
    val eventFlow = _eventFlow.receiveAsFlow()

    init {
        onRefreshNotifs()
    }

    private var loadNotifsJob: Job? = null
    fun onRefreshNotifs() {
        loadNotifsJob?.cancel()
        loadNotifsJob = viewModelScope.launch {
            notificationsRepository.getNotifications().onEach { result ->
                when (result) {
                    is Resource.Error -> {
                        isRefreshing = false
                        result.uiText?.let { _eventFlow.send(UIEvent.ShowSnackbar(it)) }
                        onRefreshNotifs()
                    }
                    is Resource.Loading -> isRefreshing = true
                    is Resource.Success -> {
                        isRefreshing = false
                        result.data?.map { it.toPresentation() }?.let { notifs ->
                            _todaysNotifs.apply {
                                clear()
                                addAll(notifs.filter { it.timestamp.isWithin(0) })
                            }

                            _yesterdaysNotifs.apply {
                                clear()
                                addAll(notifs.filter {
                                    it.timestamp.isWithin(1) && !it.timestamp.isWithin(0)
                                })
                            }

                            _lastWeeksNotifs.apply {
                                clear()
                                addAll(notifs.filter {
                                    it.timestamp.isWithin(7) && !it.timestamp.isWithin(1)
                                })
                            }

                            _olderNotifs.apply {
                                clear()
                                addAll(notifs.filter { !it.timestamp.isWithin(7) })
                            }
                        }
                    }
                }
            }.launchIn(this)
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun Long.isWithin(numOfDays: Int): Boolean {
        val sdf = SimpleDateFormat("yyyyMMdd")
        return sdf.format(Date(this)) >= sdf.format(Date().apply { time -= numOfDays * 86400000 })
    }
}