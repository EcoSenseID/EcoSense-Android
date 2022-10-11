package com.ecosense.android.featReward.presentation.detail.component

import android.content.Context
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Scale
import com.ecosense.android.R
import com.ecosense.android.core.presentation.theme.spacing
import com.ecosense.android.featDiscoverCampaign.data.util.dateFormatter
import com.ecosense.android.featReward.domain.model.MyRewardDetail
import com.ecosense.android.featReward.domain.model.RewardDetail

@Composable
fun RewardItemDetail(
    reward: RewardDetail?,
    myReward: MyRewardDetail?,
    scrollState: ScrollState,
    context: Context
) {
    var termsCondition: List<String> = emptyList()
    var bannerUrl = ""
    var description = ""
    var validity = ""
    var title = ""
    var partner = ""
    var howToUse: List<String> = emptyList()

    if (reward != null && myReward == null) {
        termsCondition = reward.termsCondition
        bannerUrl = reward.bannerUrl
        description = reward.description
        validity = reward.validity
        title = reward.title
        partner = reward.partner
        howToUse = reward.howToUse
    } else if (reward == null && myReward != null) {
        termsCondition = myReward.termsCondition
        bannerUrl = myReward.bannerUrl
        description = myReward.description
        validity = myReward.validity
        title = myReward.title
        partner = myReward.partner
        howToUse = myReward.howToUse
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(
                    horizontal = MaterialTheme.spacing.medium,
                    vertical = MaterialTheme.spacing.small
                )
                .shadow(elevation = 2.dp, shape = RoundedCornerShape(8.dp))
                .clip(shape = RoundedCornerShape(8.dp))
        ) {
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(bannerUrl)
                    .crossfade(true)
                    .scale(Scale.FILL)
                    .build(),
                contentDescription = stringResource(
                    R.string.poster_of_campaign,
                    title
                ),
                contentScale = ContentScale.FillWidth,
                modifier = Modifier.fillMaxSize()
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = MaterialTheme.spacing.small,
                    bottom = MaterialTheme.spacing.medium,
                    start = MaterialTheme.spacing.medium,
                    end = MaterialTheme.spacing.medium
                )
                .shadow(elevation = 2.dp, shape = RoundedCornerShape(8.dp))
                .clip(shape = RoundedCornerShape(8.dp))
                .background(MaterialTheme.colors.surface)
                .padding(MaterialTheme.spacing.small)
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(MaterialTheme.spacing.small),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Column {
                        Row {
                            Text(
                                text = title,
                                style = MaterialTheme.typography.h5,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colors.primary,
                                modifier = Modifier
                                    .padding(bottom = MaterialTheme.spacing.small)
                            )
                        }
                        Row {
                            Text(
                                text = "Powered by ",
                                textAlign = TextAlign.Justify,
                                style = MaterialTheme.typography.overline,
                                color = MaterialTheme.colors.primary
                            )
                            Text(
                                text = partner,
                                textAlign = TextAlign.Justify,
                                style = MaterialTheme.typography.overline,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colors.primary
                            )
                        }
                    }
                }
                Row(
                    modifier = Modifier
                        .padding(MaterialTheme.spacing.small),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.Top
                ) {
                    Column {
                        Row {
                            Text(
                                text = "Valid until",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier
                                    .padding(bottom = MaterialTheme.spacing.small)
                            )
                        }
                        Row {
                            Text(
                                text = dateFormatter(validity),
                                textAlign = TextAlign.Justify,
                                style = MaterialTheme.typography.caption,
                                color = MaterialTheme.colors.primary
                            )
                        }
                    }
                }
                Row(
                    modifier = Modifier
                        .padding(MaterialTheme.spacing.small),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.Top
                ) {
                    Column {
                        Row {
                            Text(
                                text = "Description",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier
                                    .padding(bottom = MaterialTheme.spacing.small)
                            )
                        }
                        Row {
                            Text(
                                text = description,
                                textAlign = TextAlign.Justify,
                                style = MaterialTheme.typography.caption
                            )
                        }
                    }
                }
                Row(
                    modifier = Modifier
                        .padding(MaterialTheme.spacing.small),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.Top
                ) {
                    Column {
                        Row {
                            Text(
                                text = "Terms & Conditions",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier
                                    .padding(bottom = MaterialTheme.spacing.small)
                            )
                        }
                        termsCondition.forEachIndexed { index, term ->
                            Row {
                                Text(
                                    text = "${index + 1}. $term",
                                    textAlign = TextAlign.Justify,
                                    style = MaterialTheme.typography.caption
                                )
                            }
                        }
                    }
                }
                Row(
                    modifier = Modifier
                        .padding(MaterialTheme.spacing.small),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.Top
                ) {
                    Column {
                        Row {
                            Text(
                                text = "How to Use",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier
                                    .padding(bottom = MaterialTheme.spacing.small)
                            )
                        }
                        howToUse.forEachIndexed { index, step ->
                            Row {
                                Text(
                                    text = "${index + 1}. $step",
                                    textAlign = TextAlign.Justify,
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