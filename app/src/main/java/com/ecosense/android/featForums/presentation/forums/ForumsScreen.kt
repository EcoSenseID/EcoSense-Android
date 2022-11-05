package com.ecosense.android.featForums.presentation.forums

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import com.ecosense.android.R
import com.ecosense.android.core.presentation.model.SharedCampaignPresentation
import com.ecosense.android.core.presentation.theme.GradientForButtons
import com.ecosense.android.core.presentation.util.UIEvent
import com.ecosense.android.core.presentation.util.asString
import com.ecosense.android.core.util.OnLifecycleEvent
import com.ecosense.android.destinations.*
import com.ecosense.android.featForums.presentation.forums.component.StoryItem
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshState
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.flow.collectLatest
import logcat.logcat

@Composable
@Destination
fun ForumsScreen(
    navigator: DestinationsNavigator,
    viewModel: ForumsViewModel = hiltViewModel(),
) {
    val scaffoldState = rememberScaffoldState()
    val context = LocalContext.current

    OnLifecycleEvent { if (it == Lifecycle.Event.ON_RESUME) viewModel.refreshStoriesFeed() }

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is UIEvent.ShowSnackbar -> {
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = event.uiText.asString(context)
                    )
                }
                is UIEvent.HideKeyboard -> {}
                is UIEvent.Finish -> {}
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
                        text = stringResource(id = R.string.ecoworld),
                        color = MaterialTheme.colors.primary,
                        style = MaterialTheme.typography.h6,
                        fontWeight = FontWeight.Bold,
                    )
                }
            }
        },
        floatingActionButton = {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(56.dp)
                    .shadow(elevation = 8.dp, shape = CircleShape, clip = true)
                    .background(GradientForButtons)
                    .clickable {
                        navigator.navigate(
                            if (viewModel.isLoggedIn.value != true) LoginScreenDestination()
                            else StoryComposerScreenDestination(null, null)
                        )
                    },
            ) {
                Icon(
                    imageVector = Icons.Rounded.Add,
                    contentDescription = stringResource(R.string.cd_add_story),
                    tint = MaterialTheme.colors.onPrimary,
                    modifier = Modifier.size(36.dp),
                )
            }
        },
        modifier = Modifier.fillMaxSize(),
    ) { scaffoldPadding ->

        SwipeRefresh(
            state = SwipeRefreshState(isRefreshing = viewModel.isRefreshingFeed),
            onRefresh = { viewModel.refreshStoriesFeed() },
            modifier = Modifier.padding(scaffoldPadding),
        ) {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(count = viewModel.stories.size) { i ->
                    if (i >= viewModel.stories.size - 1 && !viewModel.feedState.isEndReached && !viewModel.feedState.isLoading) viewModel.onLoadNextStoriesFeed()

                    val story = viewModel.stories[i]
                    StoryItem(
                        story = { story },
                        onClickSupport = { viewModel.onClickSupport(storyId = story.id) },
                        onClickReply = { navigator.navigate(StoryDetailScreenDestination(story)) },
                        onClickShare = { /* TODO: implement share feature */ logcat { "onClickShare $i" } },
                        onClickSupporters = {
                            navigator.navigate(
                                StorySupportersScreenDestination(
                                    story.id
                                )
                            )
                        },
                        onClickSharedCampaign = { campaign: SharedCampaignPresentation ->
                            navigator.navigate(CampaignDetailScreenDestination(id = campaign.id))
                        },
                        modifier = Modifier.clickable {
                            navigator.navigate(StoryDetailScreenDestination(story))
                        },
                    )

                    Divider()
                }

                item {
                    if (viewModel.feedState.isLoading) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                }
            }
        }
    }
}