package com.ecosense.android.featProfile.presentation.profile.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.ecosense.android.R
import com.ecosense.android.core.presentation.theme.spacing
import com.ecosense.android.featProfile.domain.model.Experience

@Composable
fun ExperiencesTab(
    experiences: List<Experience>,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxSize()) {
        AnimatedVisibility(visible = experiences.isNotEmpty()) {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(experiences.size) { i ->
                    if (i == 0) Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
                    ExperienceItem(experience = experiences[i])
                }
            }
        }

        AnimatedVisibility(visible = experiences.isEmpty()) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(MaterialTheme.spacing.medium)
            ) {
                AsyncImage(
                    model = R.drawable.ic_wind_turbine_pana,
                    contentDescription = null,
                    modifier = Modifier.size(200.dp)
                )

                Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

                Text(text = stringResource(R.string.empty_experiences), textAlign = TextAlign.Center)
            }
        }
    }
}