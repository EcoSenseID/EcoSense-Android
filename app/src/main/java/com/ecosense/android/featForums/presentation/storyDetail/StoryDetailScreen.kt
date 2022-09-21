package com.ecosense.android.featForums.presentation.storyDetail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.ecosense.android.R
import com.ecosense.android.core.presentation.theme.spacing
import com.ecosense.android.featForums.presentation.component.CommentItem
import com.ecosense.android.featForums.presentation.model.StoryPresentation
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Composable
@Destination
fun StoryDetailScreen(
    story: StoryPresentation,
    navigator: DestinationsNavigator,
    viewModel: StoryDetailViewModel = hiltViewModel()
) {
    remember { viewModel.setStory(story = story) }

    val scaffoldState = rememberScaffoldState()
    Scaffold(
        scaffoldState = scaffoldState,
        modifier = Modifier.fillMaxSize(),
    ) { scaffoldPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(scaffoldPadding)
        ) {
            TopAppBar(
                backgroundColor = MaterialTheme.colors.surface,
            ) {
                IconButton(onClick = { navigator.navigateUp() }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = MaterialTheme.colors.onSurface,
                    )
                }

                Text(
                    text = "Story",
                    color = MaterialTheme.colors.onSurface,
                    style = MaterialTheme.typography.subtitle1,
                    fontWeight = FontWeight.SemiBold,
                )
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colors.surface)
                            .padding(horizontal = MaterialTheme.spacing.medium),
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            AsyncImage(
                                model = story.avatarUrl,
                                contentDescription = null,
                                placeholder = painterResource(id = R.drawable.ic_ecosense_logo),
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(CircleShape),
                            )

                            Spacer(modifier = Modifier.width(MaterialTheme.spacing.medium))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = story.name,
                                    style = MaterialTheme.typography.subtitle1,
                                    fontWeight = FontWeight.SemiBold,
                                    modifier = Modifier.fillMaxWidth(),
                                )

                                Text(
                                    text = story.username,
                                    color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f),
                                    modifier = Modifier.fillMaxWidth(),
                                )
                            }

                        }

                        Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

                        Text(
                            text = story.caption,
                            modifier = Modifier.fillMaxWidth(),
                        )

                        Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

                        story.attachedPhotoUrl?.let {
                            AsyncImage(
                                model = it,
                                contentDescription = null,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(16.dp)),
                            )

                            Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
                        }


                        Text(
                            text = story.createdAt,
                            color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f),
                        )

                        Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

                        Divider()

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = MaterialTheme.spacing.medium),
                        ) {
                            Text(text = "${story.supportersCount} Likes")

                            Spacer(modifier = Modifier.width(MaterialTheme.spacing.medium))

                            Text(text = "${story.repliesCount} Comments")
                        }

                        Divider()
                    }
                }

                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(MaterialTheme.spacing.medium)
                            .background(MaterialTheme.colors.surface),
                    ) {
                        Text(text = "This is commment composeer")
                    }
                }

                val commentsFeedState = viewModel.commentsFeedState
                items(
                    count = commentsFeedState.comments.size,
                    key = { i -> commentsFeedState.comments[i].id },
                ) { i ->
                    if (i >= commentsFeedState.comments.size - 1 && !commentsFeedState.isEndReached && !commentsFeedState.isLoading) viewModel.onLoadNextCommentsFeed()

                    CommentItem(
                        comment = { commentsFeedState.comments[i] },
                        onClickLike = { /*TODO*/ },
                    )

                    Divider()
                }

                item {
                    if (commentsFeedState.isLoading) {
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