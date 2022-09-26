package com.ecosense.android.featReward.presentation.component

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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Scale
import com.ecosense.android.core.presentation.theme.spacing
import com.ecosense.android.featReward.domain.model.CategoryRewards
import com.ecosense.android.featReward.domain.model.MyRewards
import logcat.logcat

@Composable
fun RewardItem(
    categoryReward: CategoryRewards?,
    myReward: MyRewards?,
    onClick: () -> Unit
) {
    if (categoryReward != null && myReward == null) {
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
                        .data(categoryReward.bannerUrl)
                        .crossfade(true)
                        .scale(Scale.FILL)
                        .build(),
                    contentDescription = categoryReward.title,
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
                    text = categoryReward.title,
                    style = MaterialTheme.typography.h5,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = categoryReward.partner,
                    style = MaterialTheme.typography.subtitle2
                )
                if (categoryReward.numberOfRedeem >= categoryReward.maxRedeem) {
                    Button(
                        onClick = {
                            // TODO: Redeem Rewards
                            logcat("RewardsItem") { "redeem reward process running" }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        colors = ButtonDefaults.buttonColors(MaterialTheme.colors.primary)
                    ) {
                        Text(
                            text = "Redeem ${categoryReward.pointsNeeded} Eco Points",
                            style = MaterialTheme.typography.caption
                        )
                    }
                } else {
                    Button(
                        onClick = {
                            // TODO: Use Rewards
                            logcat("RewardsItem") { "use reward process running" }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        colors = ButtonDefaults.buttonColors(MaterialTheme.colors.secondary)
                    ) {
                        Text(
                            text = "Use Now",
                            style = MaterialTheme.typography.caption
                        )
                    }
                }
            }
        }
    } else if (categoryReward == null && myReward != null) {
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
                Button(
                    onClick = {
                        // TODO: Use Rewards
                        logcat("RewardsItem") { "use reward process running" }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.buttonColors(MaterialTheme.colors.secondary)
                ) {
                    Text(
                        text = "Use Now",
                        style = MaterialTheme.typography.caption
                    )
                }
            }
        }
    }
}