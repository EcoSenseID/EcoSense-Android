package com.ecosense.android.featNotifications.presentation

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.ecosense.android.R
import com.ecosense.android.core.presentation.constants.PatternConstants
import com.ecosense.android.core.presentation.theme.spacing
import com.ecosense.android.featNotifications.presentation.model.NotificationPresentation
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun NotificationItem(
    notification: () -> NotificationPresentation,
    onClick: (Intent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                if (!notification().deeplink.isNullOrBlank()) onClick(Intent(Intent.ACTION_VIEW).apply {
                    data = Uri.parse(notification().deeplink)
                })
            }
            .padding(vertical = MaterialTheme.spacing.small)
            .padding(horizontal = MaterialTheme.spacing.medium),
    ) {
        AsyncImage(
            model = notification().iconUrl,
            contentDescription = null,
            placeholder = painterResource(id = R.drawable.ic_ecosense_logo),
            error = painterResource(id = R.drawable.ic_ecosense_logo),
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape),
        )

        Spacer(modifier = Modifier.width(MaterialTheme.spacing.medium))

        Column(modifier = Modifier.weight(1f)) {
            Text(text = notification().content)

            Text(
                text = SimpleDateFormat(
                    PatternConstants.DEFAULT_DATE_FORMAT,
                    Locale.getDefault(),
                ).format(Date().apply { time = notification().timestamp }),
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f),
                style = MaterialTheme.typography.caption,
            )
        }
    }
}