package com.ecosense.android.featForums.presentation.paginator

import com.ecosense.android.core.util.Resource
import com.ecosense.android.core.util.UIText
import com.ecosense.android.featForums.domain.paginator.Paginator

class DefaultPaginator<Key, Item>(
    private val initialKey: Key,
    private inline val getNextKey: () -> Key,
    private inline val onRequest: suspend (nextKey: Key) -> Resource<List<Item>>,
    private inline val onLoadUpdated: (isLoading: Boolean) -> Unit,
    private inline val onError: (message: UIText?) -> Unit,
    private inline val onSuccess: (items: List<Item>?, newKey: Key) -> Unit,
) : Paginator<Key, Item> {

    private var currentKey: Key = initialKey
    private var isMakingRequest: Boolean = false

    override suspend fun loadNextItems() {
        if (isMakingRequest) return

        isMakingRequest = true
        onLoadUpdated(true)

        val result = onRequest(currentKey)

        isMakingRequest = false

        when (result) {
            is Resource.Error -> {
                onError(result.uiText)
                onLoadUpdated(false)
            }

            is Resource.Loading -> {
                onLoadUpdated(true)
            }

            is Resource.Success -> {
                currentKey = getNextKey()
                onSuccess(result.data, currentKey)
                onLoadUpdated(false)
            }
        }
    }

    override fun reset() {
        currentKey = initialKey
    }
}