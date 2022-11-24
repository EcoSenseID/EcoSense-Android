package com.ecosense.android.featForums.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.ecosense.android.R
import com.ecosense.android.core.presentation.modifier.brushForeground
import com.ecosense.android.core.presentation.theme.Gradient
import com.ecosense.android.core.presentation.theme.spacing
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator


@Composable
@Destination
fun ForumsScreen(
    navigator: DestinationsNavigator,
) {
    Scaffold(
        scaffoldState = rememberScaffoldState()
    ) { scaffoldPadding ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(MaterialTheme.spacing.medium)
                .padding(scaffoldPadding),
        ) {
            AsyncImage(
                model = R.drawable.character_01,
                contentDescription = null,
                modifier = Modifier.width(193.dp),
            )
            Text(
                text = stringResource(R.string.get_ready),
                fontWeight = FontWeight.Bold,
                fontSize = 32.sp,
                modifier = Modifier.brushForeground(Gradient),
            )
            Text(
                text = stringResource(R.string.something_really_cool_is_coming),
                fontWeight = FontWeight.Bold,
                fontSize = 17.sp,
            )
        }
    }
}