package com.ecosense.android.featRecognition.presentation.recognition.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
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
import com.ecosense.android.featRecognition.domain.model.Recognisable

@Composable
fun RecognitionResultSection(
    modifier: Modifier = Modifier,
    mainDiagnosis: Recognisable?,
    diffDiagnoses: List<Recognisable>?,
    isSavingResult: Boolean,
    onSaveResult: () -> Unit,
    onLearnMoreClick: () -> Unit,
) {
    val context = LocalContext.current

    Column(
        modifier = modifier
            .wrapContentHeight()
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colors.surface,
                shape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp)
            )
            .padding(MaterialTheme.spacing.medium)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(),
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
                        text = mainDiagnosis.readableName?.asString() ?: mainDiagnosis.label,
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
                                diffDiagnoses.joinToString {
                                    it.readableName?.asString(context) ?: it.label
                                }
                            }"
                        )
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

        if (mainDiagnosis != null) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Button(
                    enabled = !isSavingResult,
                    onClick = onSaveResult,
                ) {
                    Icon(
                        imageVector = Icons.Default.Save,
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.width(MaterialTheme.spacing.medium))
                    Text(
                        text = stringResource(
                            if (isSavingResult) R.string.saving
                            else R.string.save_result
                        )
                    )
                }

                Spacer(modifier = Modifier.width(MaterialTheme.spacing.medium))

                OutlinedButton(
                    onClick = onLearnMoreClick,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(text = stringResource(R.string.learn_more))
                }
            }
        }
    }
}