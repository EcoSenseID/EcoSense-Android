package com.ecosense.android.featForums.presentation.storyDetail

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.ecosense.android.R
import com.ecosense.android.core.presentation.theme.spacing
import com.ecosense.android.destinations.StorySupportersScreenDestination
import com.ecosense.android.featForums.presentation.forums.component.SharedCampaign
import com.ecosense.android.featForums.presentation.forums.component.StorySupportersSection
import com.ecosense.android.featForums.presentation.model.StoryPresentation
import com.ecosense.android.featForums.presentation.storyDetail.component.ReplyComposer
import com.ecosense.android.featForums.presentation.storyDetail.component.ReplyItem
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
                        text = stringResource(R.string.story),
                        color = MaterialTheme.colors.primary,
                        style = MaterialTheme.typography.h6,
                        fontWeight = FontWeight.Bold,
                    )

                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        },
        modifier = Modifier.fillMaxSize(),
    ) { scaffoldPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(scaffoldPadding)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colors.surface)
                            .padding(MaterialTheme.spacing.medium),
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
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colors.primary,
                            )

                            Text(
                                text = story.caption,
                                modifier = Modifier.fillMaxWidth(),
                            )

                            Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))

                            story.attachedPhotoUrl?.let {
                                AsyncImage(
                                    model = it,
                                    contentDescription = null,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(16.dp)),
                                )

                                Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
                            }

                            story.sharedCampaign?.let {
                                SharedCampaign(
                                    campaign = { it },
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(16.dp))
                                        .clickable { /* TODO: not yet implemented */ },
                                )
                                Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
                            }

                            if (story.supportersAvatarsUrl.isNotEmpty()) {
                                StorySupportersSection(
                                    avatarUrls = { story.supportersAvatarsUrl },
                                    totalSupportersCount = { story.supportersCount },
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .clickable {
                                            navigator.navigate(
                                                StorySupportersScreenDestination(story.id)
                                            )
                                        }
                                        .padding(MaterialTheme.spacing.extraSmall),
                                )

                                Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
                            }


                            Text(
                                text = story.createdAt,
                                color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f),
                            )

                            Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))

                            Row(
                                horizontalArrangement = Arrangement.Start,
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth(),
                            ) {
                                Row(
                                    horizontalArrangement = Arrangement.Start,
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .weight(1f)
                                        .fillMaxHeight()
                                        .clip(RoundedCornerShape(4.dp))
                                        .clickable { /* TODO: not yet implemented */ }
                                        .padding(MaterialTheme.spacing.extraSmall),
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_support),
                                        contentDescription = stringResource(R.string.cd_support),
                                        tint = if (story.isSupported) MaterialTheme.colors.secondary
                                        else MaterialTheme.colors.onSurface.copy(alpha = 0.6f),
                                        modifier = Modifier.size(16.dp),
                                    )

                                    Spacer(modifier = Modifier.width(MaterialTheme.spacing.small))

                                    Text(
                                        text = story.supportersCount.toString(),
                                        color = if (story.isSupported) MaterialTheme.colors.secondary
                                        else MaterialTheme.colors.onSurface.copy(alpha = 0.6f),
                                    )
                                }

                                Spacer(modifier = Modifier.width(MaterialTheme.spacing.medium))

                                Row(
                                    horizontalArrangement = Arrangement.Start,
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .weight(1f)
                                        .fillMaxHeight()
                                        .clip(RoundedCornerShape(4.dp))
                                        .clickable { /* TODO: not yet implemented */ }
                                        .padding(MaterialTheme.spacing.extraSmall),
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_reply),
                                        contentDescription = stringResource(R.string.cd_reply),
                                        tint = MaterialTheme.colors.onSurface.copy(alpha = 0.6f),
                                        modifier = Modifier.size(16.dp),
                                    )

                                    Spacer(modifier = Modifier.width(MaterialTheme.spacing.small))

                                    Text(
                                        text = story.repliesCount.toString(),
                                        color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f),
                                    )
                                }

                                Spacer(modifier = Modifier.width(MaterialTheme.spacing.medium))

                                Row(
                                    horizontalArrangement = Arrangement.Start,
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .weight(1f)
                                        .fillMaxHeight()
                                        .clip(RoundedCornerShape(4.dp))
                                        .clickable { /* TODO: not yet implemented */ }
                                        .padding(MaterialTheme.spacing.extraSmall),
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_share),
                                        contentDescription = stringResource(id = R.string.cd_share),
                                        tint = MaterialTheme.colors.onSurface.copy(alpha = 0.6f),
                                        modifier = Modifier.size(16.dp),
                                    )
                                }
                            }
                        }
                    }
                }

                item { Divider() }

                val repliesState = viewModel.repliesState
                items(
                    count = repliesState.replies.size,
                    key = { i -> repliesState.replies[i].id },
                ) { i ->
                    if (i >= repliesState.replies.lastIndex && !repliesState.isEndReached && !repliesState.isLoading) viewModel.onLoadNextCommentsFeed()

                    if (i == 0) Spacer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(MaterialTheme.spacing.medium)
                            .background(MaterialTheme.colors.surface)
                    )

                    ReplyItem(
                        reply = { repliesState.replies[i] },
                        onClickSupport = { /*TODO*/ },
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colors.surface)
                            .padding(
                                horizontal = MaterialTheme.spacing.medium,
                                vertical = MaterialTheme.spacing.small,
                            ),
                    )
                }

                item {
                    if (repliesState.isLoading) {
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

            Divider()

            ReplyComposer(
                state = { viewModel.replyComposerState },
                onChangeCaption = { viewModel.onChangeReplyComposerCaption(it) },
                onFocusChangeCaption = { viewModel.onFocusChangeCaption(it) },
                onClickAttach = { /*TODO*/ },
                onClickSend = { /*TODO*/ },
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colors.surface)
                    .padding(MaterialTheme.spacing.medium),
            )
        }
    }
}