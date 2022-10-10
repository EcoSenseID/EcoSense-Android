package com.ecosense.android.featReward.presentation.list.myrewards

import com.ecosense.android.featReward.domain.model.MyRewards

data class MyRewardsScreenState(
    val myRewards: List<MyRewards>,
    val isLoadingMyRewardList: Boolean
) {
    companion object {
        val defaultValue = MyRewardsScreenState(
            myRewards = emptyList(),
            isLoadingMyRewardList = false
        )
    }
}
