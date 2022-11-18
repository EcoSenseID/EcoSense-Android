package com.ecosense.android.featDiscoverCampaign.presentation.component

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ecosense.android.R

@Composable
fun DiscoverTopBar(
    screenName: String,
    onBackClick: () -> Unit
) {
    TopAppBar(
        backgroundColor = MaterialTheme.colors.surface,
        modifier = Modifier.fillMaxWidth(),
        elevation = 0.dp
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxSize()
            ) {
                Row(modifier = Modifier.weight(1f)) {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Rounded.ArrowBack,
                            contentDescription = stringResource(id = R.string.cd_back),
                            tint = MaterialTheme.colors.secondary
                        )
                    }
                }

                Text(
                    text = screenName,
                    color = MaterialTheme.colors.primary,
                    style = MaterialTheme.typography.h6,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
}