package com.ecosense.android.core.presentation

import com.ramcosta.composedestinations.annotation.NavGraph
import com.ramcosta.composedestinations.annotation.RootNavGraph

@RootNavGraph
@NavGraph
annotation class AuthNavGraph(
    val start: Boolean = false
)