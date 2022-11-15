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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Scale
import com.ecosense.android.R
import com.ecosense.android.core.presentation.theme.White
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
    val horizontalScrollState = rememberScrollState()

    if (isLoading) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            CircularProgressIndicator(
                modifier = Modifier.wrapContentSize(),
                strokeWidth = 3.dp
            )
        }
    } else {
        Row(modifier = modifier.horizontalScroll(horizontalScrollState)) {
            Column {
                Row(
                    modifier = modifier
                        .padding(
                            horizontal = MaterialTheme.spacing.medium
                        )
                        .width(120.dp)
                        .height(150.dp)
                ) {
                    Column(
                        horizontalAlignment = Alignment.Start,
                        modifier = modifier
                            .shadow(elevation = 2.dp, shape = RoundedCornerShape(20.dp))
                            .clip(shape = RoundedCornerShape(20.dp))
                            .clickable(onClick = {
                                navigator.navigate(
                                    BrowseCampaignScreenDestination(
                                        search = null,
                                        categoryId = null,
                                        categoryName = "All Campaigns"
                                    )
                                )
                            })
                            .background(MaterialTheme.colors.surface)
                            .fillMaxSize()
                    ) {
                        Box(modifier = modifier.fillMaxSize()) {
                            AsyncImage(
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(R.drawable.all_campaign)
                                    .crossfade(true)
                                    .scale(Scale.FILL)
                                    .build(),
                                contentDescription = stringResource(R.string.all_campaigns),
                                contentScale = ContentScale.Crop,
                                modifier = modifier.fillMaxSize()
                            )
                            Row(
                                modifier = modifier
                                    .fillMaxSize()
                                    .background(
                                        Brush.verticalGradient(
                                            colors = listOf(
                                                Color.Transparent,
                                                MaterialTheme.colors.secondary
                                            )
                                        )
                                    ),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.Bottom
                            ) {
                                Text(
                                    text = stringResource(R.string.all_campaigns),
                                    textAlign = TextAlign.Center,
                                    style = MaterialTheme.typography.caption,
                                    fontWeight = FontWeight.Bold,
                                    color = White,
                                    modifier = modifier.padding(MaterialTheme.spacing.medium)
                                )
                            }
                        }
                    }
                }
            }
            Column {
                Row(
                    modifier = modifier
                        .fillMaxWidth()
                        .height(150.dp)
                ) {
                    categories.forEach { category ->
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = modifier
                                .padding(end = MaterialTheme.spacing.medium)
                                .shadow(elevation = 2.dp, shape = RoundedCornerShape(20.dp))
                                .clip(shape = RoundedCornerShape(20.dp))
                                .clickable(onClick = {
                                    navigator.navigate(
                                        BrowseCampaignScreenDestination(
                                            search = null,
                                            categoryId = category.id,
                                            categoryName = category.name
                                        )
                                    )
                                })
                                .background(MaterialTheme.colors.surface)
                                .fillMaxSize()
                        ) {
                            Row(
                                modifier = Modifier.width(120.dp)
                            ) {
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
                                        modifier = modifier
                                            .fillMaxSize()
                                            .background(
                                                Brush.verticalGradient(
                                                    colors = listOf(
                                                        Color.Transparent,
                                                        Color(category.colorHex.toColorInt())
                                                    )
                                                )
                                            ),
                                        horizontalArrangement = Arrangement.Center,
                                        verticalAlignment = Alignment.Bottom
                                    ) {
                                        Text(
                                            text = category.name,
                                            textAlign = TextAlign.Center,
                                            style = MaterialTheme.typography.caption,
                                            fontWeight = FontWeight.Bold,
                                            color = White,
                                            modifier = modifier.padding(MaterialTheme.spacing.medium)
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