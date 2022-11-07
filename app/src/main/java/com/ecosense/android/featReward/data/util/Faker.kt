package com.ecosense.android.featReward.data.util

import com.ecosense.android.featReward.domain.model.*

object Faker {
    fun getRewardHomepage(): RewardHomepage {
        return RewardHomepage(
            totalPoints = 300,
            walletRewards = getWalletRewards(),
            donationRewards = getDonationRewards()
        )
    }

    private fun getWalletRewards(): List<WalletRewards> {
        val result = mutableListOf<WalletRewards>()

        for (i in 1..5) {
            result.add(
                WalletRewards(
                    partner = "Partner $i",
                    bannerUrl = "https://cdn.statically.io/og/theme=dark/E-Wallet$i.jpg",
                    id = i,
                    title = "E-Wallet $i",
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
                    bannerUrl = "https://cdn.statically.io/og/theme=dark/Donation$i.jpg",
                    id = i,
                    title = "Donation $i",
                    pointsNeeded = (50..200).random()
                )
            )
        }

        return result
    }

    fun getRewards(rewardCategory: String): List<Rewards> {
        val result = mutableListOf<Rewards>()

        for (i in 1..10) {
            result.add(
                Rewards(
                    partner = "Partner $i",
                    bannerUrl = "https://cdn.statically.io/og/theme=dark/${rewardCategory + i}.jpg",
                    numberOfRedeem = if (i < 3) 1 else if (i < 6) 2 else 3,
                    id = i,
                    title = "$rewardCategory $i",
                    category = rewardCategory,
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
                    claimId = i,
                    claimStatus = if (i < 3) 1 else if (i < 6) 2 else 3,
                    title = "My Rewards $i",
                    category = if (i < 3) "E-Wallet" else if (i < 6) "Donation" else "Voucher"
                )
            )
        }

        return result
    }

    fun getRewardDetail(rewardId: Int): RewardDetail {
        return RewardDetail(
            termsCondition = listOf(
                "Lorem Ipsum is simply dummy text of the printing and  typesetting industry.",
                "Lorem Ipsum is simply dummy text.",
                "Lorem Ipsum is simply dummy text of the printing."
            ),
            bannerUrl = "https://cdn.statically.io/og/theme=dark/RewardDetail$rewardId.jpg",
            description = "Lorem Ipsum is simply dummy text of the printing and  typesetting industry. Lorem Ipsum has been the industry's standard dummy  text ever since the 1500s.",
            validity = "1662138000",
            title = "Reward No.$rewardId",
            partner = "Partner $rewardId",
            category = if (rewardId < 3) "E-Wallet" else if (rewardId < 6) "Donation" else "Voucher",
            pointsNeeded = (50..200).random(),
            maxRedeem = 3,
            numberOfRedeem = if (rewardId < 3) 1 else if (rewardId < 6) 2 else 3,
            howToUse = listOf(
                "Lorem Ipsum is simply dummy text of the printing.",
                "Lorem Ipsum is simply dummy text of the printing and  typesetting industry.",
                "Lorem Ipsum is simply dummy text."
            )
        )
    }

    fun getMyRewardDetail(rewardId: Int): MyRewardDetail {
        return MyRewardDetail(
            termsCondition = listOf(
                "Lorem Ipsum is simply dummy text of the printing and  typesetting industry.",
                "Lorem Ipsum is simply dummy text.",
                "Lorem Ipsum is simply dummy text of the printing."
            ),
            bannerUrl = "https://cdn.statically.io/og/theme=dark/MyRewardDetail$rewardId.jpg",
            description = "Lorem Ipsum is simply dummy text of the printing and  typesetting industry. Lorem Ipsum has been the industry's standard dummy  text ever since the 1500s.",
            validity = "1662138000",
            title = "My Reward No.$rewardId",
            partner = "Partner $rewardId",
            category = if (rewardId < 3) "E-Wallet" else if (rewardId < 6) "Donation" else "Voucher",
            claimStatus = if (rewardId < 3) 1 else if (rewardId < 6) 2 else 3,
            howToUse = listOf(
                "Lorem Ipsum is simply dummy text of the printing.",
                "Lorem Ipsum is simply dummy text of the printing and  typesetting industry.",
                "Lorem Ipsum is simply dummy text."
            )
        )
    }
}