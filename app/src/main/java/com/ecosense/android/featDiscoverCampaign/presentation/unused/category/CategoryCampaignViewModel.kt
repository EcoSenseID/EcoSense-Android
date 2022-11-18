package com.ecosense.android.featDiscoverCampaign.presentation.unused.category

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ecosense.android.core.presentation.util.UIEvent
import com.ecosense.android.core.util.Resource
import com.ecosense.android.featDiscoverCampaign.domain.repository.DiscoverCampaignRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryCampaignViewModel @Inject constructor(
    private val discoverCampaignRepository: DiscoverCampaignRepository
) : ViewModel() {

    private val _state = mutableStateOf(CategoryCampaignScreenState.defaultValue)
    val state: State<CategoryCampaignScreenState> = _state

    private val _eventFlow = Channel<UIEvent>()
    val eventFlow = _eventFlow.receiveAsFlow()

    init {
        getCategory()
    }

    private var getCategoryJob: Job? = null
    private fun getCategory() {
        getCategoryJob?.cancel()
        getCategoryJob = viewModelScope.launch {
            discoverCampaignRepository.getCategories().onEach { result ->
                when (result) {
                    is Resource.Error -> {
                        _state.value = state.value.copy(isLoadingCategories = false)
                        result.uiText?.let { _eventFlow.send(UIEvent.ShowSnackbar(it)) }
                    }
                    is Resource.Loading -> {
                        _state.value = state.value.copy(
                            categories = result.data ?: emptyList(),
                            isLoadingCategories = true
                        )
                    }
                    is Resource.Success -> {
                        _state.value = state.value.copy(
                            categories = result.data ?: emptyList(),
                            isLoadingCategories = false
                        )
                    }
                }
            }.launchIn(this)
        }
    }
}