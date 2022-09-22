package com.ecosense.android.featForums.presentation.storyDetail.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Photo
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.ecosense.android.R
import com.ecosense.android.core.presentation.theme.spacing
import com.ecosense.android.featForums.presentation.storyDetail.model.ReplyComposerState

@Composable
fun ReplyComposer(
    state: () -> ReplyComposerState,
    onChangeCaption: (value: String) -> Unit,
    onClickAttach: () -> Unit,
    onClickSend: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(modifier = modifier) {
        AsyncImage(
            model = state().avatarUrl,
            contentDescription = null,
            placeholder = painterResource(id = R.drawable.ic_ecosense_logo),
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape),
        )

        Spacer(modifier = Modifier.width(MaterialTheme.spacing.medium))

        Column(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f)
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colors.onSurface.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(16.dp),
                ),
        ) {
            TextField(
                value = state().caption ?: "",
                onValueChange = onChangeCaption,
                placeholder = { Text(text = stringResource(R.string.reply_to_this_story)) },
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                ),
                modifier = Modifier.fillMaxWidth(),
            )

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth(),
            ) {
                IconButton(onClick = { onClickAttach() }) {
                    Icon(
                        imageVector = Icons.Rounded.Photo,
                        contentDescription = stringResource(R.string.cd_attach_photo),
                        tint = MaterialTheme.colors.secondary,
                    )
                }

                TextButton(onClick = { onClickSend() }) {
                    Text(
                        text = stringResource(R.string.post),
                        color = MaterialTheme.colors.secondary,
                        fontWeight = FontWeight.SemiBold,
                    )
                }
            }
        }
    }
}