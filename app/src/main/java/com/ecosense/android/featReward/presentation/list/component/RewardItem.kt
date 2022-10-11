package com.ecosense.android.featReward.presentation.list.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Scale
import com.ecosense.android.core.presentation.theme.CustardYellow
import com.ecosense.android.core.presentation.theme.spacing
import com.ecosense.android.featReward.domain.model.MyRewards
import com.ecosense.android.featReward.domain.model.Rewards
import com.ecosense.android.featReward.presentation.list.myrewards.MyRewardsViewModel
import com.ecosense.android.featReward.presentation.list.rewards.RewardsViewModel

@Composable
fun RewardItem(
    reward: Rewards?,
    myReward: MyRewards?,
    rewardViewModel: RewardsViewModel?,
    myRewardViewModel: MyRewardsViewModel?,
    onClick: () -> Unit
) {
    if (reward != null && myReward == null) {
        if (rewardViewModel != null && myRewardViewModel == null) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(
                        horizontal = MaterialTheme.spacing.medium,
                        vertical = MaterialTheme.spacing.small
                    )
                    .shadow(elevation = 2.dp, shape = RoundedCornerShape(8.dp))
                    .clip(shape = RoundedCornerShape(8.dp))
                    .clickable(onClick = onClick)
                    .background(MaterialTheme.colors.surface)
                    .fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .width(120.dp)
                        .height(160.dp)
                ) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(reward.bannerUrl)
                            .crossfade(true)
                            .scale(Scale.FILL)
                            .build(),
                        contentDescription = reward.title,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = MaterialTheme.spacing.medium)
                ) {
                    Text(
                        text = reward.title,
                        style = MaterialTheme.typography.h5,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = reward.partner,
                        style = MaterialTheme.typography.subtitle2
                    )
                    if (reward.numberOfRedeem >= reward.maxRedeem) {
                        Button(
                            onClick = {},
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(20.dp),
                            colors = ButtonDefaults.buttonColors(Color.Gray)
                        ) {
                            Text(
                                text = "Redeem Limit Reached",
                                style = MaterialTheme.typography.caption
                            )
                        }
                    } else {
                        if (!rewardViewModel.state.value.isLoadingRedeemReward) {
                            Button(
                                onClick = {
                                    rewardViewModel.onRedeemRewardJob(rewardId = reward.id)
                                },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(20.dp),
                                colors = ButtonDefaults.buttonColors(MaterialTheme.colors.primary)
                            ) {
                                Text(
                                    text = "Redeem ${reward.pointsNeeded} Eco Points",
                                    style = MaterialTheme.typography.caption
                                )
                            }
                        } else {
                            Button(
                                onClick = {},
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(20.dp),
                                colors = ButtonDefaults.buttonColors(Color.Gray)
                            ) {
                                Text(
                                    text = "Redeeming Reward...",
                                    style = MaterialTheme.typography.caption
                                )
                            }
                        }
                    }
                }
            }
        }
    } else if (reward == null && myReward != null) {
        if (rewardViewModel == null && myRewardViewModel != null) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(
                        horizontal = MaterialTheme.spacing.medium,
                        vertical = MaterialTheme.spacing.small
                    )
                    .shadow(elevation = 2.dp, shape = RoundedCornerShape(8.dp))
                    .clip(shape = RoundedCornerShape(8.dp))
                    .clickable(onClick = onClick)
                    .background(MaterialTheme.colors.surface)
                    .fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .width(120.dp)
                        .height(160.dp)
                ) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(myReward.bannerUrl)
                            .crossfade(true)
                            .scale(Scale.FILL)
                            .build(),
                        contentDescription = myReward.title,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = MaterialTheme.spacing.medium)
                ) {
                    Text(
                        text = myReward.title,
                        style = MaterialTheme.typography.h5,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = myReward.partner,
                        style = MaterialTheme.typography.subtitle2
                    )
                    when (myReward.claimStatus) {
                        1 -> {
                            if (!myRewardViewModel.state.value.isLoadingUseReward) {
                                Button(
                                    onClick = {
                                        myRewardViewModel.onUseRewardJob(rewardId = myReward.id)
                                    },
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(20.dp),
                                    colors = ButtonDefaults.buttonColors(MaterialTheme.colors.primary)
                                ) {
                                    Text(
                                        text = "Use Now",
                                        style = MaterialTheme.typography.caption
                                    )
                                }
                            } else {
                                Button(
                                    onClick = {},
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(20.dp),
                                    colors = ButtonDefaults.buttonColors(Color.Gray)
                                ) {
                                    Text(
                                        text = "Using Reward...",
                                        style = MaterialTheme.typography.caption
                                    )
                                }
                            }
                        }
                        2 -> {
                            Button(
                                onClick = {},
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(20.dp),
                                colors = ButtonDefaults.buttonColors(CustardYellow)
                            ) {
                                Text(
                                    text = "Requested",
                                    style = MaterialTheme.typography.caption
                                )
                            }
                        }
                        3 -> {
                            Button(
                                onClick = {},
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(20.dp),
                                colors = ButtonDefaults.buttonColors(Color.Gray)
                            ) {
                                Text(
                                    text = "Completed",
                                    style = MaterialTheme.typography.caption
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}