package com.ecosense.android.featProfile.presentation.profile

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Scale
import com.ecosense.android.R
import com.ecosense.android.core.presentation.theme.spacing
import com.ecosense.android.core.presentation.util.UIEvent
import com.ecosense.android.core.presentation.util.asString
import com.ecosense.android.destinations.CampaignDetailScreenDestination
import com.ecosense.android.destinations.EditProfileScreenDestination
import com.ecosense.android.featProfile.presentation.profile.component.ExperiencesTab
import com.ecosense.android.featProfile.presentation.profile.component.HistoryTab
import com.ecosense.android.featProfile.presentation.profile.component.ProfileTopBar
import com.ecosense.android.featProfile.presentation.profile.model.TabItem
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.pagerTabIndicatorOffset
import com.google.accompanist.pager.rememberPagerState
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class)
@Composable
@Destination
fun ProfileScreen(
    navigator: DestinationsNavigator,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()

    val focusManager = LocalFocusManager.current
    val context = LocalContext.current

    val state = viewModel.state.value

    val tabItems = listOf(
        TabItem(
            title = stringResource(id = R.string.experiences),
            content = {
                ExperiencesTab(
                    experiences = state.contributions.experiences
                )
            }
        ),
        TabItem(
            title = stringResource(id = R.string.history),
            content = {
                HistoryTab(
                    campaigns = state.contributions.completedCampaigns,
                    onItemClick = { navigator.navigate(CampaignDetailScreenDestination(it.id)) }
                )
            }
        )
    )

    val pagerState = rememberPagerState(pageCount = tabItems.size)

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
            }
        }
    }

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            ProfileTopBar(
                isDropdownMenuExpanded = state.isDropdownMenuExpanded,
                onExpandDropdownMenu = { viewModel.setExpandDropdownMenu(true) },
                onDropdownMenuDismissRequest = { viewModel.setExpandDropdownMenu(false) }
            ) {
                DropdownMenuItem(onClick = {
                    viewModel.setExpandDropdownMenu(false)
                    navigator.navigate(EditProfileScreenDestination)
                }) {
                    Text(text = stringResource(R.string.edit_profile))
                }

                DropdownMenuItem(onClick = { viewModel.onLogoutClick() }) {
                    Text(text = stringResource(R.string.logout))
                }
            }
        },
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = MaterialTheme.colors.surface)
                    .padding(
                        start = MaterialTheme.spacing.medium,
                        top = MaterialTheme.spacing.medium,
                        end = MaterialTheme.spacing.medium,
                    )
            ) {
                Box(modifier = Modifier.size(90.dp)) {
                    val padding by animateDpAsState(if (state.isLoadingContributions) 4.dp else 0.dp)
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(state.user.photoUrl)
                            .placeholder(R.drawable.ic_ecosense_logo)
                            .error(R.drawable.ic_ecosense_logo)
                            .crossfade(true)
                            .scale(Scale.FILL)
                            .build(),
                        contentDescription = stringResource(R.string.cd_profile_picture),
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(padding)
                            .clip(CircleShape)
                    )

                    if (state.isLoadingContributions) {
                        CircularProgressIndicator(
                            modifier = Modifier.fillMaxSize(),
                            strokeWidth = 4.dp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

                Text(
                    text =
                    if (!state.user.displayName.isNullOrBlank()) state.user.displayName
                    else stringResource(R.string.ecosense_user),
                    style = MaterialTheme.typography.h6,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colors.primary,
                )

                state.user.email?.let { Text(text = it, color = MaterialTheme.colors.primary) }

                Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

                TabRow(
                    selectedTabIndex = pagerState.currentPage,
                    backgroundColor = MaterialTheme.colors.surface,
                    indicator = { tabPositions ->
                        TabRowDefaults.Indicator(
                            height = 4.dp,
                            color = MaterialTheme.colors.primary,
                            modifier = Modifier
                                .pagerTabIndicatorOffset(pagerState, tabPositions)
                                .clip(
                                    RoundedCornerShape(
                                        topStartPercent = 100,
                                        topEndPercent = 100
                                    )
                                )
                        )
                    }
                ) {
                    tabItems.forEachIndexed { index: Int, tabItem: TabItem ->
                        val isSelected = pagerState.currentPage == index
                        Tab(
                            text = {
                                Text(
                                    text = tabItem.title,
                                    style = MaterialTheme.typography.subtitle1,
                                    color =
                                    if (isSelected) MaterialTheme.colors.primary
                                    else Color.Unspecified,
                                    fontWeight =
                                    if (isSelected) FontWeight.SemiBold
                                    else FontWeight.Normal,
                                )
                            },
                            selected = isSelected,
                            onClick = { coroutineScope.launch { pagerState.animateScrollToPage(index) } }
                        )
                    }
                }
            }

            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize()
            ) { page -> tabItems[page].content.invoke() }
        }
    }
}