package com.ecosense.android.featForums.domain.paginator

interface Paginator<Key, Item> {
    suspend fun loadNextItems()
    fun reset()
}