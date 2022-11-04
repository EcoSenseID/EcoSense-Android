package com.ecosense.android.featProfile.presentation.settings

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.ecosense.android.BuildConfig
import com.ecosense.android.R
import com.ecosense.android.core.domain.model.AuthProvider
import com.ecosense.android.core.presentation.component.BottomSheetHandleBar
import com.ecosense.android.core.presentation.component.RoundedEndsButton
import com.ecosense.android.core.presentation.modifier.brushForeground
import com.ecosense.android.core.presentation.theme.DarkRed
import com.ecosense.android.core.presentation.theme.Gradient
import com.ecosense.android.core.presentation.theme.spacing
import com.ecosense.android.destinations.ChangeEmailScreenDestination
import com.ecosense.android.destinations.ChangePasswordScreenDestination
import com.ecosense.android.destinations.EditProfileScreenDestination
import com.ecosense.android.featProfile.presentation.settings.component.LogoutSheetContent
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.launch
import logcat.logcat

@OptIn(ExperimentalUnitApi::class, ExperimentalMaterialApi::class)
@Composable
@Destination
fun SettingsScreen(
    navigator: DestinationsNavigator,
    viewModel: SettingsViewModel = hiltViewModel(),
) {
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    val sheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
    )

    ModalBottomSheetLayout(
        sheetContent = {
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))

            BottomSheetHandleBar(modifier = Modifier.fillMaxWidth())

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

            LogoutSheetContent(
                onClickYes = { viewModel.onLogoutClick() },
                onClickCancel = { scope.launch { sheetState.hide() } },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = MaterialTheme.spacing.medium)
            )

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
        },
        sheetState = sheetState,
        sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
    ) {
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
                            text = stringResource(R.string.settings),
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
                    .background(MaterialTheme.colors.surface),
            ) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(MaterialTheme.spacing.medium),
                    ) {
                        Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            val user = viewModel.userState

                            AsyncImage(
                                model = user.photoUrl,
                                contentDescription = null,
                                placeholder = painterResource(id = R.drawable.ic_ecosense_logo),
                                fallback = painterResource(id = R.drawable.ic_ecosense_logo),
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(64.dp)
                                    .clip(CircleShape),
                            )

                            Spacer(modifier = Modifier.width(MaterialTheme.spacing.medium))

                            Text(
                                text = if (!user.displayName.isNullOrBlank()) user.displayName
                                else stringResource(R.string.ecosense_user),
                                fontSize = TextUnit(22f, TextUnitType.Sp),
                                fontWeight = FontWeight.ExtraBold,
                                modifier = Modifier.brushForeground(brush = Gradient),
                            )
                        }

                        Spacer(modifier = Modifier.height(MaterialTheme.spacing.large))

                        Text(
                            text = stringResource(R.string.account_settings),
                            style = MaterialTheme.typography.subtitle1.copy(
                                textDecoration = TextDecoration.Underline,
                                fontWeight = FontWeight.Bold,
                            ),
                        )

                        Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))

                        OutlinedButton(
                            onClick = { navigator.navigate(EditProfileScreenDestination) },
                            shape = RoundedCornerShape(100),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = MaterialTheme.colors.onSurface,
                            ),
                        ) {
                            AsyncImage(
                                model = R.drawable.ic_edit_person,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp),
                            )

                            Spacer(modifier = Modifier.width(MaterialTheme.spacing.small))

                            Text(
                                text = stringResource(id = R.string.edit_profile),
                                style = MaterialTheme.typography.subtitle1,
                                fontWeight = FontWeight.SemiBold,
                            )

                            Spacer(modifier = Modifier.weight(1f))

                            Icon(
                                imageVector = Icons.Rounded.ChevronRight,
                                contentDescription = null,
                            )
                        }

                        if (viewModel.userState.authProvider == AuthProvider.EMAIL) {
                            Spacer(modifier = Modifier.height(MaterialTheme.spacing.extraSmall))

                            OutlinedButton(
                                onClick = { navigator.navigate(ChangeEmailScreenDestination) },
                                shape = RoundedCornerShape(100),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    contentColor = MaterialTheme.colors.onSurface,
                                ),
                            ) {
                                AsyncImage(
                                    model = R.drawable.ic_email,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp),
                                )

                                Spacer(modifier = Modifier.width(MaterialTheme.spacing.small))

                                Text(
                                    text = stringResource(R.string.change_email),
                                    style = MaterialTheme.typography.subtitle1,
                                    fontWeight = FontWeight.SemiBold,
                                )

                                Spacer(modifier = Modifier.weight(1f))

                                Icon(
                                    imageVector = Icons.Rounded.ChevronRight,
                                    contentDescription = null,
                                )
                            }

                            Spacer(modifier = Modifier.height(MaterialTheme.spacing.extraSmall))

                            OutlinedButton(
                                onClick = { navigator.navigate(ChangePasswordScreenDestination) },
                                shape = RoundedCornerShape(100),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    contentColor = MaterialTheme.colors.onSurface,
                                ),
                            ) {
                                AsyncImage(
                                    model = R.drawable.ic_padlock,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp),
                                )

                                Spacer(modifier = Modifier.width(MaterialTheme.spacing.small))

                                Text(
                                    text = stringResource(R.string.change_password),
                                    style = MaterialTheme.typography.subtitle1,
                                    fontWeight = FontWeight.SemiBold,
                                )

                                Spacer(modifier = Modifier.weight(1f))

                                Icon(
                                    imageVector = Icons.Rounded.ChevronRight,
                                    contentDescription = null,
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

                        Text(
                            text = stringResource(R.string.application_language),
                            style = MaterialTheme.typography.subtitle1.copy(
                                textDecoration = TextDecoration.Underline,
                                fontWeight = FontWeight.Bold,
                            ),
                        )

                        Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))

                        val resultLauncher = rememberLauncherForActivityResult(
                            contract = ActivityResultContracts.StartActivityForResult(),
                            onResult = {},
                        )
                        OutlinedButton(
                            onClick = { resultLauncher.launch(Intent(Settings.ACTION_LOCALE_SETTINGS)) },
                            shape = RoundedCornerShape(100),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = MaterialTheme.colors.onSurface,
                            ),
                        ) {
                            AsyncImage(
                                model = R.drawable.ic_language,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp),
                            )

                            Spacer(modifier = Modifier.width(MaterialTheme.spacing.small))

                            Text(
                                text = stringResource(R.string.language),
                                style = MaterialTheme.typography.subtitle1,
                                fontWeight = FontWeight.SemiBold,
                            )

                            Spacer(modifier = Modifier.weight(1f))

                            Icon(
                                imageVector = Icons.Rounded.ChevronRight,
                                contentDescription = null,
                            )
                        }

                        Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

                        Text(
                            text = stringResource(R.string.about_ecosense_application),
                            style = MaterialTheme.typography.subtitle1.copy(
                                textDecoration = TextDecoration.Underline,
                                fontWeight = FontWeight.Bold,
                            ),
                        )

                        Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))

                        OutlinedButton(
                            onClick = { logcat { "Version clicked" } },
                            shape = RoundedCornerShape(100),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = MaterialTheme.colors.onSurface,
                            ),
                        ) {
                            AsyncImage(
                                model = R.drawable.ic_stacks,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp),
                            )

                            Spacer(modifier = Modifier.width(MaterialTheme.spacing.small))

                            Text(
                                text = stringResource(R.string.version),
                                style = MaterialTheme.typography.subtitle1,
                                fontWeight = FontWeight.SemiBold,
                            )

                            Spacer(modifier = Modifier.weight(1f))

                            Text(
                                text = BuildConfig.VERSION_NAME,
                                style = MaterialTheme.typography.subtitle1,
                                fontWeight = FontWeight.SemiBold,
                            )
                        }

                        Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

                        RoundedEndsButton(
                            onClick = { scope.launch { sheetState.show() } },
                            colors = ButtonDefaults.buttonColors(backgroundColor = DarkRed),
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            Text(
                                text = stringResource(id = R.string.log_out),
                                color = MaterialTheme.colors.onPrimary,
                                fontWeight = FontWeight.SemiBold,
                            )
                        }

                        Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

                        Text(
                            text = stringResource(R.string.find_us),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.subtitle1,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(MaterialTheme.spacing.small),
                        )

                        Row(
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .clickable {
                                        val url = context.getString(R.string.url_ecosense_instagram)
                                        Intent(Intent.ACTION_VIEW)
                                            .apply { data = Uri.parse(url) }
                                            .let { context.startActivity(it) }
                                    }
                                    .background(MaterialTheme.colors.onSurface.copy(alpha = 0.1f))
                                    .padding(MaterialTheme.spacing.small),
                            ) {
                                AsyncImage(
                                    model = R.drawable.ic_instagram,
                                    contentDescription = null,
                                    colorFilter = ColorFilter.tint(MaterialTheme.colors.onSurface),
                                    modifier = Modifier.fillMaxSize(),
                                )
                            }

                            Spacer(modifier = Modifier.width(MaterialTheme.spacing.small))

                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .clickable {
                                        val url = context.getString(R.string.url_ecosense_youtube)
                                        Intent(Intent.ACTION_VIEW)
                                            .apply { data = Uri.parse(url) }
                                            .let { context.startActivity(it) }
                                    }
                                    .background(MaterialTheme.colors.onSurface.copy(alpha = 0.1f))
                                    .padding(MaterialTheme.spacing.small),
                            ) {
                                AsyncImage(
                                    model = R.drawable.ic_youtube,
                                    contentDescription = null,
                                    colorFilter = ColorFilter.tint(MaterialTheme.colors.onSurface),
                                    modifier = Modifier.fillMaxSize(),
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}