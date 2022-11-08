package com.ecosense.android.featForums.presentation.storyDetail.component

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
import com.ecosense.android.core.presentation.theme.spacing
import com.ecosense.android.featForums.presentation.storyDetail.model.ReplyPresentation

@Composable
fun ReplyItem(
    reply: () -> ReplyPresentation,
    onClickSupport: () -> Unit,
    onClickProfile: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
    ) {
        AsyncImage(
            model = reply().avatarUrl,
            contentDescription = null,
            placeholder = painterResource(id = R.drawable.ic_ecosense_logo),
            error = painterResource(id = R.drawable.ic_ecosense_logo),
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .clickable { onClickProfile() },
        )

        Spacer(modifier = Modifier.width(MaterialTheme.spacing.medium))

        Column(modifier = Modifier.weight(1f)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(
                    text = reply().name,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colors.primary,
                    modifier = Modifier.clickable { onClickProfile() },
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
                    text = reply().createdAt,
                    color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f),
                )
            }

            Text(
                text = reply().caption,
                modifier = Modifier.fillMaxWidth(),
            )

            if (!reply().attachedPhotoUrl.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))

                AsyncImage(
                    model = reply().attachedPhotoUrl,
                    error = painterResource(id = R.drawable.error_placeholder),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp)),
                )
            }

            Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .clickable { onClickSupport() }
                    .padding(MaterialTheme.spacing.extraSmall),
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_support),
                    contentDescription = stringResource(R.string.cd_support),
                    tint = if (reply().isSupported) MaterialTheme.colors.secondary
                    else MaterialTheme.colors.onSurface.copy(alpha = 0.6f),
                    modifier = Modifier.size(16.dp),
                )

                Spacer(modifier = Modifier.width(MaterialTheme.spacing.small))

                Text(
                    text = reply().supportersCount.toString(),
                    color = if (reply().isSupported) MaterialTheme.colors.secondary
                    else MaterialTheme.colors.onSurface.copy(alpha = 0.6f),
                )
            }
        }
    }
}