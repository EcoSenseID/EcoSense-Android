package com.ecosense.android.featRecognition.presentation.saved.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ecosense.android.core.presentation.theme.spacing
import com.ecosense.android.core.presentation.util.asString
import com.ecosense.android.core.util.Constants
import com.ecosense.android.featRecognition.domain.model.SavedRecognisable
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun SavedRecognisableItem(
    savedRecognisable: SavedRecognisable,
    modifier: Modifier = Modifier,
    onClick: (SavedRecognisable) -> Unit,
) {
    Column(
        modifier = modifier
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
            .clickable { onClick(savedRecognisable) }
            .padding(MaterialTheme.spacing.medium)
            .fillMaxWidth()
    ) {
        val sdf = SimpleDateFormat(Constants.DATE_FORMAT, Locale.getDefault())
        val date = Date().apply { this.time = savedRecognisable.timeInMillis }
        Text(
            text = sdf.format(date),
            style = MaterialTheme.typography.caption,
            color = MaterialTheme.colors.onSurface
        )

        Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))

        Text(
            text = savedRecognisable.readableName?.asString() ?: savedRecognisable.label,
            style = MaterialTheme.typography.subtitle1,
            color = MaterialTheme.colors.onSurface,
            fontWeight = FontWeight.SemiBold,
        )

        Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))

        Text(
            text = "Confidence: ${savedRecognisable.confidencePercent}%",
            color = MaterialTheme.colors.onSurface
        )
    }
}
