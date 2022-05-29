package com.ecosense.android.featDiscoverCampaign.presentation.dashboard.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Scale
import com.ecosense.android.R
import com.ecosense.android.core.presentation.theme.spacing
import com.ecosense.android.destinations.BrowseCampaignScreenDestination
import com.ecosense.android.featDiscoverCampaign.presentation.dashboard.DiscoverCampaignViewModel
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Composable
fun BrowseCategory(
    modifier: Modifier = Modifier,
    navigator: DestinationsNavigator,
    viewModel: DiscoverCampaignViewModel = hiltViewModel()
) {
    val scrollState = rememberScrollState()

    Row(modifier = modifier.horizontalScroll(scrollState)) {
        Column {
            Row(
                modifier = modifier
                    .width(250.dp)
                    .fillMaxHeight(0.75f)
                    .padding(vertical = MaterialTheme.spacing.small)
            ) {
                Column(
                    horizontalAlignment = Alignment.Start,
                    modifier = modifier
                        .padding(end = MaterialTheme.spacing.small)
                        .clip(shape = RoundedCornerShape(8.dp))
                        .clickable(onClick = {
                            navigator.navigate(BrowseCampaignScreenDestination(category = null))
                        })
                        .background(MaterialTheme.colors.surface)
                        .fillMaxSize()
                ) {
                    Row(
                        modifier = modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colors.primary),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(R.string.all_campaign),
                            style = MaterialTheme.typography.h5,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colors.onPrimary,
                            modifier = modifier
                                .wrapContentSize()
                        )
                    }
                }
            }
        }
        Column {
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.75f)
                    .padding(vertical = MaterialTheme.spacing.small)
            ) {
                viewModel.categoryList.value.forEachIndexed { index, category ->
                    if (index < 3) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = modifier
                                .padding(horizontal = MaterialTheme.spacing.small)
                                .clip(shape = RoundedCornerShape(8.dp))
                                .clickable(onClick = {
                                    navigator.navigate(BrowseCampaignScreenDestination(category = category.name))
                                })
                                .background(MaterialTheme.colors.surface)
                                .fillMaxSize()
                        ) {
                            Row(
                                modifier = modifier
                                    .width(250.dp)
                            ) {
                                Box(modifier = modifier.fillMaxSize()) {
                                    AsyncImage(
                                        model = ImageRequest.Builder(LocalContext.current)
                                            .data(category.photoUrl)
                                            .crossfade(true)
                                            .scale(Scale.FILL)
                                            .build(),
                                        contentDescription = category.name,
                                        contentScale = ContentScale.FillWidth,
                                        modifier = modifier.fillMaxSize()
                                    )
                                    Row(
                                        modifier = modifier.fillMaxSize(),
                                        horizontalArrangement = Arrangement.End,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = category.name,
                                            style = MaterialTheme.typography.subtitle2,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colors.onPrimary,
                                            modifier = modifier
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