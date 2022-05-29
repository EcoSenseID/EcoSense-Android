package com.ecosense.android.featDiscoverCampaign.presentation.dashboard

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ecosense.android.core.util.Resource
import com.ecosense.android.core.util.UIText
import com.ecosense.android.featDiscoverCampaign.domain.model.CampaignDetail
import com.ecosense.android.featDiscoverCampaign.domain.model.Category
import com.ecosense.android.featDiscoverCampaign.domain.repository.DiscoverCampaignRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DiscoverCampaignViewModel @Inject constructor(
    private val repository: DiscoverCampaignRepository
) : ViewModel() {
    private val _discoverCampaignList = mutableStateOf(emptyList<CampaignDetail>())
    val discoverCampaignList: State<List<CampaignDetail>> = _discoverCampaignList

    private val _categoryList = mutableStateOf(emptyList<Category>())
    val categoryList: State<List<Category>> = _categoryList

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
                        result.data?.let { _discoverCampaignList.value = it }
                        _isLoading.value = false
                    }
                    is Resource.Loading -> {
                        result.data?.let { _discoverCampaignList.value = it }
                        _isLoading.value = true
                    }
                    is Resource.Success -> {
                        result.data?.let { _discoverCampaignList.value = it }
                        _isLoading.value = false
                    }
                }
            }.launchIn(this)

            repository.getCategory().onEach { result ->
                when (result) {
                    is Resource.Error -> {
                        result.uiText?.let { _errorMessage.value = it }
                        result.data?.let { _categoryList.value = it }
                        _isLoading.value = false
                    }
                    is Resource.Loading -> {
                        result.data?.let { _categoryList.value = it }
                        _isLoading.value = true
                    }
                    is Resource.Success -> {
                        result.data?.let { _categoryList.value = it }
                        _isLoading.value = false
                    }
                }
            }.launchIn(this)
        }
    }
}