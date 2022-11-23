package com.ecosense.android.featForums.presentation.forums

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
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
import com.ecosense.android.core.presentation.component.GradientButton
import com.ecosense.android.core.presentation.model.SharedCampaignPresentation
import com.ecosense.android.core.presentation.theme.GradientForButtons
import com.ecosense.android.core.presentation.theme.spacing
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
            val storiesLazyListState = rememberLazyListState()
            LaunchedEffect(viewModel.stories.size) {
                snapshotFlow { storiesLazyListState.firstVisibleItemIndex }.collect {
                    val visibleItemsSize = storiesLazyListState.layoutInfo.visibleItemsInfo.size
                    if (it + visibleItemsSize >= viewModel.stories.size && !viewModel.feedState.isEndReached && !viewModel.feedState.isLoading) {
                        viewModel.onLoadNextStoriesFeed()
                    }
                }
            }

            LazyColumn(
                state = storiesLazyListState,
                modifier = Modifier.fillMaxSize(),
            ) {
                items(count = viewModel.stories.size) { i ->
                    StoryItem(story = { viewModel.stories[i] },
                        onClickSupport = { viewModel.onClickSupport(viewModel.stories[i].id) },
                        onClickReply = { navigator.navigate(StoryDetailScreenDestination(viewModel.stories[i].id)) },
                        onClickShare = {
                            val shareText = context.getString(
                                R.string.format_share_message, viewModel.stories[i].id
                            )

                            Intent(Intent.ACTION_SEND).let { intent ->
                                intent.type = context.getString(R.string.intent_type_plain_text)
                                intent.putExtra(Intent.EXTRA_TEXT, shareText)
                                context.startActivity(intent)
                            }
                        },
                        onClickSupporters = {
                            navigator.navigate(
                                StorySupportersScreenDestination(storyId = viewModel.stories[i].id)
                            )
                        },
                        onClickSharedCampaign = { campaign: SharedCampaignPresentation ->
                            navigator.navigate(
                                CampaignDetailScreenDestination(
                                    id = campaign.id,
                                    recordId = null,
                                )
                            )
                        },
                        modifier = Modifier.clickable {
                            navigator.navigate(StoryDetailScreenDestination(viewModel.stories[i].id))
                        },
                        onClickProfile = {
                            navigator.navigate(
                                OthersProfileScreenDestination(
                                    userId = viewModel.stories[i].userId,
                                )
                            )
                        })

                    Divider()
                }

                item {
                    if (viewModel.feedState.isLoading) {
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
                    viewModel.feedState.errorMessage?.let {
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
                                    onClick = { viewModel.onLoadNextStoriesFeed() },
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