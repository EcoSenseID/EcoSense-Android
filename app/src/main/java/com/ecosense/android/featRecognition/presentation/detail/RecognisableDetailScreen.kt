package com.ecosense.android.featRecognition.presentation.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ecosense.android.core.presentation.theme.spacing
import com.ecosense.android.core.presentation.util.asString
import com.ecosense.android.core.util.Constants
import com.ecosense.android.featRecognition.presentation.model.RecognisableDetailParcelable
import com.ecosense.android.featRecognition.presentation.saved.component.SavedRecognisableDetailTopBar
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

    Scaffold(
        topBar = { SavedRecognisableDetailTopBar(onBackClick = { navigator.navigateUp() }) },
        scaffoldState = scaffoldState,
        modifier = Modifier.fillMaxSize()
    ) { scaffoldPadding ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(scaffoldPadding)
                .padding(MaterialTheme.spacing.medium)
                .background(
                    color = MaterialTheme.colors.surface,
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(MaterialTheme.spacing.medium)
        ) {
            val sdf = SimpleDateFormat(Constants.DATE_FORMAT, Locale.getDefault())
            val date = Date().apply { this.time = state.recognisableDetail.timeInMillis }
            val formattedTime = sdf.format(date)
            Text(text = formattedTime, style = MaterialTheme.typography.caption)
            Text(text = state.recognisableDetail.confidencePercent.toString().plus("%"))

            Text(text = state.disease.readableName?.asString() ?: state.disease.label)
            state.disease.symptoms?.let { Text(text = it.asString()) }
            state.disease.treatments?.let { Text(text = it.asString()) }
            state.disease.preventiveMeasures?.let { Text(text = it.asString()) }
        }
    }
}