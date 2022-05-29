package com.ecosense.android.featDiscoverCampaign.presentation.detail

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ecosense.android.core.util.Resource
import com.ecosense.android.core.util.UIText
import com.ecosense.android.featDiscoverCampaign.domain.model.CampaignDetail
import com.ecosense.android.featDiscoverCampaign.domain.repository.DiscoverCampaignRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailCampaignViewModel @Inject constructor(
    private val repository: DiscoverCampaignRepository
) : ViewModel() {
    private val _campaignDetailList = mutableStateOf(emptyList<CampaignDetail>())
    val campaignDetailList: State<List<CampaignDetail>> = _campaignDetailList

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    private val _errorMessage = mutableStateOf<UIText>(UIText.DynamicString(""))
    val errorMessage: State<UIText> = _errorMessage

    init {
        viewModelScope.launch {
            repository.getCampaignDetail().onEach { result ->
                when (result) {
                    is Resource.Error -> {
                        result.uiText?.let { _errorMessage.value = it }
                        result.data?.let { _campaignDetailList.value = it }
                        _isLoading.value = false
                    }
                    is Resource.Loading -> {
                        result.data?.let { _campaignDetailList.value = it }
                        _isLoading.value = true
                    }
                    is Resource.Success -> {
                        result.data?.let { _campaignDetailList.value = it }
                        _isLoading.value = false
                    }
                }
            }.launchIn(this)
        }
    }}