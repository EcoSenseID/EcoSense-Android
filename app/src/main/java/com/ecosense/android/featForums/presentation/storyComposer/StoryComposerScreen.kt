package com.ecosense.android.featForums.presentation.storyComposer

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.ecosense.android.R
import com.ecosense.android.core.presentation.model.SharedCampaignPresentation
import com.ecosense.android.core.presentation.theme.spacing
import com.ecosense.android.core.presentation.util.UIEvent
import com.ecosense.android.core.presentation.util.asString
import com.ecosense.android.featForums.presentation.forums.component.SharedCampaign
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.flow.collectLatest

@Composable
@Destination
fun StoryComposerScreen(
    caption: String?,
    campaign: SharedCampaignPresentation?,
    navigator: DestinationsNavigator,
    viewModel: StoryComposerViewModel = hiltViewModel(),
) {
    remember { caption?.let { viewModel.onChangeCaption(caption) } }
    remember { campaign?.let { viewModel.onReceivedSharedCampaign(campaign) } }

    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    val scaffoldState = rememberScaffoldState()

    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? -> viewModel.onImagePicked(uri) }

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is UIEvent.ShowSnackbar -> scaffoldState.snackbarHostState.showSnackbar(
                    message = event.uiText.asString(context)
                )
                is UIEvent.HideKeyboard -> focusManager.clearFocus()
                is UIEvent.Finish -> navigator.navigateUp()
            }
        }
    }

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                backgroundColor = MaterialTheme.colors.background,
                elevation = 0.dp,
            ) {
                Column(modifier = Modifier.fillMaxSize()) {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
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
                            text = stringResource(R.string.create_a_story),
                            color = MaterialTheme.colors.primary,
                            style = MaterialTheme.typography.h6,
                            fontWeight = FontWeight.Bold,
                        )

                        Spacer(modifier = Modifier.weight(1f))
                    }

                    AnimatedVisibility(visible = viewModel.state.isUploading) {
                        LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                    }
                }
            }
        },
        modifier = Modifier.fillMaxSize(),
    ) { scaffoldPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(scaffoldPadding),
        ) {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .background(MaterialTheme.colors.surface),
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(MaterialTheme.spacing.medium),
                ) {
                    AsyncImage(
                        model = viewModel.state.avatarUrl,
                        contentDescription = null,
                        placeholder = painterResource(id = R.drawable.ic_ecosense_logo),
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape),
                    )

                    Spacer(modifier = Modifier.width(MaterialTheme.spacing.medium))

                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .border(
                                width = 1.dp,
                                color = MaterialTheme.colors.onSurface.copy(alpha = 0.1f),
                                shape = RoundedCornerShape(16.dp),
                            ),
                    ) {
                        TextField(
                            value = viewModel.state.caption ?: "",
                            onValueChange = { viewModel.onChangeCaption(it) },
                            enabled = !viewModel.state.isUploading,
                            placeholder = { Text(text = stringResource(R.string.whats_happening)) },
                            colors = TextFieldDefaults.textFieldColors(
                                backgroundColor = Color.Transparent,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                disabledIndicatorColor = Color.Transparent,
                            ),
                            modifier = Modifier.fillMaxWidth(),
                        )

                        viewModel.state.attachedPhotoUri?.let {
                            AsyncImage(
                                model = it,
                                contentDescription = null,
                                contentScale = ContentScale.Fit,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(MaterialTheme.spacing.small)
                                    .clip(RoundedCornerShape(16.dp)),
                            )
                        }

                        viewModel.state.sharedCampaign?.let {
                            SharedCampaign(
                                campaign = { it },
                                modifier = Modifier.padding(MaterialTheme.spacing.small),
                            )
                        }

                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            IconButton(
                                onClick = {
                                    imagePicker.launch(context.getString(R.string.content_type_image))
                                },
                                enabled = !viewModel.state.isUploading,
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_photo),
                                    contentDescription = stringResource(R.string.cd_attach_photo),
                                    tint = MaterialTheme.colors.secondary,
                                )
                            }

                            TextButton(
                                onClick = { viewModel.onClickSend() },
                                enabled = !viewModel.state.isUploading,
                            ) {
                                Text(
                                    text = stringResource(
                                        if (viewModel.state.isUploading) R.string.posting
                                        else R.string.post
                                    ),
                                    color = MaterialTheme.colors.secondary,
                                    fontWeight = FontWeight.SemiBold,
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}