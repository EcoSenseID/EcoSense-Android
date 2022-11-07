package com.ecosense.android.featNotifications.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import com.ecosense.android.R
import com.ecosense.android.core.presentation.theme.spacing
import com.ecosense.android.core.presentation.util.UIEvent
import com.ecosense.android.core.presentation.util.asString
import com.ecosense.android.core.util.OnLifecycleEvent
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshState
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.flow.collectLatest

@Composable
@Destination
fun NotificationsScreen(
    navigator: DestinationsNavigator,
    viewModel: NotificationsViewModel = hiltViewModel(),
) {
    OnLifecycleEvent { if (it == Lifecycle.Event.ON_RESUME) viewModel.onRefreshNotifs() }

    val scaffoldState = rememberScaffoldState()
    val context = LocalContext.current

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is UIEvent.ShowSnackbar -> scaffoldState.snackbarHostState.showSnackbar(
                    message = event.uiText.asString(context)
                )
                else -> {}
            }
        }
    }


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
                    Text(
                        text = stringResource(R.string.notifications),
                        color = MaterialTheme.colors.primary,
                        style = MaterialTheme.typography.h6,
                        fontWeight = FontWeight.Bold,
                    )
                }
            }
        },
    ) { scaffoldPadding ->
        SwipeRefresh(
            state = SwipeRefreshState(isRefreshing = viewModel.isRefreshing),
            onRefresh = { viewModel.onRefreshNotifs() },
            modifier = Modifier
                .fillMaxSize()
                .padding(scaffoldPadding)
                .background(MaterialTheme.colors.surface),
        ) {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                if (viewModel.todaysNotifs.isNotEmpty()) {
                    item {
                        Text(
                            text = stringResource(R.string.today),
                            color = MaterialTheme.colors.primary,
                            style = MaterialTheme.typography.h6,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(MaterialTheme.spacing.medium),
                        )
                    }

                    items(viewModel.todaysNotifs.size) { i ->
                        NotificationItem(
                            notification = { viewModel.todaysNotifs[i] },
                            onClick = { context.startActivity(it) },
                        )
                    }

                    item {
                        Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
                        Divider(modifier = Modifier.padding(horizontal = MaterialTheme.spacing.medium))
                    }
                }

                if (viewModel.yesterdaysNotifs.isNotEmpty()) {
                    item {
                        Text(
                            text = stringResource(R.string.yesterday),
                            color = MaterialTheme.colors.primary,
                            style = MaterialTheme.typography.h6,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(MaterialTheme.spacing.medium),
                        )
                    }

                    items(viewModel.yesterdaysNotifs.size) { i ->
                        NotificationItem(
                            notification = { viewModel.yesterdaysNotifs[i] },
                            onClick = { context.startActivity(it) },
                        )
                    }

                    item {
                        Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
                        Divider(modifier = Modifier.padding(horizontal = MaterialTheme.spacing.medium))
                    }
                }

                if (viewModel.lastWeeksNotifs.isNotEmpty()) {
                    item {
                        Text(
                            text = stringResource(R.string.last_week),
                            color = MaterialTheme.colors.primary,
                            style = MaterialTheme.typography.h6,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(MaterialTheme.spacing.medium),
                        )
                    }

                    items(viewModel.lastWeeksNotifs.size) { i ->
                        NotificationItem(
                            notification = { viewModel.lastWeeksNotifs[i] },
                            onClick = { context.startActivity(it) },
                        )
                    }

                    item {
                        Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
                        Divider(modifier = Modifier.padding(horizontal = MaterialTheme.spacing.medium))
                    }
                }

                if (viewModel.olderNotifs.isNotEmpty()) {
                    item {
                        Text(
                            text = stringResource(R.string.older),
                            color = MaterialTheme.colors.primary,
                            style = MaterialTheme.typography.h6,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(MaterialTheme.spacing.medium),
                        )
                    }

                    items(viewModel.olderNotifs.size) { i ->
                        NotificationItem(
                            notification = { viewModel.olderNotifs[i] },
                            onClick = { context.startActivity(it) },
                        )
                    }

                    item {
                        Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
                        Divider(modifier = Modifier.padding(horizontal = MaterialTheme.spacing.medium))
                    }
                }
            }
        }
    }
}