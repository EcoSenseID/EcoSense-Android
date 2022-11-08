package com.ecosense.android.featReward.presentation.detail.rewarddetail

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import androidx.hilt.navigation.compose.hiltViewModel
import com.ecosense.android.R
import com.ecosense.android.core.presentation.component.RoundedEndsButton
import com.ecosense.android.core.presentation.theme.spacing
import com.ecosense.android.core.presentation.util.UIEvent
import com.ecosense.android.core.presentation.util.asString
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
            if (totalPoints < state.rewardDetail.pointsNeeded) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(175.dp)
                        .padding(MaterialTheme.spacing.medium)
                        .clip(shape = RoundedCornerShape(8.dp, 8.dp, 0.dp, 0.dp))
                ) {
                    Column {
                        Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

                        Row(
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            Text(
                                text = "You don't have enough EcoPoints",
                                style = MaterialTheme.typography.subtitle1,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

                        Row(
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            Text(
                                text = "You don’t quite have enough EcoPoints yet. Let’s join another campaign!",
                                style = MaterialTheme.typography.body2,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

                        Row(
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            RoundedEndsButton(
                                enabled = !state.isLoadingRequestReward,
                                onClick = {
                                    coroutineScope.launch {
                                        if (sheetState.isExpanded) {
                                            sheetState.collapse()
                                        }
                                    }
                                }
                            ) {
                                Text(
                                    text = "Okay"
                                )
                            }
                        }
                    }
                }
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(550.dp)
                        .padding(MaterialTheme.spacing.medium)
                        .clip(shape = RoundedCornerShape(8.dp, 8.dp, 0.dp, 0.dp))
                ) {
                    Column {
                        Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

                        Row(
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            Text("Reward Form", style = MaterialTheme.typography.subtitle1)
                        }
                        Text(
                            text = "Email Address",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .padding(bottom = MaterialTheme.spacing.small)
                        )
                        OutlinedTextField(
                            value = state.email,
                            onValueChange = { viewModel.onEmailValueChange(it) },
                            label = { Text(text = "Enter your email address") },
                            placeholder = { Text(text = "Enter your email address") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

                        Text(
                            text = "Select the destination e-wallet",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
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
                                label = { Text("Choose your type of e-wallet") },
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
                            text = "E-Wallet Number",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .padding(bottom = MaterialTheme.spacing.small)
                        )
                        OutlinedTextField(
                            value = state.walletNumber,
                            onValueChange = { viewModel.onWalletNumberValueChange(it) },
                            label = { Text(text = "Enter the associated e-wallet number") },
                            placeholder = { Text(text = "Enter the associated e-wallet number") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

                        Row(
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            Text(
                                "After you click the submit button, you can’t change your information. \n" +
                                        "So, make sure you enter the right information.",
                                style = MaterialTheme.typography.caption
                            )
                        }

                        Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

                        Row(
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            Column {
                                RoundedEndsButton(
                                    enabled = !state.isLoadingRequestReward,
                                    onClick = {
                                        coroutineScope.launch {
                                            if (sheetState.isExpanded) {
                                                sheetState.collapse()
                                            }
                                        }
                                    }
                                ) {
                                    Text(
                                        text = "Cancel"
                                    )
                                }
                            }

                            Column {
                                RoundedEndsButton(
                                    enabled = !state.isLoadingRequestReward,
                                    onClick = { viewModel.onRequestRewardJob(rewardId = rewardId) }
                                ) {
                                    Text(
                                        text =
                                        if (state.isLoadingRequestReward) "Submitting Request..."
                                        else "Submit"
                                    )
                                }
                            }
                        }
                    }
                }
            }
        },
        sheetPeekHeight = 24.dp,
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
                if (reward.numberOfRedeem >= reward.maxRedeem) {
                    ExtendedFloatingActionButton(
                        text = {
                            Text(
                                text = "Redeem Limit Reached",
                                color = MaterialTheme.colors.onPrimary
                            )
                        },
                        backgroundColor = Color.Gray,
                        onClick = {}
                    )
                } else {
                    if (!state.isLoadingRedeemReward) {
                        ExtendedFloatingActionButton(
                            text = {
                                Text(
                                    text = stringResource(
                                        R.string.redeem_reward,
                                        reward.pointsNeeded
                                    ),
                                    color = MaterialTheme.colors.onPrimary
                                )
                            },
                            backgroundColor = MaterialTheme.colors.primary,
                            onClick = {
                                if (reward.category == "e-wallet") {
                                    coroutineScope.launch {
                                        if (sheetState.isCollapsed) {
                                            sheetState.expand()
                                        }
                                    }
                                } else {
                                    viewModel.onRedeemRewardJob(
                                        rewardId = rewardId
                                    )
                                }
                            }
                        )
                    } else {
                        ExtendedFloatingActionButton(
                            text = {
                                Text(
                                    text = "Redeeming Reward...",
                                    color = MaterialTheme.colors.onPrimary
                                )
                            },
                            backgroundColor = Color.Gray,
                            onClick = {}
                        )
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