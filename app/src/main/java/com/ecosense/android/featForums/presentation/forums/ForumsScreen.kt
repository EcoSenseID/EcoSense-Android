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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ecosense.android.R
import com.ecosense.android.core.presentation.model.CampaignPresentation
import com.ecosense.android.core.presentation.theme.GradientForButtons
import com.ecosense.android.destinations.CampaignDetailScreenDestination
import com.ecosense.android.destinations.StoryComposerScreenDestination
import com.ecosense.android.destinations.StoryDetailScreenDestination
import com.ecosense.android.destinations.StorySupportersScreenDestination
import com.ecosense.android.featForums.presentation.forums.component.StoryItem
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import logcat.logcat

@Composable
@Destination
@RootNavGraph(start = true)
fun ForumsScreen(
    navigator: DestinationsNavigator,
    viewModel: ForumsViewModel = hiltViewModel(),
) {
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
                            StoryComposerScreenDestination(
                                caption = null,
                                campaign = null,
                            )
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
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(scaffoldPadding),
        ) {
            val feedState = viewModel.feedState
            items(
                count = feedState.stories.size,
                key = { i -> feedState.stories[i].id },
            ) { i ->
                if (i >= feedState.stories.size - 1 && !feedState.isEndReached && !feedState.isLoading) viewModel.onLoadNextStoriesFeed()

                val story = feedState.stories[i]
                StoryItem(
                    story = { story },
                    onClickSupport = { viewModel.onClickSupport(storyId = story.id) },
                    onClickReply = { navigator.navigate(StoryDetailScreenDestination(story)) },
                    onClickShare = { /*TODO*/ logcat { "onClickShare $i" } },
                    onClickSupporters = { navigator.navigate(StorySupportersScreenDestination(story.id)) },
                    onClickSharedCampaign = { campaign: CampaignPresentation ->
                        navigator.navigate(CampaignDetailScreenDestination(id = campaign.id))
                    },
                    modifier = Modifier.clickable {
                        navigator.navigate(StoryDetailScreenDestination(story))
                    },
                )

                Divider()
            }

            item {
                if (feedState.isLoading) {
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