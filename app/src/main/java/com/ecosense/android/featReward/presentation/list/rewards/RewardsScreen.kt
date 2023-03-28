package com.ecosense.android.featReward.presentation.list.rewards

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Scale
import com.ecosense.android.R
import com.ecosense.android.core.presentation.component.GradientButton
import com.ecosense.android.core.presentation.theme.*
import com.ecosense.android.core.presentation.util.UIEvent
import com.ecosense.android.core.presentation.util.asString
import com.ecosense.android.destinations.RewardDetailScreenDestination
import com.ecosense.android.featReward.data.util.ecopointsFormatter
import com.ecosense.android.featReward.presentation.component.RewardTopBar
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
@Destination
fun RewardsScreen(
    navigator: DestinationsNavigator,
    rewardCategory: String,
    categoryId: Int,
    totalPoints: Int,
    viewModel: RewardsViewModel = hiltViewModel()
) {
    remember { viewModel.getRewards(categoryId = categoryId) }

    val sheetState = rememberBottomSheetState(
        initialValue = BottomSheetValue.Collapsed
    )

    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = sheetState
    )

    val coroutineScope = rememberCoroutineScope()

    var requestIndex by remember { mutableStateOf(0) }

    var tempPointsNeeded by remember { mutableStateOf(0) }

    var expanded by remember { mutableStateOf(false) }
    val sortByList = listOf(
        "Go-Pay",
        "DANA",
        "OVO"
    )

    var textFieldSize by remember { mutableStateOf(Size.Zero) }

    val icon = if (expanded)
        Icons.Filled.KeyboardArrowUp
    else
        Icons.Filled.KeyboardArrowDown

    val state = viewModel.state.value
    val reward = state.rewards

    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    if (totalPoints < tempPointsNeeded) {
        viewModel.onSheetConditionalValueChange(2)
    }

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

    BottomSheetScaffold(
        sheetContent =
        {
            when (state.sheetConditional) {
                1 -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .padding(MaterialTheme.spacing.medium)
                    ) {
                        Column {
                            Row(
                                horizontalArrangement = Arrangement.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(MaterialTheme.spacing.medium)
                                    .clip(RoundedCornerShape(20.dp))
                                    .background(MintGreen)
                            ) {
                                Text(
                                    text = stringResource(R.string.reward_form),
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colors.primary,
                                    style = MaterialTheme.typography.h6,
                                    modifier = Modifier.padding(MaterialTheme.spacing.medium)
                                )
                            }

                            Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))

                            Text(
                                text = stringResource(R.string.email_address),
                                fontWeight = FontWeight.ExtraBold,
                                style = MaterialTheme.typography.subtitle1,
                                modifier = Modifier
                                    .padding(bottom = MaterialTheme.spacing.small)
                            )
                            OutlinedTextField(
                                value = state.email,
                                onValueChange = { viewModel.onEmailValueChange(it) },
                                label = { Text(text = stringResource(R.string.enter_email_address)) },
                                placeholder = { Text(text = stringResource(R.string.enter_email_address)) },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                                maxLines = 1,
                                modifier = Modifier.fillMaxWidth()
                            )

                            Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

                            Text(
                                text = stringResource(R.string.select_preferred_ewallet),
                                fontWeight = FontWeight.ExtraBold,
                                style = MaterialTheme.typography.subtitle1,
                                modifier = Modifier
                                    .padding(bottom = MaterialTheme.spacing.small)
                            )
                            Row {
                                OutlinedTextField(
                                    value = state.walletType,
                                    onValueChange = { viewModel.onWalletTypeValueChange(it) },
                                    enabled = false,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable { expanded = !expanded }
                                        .onGloballyPositioned { coordinates ->
                                            textFieldSize = coordinates.size.toSize()
                                        },
                                    label = { Text(stringResource(R.string.choose_ewallet)) },
                                    trailingIcon = {
                                        Icon(icon, stringResource(R.string.show_hide_dropdown))
                                    }
                                )
                                DropdownMenu(
                                    expanded = expanded,
                                    onDismissRequest = { expanded = false },
                                    modifier = Modifier
                                        .width(with(LocalDensity.current) { textFieldSize.width.toDp() })
                                ) {
                                    sortByList.forEach { label ->
                                        DropdownMenuItem(onClick = {
                                            viewModel.onWalletTypeValueChange(label)
                                            expanded = false
                                        }) {
                                            Text(text = label)
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

                            Text(
                                text = stringResource(R.string.ewallet_number),
                                fontWeight = FontWeight.ExtraBold,
                                style = MaterialTheme.typography.subtitle1,
                                modifier = Modifier
                                    .padding(bottom = MaterialTheme.spacing.small)
                            )
                            OutlinedTextField(
                                value = state.walletNumber,
                                onValueChange = { viewModel.onWalletNumberValueChange(it) },
                                label = { Text(text = stringResource(R.string.enter_ewallet_number)) },
                                placeholder = { Text(text = stringResource(R.string.enter_ewallet_number)) },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                                maxLines = 1,
                                modifier = Modifier.fillMaxWidth()
                            )

                            Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

                            Row(
                                horizontalArrangement = Arrangement.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
                            ) {
                                Text(
                                    text = stringResource(R.string.ewallet_form_caution),
                                    style = MaterialTheme.typography.caption,
                                    textAlign = TextAlign.Center
                                )
                            }

                            Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

                            Row(
                                horizontalArrangement = Arrangement.SpaceEvenly,
                                modifier = Modifier
                                    .fillMaxWidth()
                            ) {
                                Column(
                                    modifier = Modifier
                                        .height(40.dp)
                                        .width(150.dp)
                                ) {
                                    if (!state.isLoadingRequestReward) {
                                        OutlinedButton(
                                            enabled = !state.isLoadingRequestReward,
                                            border = BorderStroke(width = 1.dp, color = DarkRed),
                                            shape = RoundedCornerShape(10.dp),
                                            colors = ButtonDefaults.buttonColors(MaterialTheme.colors.surface),
                                            onClick = {
                                                coroutineScope.launch {
                                                    if (sheetState.isExpanded) {
                                                        sheetState.collapse()
                                                    }
                                                }
                                            },
                                            modifier = Modifier.fillMaxSize()
                                        ) {
                                            Text(
                                                text = stringResource(R.string.cancel),
                                                fontWeight = FontWeight.Medium,
                                                color = DarkRed,
                                                style = MaterialTheme.typography.button
                                            )
                                        }
                                    } else {
                                        Button(
                                            onClick = {},
                                            modifier = Modifier.fillMaxSize(),
                                            shape = RoundedCornerShape(10.dp),
                                            colors = ButtonDefaults.buttonColors(DarkGrey)
                                        ) {
                                            Text(
                                                text = stringResource(R.string.cancel),
                                                style = MaterialTheme.typography.button,
                                                color = White,
                                                fontWeight = FontWeight.Medium
                                            )
                                        }
                                    }
                                }

                                Column(
                                    modifier = Modifier
                                        .height(40.dp)
                                        .width(150.dp)
                                ) {
                                    if (!state.isLoadingRequestReward) {
                                        GradientButton(
                                            enabled = !state.isLoadingRequestReward,
                                            shape = RoundedCornerShape(10.dp),
                                            onClick = {
                                                viewModel.onRequestRewardJob(
                                                    rewardId = requestIndex,
                                                    categoryId = categoryId
                                                )
                                            },
                                            modifier = Modifier.fillMaxSize()
                                        ) {
                                            Text(
                                                text = stringResource(R.string.submit),
                                                fontWeight = FontWeight.Medium,
                                                color = White,
                                                style = MaterialTheme.typography.button
                                            )
                                        }
                                    } else {
                                        Button(
                                            onClick = {},
                                            modifier = Modifier.fillMaxSize(),
                                            shape = RoundedCornerShape(10.dp),
                                            colors = ButtonDefaults.buttonColors(DarkGrey)
                                        ) {
                                            Text(
                                                text = stringResource(R.string.submitting),
                                                textAlign = TextAlign.Center,
                                                style = MaterialTheme.typography.button,
                                                color = White,
                                                fontWeight = FontWeight.Medium
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                2 -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .padding(MaterialTheme.spacing.large)
                    ) {
                        Column {
                            Row(
                                horizontalArrangement = Arrangement.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
                            ) {
                                Text(
                                    text = stringResource(R.string.not_enough_ecopoints_title),
                                    style = MaterialTheme.typography.subtitle1,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.width(2.dp))
                                Text(
                                    text = stringResource(R.string.ecopoints),
                                    style = MaterialTheme.typography.subtitle1,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colors.secondary
                                )
                            }

                            Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

                            Row(
                                horizontalArrangement = Arrangement.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
                            ) {
                                Text(
                                    text = stringResource(R.string.not_enough_ecopoints_description),
                                    textAlign = TextAlign.Center,
                                    style = MaterialTheme.typography.body2
                                )
                            }

                            Spacer(modifier = Modifier.height(MaterialTheme.spacing.large))

                            Row(
                                horizontalArrangement = Arrangement.Center,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(
                                    modifier = Modifier
                                        .height(40.dp)
                                        .width(150.dp)
                                ) {
                                    GradientButton(
                                        shape = RoundedCornerShape(10.dp),
                                        onClick = {
                                            coroutineScope.launch {
                                                if (sheetState.isExpanded) {
                                                    sheetState.collapse()
                                                }
                                            }
                                        },
                                        modifier = Modifier.fillMaxSize()
                                    ) {
                                        Text(
                                            text = stringResource(R.string.okay),
                                            fontWeight = FontWeight.Medium,
                                            color = White,
                                            style = MaterialTheme.typography.button
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
                3 -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .padding(MaterialTheme.spacing.medium)
                    ) {
                        Column {
                            Row(
                                horizontalArrangement = Arrangement.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
                            ) {
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier
                                        .size(50.dp)
                                        .background(brush = GradientLighter, shape = CircleShape)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Done,
                                        contentDescription = stringResource(R.string.reward_form_submitted),
                                        tint = White,
                                        modifier = Modifier.size(30.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))

                            Row(
                                horizontalArrangement = Arrangement.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
                            ) {
                                Text(
                                    text = stringResource(R.string.reward_form_submitted_description),
                                    textAlign = TextAlign.Center,
                                    style = MaterialTheme.typography.body1
                                )
                            }

                            Row(
                                horizontalArrangement = Arrangement.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
                            ) {
                                Text(
                                    text = stringResource(R.string.max_1x24),
                                    textAlign = TextAlign.Center,
                                    style = MaterialTheme.typography.body1,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))

                            Row(
                                horizontalArrangement = Arrangement.Center,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                OutlinedButton(
                                    border = BorderStroke(1.dp, MaterialTheme.colors.secondary),
                                    shape = RoundedCornerShape(20.dp),
                                    colors = ButtonDefaults.buttonColors(MaterialTheme.colors.surface),
                                    onClick = {
                                        coroutineScope.launch {
                                            if (sheetState.isExpanded) {
                                                sheetState.collapse()
                                            }
                                        }
                                    },
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = stringResource(R.string.okay),
                                        fontWeight = FontWeight.Medium,
                                        color = MaterialTheme.colors.secondary,
                                        style = MaterialTheme.typography.button
                                    )
                                }
                            }
                        }
                    }
                }
                4 -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .padding(MaterialTheme.spacing.medium)
                    ) {
                        Column {
                            Row(
                                horizontalArrangement = Arrangement.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
                            ) {
                                Text(
                                    text = stringResource(R.string.donation_caution),
                                    style = MaterialTheme.typography.body1,
                                    textAlign = TextAlign.Center
                                )
                            }

                            Row(
                                horizontalArrangement = Arrangement.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
                            ) {
                                Text(
                                    text = stringResource(R.string.ecopoints),
                                    style = MaterialTheme.typography.body1,
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.Center,
                                    color = MaterialTheme.colors.secondary
                                )
                                Text(
                                    text = stringResource(R.string.question_mark),
                                    style = MaterialTheme.typography.body1,
                                    textAlign = TextAlign.Center
                                )
                            }

                            Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

                            Row(
                                horizontalArrangement = Arrangement.SpaceEvenly,
                                modifier = Modifier
                                    .fillMaxWidth()
                            ) {
                                Column(
                                    modifier = Modifier
                                        .height(40.dp)
                                        .width(150.dp)
                                ) {
                                    if (!state.isLoadingRedeemReward) {
                                        OutlinedButton(
                                            enabled = !state.isLoadingRedeemReward,
                                            border = BorderStroke(width = 1.dp, color = DarkRed),
                                            shape = RoundedCornerShape(10.dp),
                                            colors = ButtonDefaults.buttonColors(MaterialTheme.colors.surface),
                                            onClick = {
                                                coroutineScope.launch {
                                                    if (sheetState.isExpanded) {
                                                        sheetState.collapse()
                                                    }
                                                }
                                            },
                                            modifier = Modifier.fillMaxSize()
                                        ) {
                                            Text(
                                                text = stringResource(R.string.cancel),
                                                fontWeight = FontWeight.Medium,
                                                color = DarkRed,
                                                style = MaterialTheme.typography.button
                                            )
                                        }
                                    } else {
                                        Button(
                                            onClick = {},
                                            modifier = Modifier.fillMaxSize(),
                                            shape = RoundedCornerShape(10.dp),
                                            colors = ButtonDefaults.buttonColors(DarkGrey)
                                        ) {
                                            Text(
                                                text = stringResource(R.string.cancel),
                                                style = MaterialTheme.typography.button,
                                                color = White,
                                                fontWeight = FontWeight.Medium
                                            )
                                        }
                                    }
                                }

                                Column(
                                    modifier = Modifier
                                        .height(40.dp)
                                        .width(150.dp)
                                ) {
                                    if (!state.isLoadingRedeemReward) {
                                        GradientButton(
                                            enabled = !state.isLoadingRedeemReward,
                                            shape = RoundedCornerShape(10.dp),
                                            onClick = {
                                                viewModel.onRedeemRewardJob(
                                                    rewardId = requestIndex,
                                                    categoryId = categoryId
                                                )
                                            },
                                            modifier = Modifier.fillMaxSize()
                                        ) {
                                            Text(
                                                text = stringResource(R.string.yes_exclamation),
                                                fontWeight = FontWeight.Medium,
                                                color = White,
                                                style = MaterialTheme.typography.button
                                            )
                                        }
                                    } else {
                                        Button(
                                            onClick = {},
                                            modifier = Modifier.fillMaxSize(),
                                            shape = RoundedCornerShape(10.dp),
                                            colors = ButtonDefaults.buttonColors(DarkGrey)
                                        ) {
                                            Text(
                                                text = stringResource(R.string.sending),
                                                textAlign = TextAlign.Center,
                                                style = MaterialTheme.typography.button,
                                                color = White,
                                                fontWeight = FontWeight.Medium
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                5 -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .padding(MaterialTheme.spacing.medium)
                    ) {
                        Column {
                            Row(
                                horizontalArrangement = Arrangement.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
                            ) {
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier
                                        .size(50.dp)
                                        .background(brush = GradientLighter, shape = CircleShape)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Done,
                                        contentDescription = stringResource(R.string.donation_sent),
                                        tint = White,
                                        modifier = Modifier.size(30.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))

                            Row(
                                horizontalArrangement = Arrangement.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
                            ) {
                                Text(
                                    text = stringResource(R.string.donation_sent_description),
                                    textAlign = TextAlign.Center,
                                    style = MaterialTheme.typography.body1
                                )
                            }

                            Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))

                            Row(
                                horizontalArrangement = Arrangement.Center,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                OutlinedButton(
                                    border = BorderStroke(1.dp, MaterialTheme.colors.secondary),
                                    shape = RoundedCornerShape(20.dp),
                                    colors = ButtonDefaults.buttonColors(MaterialTheme.colors.surface),
                                    onClick = {
                                        coroutineScope.launch {
                                            if (sheetState.isExpanded) {
                                                sheetState.collapse()
                                            }
                                        }
                                    },
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = stringResource(R.string.okay),
                                        fontWeight = FontWeight.Medium,
                                        color = MaterialTheme.colors.secondary,
                                        style = MaterialTheme.typography.button
                                    )
                                }
                            }
                        }
                    }
                }
            }
        },
        sheetPeekHeight = 0.dp,
        sheetShape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
        topBar = {
            RewardTopBar(
                onBackClick = {
                    navigator.popBackStack()
                },
                screenName = rewardCategory
            )
        },
        scaffoldState = scaffoldState
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Row {
                if (state.isLoadingRewardList) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.wrapContentSize(),
                            strokeWidth = 3.dp
                        )
                    }
                }
                LazyColumn(modifier = Modifier.fillMaxWidth()) {
                    items(state.rewards.size) { i ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .padding(
                                    horizontal = MaterialTheme.spacing.medium,
                                    vertical = MaterialTheme.spacing.small
                                )
                                .shadow(elevation = 2.dp, shape = RoundedCornerShape(20.dp))
                                .clip(shape = RoundedCornerShape(20.dp))
                                .clickable(onClick = {
                                    navigator.navigate(
                                        RewardDetailScreenDestination(
                                            rewardId = reward[i].id,
                                            totalPoints
                                        )
                                    )
                                })
                                .background(MaterialTheme.colors.surface)
                                .fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier
                                    .width(150.dp)
                                    .height(160.dp)
                            ) {
                                AsyncImage(
                                    model = ImageRequest.Builder(LocalContext.current)
                                        .data(reward[i].bannerUrl)
                                        .crossfade(true)
                                        .scale(Scale.FILL)
                                        .build(),
                                    contentDescription = reward[i].title,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )
                            }

                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = MaterialTheme.spacing.medium)
                            ) {
                                Text(
                                    text = reward[i].title,
                                    style = MaterialTheme.typography.subtitle1,
                                    fontWeight = FontWeight.ExtraBold
                                )
                                Text(
                                    text = reward[i].partner,
                                    color = SuperDarkGrey,
                                    style = MaterialTheme.typography.subtitle2
                                )

                                if (viewModel.isLoggedIn.collectAsState().value == true) {
                                    Spacer(modifier = Modifier.height(MaterialTheme.spacing.large))

                                    if (reward[i].numberOfRedeem >= reward[i].maxRedeem) {
                                        Button(
                                            onClick = {},
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(30.dp),
                                            shape = RoundedCornerShape(20.dp),
                                            colors = ButtonDefaults.buttonColors(SuperDarkGrey)
                                        ) {
                                            Text(
                                                text = stringResource(R.string.redeem_limit_reached),
                                                style = MaterialTheme.typography.overline,
                                                color = White,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    } else {
                                        if (!state.isLoadingRedeemReward && !state.isLoadingRequestReward) {
                                            GradientButton(
                                                onClick = {
                                                    tempPointsNeeded = reward[i].pointsNeeded

                                                    when (reward[i].category) {
                                                        "e-wallet" -> {
                                                            coroutineScope.launch {
                                                                if (sheetState.isCollapsed) {
                                                                    requestIndex = reward[i].id
                                                                    sheetState.expand()
                                                                }
                                                            }
                                                        }
                                                        "donation" -> {
                                                            viewModel.onSheetConditionalValueChange(
                                                                4
                                                            )
                                                            coroutineScope.launch {
                                                                if (sheetState.isCollapsed) {
                                                                    requestIndex = reward[i].id
                                                                    sheetState.expand()
                                                                }
                                                            }
                                                        }
                                                        else -> {
                                                            viewModel.onRedeemRewardJob(
                                                                rewardId = reward[i].id,
                                                                categoryId = 3
                                                            )
                                                        }
                                                    }
                                                },
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .height(30.dp),
                                                shape = RoundedCornerShape(20.dp)
                                            ) {
                                                Text(
                                                    text = stringResource(R.string.redeem),
                                                    fontSize = 10.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = White,
                                                    modifier = Modifier.padding(start = MaterialTheme.spacing.extraSmall)
                                                )
                                                Spacer(modifier = Modifier.width(MaterialTheme.spacing.extraSmall))
                                                Box(
                                                    contentAlignment = Alignment.Center,
                                                    modifier = Modifier
                                                        .size(12.dp)
                                                        .clip(CircleShape)
                                                        .padding(1.dp)
                                                        .border(
                                                            width = 1.dp,
                                                            color = EcoPointsColor,
                                                            shape = CircleShape,
                                                        )
                                                        .padding(1.dp),
                                                ) {
                                                    AsyncImage(
                                                        model = R.drawable.ic_ecosense_logo_vector,
                                                        contentDescription = null,
                                                        colorFilter = ColorFilter.tint(
                                                            EcoPointsColor
                                                        ),
                                                        modifier = Modifier.fillMaxSize(),
                                                    )
                                                }
                                                Text(
                                                    text = ecopointsFormatter(reward[i].pointsNeeded),
                                                    fontSize = 10.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = EcoPointsColor
                                                )
                                                Spacer(modifier = Modifier.width(2.dp))
                                                Text(
                                                    text = stringResource(R.string.ecopoints),
                                                    fontSize = 10.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = White,
                                                    modifier = Modifier.padding(end = MaterialTheme.spacing.extraSmall)
                                                )
                                            }
                                        } else {
                                            Button(
                                                onClick = {},
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .height(30.dp),
                                                shape = RoundedCornerShape(20.dp),
                                                colors = ButtonDefaults.buttonColors(DarkGrey)
                                            ) {
                                                Text(
                                                    text = stringResource(R.string.redeeming_reward),
                                                    fontSize = 10.sp,
                                                    color = White,
                                                    fontWeight = FontWeight.Bold,
                                                    modifier = Modifier.padding(horizontal = MaterialTheme.spacing.extraSmall)
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
        }
    }
}