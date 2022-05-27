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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Person
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Scale
import com.ecosense.android.core.presentation.theme.spacing
import com.ecosense.android.featDiscoverCampaign.data.util.dateFormatter
import com.ecosense.android.featDiscoverCampaign.presentation.detail.component.DetailTopBar
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Composable
@Destination
fun DetailCampaignScreen(
    navigator: DestinationsNavigator,
    id: Int,
    viewModel: DetailCampaignViewModel = hiltViewModel()
) {
    val scaffoldState = rememberScaffoldState()
    val inputValue = remember { mutableStateOf(TextFieldValue()) }

    var imageUri by remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current
    val bitmap = remember {
        mutableStateOf<Bitmap?>(null)
    }

    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
            imageUri = uri
        }

    for (campaign in viewModel.campaignDetailList.value) {
        if (campaign.id == id) {
            Scaffold(
                topBar = {
                    DetailTopBar(
                        onBackClick = {
                            navigator.popBackStack()
                        }
                    )
                },
                scaffoldState = scaffoldState,
                floatingActionButtonPosition = FabPosition.Center,
                floatingActionButton = {
                    if (!campaign.isJoined) {
                        ExtendedFloatingActionButton(
                            text = {
                                Text(
                                    text = "Join Campaign",
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
                // TODO: find a way to make it scrollable, currently can't do scroll because I used lazycolumn and lazyrow (which makes them the only things that scrollable)
                Column(modifier = Modifier.fillMaxSize()) {
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
                                    contentDescription = "Poster of ${campaign.title} campaign.",
                                    contentScale = ContentScale.FillWidth,
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .clip(shape = RoundedCornerShape(8.dp))
                                )
                                Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(MaterialTheme.spacing.small),
                                    verticalArrangement = Arrangement.Bottom,
                                    horizontalAlignment = Alignment.Start
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Filled.Person,
                                            contentDescription = "Participant Count",
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
                                        LazyRow(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(vertical = MaterialTheme.spacing.extraSmall)
                                        ) {
                                            items(campaign.category.size) { i ->
                                                Text(
                                                    text = campaign.category[i],
                                                    style = MaterialTheme.typography.overline,
                                                    color = MaterialTheme.colors.onPrimary,
                                                    modifier = Modifier
                                                        .clip(shape = RoundedCornerShape(10.dp))
                                                        .background(
                                                            if (campaign.category[i] == "#AirPollution") {
                                                                Color.Green
                                                            } else if (campaign.category[i] == "#FoodWaste") {
                                                                Color.Magenta
                                                            } else {
                                                                MaterialTheme.colors.primary
                                                            }
                                                        )
                                                        .padding(MaterialTheme.spacing.extraSmall)
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
                                            contentDescription = "Initiator",
                                            tint = MaterialTheme.colors.onSurface
                                        )
                                        Spacer(modifier = Modifier.width(2.dp))
                                        Text(
                                            text = "Initiated by ",
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
                                        horizontalArrangement = Arrangement.SpaceEvenly
                                    ) {
                                        Column(
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            Row {
                                                Text(
                                                    text =
                                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                                                        dateFormatter(campaign.startDate)
                                                    else
                                                        campaign.startDate,
                                                    fontSize = 18.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = MaterialTheme.colors.primary
                                                )
                                            }
                                            Row {
                                                Text(
                                                    text = "Start Date",
                                                    style = MaterialTheme.typography.body2,
                                                    color = MaterialTheme.colors.primary
                                                )
                                            }
                                        }

                                        Column {
                                            Divider(
                                                color = MaterialTheme.colors.primary,
                                                modifier = Modifier
                                                    .height(50.dp)
                                                    .width(1.dp)
                                            )
                                        }

                                        Column(
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            Row {
                                                Text(
                                                    text =
                                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                                                        dateFormatter(campaign.endDate)
                                                    else
                                                        campaign.endDate,
                                                    fontSize = 18.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = MaterialTheme.colors.primary
                                                )
                                            }
                                            Row {
                                                Text(
                                                    text = "End Date",
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
                                                    text = "Impact",
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
                                                    text = "Tasks",
                                                    fontSize = 18.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    modifier = Modifier
                                                        .padding(bottom = MaterialTheme.spacing.small)
                                                )
                                            }
                                            Row(modifier = Modifier.padding(horizontal = MaterialTheme.spacing.medium)) {
                                                LazyColumn(modifier = Modifier.fillMaxWidth()) {
                                                    items(campaign.tasks.size) { i ->
                                                        val task = campaign.tasks[i]
                                                        Row(
                                                            verticalAlignment = Alignment.CenterVertically,
                                                            modifier = Modifier.padding(
                                                                bottom = MaterialTheme.spacing.small
                                                            )
                                                        ) {
                                                            Column {
                                                                Text(
                                                                    text = "${i + 1}",
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
                                                            if (campaign.isJoined && task.completed) {
                                                                Column(
                                                                    modifier = Modifier.fillMaxWidth(),
                                                                    horizontalAlignment = Alignment.End
                                                                ) {
                                                                    Text(
                                                                        text = "Completed",
                                                                        textAlign = TextAlign.Right,
                                                                        fontWeight = FontWeight.Bold,
                                                                        fontStyle = FontStyle.Italic,
                                                                        style = MaterialTheme.typography.caption,
                                                                        color = Color.Green
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
                                                        if (campaign.isJoined) {
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
                                                                        contentDescription = "Proof of ${task.name} task completion.",
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
                                                                        text = "Finished on " +
                                                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                                                                                    dateFormatter(
                                                                                        task.completedTimeStamp
                                                                                    )
                                                                                else
                                                                                    task.completedTimeStamp,
                                                                        style = MaterialTheme.typography.caption,
                                                                        color = MaterialTheme.colors.primary
                                                                    )
                                                                }
                                                            } else {
                                                                // FIXME: still haven't figured out how to make the submission only available for the next uncompleted task
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
                                                                    ) { Text("Select Image") }
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
                                                                                    contentDescription = "Unsubmitted proof of ${task.name} task completion.",
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
                                                                        label = { Text("Add Caption") },
                                                                        modifier = Modifier.fillMaxSize(),
                                                                        textStyle = MaterialTheme.typography.caption,
                                                                        singleLine = true
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
                                                                        Text("Submit")
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
        } else {
            Log.d("TAG", "DetailCampaignScreen: Detail not Found")
        }
    }
}