package com.ecosense.android.featReward.presentation.detail

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import com.ramcosta.composedestinations.annotation.Destination

@Composable
@Destination
fun RewardDetailScreen(rewardId: Int) {
    Text(text = "RewardDetailScreen No.$rewardId")
}