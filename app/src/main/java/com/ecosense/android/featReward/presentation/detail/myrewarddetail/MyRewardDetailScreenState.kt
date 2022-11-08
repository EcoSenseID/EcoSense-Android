package com.ecosense.android.featReward.presentation.detail.myrewarddetail

import com.ecosense.android.featReward.domain.model.MyRewardDetail

data class MyRewardDetailScreenState(
    val myRewardDetail: MyRewardDetail,
    val isLoadingMyRewardDetail: Boolean,
    val isLoadingUseReward: Boolean
) {
    companion object {
        val defaultValue = MyRewardDetailScreenState(
            myRewardDetail = MyRewardDetail(
                termsCondition = emptyList(),
                bannerUrl = "",
                description = "",
                validity = "",
                title = "",
                partner = "",
                category = "",
                claimStatus = 0,
                howToUse = emptyList()
            ),
            isLoadingMyRewardDetail = false,
            isLoadingUseReward = false
        )
    }
}