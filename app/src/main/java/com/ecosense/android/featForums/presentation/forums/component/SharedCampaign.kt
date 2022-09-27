package com.ecosense.android.featForums.presentation.forums.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.ecosense.android.core.presentation.model.CampaignPresentation
import com.ecosense.android.core.presentation.modifier.brushForeground
import com.ecosense.android.core.presentation.theme.CategoryColors
import com.ecosense.android.core.presentation.theme.GradientForButtons
import com.ecosense.android.core.presentation.theme.spacing

@Composable
fun SharedCampaign(
    campaign: () -> CampaignPresentation,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .border(
                width = 1.dp,
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.1f),
                shape = RoundedCornerShape(16.dp),
            ),
    ) {
        Text(
            text = campaign().title,
            style = MaterialTheme.typography.subtitle1,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .brushForeground(brush = GradientForButtons)
                .padding(MaterialTheme.spacing.medium),
        )

        LazyRow(modifier = Modifier.fillMaxWidth()) {
            item {
                Spacer(modifier = Modifier.width(MaterialTheme.spacing.medium))
            }

            items(count = campaign().categories.size) { i ->
                Text(
                    text = campaign().categories[i],
                    color = MaterialTheme.colors.onPrimary,
                    style = MaterialTheme.typography.caption,
                    modifier = Modifier
                        .clip(RoundedCornerShape(100))
                        .background(CategoryColors[i % CategoryColors.size])
                        .padding(horizontal = MaterialTheme.spacing.small),
                )

                Spacer(modifier = Modifier.width(MaterialTheme.spacing.small))
            }
        }

        Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

        AsyncImage(
            model = campaign().posterUrl,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(3f),
        )
    }
}