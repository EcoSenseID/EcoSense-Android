package com.ecosense.android.featReward.data.repository

import android.content.Context
import android.util.Patterns
import androidx.core.util.PatternsCompat
import com.ecosense.android.R
import com.ecosense.android.core.domain.api.AuthApi
import com.ecosense.android.core.util.Resource
import com.ecosense.android.core.util.SimpleResource
import com.ecosense.android.core.util.UIText
import com.ecosense.android.featReward.data.api.RewardApi
import com.ecosense.android.featReward.data.model.*
import com.ecosense.android.featReward.domain.model.*
import com.ecosense.android.featReward.domain.repository.RewardRepository
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import logcat.asLog
import logcat.logcat
import retrofit2.HttpException
import java.io.IOException

class RewardRepositoryImpl(
    private val authApi: AuthApi,
    private val rewardApi: RewardApi,
    private val appContext: Context
) : RewardRepository {
    override fun getRewardHomepage(): Flow<Resource<RewardHomepage>> = flow {
        emit(Resource.Loading())

//        // dummy data
//        val response = Faker.getRewardHomepage()
//        emit(Resource.Success(response))

        try {
            val idToken = authApi.getIdToken(true)
            val bearerToken = "Bearer $idToken"
            val response = rewardApi.getRewardHomepage(bearerToken = bearerToken)

            when {
                response.error == true -> emit(Resource.Error(
                    uiText = response.message?.let { UIText.DynamicString(it) }
                        ?: UIText.StringResource(R.string.em_unknown))
                )

                response.totalPoints == null || response.walletRewards == null || response.donationRewards == null -> {
                    emit(Resource.Error(UIText.StringResource(R.string.em_unknown)))
                }

                else -> emit(
                    Resource.Success(
                        RewardHomepage(
                            totalPoints = response.totalPoints,
                            walletRewards = response.walletRewards.map { it.toWalletRewards() },
                            donationRewards = response.donationRewards.map { it.toDonationRewards() }
                        )
                    )
                )
            }
        } catch (e: Exception) {
            logcat { e.asLog() }
            when (e) {
                is HttpException -> {
                    try {
                        val response = Gson().fromJson<RewardHomepageDto>(
                            e.response()?.errorBody()?.charStream(),
                            object : TypeToken<RewardHomepageDto>() {}.type
                        )
                        UIText.DynamicString(response.message!!)
                    } catch (e: Exception) {
                        UIText.StringResource(R.string.em_unknown)
                    }
                }

                is IOException -> UIText.StringResource(R.string.em_io_exception)

                else -> UIText.StringResource(R.string.em_unknown)

            }.let { emit(Resource.Error(it)) }
        }
    }

    override fun getRewards(categoryId: Int): Flow<Resource<List<Rewards>>> = flow {
        emit(Resource.Loading())

//        // dummy data
//        val response = Faker.getRewards(rewardCategory)
//        emit(Resource.Success(response))

        try {
            val idToken = authApi.getIdToken(true)
            val bearerToken = "Bearer $idToken"
            val response =
                rewardApi.getRewards(bearerToken = bearerToken, categoryId = categoryId)

            when {
                response.error == true -> emit(Resource.Error(
                    uiText = response.message?.let { UIText.DynamicString(it) }
                        ?: UIText.StringResource(R.string.em_unknown))
                )

                response.rewards == null -> {
                    emit(Resource.Error(UIText.StringResource(R.string.em_unknown)))
                }

                else -> emit(Resource.Success(response.rewards.map { it.toRewards() }))
            }
        } catch (e: Exception) {
            logcat { e.asLog() }
            when (e) {
                is HttpException -> {
                    try {
                        val response = Gson().fromJson<RewardsDto>(
                            e.response()?.errorBody()?.charStream(),
                            object : TypeToken<RewardsDto>() {}.type
                        )
                        UIText.DynamicString(response.message!!)
                    } catch (e: Exception) {
                        UIText.StringResource(R.string.em_unknown)
                    }
                }

                is IOException -> UIText.StringResource(R.string.em_io_exception)

                else -> UIText.StringResource(R.string.em_unknown)

            }.let { emit(Resource.Error(it)) }
        }
    }

    override fun getMyRewards(): Flow<Resource<List<MyRewards>>> = flow {
        emit(Resource.Loading())

//        // dummy data
//        val response = Faker.getMyRewards()
//        emit(Resource.Success(response))

        try {
            val idToken = authApi.getIdToken(true)
            val bearerToken = "Bearer $idToken"
            val response = rewardApi.getMyRewards(bearerToken = bearerToken)

            when {
                response.error == true -> emit(Resource.Error(
                    uiText = response.message?.let { UIText.DynamicString(it) }
                        ?: UIText.StringResource(R.string.em_unknown))
                )

                response.myRewards == null -> {
                    emit(Resource.Error(UIText.StringResource(R.string.em_unknown)))
                }

                else -> emit(Resource.Success(response.myRewards.map { it.toMyRewards() }))
            }
        } catch (e: Exception) {
            logcat { e.asLog() }
            when (e) {
                is HttpException -> {
                    try {
                        val response = Gson().fromJson<MyRewardsDto>(
                            e.response()?.errorBody()?.charStream(),
                            object : TypeToken<MyRewardsDto>() {}.type
                        )
                        UIText.DynamicString(response.message!!)
                    } catch (e: Exception) {
                        UIText.StringResource(R.string.em_unknown)
                    }
                }

                is IOException -> UIText.StringResource(R.string.em_io_exception)

                else -> UIText.StringResource(R.string.em_unknown)

            }.let { emit(Resource.Error(it)) }
        }
    }

    override fun getRewardDetail(rewardId: Int): Flow<Resource<RewardDetail>> = flow {
        emit(Resource.Loading())

//        // dummy data
//        val response = Faker.getRewardDetail(rewardId)
//        emit(Resource.Success(response))

        try {
            val idToken = authApi.getIdToken(true)
            val bearerToken = "Bearer $idToken"
            val response = rewardApi.getRewardDetail(bearerToken = bearerToken, rewardId = rewardId)

            when (response.error) {
                true -> emit(Resource.Error(
                    uiText = response.message?.let { UIText.DynamicString(it) }
                        ?: UIText.StringResource(R.string.em_unknown))
                )

                else -> emit(Resource.Success(response.toRewardDetail()))
            }
        } catch (e: Exception) {
            logcat { e.asLog() }
            when (e) {
                is HttpException -> {
                    try {
                        val response = Gson().fromJson<RewardDetailDto>(
                            e.response()?.errorBody()?.charStream(),
                            object : TypeToken<RewardDetailDto>() {}.type
                        )
                        UIText.DynamicString(response.message!!)
                    } catch (e: Exception) {
                        UIText.StringResource(R.string.em_unknown)
                    }
                }

                is IOException -> UIText.StringResource(R.string.em_io_exception)

                else -> UIText.StringResource(R.string.em_unknown)

            }.let { emit(Resource.Error(it)) }
        }
    }

    override fun getMyRewardDetail(claimId: Int): Flow<Resource<MyRewardDetail>> = flow {
        emit(Resource.Loading())

//        // dummy data
//        val response = Faker.getMyRewardDetail(rewardId)
//        emit(Resource.Success(response))

        try {
            val idToken = authApi.getIdToken(true)
            val bearerToken = "Bearer $idToken"
            val response = rewardApi.getMyRewardDetail(bearerToken = bearerToken, claimId = claimId)

            when (response.error) {
                true -> emit(Resource.Error(
                    uiText = response.message?.let { UIText.DynamicString(it) }
                        ?: UIText.StringResource(R.string.em_unknown))
                )

                else -> emit(Resource.Success(response.toMyRewardDetail()))
            }
        } catch (e: Exception) {
            logcat { e.asLog() }
            when (e) {
                is HttpException -> {
                    try {
                        val response = Gson().fromJson<MyRewardDetailDto>(
                            e.response()?.errorBody()?.charStream(),
                            object : TypeToken<MyRewardDetailDto>() {}.type
                        )
                        UIText.DynamicString(response.message!!)
                    } catch (e: Exception) {
                        UIText.StringResource(R.string.em_unknown)
                    }
                }

                is IOException -> UIText.StringResource(R.string.em_io_exception)

                else -> UIText.StringResource(R.string.em_unknown)

            }.let { emit(Resource.Error(it)) }
        }
    }

    override fun setRedeemReward(rewardId: Int): Flow<SimpleResource> =
        flow {
            emit(Resource.Loading())

//        emit(Resource.Error(UIText.DynamicString("Redeem Reward API not yet implemented"))) // uncomment if API ready to use

            try {
                val idToken = authApi.getIdToken(true)
                val bearerToken = "Bearer $idToken"
                val response = rewardApi.setRedeemReward(
                    bearerToken = bearerToken,
                    rewardId = rewardId
                )

                when {
                    response.error == false -> emit(Resource.Success(Unit))

                    response.message != null -> emit(Resource.Error(UIText.DynamicString(response.message)))

                    else -> emit(Resource.Error(UIText.StringResource(R.string.em_unknown)))
                }
            } catch (e: Exception) {
                logcat { e.asLog() }
                when (e) {
                    is HttpException -> {
                        try {
                            val response = Gson().fromJson<RedeemRewardDto>(
                                e.response()?.errorBody()?.charStream(),
                                object : TypeToken<RedeemRewardDto>() {}.type
                            )
                            UIText.DynamicString(response.message!!)
                        } catch (e: Exception) {
                            UIText.StringResource(R.string.em_unknown)
                        }
                    }

                    is IOException -> UIText.StringResource(R.string.em_io_exception)

                    else -> UIText.StringResource(R.string.em_unknown)

                }.let { emit(Resource.Error(it)) }
            }
        }

    override fun setRequestReward(
        rewardId: Int,
        email: String,
        walletType: String,
        walletNumber: String
    ): Flow<SimpleResource> = flow {
        emit(Resource.Loading())

//        emit(Resource.Error(UIText.DynamicString("Request Reward API not yet implemented"))) // uncomment if API ready to use

        when {
            email.isBlank() -> {
                emit(Resource.Error(UIText.StringResource(R.string.em_email_blank)))
                return@flow
            }
            !PatternsCompat.EMAIL_ADDRESS.matcher(email).matches() -> {
                emit(Resource.Error(UIText.StringResource(R.string.em_invalid_email)))
                return@flow
            }
            walletType.isBlank() -> {
                emit(Resource.Error(UIText.StringResource(R.string.em_wallet_type_blank)))
                return@flow
            }
            walletNumber.isBlank() -> {
                emit(Resource.Error(UIText.StringResource(R.string.em_wallet_number_blank)))
                return@flow
            }
            !Patterns.PHONE.matcher(walletNumber).matches() -> {
                emit(Resource.Error(UIText.StringResource(R.string.em_invalid_wallet_number)))
                return@flow
            }
        }

        try {
            val idToken = authApi.getIdToken(true)
            val bearerToken = "Bearer $idToken"
            val response = rewardApi.setRequestReward(
                bearerToken = bearerToken,
                rewardId = rewardId,
                email = email,
                walletType = walletType,
                walletNumber = walletNumber
            )

            when {
                response.error == false -> emit(Resource.Success(Unit))

                response.message != null -> emit(Resource.Error(UIText.DynamicString(response.message)))

                else -> emit(Resource.Error(UIText.StringResource(R.string.em_unknown)))
            }
        } catch (e: Exception) {
            logcat { e.asLog() }
            when (e) {
                is HttpException -> {
                    try {
                        val response = Gson().fromJson<UseRewardDto>(
                            e.response()?.errorBody()?.charStream(),
                            object : TypeToken<UseRewardDto>() {}.type
                        )
                        UIText.DynamicString(response.message!!)
                    } catch (e: Exception) {
                        UIText.StringResource(R.string.em_unknown)
                    }
                }

                is IOException -> UIText.StringResource(R.string.em_io_exception)

                else -> UIText.StringResource(R.string.em_unknown)

            }.let { emit(Resource.Error(it)) }
        }
    }

    override fun setUseReward(claimId: Int): Flow<SimpleResource> = flow {
        emit(Resource.Loading())

//        emit(Resource.Error(UIText.DynamicString("Use Reward API not yet implemented"))) // uncomment if API ready to use

        try {
            val idToken = authApi.getIdToken(true)
            val bearerToken = "Bearer $idToken"
            val response = rewardApi.setUseReward(bearerToken = bearerToken, claimId = claimId)

            when {
                response.error == false -> emit(Resource.Success(Unit))

                response.message != null -> emit(Resource.Error(UIText.DynamicString(response.message)))

                else -> emit(Resource.Error(UIText.StringResource(R.string.em_unknown)))
            }
        } catch (e: Exception) {
            logcat { e.asLog() }
            when (e) {
                is HttpException -> {
                    try {
                        val response = Gson().fromJson<UseRewardDto>(
                            e.response()?.errorBody()?.charStream(),
                            object : TypeToken<UseRewardDto>() {}.type
                        )
                        UIText.DynamicString(response.message!!)
                    } catch (e: Exception) {
                        UIText.StringResource(R.string.em_unknown)
                    }
                }

                is IOException -> UIText.StringResource(R.string.em_io_exception)

                else -> UIText.StringResource(R.string.em_unknown)

            }.let { emit(Resource.Error(it)) }
        }
    }
}