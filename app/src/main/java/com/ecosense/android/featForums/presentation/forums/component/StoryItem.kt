package com.ecosense.android.featForums.presentation.forums.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.ecosense.android.R
import com.ecosense.android.core.presentation.model.SharedCampaignPresentation
import com.ecosense.android.core.presentation.theme.spacing
import com.ecosense.android.featForums.presentation.model.StoryPresentation

@Composable
fun StoryItem(
    story: () -> StoryPresentation,
    onClickSupport: () -> Unit,
    onClickReply: () -> Unit,
    onClickShare: () -> Unit,
    onClickSupporters: () -> Unit,
    onClickSharedCampaign: (campaign: SharedCampaignPresentation) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colors.surface)
            .padding(MaterialTheme.spacing.medium),
    ) {
        AsyncImage(
            model = story().avatarUrl,
            contentDescription = null,
            placeholder = painterResource(id = R.drawable.ic_ecosense_logo),
            fallback = painterResource(id = R.drawable.ic_ecosense_logo),
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape),
        )

        Spacer(modifier = Modifier.width(MaterialTheme.spacing.medium))

        Column(modifier = Modifier.weight(1f)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(
                    text = story().name,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colors.primary,
                )

                Spacer(modifier = Modifier.width(MaterialTheme.spacing.small))

                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f))
                        .size(4.dp)
                )

                Spacer(modifier = Modifier.width(MaterialTheme.spacing.small))

                Text(
                    text = story().createdAt,
                    color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f),
                )
            }

            Text(
                text = story().caption,
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))

            if (!story().attachedPhotoUrl.isNullOrBlank()) {
                AsyncImage(
                    model = story().attachedPhotoUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp)),
                )

                Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
            }

            story().sharedCampaign?.let {
                SharedCampaign(
                    campaign = { it },
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .clickable { onClickSharedCampaign(it) },
                )
                Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
            }

            if (story().supportersAvatarsUrl.isNotEmpty()) {
                StorySupportersSection(
                    avatarUrls = { story().supportersAvatarsUrl },
                    totalSupportersCount = { story().supportersCount },
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .clickable { onClickSupporters() }
                        .padding(MaterialTheme.spacing.extraSmall),
                )

                Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
            }

            Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Row(
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(4.dp))
                        .clickable { if (!story().isLoadingSupport) onClickSupport() }
                        .padding(MaterialTheme.spacing.extraSmall),
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_support),
                        contentDescription = stringResource(R.string.cd_support),
                        tint = if (story().isSupported) MaterialTheme.colors.secondary
                        else MaterialTheme.colors.onSurface.copy(alpha = 0.6f),
                        modifier = Modifier.size(16.dp),
                    )

                    Spacer(modifier = Modifier.width(MaterialTheme.spacing.small))

                    Text(
                        text = story().supportersCount.toString(),
                        color = if (story().isSupported) MaterialTheme.colors.secondary
                        else MaterialTheme.colors.onSurface.copy(alpha = 0.6f),
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                Row(
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(4.dp))
                        .clickable { onClickReply() }
                        .padding(MaterialTheme.spacing.extraSmall),
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_reply),
                        contentDescription = stringResource(R.string.cd_reply),
                        tint = MaterialTheme.colors.onSurface.copy(alpha = 0.6f),
                        modifier = Modifier.size(16.dp),
                    )

                    Spacer(modifier = Modifier.width(MaterialTheme.spacing.small))

                    Text(
                        text = story().repliesCount.toString(),
                        color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f),
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                Row(
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(4.dp))
                        .clickable { onClickShare() }
                        .padding(MaterialTheme.spacing.extraSmall),
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_share),
                        contentDescription = stringResource(id = R.string.cd_share),
                        tint = MaterialTheme.colors.onSurface.copy(alpha = 0.6f),
                        modifier = Modifier.size(16.dp),
                    )
                }

                Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
}