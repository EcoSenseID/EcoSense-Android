package com.ecosense.android.featReward.presentation.detail.rewarddetail

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
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
import com.ecosense.android.core.presentation.component.RoundedEndsButton
import com.ecosense.android.core.presentation.theme.*
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
                                text = "Reward Form",
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colors.primary,
                                style = MaterialTheme.typography.h6,
                                modifier = Modifier.padding(MaterialTheme.spacing.medium)
                            )
                        }

                        Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))

                        Text(
                            text = "Email Address",
                            fontWeight = FontWeight.ExtraBold,
                            style = MaterialTheme.typography.subtitle1,
                            modifier = Modifier
                                .padding(bottom = MaterialTheme.spacing.small)
                        )
                        OutlinedTextField(
                            value = state.email,
                            onValueChange = { viewModel.onEmailValueChange(it) },
                            label = { Text(text = "Choose your type of e-wallet") },
                            placeholder = { Text(text = "Choose your type of e-wallet") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

                        Text(
                            text = "Select the destination e-wallet",
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
                            fontWeight = FontWeight.ExtraBold,
                            style = MaterialTheme.typography.subtitle1,
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
                                OutlinedButton(
                                    enabled = !state.isLoadingRequestReward,
                                    border = BorderStroke(1.dp, DarkRed),
                                    shape = RoundedCornerShape(20.dp),
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
                                        text = "Cancel",
                                        fontWeight = FontWeight.Medium,
                                        color = DarkRed,
                                        style = MaterialTheme.typography.button
                                    )
                                }
                            }

                            Column(
                                modifier = Modifier
                                    .height(40.dp)
                                    .width(150.dp)
                            ) {
                                GradientButton(
                                    enabled = !state.isLoadingRequestReward,
                                    shape = RoundedCornerShape(20.dp),
                                    onClick = { viewModel.onRequestRewardJob(rewardId = rewardId) },
                                    modifier = Modifier.fillMaxSize()
                                ) {
                                    Text(
                                        text =
                                        if (state.isLoadingRequestReward) "Submitting Request..."
                                        else "Submit",
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
                    if (reward.numberOfRedeem >= reward.maxRedeem) {
                        ExtendedFloatingActionButton(
                            text = {
                                Text(
                                    text = "Redeem Limit Reached",
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
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = MaterialTheme.spacing.medium)
                            ) {
                                Text(
                                    text = "Redeem",
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
                                    text = reward.pointsNeeded.toString(),
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
                                        text = "Redeeming Reward...",
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