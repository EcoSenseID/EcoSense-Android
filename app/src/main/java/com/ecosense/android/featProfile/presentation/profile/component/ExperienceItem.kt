package com.ecosense.android.featProfile.presentation.profile.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ecosense.android.R
import com.ecosense.android.core.presentation.theme.spacing
import com.ecosense.android.featProfile.domain.model.Experience

@Composable
fun ExperienceItem(experience: Experience) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(
                start = MaterialTheme.spacing.medium,
                end = MaterialTheme.spacing.medium,
                bottom = MaterialTheme.spacing.medium
            )
            .shadow(
                elevation = 2.dp,
                shape = RoundedCornerShape(8.dp),
                clip = true
            )
            .background(MaterialTheme.colors.surface)
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .padding(MaterialTheme.spacing.medium)
        ) {
            Text(
                text = experience.categoryName,
                style = MaterialTheme.typography.subtitle1,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
            LinearProgressIndicator(
                progress = experience.currentExperience.toFloat() / experience.levelExperience,
                color = MaterialTheme.colors.secondary,
                modifier = Modifier
                    .height(16.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(4.dp))
            )
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
            Text(
                text = "${experience.currentExperience} / ${experience.levelExperience} XP",
                style = MaterialTheme.typography.caption
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxHeight()
                .wrapContentWidth()
                .padding(
                    top = MaterialTheme.spacing.medium,
                    end = MaterialTheme.spacing.medium,
                    bottom = MaterialTheme.spacing.medium,
                )
        ) {
            Text(
                text = stringResource(R.string.level).uppercase(),
                style = MaterialTheme.typography.caption,
            )
            Text(
                text = experience.level.toString(),
                style = MaterialTheme.typography.h5,
                fontWeight = FontWeight.SemiBold,
            )
        }
    }
}