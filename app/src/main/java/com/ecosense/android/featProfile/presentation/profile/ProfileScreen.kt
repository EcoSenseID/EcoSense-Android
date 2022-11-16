package com.ecosense.android.featProfile.presentation.profile

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import coil.compose.AsyncImage
import com.ecosense.android.R
import com.ecosense.android.core.presentation.component.GradientButton
import com.ecosense.android.core.presentation.modifier.brushForeground
import com.ecosense.android.core.presentation.theme.Gradient
import com.ecosense.android.core.presentation.theme.spacing
import com.ecosense.android.core.presentation.util.UIEvent
import com.ecosense.android.core.presentation.util.asString
import com.ecosense.android.core.util.OnLifecycleEvent
import com.ecosense.android.destinations.*
import com.ecosense.android.featProfile.presentation.component.RecentStoryItem
import com.ecosense.android.featProfile.presentation.profile.component.ProfileTopBar
import com.ecosense.android.featProfile.presentation.component.RecentCampaignItem
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalUnitApi::class, ExperimentalMaterialApi::class)
@Composable
@Destination
fun ProfileScreen(
    navigator: DestinationsNavigator,
    viewModel: ProfileViewModel = hiltViewModel(),
) {
    OnLifecycleEvent { if (it == Lifecycle.Event.ON_RESUME) viewModel.onRefreshProfile() }

    val scaffoldState = rememberScaffoldState()

    val focusManager = LocalFocusManager.current
    val context = LocalContext.current

    val state = viewModel.state.value

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is UIEvent.ShowSnackbar -> {
                    focusManager.clearFocus()
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = event.uiText.asString(context)
                    )
                }

                is UIEvent.HideKeyboard -> {
                    focusManager.clearFocus()
                }
                else -> {}
            }
        }
    }

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            ProfileTopBar(
                isDropdownMenuVisible = viewModel.isLoggedIn.collectAsState().value == true,
                isDropdownMenuExpanded = state.isDropdownMenuExpanded,
                onExpandDropdownMenu = { viewModel.setExpandDropdownMenu(true) },
                onDropdownMenuDismissRequest = { viewModel.setExpandDropdownMenu(false) },
            ) {
                DropdownMenuItem(onClick = {
                    viewModel.setExpandDropdownMenu(false)
                    navigator.navigate(SettingsScreenDestination)
                }) { Text(text = stringResource(R.string.settings)) }
            }
        },
    ) { scaffoldPadding ->
        if (viewModel.isLoggedIn.collectAsState().value != true) Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.surface)
                .padding(scaffoldPadding)
                .padding(MaterialTheme.spacing.medium),
        ) {
            AsyncImage(
                model = R.drawable.character_03,
                contentDescription = null,
                modifier = Modifier.width(160.dp),
            )

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

            Text(
                text = stringResource(id = R.string.em_please_login_first),
                color = MaterialTheme.colors.onSurface,
                style = MaterialTheme.typography.h6,
                fontWeight = FontWeight.Bold,
            )

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

            GradientButton(
                onClick = { navigator.navigate(LoginScreenDestination) },
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(
                    text = stringResource(id = R.string.login),
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colors.onPrimary,
                )
            }
        } else LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.surface)
                .padding(scaffoldPadding),
        ) {
            item {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(MaterialTheme.spacing.medium),
                ) {
                    Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

                    AsyncImage(
                        model = state.user.photoUrl,
                        contentDescription = null,
                        placeholder = painterResource(id = R.drawable.ic_ecosense_logo),
                        fallback = painterResource(id = R.drawable.ic_ecosense_logo),
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .padding(MaterialTheme.spacing.medium)
                            .size(72.dp)
                            .clip(CircleShape),
                    )

                    Text(
                        text = if (!state.user.displayName.isNullOrBlank()) state.user.displayName
                        else stringResource(R.string.ecosense_user),
                        fontSize = TextUnit(22f, TextUnitType.Sp),
                        fontWeight = FontWeight.ExtraBold,
                        modifier = Modifier.brushForeground(brush = Gradient),
                    )

                    Spacer(modifier = Modifier.height(MaterialTheme.spacing.large))

                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(
                                width = 1.dp,
                                color = MaterialTheme.colors.onSurface.copy(alpha = 0.1f),
                                shape = RoundedCornerShape(16.dp),
                            )
                            .padding(MaterialTheme.spacing.medium + MaterialTheme.spacing.small),
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = stringResource(R.string.total_ecopoints),
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colors.onSurface.copy(alpha = 0.4f),
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier
                                        .size(32.dp)
                                        .clip(CircleShape)
                                        .background(MaterialTheme.colors.primary)
                                        .padding(MaterialTheme.spacing.extraSmall)
                                        .border(
                                            width = 1.dp,
                                            color = MaterialTheme.colors.onPrimary,
                                            shape = CircleShape,
                                        )
                                        .padding(MaterialTheme.spacing.extraSmall),
                                ) {
                                    AsyncImage(
                                        model = R.drawable.ic_ecosense_logo_vector,
                                        contentDescription = null,
                                        colorFilter = ColorFilter.tint(MaterialTheme.colors.onPrimary),
                                        modifier = Modifier.fillMaxSize(),
                                    )
                                }

                                Spacer(modifier = Modifier.width(MaterialTheme.spacing.small))

                                Text(
                                    text = state.totalEcoPoints.toString(),
                                    fontSize = TextUnit(32f, TextUnitType.Sp),
                                    color = MaterialTheme.colors.primary,
                                    fontWeight = FontWeight.ExtraBold,
                                    textAlign = TextAlign.Center,
                                )
                            }
                        }

                        Spacer(
                            modifier = Modifier.width(
                                MaterialTheme.spacing.medium + MaterialTheme.spacing.small
                            ),
                        )

                        GradientButton(
                            onClick = {
                                navigator.navigate(RewardHomepageScreenDestination) {
                                    popUpTo(DiscoverCampaignScreenDestination.route) {
                                        saveState = true
                                    }
                                }
                            },
                            height = 36.dp,
                            modifier = Modifier.weight(1f),
                        ) {
                            Text(
                                text = stringResource(R.string.get_rewards),
                                color = MaterialTheme.colors.onPrimary,
                                fontWeight = FontWeight.Bold,
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Text(
                            text = stringResource(R.string.my_stories),
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.h6,
                        )

                        TextButton(onClick = {
                            navigator.navigate(StoryHistoryScreenDestination(userId = null))
                        }) {
                            Text(
                                text = stringResource(R.string.see_all),
                                fontWeight = FontWeight.SemiBold,
                            )
                            Icon(
                                imageVector = Icons.Rounded.ChevronRight,
                                contentDescription = null,
                            )
                        }
                    }

                    for (i in viewModel.recentStories.indices) {
                        RecentStoryItem(
                            story = { viewModel.recentStories[i] },
                            onClickSupport = { viewModel.onClickSupport(viewModel.recentStories[i].id) },
                            onClickReply = {
                                navigator.navigate(
                                    StoryDetailScreenDestination(
                                        viewModel.recentStories[i].id
                                    )
                                )
                            },
                            onClickShare = {
                                val shareText = context.getString(
                                    R.string.format_share_message,
                                    viewModel.recentStories[i].id,
                                )

                                Intent(Intent.ACTION_SEND).let { intent ->
                                    intent.type = context.getString(R.string.intent_type_plain_text)
                                    intent.putExtra(Intent.EXTRA_TEXT, shareText)
                                    context.startActivity(intent)
                                }
                            },
                            onClickSupporters = {
                                navigator.navigate(
                                    StorySupportersScreenDestination(
                                        viewModel.recentStories[i].id
                                    )
                                )
                            },
                            onClickSharedCampaign = { campaign ->
                                navigator.navigate(CampaignDetailScreenDestination(id = campaign.id))
                            },
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp))
                                .border(
                                    width = 1.dp,
                                    color = MaterialTheme.colors.onSurface.copy(alpha = 0.1f),
                                    shape = RoundedCornerShape(16.dp),
                                )
                                .clickable {
                                    navigator.navigate(
                                        StoryDetailScreenDestination(viewModel.recentStories[i].id)
                                    )
                                }
                                .fillMaxWidth()
                                .background(MaterialTheme.colors.surface)
                                .padding(MaterialTheme.spacing.medium),
                        )

                        if (i != viewModel.recentStories.lastIndex) Spacer(
                            modifier = Modifier.height(MaterialTheme.spacing.small)
                        )
                    }

                    if (viewModel.recentStories.isEmpty()) Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(MaterialTheme.spacing.medium),
                    ) {
                        AsyncImage(
                            model = R.drawable.character_06,
                            contentDescription = null,
                            modifier = Modifier.width(120.dp),
                        )

                        Text(
                            text = stringResource(R.string.em_empty_story_history),
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                        )
                    }

                    Spacer(modifier = Modifier.height(MaterialTheme.spacing.large))

                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Text(
                            text = stringResource(R.string.my_campaign_history),
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.h6,
                        )

                        TextButton(
                            onClick = {
                                navigator.navigate(CampaignHistoryScreenDestination(userId = null))
                            },
                        ) {
                            Text(
                                text = stringResource(R.string.see_all),
                                fontWeight = FontWeight.SemiBold,
                            )
                            Icon(
                                imageVector = Icons.Rounded.ChevronRight,
                                contentDescription = null,
                            )
                        }
                    }

                    for (i in state.recentCampaigns.indices) {
                        val campaign = state.recentCampaigns[i]
                        Card(
                            shape = RoundedCornerShape(16.dp),
                            onClick = {
                                navigator.navigate(CampaignDetailScreenDestination(campaign.id))
                            },
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            RecentCampaignItem(
                                campaign = { campaign },
                                modifier = Modifier.fillMaxWidth(),
                            )
                        }

                        if (i != state.recentCampaigns.lastIndex) Spacer(
                            modifier = Modifier.height(MaterialTheme.spacing.medium)
                        )
                    }

                    if (state.recentCampaigns.isEmpty()) Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(MaterialTheme.spacing.medium),
                    ) {
                        AsyncImage(
                            model = R.drawable.character_04,
                            contentDescription = null,
                            modifier = Modifier.width(120.dp),
                        )

                        Text(
                            text = stringResource(R.string.em_empty_campaign_history),
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                        )
                    }

                    Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
                }
            }
        }
    }
}