package com.ecosense.android.featProfile.presentation.profile.component

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ecosense.android.core.presentation.theme.spacing
import com.ecosense.android.featProfile.domain.model.Experience

@Composable
fun ExperiencesTab(
    experiences: List<Experience>,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier.fillMaxSize()) {
        items(experiences.size) { i ->
            if (i == 0) Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
            ExperienceItem(experience = experiences[i])
        }
    }
}