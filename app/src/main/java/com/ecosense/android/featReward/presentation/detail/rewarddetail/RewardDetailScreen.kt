package com.ecosense.android.featReward.presentation.detail.rewarddetail

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.ecosense.android.R
import com.ecosense.android.core.presentation.component.GradientButton
import com.ecosense.android.core.presentation.theme.*
import com.ecosense.android.core.presentation.util.UIEvent
import com.ecosense.android.core.presentation.util.asString
import com.ecosense.android.destinations.LoginScreenDestination
import com.ecosense.android.featReward.data.util.ecopointsFormatter
import com.ecosense.android.featReward.presentation.component.RewardTopBar
import com.ecosense.android.featReward.presentation.detail.component.RewardItemDetail
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
@Destination
fun RewardDetailScreen(
    navigator: DestinationsNavigator,
    rewardId: Int,
    totalPoints: Int,
    viewModel: RewardDetailViewModel = hiltViewModel()
) {
    remember { viewModel.getRewardDetail(rewardId = rewardId) }

    val sheetState = rememberBottomSheetState(
        initialValue = BottomSheetValue.Collapsed
    )

    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = sheetState
    )

    val coroutineScope = rememberCoroutineScope()

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

    val scrollState = rememberScrollState()

    val state = viewModel.state.value
    val reward = state.rewardDetail

    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    if (totalPoints < state.rewardDetail.pointsNeeded) {
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
                                text = stringResource(R.string.select_destination_ewallet),
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
                                            border = BorderStroke(1.dp, color = DarkRed),
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
                                            onClick = { viewModel.onRequestRewardJob(rewardId = rewardId) },
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
                                                viewModel.onRedeemRewardJob(
                                                    rewardId = rewardId
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
        sheetPeekHeight = 24.dp,
        sheetShape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
        topBar = {
            RewardTopBar(
                onBackClick = {
                    navigator.popBackStack()
                },
                screenName = reward.title
            )
        },
        scaffoldState = scaffoldState,
        floatingActionButtonPosition = FabPosition.Center,
        floatingActionButton = {
            if (!state.isLoadingRewardDetail) {
                if (!sheetState.isExpanded) {
                    if (viewModel.isLoggedIn.collectAsState().value != true) {
                        GradientButton(
                            onClick = {
                                navigator.navigate(
                                    LoginScreenDestination()
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = MaterialTheme.spacing.medium)
                        ) {
                            Text(
                                text = stringResource(R.string.login_first_to_redeem),
                                style = MaterialTheme.typography.body1,
                                color = White,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    } else {
                        if (reward.numberOfRedeem >= reward.maxRedeem) {
                            ExtendedFloatingActionButton(
                                text = {
                                    Text(
                                        text = stringResource(R.string.redeem_limit_reached),
                                        style = MaterialTheme.typography.body1,
                                        color = White,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                },
                                backgroundColor = SuperDarkGrey,
                                onClick = {},
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = MaterialTheme.spacing.medium)
                            )
                        } else {
                            if (!state.isLoadingRedeemReward) {
                                GradientButton(
                                    onClick = {
                                        when (reward.category) {
                                            "e-wallet" -> {
                                                coroutineScope.launch {
                                                    if (sheetState.isCollapsed) {
                                                        sheetState.expand()
                                                    }
                                                }
                                            }
                                            "donation" -> {
                                                viewModel.onSheetConditionalValueChange(4)
                                                coroutineScope.launch {
                                                    if (sheetState.isCollapsed) {
                                                        sheetState.expand()
                                                    }
                                                }
                                            }
                                            else -> {
                                                viewModel.onRedeemRewardJob(
                                                    rewardId = rewardId
                                                )
                                            }
                                        }
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = MaterialTheme.spacing.medium)
                                ) {
                                    Text(
                                        text = stringResource(R.string.redeem),
                                        style = MaterialTheme.typography.body1,
                                        color = White,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                    Spacer(modifier = Modifier.width(MaterialTheme.spacing.extraSmall))
                                    Box(
                                        contentAlignment = Alignment.Center,
                                        modifier = Modifier
                                            .size(14.dp)
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
                                            colorFilter = ColorFilter.tint(EcoPointsColor),
                                            modifier = Modifier.fillMaxSize(),
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(2.dp))
                                    Text(
                                        text = ecopointsFormatter(reward.pointsNeeded),
                                        style = MaterialTheme.typography.body1,
                                        fontWeight = FontWeight.SemiBold,
                                        color = EcoPointsColor
                                    )
                                    Spacer(modifier = Modifier.width(MaterialTheme.spacing.extraSmall))
                                    Text(
                                        text = stringResource(R.string.ecopoints),
                                        style = MaterialTheme.typography.body1,
                                        color = White,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                            } else {
                                ExtendedFloatingActionButton(
                                    text = {
                                        Text(
                                            text = stringResource(R.string.redeeming_reward),
                                            style = MaterialTheme.typography.body1,
                                            color = White,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                    },
                                    backgroundColor = DarkGrey,
                                    onClick = {},
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = MaterialTheme.spacing.medium)
                                )
                            }
                        }
                    }
                }
            }
        }
    ) {
        if (state.isLoadingRewardDetail) {
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
        } else {
            RewardItemDetail(
                reward = reward,
                myReward = null,
                scrollState = scrollState,
                context = context
            )
        }
    }
}