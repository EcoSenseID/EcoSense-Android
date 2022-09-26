package com.ecosense.android.featReward.presentation.myrewards

import com.ecosense.android.featReward.domain.model.MyRewards

data class MyRewardsScreenState(
    val myReward: List<MyRewards>,
    val isLoadingMyRewardList: Boolean
) {
    companion object {
        val defaultValue = MyRewardsScreenState(
            myReward = emptyList(),
            isLoadingMyRewardList = false
        )
    }
}
