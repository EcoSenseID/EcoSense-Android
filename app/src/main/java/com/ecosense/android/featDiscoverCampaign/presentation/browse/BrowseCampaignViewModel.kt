package com.ecosense.android.featDiscoverCampaign.presentation.browse

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ecosense.android.core.util.Resource
import com.ecosense.android.core.util.UIText
import com.ecosense.android.core.domain.model.Campaign
import com.ecosense.android.featDiscoverCampaign.domain.repository.DiscoverCampaignRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BrowseCampaignViewModel @Inject constructor(
    private val repository: DiscoverCampaignRepository
) : ViewModel() {

    private val _campaignsList = mutableStateOf(emptyList<Campaign>())
    val campaignList: State<List<Campaign>> = _campaignsList

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    private val _errorMessage = mutableStateOf<UIText>(UIText.DynamicString(""))
    val errorMessage: State<UIText> = _errorMessage

    init {
        viewModelScope.launch {
            repository.getBrowseCampaign().onEach { result ->
                when (result) {
                    is Resource.Error -> {
                        result.uiText?.let { _errorMessage.value = it }
                        result.data?.let { _campaignsList.value = it }
                        _isLoading.value = false
                    }

                    is Resource.Loading -> {
                        result.data?.let { _campaignsList.value = it }
                        _isLoading.value = true
                    }

                    is Resource.Success -> {
                        result.data?.let { _campaignsList.value = it }
                        _isLoading.value = false
                    }
                }
            }.launchIn(this)
        }
    }
}