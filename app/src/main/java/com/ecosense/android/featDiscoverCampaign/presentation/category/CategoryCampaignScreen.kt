package com.ecosense.android.featDiscoverCampaign.presentation.category

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Scale
import com.ecosense.android.core.presentation.theme.spacing
import com.ecosense.android.destinations.BrowseCampaignScreenDestination
import com.ecosense.android.destinations.CategoryCampaignScreenDestination
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Composable
@Destination
fun CategoryCampaignScreen(
    navigator: DestinationsNavigator,
    viewModel: CategoryCampaignViewModel = hiltViewModel()
) {
    val scaffoldState = rememberScaffoldState()

    Scaffold(scaffoldState = scaffoldState) {
        Column(modifier = Modifier.fillMaxSize()) {
            Row {
               Text("Choose a Category")
            }
            Row {
                LazyColumn(modifier = Modifier.fillMaxWidth()) {
                    items(viewModel.categoryList.value.size) { i ->
                        val category = viewModel.categoryList.value[i]
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .padding(
                                    start = MaterialTheme.spacing.medium,
                                    end = MaterialTheme.spacing.medium,
                                    bottom = MaterialTheme.spacing.medium
                                )
                                .clip(shape = RoundedCornerShape(8.dp))
                                .clickable(onClick = {
                                    navigator.navigate(BrowseCampaignScreenDestination)
                                    Log.d("TAG", "CategoryCampaignScreen: clicked $i")
                                })
                                .background(MaterialTheme.colors.surface)
                                .padding(MaterialTheme.spacing.small)
                                .fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(128.dp)
                            ) {
                                Box(modifier = Modifier.fillMaxSize()) {
                                    AsyncImage(
                                        model = ImageRequest.Builder(LocalContext.current)
                                            .data(category.photoUrl)
                                            .crossfade(true)
                                            .scale(Scale.FILL)
                                            .build(),
                                        contentDescription = category.name,
                                        contentScale = ContentScale.FillWidth,
                                        modifier = Modifier.fillMaxSize()
                                    )
                                    Column(
                                        modifier = Modifier.fillMaxSize(),
                                        verticalArrangement = Arrangement.Center,
                                        horizontalAlignment = Alignment.End
                                    ) {
                                        Text(
                                            text = category.name,
                                            fontSize = 18.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colors.onPrimary,
                                            modifier = Modifier
                                                .background(MaterialTheme.colors.primary)
                                                .padding(MaterialTheme.spacing.small)
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