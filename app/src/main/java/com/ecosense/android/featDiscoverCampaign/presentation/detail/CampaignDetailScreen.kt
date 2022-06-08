package com.ecosense.android.featDiscoverCampaign.presentation.detail

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Person
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Scale
import com.ecosense.android.R
import com.ecosense.android.core.presentation.theme.spacing
import com.ecosense.android.featDiscoverCampaign.data.util.dateFormatter
import com.ecosense.android.featDiscoverCampaign.data.util.detailDateFormatter
import com.ecosense.android.featDiscoverCampaign.presentation.component.DiscoverTopBar
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Composable
@Destination
fun CampaignDetailScreen(
    navigator: DestinationsNavigator,
    id: Int,
    viewModel: CampaignDetailViewModel = hiltViewModel()
) {
    remember { viewModel.setCampaignId(id = id) }

    val scaffoldState = rememberScaffoldState()

    val state = viewModel.state.value
    val campaign = state.campaignDetail

    val inputValue = remember { mutableStateOf(TextFieldValue()) }
    val scrollState = rememberScrollState()

    var imageUri by remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current
    val bitmap = remember {
        mutableStateOf<Bitmap?>(null)
    }

    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
            imageUri = uri
        }

    Scaffold(
        topBar = {
            DiscoverTopBar(
                onBackClick = {
                    navigator.popBackStack()
                }
            )
        },
        scaffoldState = scaffoldState,
        floatingActionButtonPosition = FabPosition.Center,
        floatingActionButton = {
            if (!campaign.joined) {
                ExtendedFloatingActionButton(
                    text = {
                        Text(
                            text = stringResource(R.string.join_campaign),
                            color = MaterialTheme.colors.onPrimary
                        )
                    },
                    backgroundColor = MaterialTheme.colors.primary,
                    onClick = {
                        // TODO: change onClick to change isJoined value in the database (at the moment the real database haven't finished yet, so it can't be simulated)
                        Log.d("TAG", "DetailCampaignScreen: FAB Clicked")
                    }
                )
            }
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(
                        horizontal = MaterialTheme.spacing.medium,
                        vertical = MaterialTheme.spacing.small
                    )
                    .fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                ) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(campaign.posterUrl)
                                .crossfade(true)
                                .scale(Scale.FILL)
                                .build(),
                            contentDescription = stringResource(
                                R.string.poster_of_campaign,
                                campaign.title
                            ),
                            contentScale = ContentScale.FillWidth,
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(shape = RoundedCornerShape(8.dp))
                        )
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Brush.verticalGradient(
                                        colors = listOf(
                                            Color.Transparent,
                                            MaterialTheme.colors.primary
                                        )
                                    )
                                )
                                .padding(MaterialTheme.spacing.small),
                            verticalArrangement = Arrangement.Bottom,
                            horizontalAlignment = Alignment.Start
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Filled.Person,
                                    contentDescription = stringResource(R.string.participant_count),
                                    tint = MaterialTheme.colors.onPrimary
                                )
                                Spacer(modifier = Modifier.width(2.dp))
                                Text(
                                    text = campaign.participantsCount.toString(),
                                    style = MaterialTheme.typography.body2,
                                    color = MaterialTheme.colors.onPrimary
                                )
                            }
                            Row {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = MaterialTheme.spacing.extraSmall)
                                ) {
                                    for (campaignCategory in campaign.category) {
                                        Text(
                                            text = campaignCategory,
                                            style = MaterialTheme.typography.overline,
                                            color = Color.White,
                                            modifier = Modifier
                                                .clip(shape = RoundedCornerShape(10.dp))
                                                .background(
                                                    when (campaignCategory) {
                                                        stringResource(R.string.cat_water_pollution) -> {
                                                            Color("#206A5D".toColorInt())
                                                        }
                                                        stringResource(R.string.cat_air_pollution) -> {
                                                            Color("#81B214".toColorInt())
                                                        }
                                                        stringResource(R.string.cat_food_waste) -> {
                                                            Color("#F58634".toColorInt())
                                                        }
                                                        stringResource(R.string.cat_plastic_free) -> {
                                                            Color("#E25DD7".toColorInt())
                                                        }
                                                        stringResource(R.string.cat_energy_efficiency) -> {
                                                            Color("#DB3069".toColorInt())
                                                        }
                                                        else -> {
                                                            MaterialTheme.colors.primary
                                                        }
                                                    }
                                                )
                                                .padding(horizontal = MaterialTheme.spacing.extraSmall, vertical = 1.dp)
                                        )
                                        Spacer(modifier = Modifier.width(MaterialTheme.spacing.extraSmall))
                                    }
                                }
                            }
                            Row {
                                Text(
                                    text = campaign.title,
                                    style = MaterialTheme.typography.h5,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colors.onPrimary
                                )
                            }

                        }
                    }
                }
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(
                        top = MaterialTheme.spacing.small,
                        start = MaterialTheme.spacing.medium,
                        end = MaterialTheme.spacing.medium
                    )
                    .clip(shape = RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colors.surface)
                    .padding(MaterialTheme.spacing.small)
                    .fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        Column(
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Row(
                                modifier = Modifier
                                    .padding(MaterialTheme.spacing.small),
                                horizontalArrangement = Arrangement.Start,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.Person,
                                    contentDescription = stringResource(R.string.initiator),
                                    tint = MaterialTheme.colors.onSurface
                                )
                                Spacer(modifier = Modifier.width(2.dp))
                                Text(
                                    text = stringResource(R.string.initiated_by),
                                    style = MaterialTheme.typography.caption
                                )
                                Spacer(modifier = Modifier.width(1.dp))
                                Text(
                                    text = campaign.initiator,
                                    style = MaterialTheme.typography.caption,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colors.primary
                                )
                            }

                            Row(
                                modifier = Modifier
                                    .padding(MaterialTheme.spacing.small)
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Row {
                                        Text(
                                            text = detailDateFormatter(campaign.startDate),
                                            style = MaterialTheme.typography.subtitle1,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colors.primary
                                        )
                                    }
                                    Row {
                                        Text(
                                            text = stringResource(R.string.start_date),
                                            style = MaterialTheme.typography.body2,
                                            color = MaterialTheme.colors.primary
                                        )
                                    }
                                }

                                Column {
                                    Divider(
                                        color = MaterialTheme.colors.primary,
                                        modifier = Modifier
                                            .height(40.dp)
                                            .width(1.dp)
                                    )
                                }

                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Row {
                                        Text(
                                            text = detailDateFormatter(campaign.endDate),
                                            style = MaterialTheme.typography.subtitle1,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colors.primary
                                        )
                                    }
                                    Row {
                                        Text(
                                            text = stringResource(R.string.end_date),
                                            style = MaterialTheme.typography.body2,
                                            color = MaterialTheme.colors.primary
                                        )
                                    }
                                }
                            }

                            Row(
                                modifier = Modifier
                                    .padding(MaterialTheme.spacing.small),
                                horizontalArrangement = Arrangement.Start,
                                verticalAlignment = Alignment.Top
                            ) {
                                Column {
                                    Row {
                                        Text(
                                            text = stringResource(R.string.impact),
                                            fontSize = 18.sp,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier
                                                .padding(bottom = MaterialTheme.spacing.small)
                                        )
                                    }
                                    Row {
                                        Text(
                                            text = campaign.description,
                                            textAlign = TextAlign.Justify,
                                            style = MaterialTheme.typography.caption
                                        )
                                    }
                                }
                            }

                            Row(
                                modifier = Modifier
                                    .padding(MaterialTheme.spacing.small),
                                horizontalArrangement = Arrangement.Start,
                                verticalAlignment = Alignment.Top
                            ) {
                                Column {
                                    Row {
                                        Text(
                                            text = stringResource(R.string.tasks),
                                            fontSize = 18.sp,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier
                                                .padding(bottom = MaterialTheme.spacing.small)
                                        )
                                    }
                                    Row(modifier = Modifier.padding(horizontal = MaterialTheme.spacing.medium)) {
                                        Column(modifier = Modifier.fillMaxWidth()) {
                                            campaign.campaignTasks.forEachIndexed { index, task ->
                                                Row(
                                                    verticalAlignment = Alignment.CenterVertically,
                                                    modifier = Modifier.padding(
                                                        bottom = MaterialTheme.spacing.small
                                                    )
                                                ) {
                                                    Column {
                                                        Text(
                                                            text = "${index + 1}",
                                                            fontSize = 18.sp,
                                                            fontWeight = FontWeight.Bold,
                                                            color = MaterialTheme.colors.primary,
                                                            modifier = Modifier
                                                                .padding(end = MaterialTheme.spacing.small)
                                                        )
                                                    }
                                                    Column {
                                                        Text(
                                                            text = task.name,
                                                            textAlign = TextAlign.Justify,
                                                            style = MaterialTheme.typography.body1
                                                        )
                                                    }
                                                    if (campaign.joined && task.completed) {
                                                        Column(
                                                            modifier = Modifier.fillMaxWidth(),
                                                            horizontalAlignment = Alignment.End
                                                        ) {
                                                            Text(
                                                                text = stringResource(R.string.completed),
                                                                textAlign = TextAlign.Right,
                                                                fontWeight = FontWeight.Bold,
                                                                fontStyle = FontStyle.Italic,
                                                                style = MaterialTheme.typography.caption,
                                                                color = MaterialTheme.colors.secondary
                                                            )
                                                        }
                                                    }
                                                }
                                                Row(
                                                    modifier = Modifier.padding(
                                                        bottom = MaterialTheme.spacing.small
                                                    )
                                                ) {
                                                    Text(
                                                        text = task.taskDescription,
                                                        textAlign = TextAlign.Justify,
                                                        style = MaterialTheme.typography.caption
                                                    )
                                                }
                                                if (campaign.joined) {
                                                    if (task.completed) {
                                                        Row(
                                                            modifier = Modifier
                                                                .fillMaxWidth()
                                                                .height(100.dp)
                                                                .padding(bottom = MaterialTheme.spacing.small)
                                                        ) {
                                                            AsyncImage(
                                                                model = ImageRequest.Builder(
                                                                    LocalContext.current
                                                                )
                                                                    .data(task.proofPhotoUrl)
                                                                    .crossfade(true)
                                                                    .scale(Scale.FILL)
                                                                    .build(),
                                                                contentDescription = stringResource(
                                                                    R.string.proof_completion,
                                                                    task.name
                                                                ),
                                                                contentScale = ContentScale.FillWidth,
                                                                modifier = Modifier
                                                                    .fillMaxSize()
                                                                    .clip(
                                                                        shape = RoundedCornerShape(
                                                                            8.dp
                                                                        )
                                                                    )
                                                            )
                                                        }
                                                        Row(
                                                            modifier = Modifier.padding(
                                                                bottom = MaterialTheme.spacing.small
                                                            )
                                                        ) {
                                                            Text(
                                                                text = "\"${task.proofCaption}\"",
                                                                fontStyle = FontStyle.Italic,
                                                                style = MaterialTheme.typography.caption
                                                            )
                                                        }
                                                        Row {
                                                            Text(
                                                                text = stringResource(
                                                                    R.string.finished_on, task.completedTimeStamp),
                                                                style = MaterialTheme.typography.caption,
                                                                color = MaterialTheme.colors.primary
                                                            )
                                                        }
                                                    } else {

                                                        if (!campaign.campaignTasks[index].completed) { // make the submission only available for the next uncompleted task
                                                            Row(
                                                                modifier = Modifier.padding(
                                                                    bottom = MaterialTheme.spacing.small
                                                                )
                                                            ) {
                                                                Button(
                                                                    onClick = {
                                                                        // TODO: currently can only pick image from gallery, find a way to make it able to use camera x too
                                                                        launcher.launch("image/*")

                                                                        Log.d(
                                                                            "TAG",
                                                                            "DetailCampaignScreen: Add Picture Button Clicked"
                                                                        )
                                                                    },
                                                                    modifier = Modifier
                                                                        .fillMaxWidth()
                                                                        .clip(
                                                                            shape = RoundedCornerShape(
                                                                                8.dp
                                                                            )
                                                                        )
                                                                ) { Text(stringResource(R.string.select_image)) }
                                                            }

                                                            // show the image if there's any
                                                            if (imageUri != null) {
                                                                Row(
                                                                    modifier = Modifier
                                                                        .fillMaxWidth()
                                                                        .height(100.dp)
                                                                        .padding(bottom = MaterialTheme.spacing.small)
                                                                ) {
                                                                    imageUri?.let {
                                                                        if (Build.VERSION.SDK_INT < 28) {
                                                                            bitmap.value =
                                                                                MediaStore.Images
                                                                                    .Media.getBitmap(
                                                                                        context.contentResolver,
                                                                                        it
                                                                                    )

                                                                        } else {
                                                                            val source =
                                                                                ImageDecoder
                                                                                    .createSource(
                                                                                        context.contentResolver,
                                                                                        it
                                                                                    )
                                                                            bitmap.value =
                                                                                ImageDecoder.decodeBitmap(
                                                                                    source
                                                                                )
                                                                        }

                                                                        bitmap.value?.let { btm ->
                                                                            Image(
                                                                                bitmap = btm.asImageBitmap(),
                                                                                contentScale = ContentScale.FillWidth,
                                                                                contentDescription = stringResource(
                                                                                    R.string.unsubmitted_proof,
                                                                                    task.name
                                                                                ),
                                                                                modifier = Modifier
                                                                                    .fillMaxSize()
                                                                                    .clip(
                                                                                        shape = RoundedCornerShape(
                                                                                            8.dp
                                                                                        )
                                                                                    )
                                                                            )
                                                                        }
                                                                    }
                                                                }
                                                            }

                                                            Row(
                                                                modifier = Modifier.padding(
                                                                    bottom = MaterialTheme.spacing.small
                                                                )
                                                            ) {
                                                                OutlinedTextField(
                                                                    value = inputValue.value,
                                                                    onValueChange = {
                                                                        inputValue.value =
                                                                            it
                                                                    },
                                                                    label = {
                                                                        Text(
                                                                            stringResource(R.string.add_caption)
                                                                        )
                                                                    },
                                                                    modifier = Modifier.fillMaxSize(),
                                                                    textStyle = MaterialTheme.typography.caption
                                                                )
                                                            }
                                                            Row(
                                                                modifier = Modifier.padding(
                                                                    bottom = MaterialTheme.spacing.small
                                                                )
                                                            ) {
                                                                Button(
                                                                    onClick = {
                                                                        Log.d(
                                                                            "TAG",
                                                                            "DetailCampaignScreen: Submit Button Clicked"
                                                                        )
                                                                    },
                                                                    modifier = Modifier
                                                                        .fillMaxWidth()
                                                                        .clip(
                                                                            shape = RoundedCornerShape(
                                                                                8.dp
                                                                            )
                                                                        )
                                                                ) {
                                                                    Text(stringResource(R.string.submit))
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                                Row(
                                                    modifier = Modifier.padding(
                                                        vertical = MaterialTheme.spacing.small
                                                    )
                                                ) {
                                                    Divider(
                                                        color = Color.Gray,
                                                        thickness = 1.dp,
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
}