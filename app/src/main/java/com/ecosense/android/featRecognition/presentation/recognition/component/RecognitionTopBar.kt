package com.ecosense.android.featRecognition.presentation.recognition.component

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ecosense.android.R

@Composable
fun RecognitionTopBar(
    modifier: Modifier = Modifier,
    onHistoryClick: () -> Unit,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        TopAppBar(
            backgroundColor = MaterialTheme.colors.surface,
            elevation = 0.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Spacer(modifier = Modifier.weight(1f))

                Text(
                    text = stringResource(R.string.ecobot),
                    style = MaterialTheme.typography.h6,
                    fontWeight = FontWeight.Bold,
                )

                Row(
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                ) {
                    IconButton(onClick = onHistoryClick) {
                        Icon(
                            imageVector = Icons.Default.History,
                            contentDescription = stringResource(R.string.history),
                            tint = MaterialTheme.colors.onSurface
                        )
                    }
                }
            }
        }
    }
}