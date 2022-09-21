package com.ecosense.android.featForums.presentation.storySupporters

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ecosense.android.R
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Composable
@Destination
fun StorySupportersScreen(
    storyId: Int,
    navigator: DestinationsNavigator,
    viewModel: StorySupportersViewModel = hiltViewModel()
) {
    remember { viewModel.setStoryId(storyId = storyId) }

    val scaffoldState = rememberScaffoldState()
    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                backgroundColor = MaterialTheme.colors.surface,
            ) {
                IconButton(onClick = { navigator.navigateUp() }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = stringResource(R.string.cd_back),
                        tint = MaterialTheme.colors.onSurface,
                    )
                }

                Text(
                    text = stringResource(id = R.string.supported_by),
                    color = MaterialTheme.colors.onSurface,
                    style = MaterialTheme.typography.subtitle1,
                    fontWeight = FontWeight.SemiBold,
                )
            }
        },
        modifier = Modifier.fillMaxSize(),
    ) {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(
                count = viewModel.state.supporters.count(),
                key = { i -> viewModel.state.supporters[i].id },
            ) { i ->
                if (i >= viewModel.state.supporters.lastIndex && !viewModel.state.isEndReached && !viewModel.state.isLoading) viewModel.onLoadNextCommentsFeed()

                SupporterItem(supporter = { viewModel.state.supporters[i] })

                Divider()
            }

            item {
                if (viewModel.state.isLoading) {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }
        }
    }
}