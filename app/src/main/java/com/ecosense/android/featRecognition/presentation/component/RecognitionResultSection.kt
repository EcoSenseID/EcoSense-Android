package com.ecosense.android.featRecognition.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.ecosense.android.R
import com.ecosense.android.core.presentation.theme.spacing
import com.ecosense.android.core.presentation.util.asString
import com.ecosense.android.featRecognition.domain.model.RecognitionResult

@Composable
fun DiseaseRecognitionResultSection(
    modifier: Modifier = Modifier,
    mainDiagnosis: RecognitionResult?,
    diffDiagnoses: List<RecognitionResult>?,
    onSaveResult: () -> Unit,
) {
    val context = LocalContext.current

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .wrapContentHeight()
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colors.surface,
                shape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp)
            )
            .padding(MaterialTheme.spacing.medium)
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .wrapContentHeight()
        ) {
            if (mainDiagnosis != null) {
                Text(
                    text = stringResource(R.string.recognised_title),
                    style = MaterialTheme.typography.caption
                )

                Text(
                    text = mainDiagnosis.label.asString(),
                    style = MaterialTheme.typography.h6,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colors.primary
                )

                Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
                Text(text = "I'm ${mainDiagnosis.confidencePercent}% confident")

                if (!diffDiagnoses.isNullOrEmpty()) {
                    Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
                    Text(
                        text = "But, it could also be ${
                            diffDiagnoses.joinToString { it.label.asString(context) }
                        }"
                    )
                }

                Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
                Button(onClick = onSaveResult) {
                    Icon(
                        imageVector = Icons.Default.Save,
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.width(MaterialTheme.spacing.medium))
                    Text(text = stringResource(R.string.save_result))
                }
            } else {
                Text(
                    text = stringResource(R.string.recognising),
                    style = MaterialTheme.typography.caption
                )
                Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
                Text(text = stringResource(R.string.recognition_tip))
            }
        }

        Spacer(modifier = Modifier.width(MaterialTheme.spacing.medium))

        Column(modifier = Modifier.wrapContentSize()) {
            AsyncImage(
                model = R.drawable.ic_robot_face_rafiki,
                contentDescription = null,
                modifier = Modifier.size(120.dp)
            )
        }
    }
}