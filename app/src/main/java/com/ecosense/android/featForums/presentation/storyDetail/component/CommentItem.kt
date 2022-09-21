package com.ecosense.android.featForums.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.outlined.ThumbUp
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
import com.ecosense.android.featForums.presentation.storyDetail.model.CommentPresentation

@Composable
fun CommentItem(
    comment: () -> CommentPresentation,
    onClickLike: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colors.surface)
            .padding(MaterialTheme.spacing.medium),
    ) {
        AsyncImage(
            model = comment().profilePictureUrl,
            contentDescription = null,
            placeholder = painterResource(id = R.drawable.ic_ecosense_logo),
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
                    text = comment().name,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colors.primary,
                )

                Spacer(modifier = Modifier.width(MaterialTheme.spacing.small))

                Text(
                    text = comment().username,
                    color = MaterialTheme.colors.onSurface.copy(0.7f),
                )

                Spacer(modifier = Modifier.width(MaterialTheme.spacing.small))

                Box(
                    modifier = Modifier
                        .background(color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f))
                        .clip(CircleShape)
                        .size(2.dp)
                )

                Spacer(modifier = Modifier.width(MaterialTheme.spacing.small))

                Text(
                    text = comment().createdAt,
                    color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f),
                )
            }

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))

            Text(
                text = comment().caption,
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))

            comment().photoUrl?.let {
                AsyncImage(
                    model = it,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp)),
                )

                Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
            }


            Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable { onClickLike() },
            ) {
                Icon(
                    imageVector = if (comment().isLiked) Icons.Filled.ThumbUp else Icons.Outlined.ThumbUp,
                    contentDescription = stringResource(R.string.cd_support),
                    tint = MaterialTheme.colors.onSurface.copy(alpha = 0.7f),
                )

                Spacer(modifier = Modifier.width(MaterialTheme.spacing.small))

                Text(
                    text = comment().likesCount.toString(),
                    color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f),
                )
            }
        }
    }
}