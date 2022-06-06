package com.ecosense.android.featDiscoverCampaign.presentation.category

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Scale
import com.ecosense.android.R
import com.ecosense.android.core.presentation.theme.spacing
import com.ecosense.android.destinations.BrowseCampaignScreenDestination
import com.ecosense.android.featDiscoverCampaign.domain.model.Category
import com.ecosense.android.featDiscoverCampaign.presentation.component.DiscoverTopBar
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Composable
@Destination
fun CategoryCampaignScreen(
    navigator: DestinationsNavigator,
    viewModel: CategoryCampaignViewModel = hiltViewModel()
) {
    val scaffoldState = rememberScaffoldState()
    val scrollState = rememberScrollState()

    val state = viewModel.state.value
    val categories : List<Category> = state.categories

    Scaffold(
        topBar = {
            DiscoverTopBar(
                onBackClick = {
                    navigator.popBackStack()
                }
            )
        },
        scaffoldState = scaffoldState
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            Row {
                Text(
                    text = stringResource(R.string.choose_category),
                    fontWeight = FontWeight.SemiBold,
                    style = MaterialTheme.typography.subtitle1,
                    modifier = Modifier
                        .padding(MaterialTheme.spacing.medium)
                )
            }
            Row {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .padding(
                                start = MaterialTheme.spacing.medium,
                                end = MaterialTheme.spacing.medium,
                                bottom = MaterialTheme.spacing.medium
                            )
                            .shadow(elevation = 5.dp, shape = RoundedCornerShape(8.dp))
                            .clip(shape = RoundedCornerShape(8.dp))
                            .clickable(onClick = {
                                navigator.navigate(BrowseCampaignScreenDestination(search = null, categoryId = null))
                            })
                            .background(MaterialTheme.colors.surface)
                            .fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(105.dp)
                                .background(MaterialTheme.colors.primary),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = stringResource(R.string.all_campaign),
                                style = MaterialTheme.typography.h5,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colors.onPrimary,
                                modifier = Modifier
                                    .wrapContentSize()
                            )
                        }
                    }
                }
            }
            Row {
                Column(modifier = Modifier.fillMaxWidth()) {
                    for (category in categories) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .padding(
                                    start = MaterialTheme.spacing.medium,
                                    end = MaterialTheme.spacing.medium,
                                    bottom = MaterialTheme.spacing.medium
                                )
                                .shadow(elevation = 5.dp, shape = RoundedCornerShape(8.dp))
                                .clip(shape = RoundedCornerShape(8.dp))
                                .clickable(onClick = {
                                    navigator.navigate(BrowseCampaignScreenDestination(search = null, categoryId = category.id))
                                })
                                .background(MaterialTheme.colors.surface)
                                .fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(105.dp)
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
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .background(
                                                Brush.horizontalGradient(
                                                    colors = listOf(
                                                        Color(category.colorHex.toColorInt()),
                                                        Color.Transparent
                                                    )
                                                )
                                            ),
                                        verticalArrangement = Arrangement.Center,
                                        horizontalAlignment = Alignment.Start
                                    ) {
                                        Text(
                                            text = category.name,
                                            fontSize = 18.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White,
                                            modifier = Modifier
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