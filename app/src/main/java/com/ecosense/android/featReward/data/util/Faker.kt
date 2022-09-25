package com.ecosense.android.featReward.data.util

import com.ecosense.android.featReward.domain.model.*

object Faker {
    fun getRewardHomepage(): RewardHomepage {
        return RewardHomepage(
            totalPoints = 300,
            hotDealsRewards = getHotDealsRewards(),
            donationRewards = getDonationRewards()
        )
    }

    private fun getHotDealsRewards(): List<HotDealsRewards> {
        val result = mutableListOf<HotDealsRewards>()

        for (i in 1..5) {
            result.add(
                HotDealsRewards(
                    partner = "Partner $i",
                    bannerUrl = "https://cdn.statically.io/og/theme=dark/HotDeals$i.jpg",
                    id = i,
                    title = "Hot Deals $i",
                    pointsNeeded = (50..200).random()
                )
            )
        }

        return result
    }

    private fun getDonationRewards(): List<DonationRewards> {
        val result = mutableListOf<DonationRewards>()

        for (i in 1..5) {
            result.add(
                DonationRewards(
                    partner = "Partner $i",
                    bannerUrl = "https://cdn.statically.io/og/theme=dark/HotDeals$i.jpg",
                    id = i,
                    title = "Hot Deals $i",
                    pointsNeeded = (50..200).random()
                )
            )
        }

        return result
    }

    fun getRewards(rewardCategory: String): Rewards {
        return Rewards(
            category = rewardCategory,
            categoryRewards = getCategoryRewards(rewardCategory)
        )
    }

    private fun getCategoryRewards(rewardCategory: String): List<CategoryRewards> {
        val result = mutableListOf<CategoryRewards>()

        for (i in 1..10) {
            result.add(
                CategoryRewards(
                    partner = "Partner $i",
                    bannerUrl = "https://cdn.statically.io/og/theme=dark/${rewardCategory + i}.jpg",
                    numberOfRedeem = (1..3).random(),
                    id = i,
                    title = "$rewardCategory $i",
                    maxRedeem = 3,
                    pointsNeeded = (50..200).random()
                )
            )
        }

        return result
    }

    fun getMyRewards(): List<MyRewards> {
        val result = mutableListOf<MyRewards>()

        for (i in 1..10) {
            result.add(
                MyRewards(
                    partner = "Partner $i",
                    bannerUrl = "https://cdn.statically.io/og/theme=dark/MyRewards$i.jpg",
                    id = i,
                    title = "My Rewards $i",
                    category = if (i < 3) "Entertainment" else if (i < 6) "Food & Beverage" else "Environment"
                )
            )
        }

        return result
    }

    fun getRewardDetail(rewardId: Int): RewardDetail {
        val redeemState = (0..1).random()

        return RewardDetail(
            termsCondition = listOf(
                "Lorem Ipsum is simply dummy text of the printing and  typesetting industry.",
                "Lorem Ipsum is simply dummy text.",
                "Lorem Ipsum is simply dummy text of the printing."
            ),
            isRedeemed = redeemState != 0,
            bannerUrl = "https://cdn.statically.io/og/theme=dark/RewardDetail$rewardId.jpg",
            description = "Lorem Ipsum is simply dummy text of the printing and  typesetting industry. Lorem Ipsum has been the industry's standard dummy  text ever since the 1500s.",
            validity = "1662138000",
            title = "Reward No.$rewardId",
            pointsNeeded = (50..200).random(),
            howToUse = listOf(
                "Lorem Ipsum is simply dummy text of the printing.",
                "Lorem Ipsum is simply dummy text of the printing and  typesetting industry.",
                "Lorem Ipsum is simply dummy text."
            )
        )
    }
}