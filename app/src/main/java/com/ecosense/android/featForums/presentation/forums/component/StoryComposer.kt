package com.ecosense.android.featForums.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Attachment
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.ecosense.android.R
import com.ecosense.android.core.presentation.theme.spacing
import com.ecosense.android.featForums.presentation.forums.model.StoryComposerState

@Composable
fun StoryComposer(
    state: () -> StoryComposerState,
    onCaptionChange: (value: String) -> Unit,
    onAttachClick: () -> Unit,
    onShareClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.Top,
        modifier = modifier.fillMaxWidth(),
    ) {
        AsyncImage(
            model = state().profilePictureUrl,
            contentDescription = null,
            placeholder = painterResource(id = R.drawable.ic_ecosense_logo),
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(45.dp)
                .clip(CircleShape),
        )

        Spacer(modifier = Modifier.width(MaterialTheme.spacing.medium))

        Column(
            modifier = Modifier
                .background(MaterialTheme.colors.surface)
                .shadow(elevation = 2.dp, shape = RoundedCornerShape(8.dp), clip = true)
                .weight(1f)
                .padding(MaterialTheme.spacing.small),
        ) {
            TextField(
                value = state().caption ?: "",
                onValueChange = onCaptionChange,
                placeholder = { Text(text = stringResource(id = R.string.whats_happening)) },
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                ),
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth(),
            ) {
                IconButton(onClick = onAttachClick) {
                    Icon(
                        imageVector = Icons.Default.Attachment,
                        contentDescription = stringResource(R.string.attachment),
                    )
                }

                Button(onClick = onShareClick) {
                    Text(text = stringResource(R.string.share))
                }
            }
        }
    }
}