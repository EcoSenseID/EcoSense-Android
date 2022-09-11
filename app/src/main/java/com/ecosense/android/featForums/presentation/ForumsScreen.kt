package com.ecosense.android.featForums.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ecosense.android.core.presentation.theme.spacing
import com.ecosense.android.featForums.presentation.component.StoryItem
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
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            val state = viewModel.storiesState

            items(
                count = state.stories.size,
                key = { i -> state.stories[i].id },
            ) { i ->
                if (i >= state.stories.size - 1 && !state.endReached && !state.isLoading) {
                    viewModel.loadNextItems()
                }

                StoryItem(
                    story = { state.stories[i] },
                    onClickLike = { /*TODO*/ logcat { "onClickLike $i" } },
                    onClickComment = { /*TODO*/ logcat { "onClickComment $i" } })

                Spacer(modifier = Modifier.height(MaterialTheme.spacing.extraSmall))
            }

            item {
                if (state.isLoading) {
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