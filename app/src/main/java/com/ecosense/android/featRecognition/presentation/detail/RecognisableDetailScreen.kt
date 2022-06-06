package com.ecosense.android.featRecognition.presentation.detail

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ecosense.android.R
import com.ecosense.android.core.presentation.component.RoundedEndsButton
import com.ecosense.android.core.presentation.theme.spacing
import com.ecosense.android.core.presentation.util.asString
import com.ecosense.android.core.util.Constants
import com.ecosense.android.featRecognition.presentation.model.RecognisableDetailParcelable
import com.ecosense.android.featRecognition.presentation.saved.component.RecognisableDetailTopBar
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import java.text.SimpleDateFormat
import java.util.*

@Composable
@Destination
fun RecognisableDetailScreen(
    navigator: DestinationsNavigator,
    recognisableDetailParcelable: RecognisableDetailParcelable,
    viewModel: RecognisableDetailViewModel = hiltViewModel(),
) {
    remember { viewModel.setDetailParcelable(recognisableDetailParcelable) }

    val state = viewModel.state.value
    val scaffoldState = rememberScaffoldState()
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            RecognisableDetailTopBar(
                onBackClick = { navigator.navigateUp() },
                showDeleteButton = state.isSaved,
                onDeleteClick = { viewModel.onDeleteClick() }
            )
        },
        scaffoldState = scaffoldState,
        modifier = Modifier.fillMaxSize()
    ) { scaffoldPadding ->
        Column(
            modifier = Modifier
                .padding(scaffoldPadding)
                .fillMaxSize()
                .verticalScroll(state = scrollState)
        ) {
            Column(
                modifier = Modifier
                    .padding(MaterialTheme.spacing.medium)
                    .shadow(
                        elevation = 2.dp,
                        shape = RoundedCornerShape(8.dp),
                        clip = true,
                    )
                    .background(color = MaterialTheme.colors.surface)
                    .padding(MaterialTheme.spacing.medium)
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {
                val sdf = SimpleDateFormat(Constants.DATE_FORMAT, Locale.getDefault())
                val date = Date().apply { this.time = state.recognisableDetail.timeInMillis }
                Text(
                    text = sdf.format(date),
                    style = MaterialTheme.typography.caption,
                    color = MaterialTheme.colors.onSurface
                )

                Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))

                Text(
                    text = state.disease.readableName?.asString() ?: state.disease.label,
                    style = MaterialTheme.typography.subtitle1,
                    color = MaterialTheme.colors.onSurface,
                    fontWeight = FontWeight.SemiBold,
                )

                Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))

                Text(
                    text = "Confidence: ${state.recognisableDetail.confidencePercent}%",
                    color = MaterialTheme.colors.onSurface
                )

                Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))

                state.disease.symptoms?.let {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = stringResource(R.string.symptoms),
                            color = MaterialTheme.colors.primaryVariant,
                            style = MaterialTheme.typography.subtitle1,
                            fontWeight = FontWeight.SemiBold,
                        )

                        Spacer(modifier = Modifier.width(MaterialTheme.spacing.small))

                        Divider(modifier = Modifier.weight(1f))
                    }

                    Text(text = it.asString())
                }

                Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))

                state.disease.preventiveMeasures?.let {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = stringResource(R.string.preventive_measures),
                            color = MaterialTheme.colors.primaryVariant,
                            style = MaterialTheme.typography.subtitle1,
                            fontWeight = FontWeight.SemiBold,
                        )

                        Spacer(modifier = Modifier.width(MaterialTheme.spacing.small))

                        Divider(modifier = Modifier.weight(1f))
                    }

                    Text(text = it.asString())
                }

                Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))

                state.disease.treatments?.let {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = stringResource(R.string.treatments),
                            color = MaterialTheme.colors.primaryVariant,
                            style = MaterialTheme.typography.subtitle1,
                            fontWeight = FontWeight.SemiBold,
                        )

                        Spacer(modifier = Modifier.width(MaterialTheme.spacing.small))

                        Divider(modifier = Modifier.weight(1f))
                    }

                    Text(text = it.asString())
                }

                if (!state.isSaved) Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

                AnimatedVisibility(visible = !state.isSaved) {
                    RoundedEndsButton(
                        enabled = !state.isSaved,
                        onClick = { viewModel.onSaveClick() },
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Icon(
                            imageVector = Icons.Default.Save,
                            contentDescription = null
                        )
                        Spacer(modifier = Modifier.width(MaterialTheme.spacing.medium))
                        Text(text = stringResource(R.string.save_result))
                    }
                }
            }
        }
    }
}