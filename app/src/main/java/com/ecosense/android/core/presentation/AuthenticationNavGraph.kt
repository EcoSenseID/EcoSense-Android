package com.ecosense.android.core.presentation

import com.ramcosta.composedestinations.annotation.NavGraph
import com.ramcosta.composedestinations.annotation.RootNavGraph

@RootNavGraph
@NavGraph
annotation class AuthenticationNavGraph(
    val start: Boolean = false
)