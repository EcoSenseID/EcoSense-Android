package com.ecosense.android.featForums.presentation.forums

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ecosense.android.core.presentation.theme.spacing
import com.ecosense.android.destinations.StoryDetailScreenDestination
import com.ecosense.android.featForums.presentation.component.StoryComposer
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
        modifier = Modifier.fillMaxSize(),
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
        ) {
            item {
                StoryComposer(
                    state = { viewModel.storyComposerState },
                    modifier = Modifier.padding(MaterialTheme.spacing.medium),
                    onCaptionChange = { value: String -> viewModel.onComposerCaptionChange(value) },
                    onAttachClick = { /* TODO */ },
                    onShareClick = { /* TODO */ },
                )
            }

            val feedState = viewModel.storiesFeedState
            items(
                count = feedState.stories.size,
                key = { i -> feedState.stories[i].id },
            ) { i ->
                if (i >= feedState.stories.size - 1 && !feedState.isEndReached && !feedState.isLoading) viewModel.onLoadNextStoriesFeed()

                StoryItem(
                    story = { feedState.stories[i] },
                    onClickLike = { /*TODO*/ logcat { "onClickLike $i" } },
                    onClickComment = { /*TODO*/ logcat { "onClickComment $i" } },
                    onClickShare = { /*TODO*/ logcat { "onClickShare $i" } },
                    modifier = Modifier.clickable {
                        navigator.navigate(StoryDetailScreenDestination(feedState.stories[i]))
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