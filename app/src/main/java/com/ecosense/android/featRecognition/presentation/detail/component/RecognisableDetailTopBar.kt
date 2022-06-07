package com.ecosense.android.featRecognition.presentation.saved.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ecosense.android.R

@Composable
fun RecognisableDetailTopBar(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
) {
    TopAppBar(
        backgroundColor = MaterialTheme.colors.surface,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = stringResource(R.string.back),
                    tint = MaterialTheme.colors.onSurface
                )
            }

            Text(
                text = stringResource(R.string.recognition_result_detail),
                color = MaterialTheme.colors.onSurface,
                style = MaterialTheme.typography.subtitle1,
                fontWeight = FontWeight.SemiBold,
            )

            Spacer(modifier = Modifier.weight(1f))
        }
    }
}