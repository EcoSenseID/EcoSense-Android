package com.ecosense.android.featForums.presentation.storySupporters

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ecosense.android.R
import com.ecosense.android.core.presentation.component.GradientButton
import com.ecosense.android.core.presentation.theme.spacing
import com.ecosense.android.core.presentation.util.asString
import com.ecosense.android.destinations.OthersProfileScreenDestination
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshState
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
                backgroundColor = MaterialTheme.colors.background,
                elevation = 0.dp,
            ) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxSize(),
                ) {
                    Row(modifier = Modifier.weight(1f)) {
                        IconButton(onClick = { navigator.navigateUp() }) {
                            Icon(
                                imageVector = Icons.Rounded.ArrowBack,
                                contentDescription = stringResource(id = R.string.cd_back),
                                tint = MaterialTheme.colors.secondary,
                            )
                        }
                    }

                    Text(
                        text = stringResource(R.string.supported_by),
                        color = MaterialTheme.colors.primary,
                        style = MaterialTheme.typography.h6,
                        fontWeight = FontWeight.Bold,
                    )

                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        },
        modifier = Modifier.fillMaxSize(),
    ) {
        SwipeRefresh(
            state = SwipeRefreshState(viewModel.isRefreshing),
            onRefresh = { viewModel.onRefresh() },
        ) {
            val lazyListState = rememberLazyListState()
            LaunchedEffect(viewModel.state.supporters.size) {
                snapshotFlow { lazyListState.firstVisibleItemIndex }.collect {
                    val visibleItemsSize = lazyListState.layoutInfo.visibleItemsInfo.size
                    if (it + visibleItemsSize >= viewModel.state.supporters.size && !viewModel.state.isEndReached && !viewModel.state.isLoading) {
                        viewModel.onLoadNextSupporters()
                    }
                }
            }

            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(count = viewModel.state.supporters.count()) { i ->
                    SupporterItem(
                        supporter = { viewModel.state.supporters[i] },
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colors.surface)
                            .clickable {
                                navigator.navigate(
                                    OthersProfileScreenDestination(
                                        userId = viewModel.state.supporters[i].userId
                                    )
                                )
                            }
                            .padding(horizontal = MaterialTheme.spacing.medium)
                            .padding(vertical = MaterialTheme.spacing.small),
                    )
                }

                item { Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium)) }

                item {
                    if (viewModel.state.isLoading) {
                        Card(
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(MaterialTheme.spacing.medium),
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(MaterialTheme.spacing.medium),
                                horizontalArrangement = Arrangement.Center,
                            ) {
                                CircularProgressIndicator()
                            }
                        }
                    }
                }

                item {
                    viewModel.state.errorMessage?.let {
                        Card(
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(MaterialTheme.spacing.medium),
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(MaterialTheme.spacing.medium),
                            ) {
                                Text(
                                    text = it.asString(),
                                    color = MaterialTheme.colors.primary,
                                    style = MaterialTheme.typography.h6,
                                    fontWeight = FontWeight.Bold,
                                )

                                Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

                                GradientButton(
                                    onClick = { viewModel.onLoadNextSupporters() },
                                    modifier = Modifier.fillMaxWidth(),
                                ) {
                                    Text(
                                        text = stringResource(R.string.retry),
                                        fontWeight = FontWeight.SemiBold,
                                        color = MaterialTheme.colors.onPrimary,
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}