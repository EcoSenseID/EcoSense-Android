package com.ecosense.android.featReward.presentation.categoryrewards

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import com.ramcosta.composedestinations.annotation.Destination

@Composable
@Destination
fun CategoryRewardsScreen(rewardCategory: String) {
    Text(text = "$rewardCategory CategoryRewardsScreen")
}