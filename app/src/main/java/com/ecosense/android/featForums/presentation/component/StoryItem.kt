package com.ecosense.android.featForums.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.outlined.Comment
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
import com.ecosense.android.featForums.presentation.model.StoryPresentation

@Composable
fun StoryItem(
    story: () -> StoryPresentation,
    onClickLike: () -> Unit,
    onClickComment: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colors.surface)
            .padding(MaterialTheme.spacing.medium),
    ) {
        AsyncImage(
            model = story().profilePictureUrl,
            contentDescription = null,
            placeholder = painterResource(id = R.drawable.ic_ecosense_logo),
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(45.dp)
                .clip(CircleShape),
        )

        Spacer(modifier = Modifier.width(MaterialTheme.spacing.medium))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = story().name,
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.subtitle1,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colors.primary,
            )

            Text(
                text = story().username,
                modifier = Modifier.fillMaxWidth(),
            )

            Text(
                text = story().caption,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = MaterialTheme.spacing.small),
            )

            story().photoUrl?.let {
                AsyncImage(
                    model = it,
                    contentDescription = null,
                    modifier = Modifier.fillMaxWidth(),
                )
            }

            Text(
                text = story().createdAt,
                style = MaterialTheme.typography.caption,
                color = MaterialTheme.colors.primary,
                modifier = Modifier.fillMaxWidth(),
            )

            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Row(
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f),
                ) {
                    IconButton(onClick = onClickLike) {
                        Icon(
                            imageVector =
                            if (story().isLiked) Icons.Filled.ThumbUp
                            else Icons.Outlined.ThumbUp,
                            contentDescription = stringResource(R.string.cd_like),
                        )
                    }

                    Text(
                        text = story().likesCount.toString(),
                        style = MaterialTheme.typography.caption,
                    )
                }

                Row(
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f),
                ) {
                    IconButton(onClick = onClickComment) {
                        Icon(
                            imageVector = Icons.Outlined.Comment,
                            contentDescription = stringResource(R.string.cd_comment),
                        )
                    }

                    Text(
                        text = story().commentsCount.toString(),
                        style = MaterialTheme.typography.caption,
                    )
                }

                Row(
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f),
                ) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = stringResource(R.string.cd_share),
                    )
                }
            }
        }
    }
}