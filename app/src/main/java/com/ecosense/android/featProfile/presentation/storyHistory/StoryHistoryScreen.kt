package com.ecosense.android.featProfile.presentation.storyHistory

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ecosense.android.R
import com.ecosense.android.core.presentation.theme.spacing
import com.ecosense.android.destinations.CampaignDetailScreenDestination
import com.ecosense.android.destinations.StoryDetailScreenDestination
import com.ecosense.android.destinations.StorySupportersScreenDestination
import com.ecosense.android.featProfile.presentation.profile.component.RecentStoryItem
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Composable
@Destination
fun StoryHistoryScreen(
    navigator: DestinationsNavigator,
    viewModel: StoryHistoryViewModel = hiltViewModel(),
) {
    val scaffoldState = rememberScaffoldState()

    val context = LocalContext.current

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
                        text = stringResource(R.string.my_story_history),
                        color = MaterialTheme.colors.primary,
                        style = MaterialTheme.typography.h6,
                        fontWeight = FontWeight.Bold,
                    )

                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        },
    ) { scaffoldPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(scaffoldPadding)
        ) {
            if (viewModel.isLoading) item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(MaterialTheme.spacing.medium),
                    horizontalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            item { Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium)) }

            items(viewModel.stories.size) { i ->
                RecentStoryItem(
                    story = { viewModel.stories[i] },
                    onClickSupport = { viewModel.onClickSupport(storyId = viewModel.stories[i].id) },
                    onClickReply = { navigator.navigate(StoryDetailScreenDestination(viewModel.stories[i].id)) },
                    onClickShare = {
                        val shareText = context.getString(
                            R.string.format_share_message,
                            viewModel.stories[i].id,
                        )

                        Intent(Intent.ACTION_SEND).let { intent ->
                            intent.type = context.getString(R.string.intent_type_plain_text)
                            intent.putExtra(Intent.EXTRA_TEXT, shareText)
                            context.startActivity(intent)
                        }
                    },
                    onClickSupporters = {
                        navigator.navigate(StorySupportersScreenDestination(viewModel.stories[i].id))
                    },
                    onClickSharedCampaign = { campaign ->
                        navigator.navigate(CampaignDetailScreenDestination(id = campaign.id))
                    },
                    modifier = Modifier
                        .padding(horizontal = MaterialTheme.spacing.medium)
                        .clip(RoundedCornerShape(16.dp))
                        .border(
                            width = 1.dp,
                            color = MaterialTheme.colors.onSurface.copy(alpha = 0.1f),
                            shape = RoundedCornerShape(16.dp),
                        )
                        .clickable { navigator.navigate(StoryDetailScreenDestination(viewModel.stories[i].id)) }
                        .fillMaxWidth()
                        .background(MaterialTheme.colors.surface)
                        .padding(MaterialTheme.spacing.medium),
                )

                if (i != viewModel.stories.lastIndex) Spacer(
                    modifier = Modifier.height(MaterialTheme.spacing.small)
                )
            }
        }
    }
}