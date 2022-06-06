package com.ecosense.android.featDiscoverCampaign.presentation.dashboard.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Scale
import com.ecosense.android.R
import com.ecosense.android.core.presentation.theme.spacing
import com.ecosense.android.destinations.BrowseCampaignScreenDestination
import com.ecosense.android.featDiscoverCampaign.domain.model.Category
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Composable
fun BrowseCategory(
    modifier: Modifier = Modifier,
    navigator: DestinationsNavigator,
    categories: List<Category>,
    isLoading: Boolean
) {
    val scrollState = rememberScrollState()

    if (isLoading) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = MaterialTheme.spacing.medium),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            CircularProgressIndicator(
                modifier = Modifier.wrapContentSize(),
                strokeWidth = 3.dp
            )
        }
    } else {
        Row(modifier = modifier.horizontalScroll(scrollState)) {
            Column {
                Row(
                    modifier = modifier
                        .width(125.dp)
                        .fillMaxHeight()
                        .padding(bottom = MaterialTheme.spacing.small)
                ) {
                    Column(
                        horizontalAlignment = Alignment.Start,
                        modifier = modifier
                            .padding(end = MaterialTheme.spacing.small)
                            .shadow(elevation = 5.dp, shape = RoundedCornerShape(8.dp))
                            .clip(shape = RoundedCornerShape(8.dp))
                            .clickable(onClick = {
                                navigator.navigate(
                                    BrowseCampaignScreenDestination(
                                        search = null,
                                        categoryId = null
                                    )
                                )
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
                                text = stringResource(R.string.all),
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
                        .fillMaxHeight()
                        .padding(vertical = MaterialTheme.spacing.small)
                ) {
                    categories.forEachIndexed { _, category ->
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = modifier
                                .padding(horizontal = MaterialTheme.spacing.small)
                                .shadow(elevation = 5.dp, shape = RoundedCornerShape(8.dp))
                                .clip(shape = RoundedCornerShape(8.dp))
                                .clickable(onClick = {
                                    navigator.navigate(
                                        BrowseCampaignScreenDestination(
                                            search = null,
                                            categoryId = category.id
                                        )
                                    )
                                })
                                .background(MaterialTheme.colors.surface)
                                .fillMaxSize()
                        ) {
                            Row(modifier = Modifier.width(125.dp)) {
                                Box(modifier = modifier.fillMaxSize()) {
                                    AsyncImage(
                                        model = ImageRequest.Builder(LocalContext.current)
                                            .data(category.photoUrl)
                                            .crossfade(true)
                                            .scale(Scale.FILL)
                                            .build(),
                                        contentDescription = category.name,
                                        contentScale = ContentScale.Crop,
                                        modifier = modifier.fillMaxSize()
                                    )
                                    Row(
                                        modifier = modifier.fillMaxSize(),
                                        horizontalArrangement = Arrangement.Center,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = category.name,
                                            style = MaterialTheme.typography.caption,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White,
                                            modifier = modifier
                                                .background(Color(category.colorHex.toColorInt()))
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