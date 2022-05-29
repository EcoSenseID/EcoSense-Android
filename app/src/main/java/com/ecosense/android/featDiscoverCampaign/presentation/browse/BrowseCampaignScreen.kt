package com.ecosense.android.featDiscoverCampaign.presentation.browse

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import androidx.hilt.navigation.compose.hiltViewModel
import com.ecosense.android.R
import com.ecosense.android.core.presentation.component.CampaignItem
import com.ecosense.android.core.presentation.theme.spacing
import com.ecosense.android.destinations.DetailCampaignScreenDestination
import com.ecosense.android.featDiscoverCampaign.presentation.component.DiscoverTopBar
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Composable
@Destination
fun BrowseCampaignScreen(
    navigator: DestinationsNavigator,
    search: String?,
    category: String?,
    viewModel: BrowseCampaignViewModel = hiltViewModel()
) {
    val scaffoldState = rememberScaffoldState()

    var expanded by remember { mutableStateOf(false) }
    val sortByList = listOf(
        stringResource(R.string.show_all_campaign),
        stringResource(R.string.new_campaign),
        stringResource(R.string.trending_campaign)
    )
    var selectedSort by remember { mutableStateOf("") }

    var textFieldSize by remember { mutableStateOf(Size.Zero) }

    val icon = if (expanded)
        Icons.Filled.KeyboardArrowUp
    else
        Icons.Filled.KeyboardArrowDown

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
        Column(modifier = Modifier.fillMaxSize()) {
            Row {
                if (search != null) {
                    Text(
                        text = stringResource(R.string.showing_search_result, search),
                        modifier = Modifier
                            .padding(
                                top = MaterialTheme.spacing.medium,
                                start = MaterialTheme.spacing.medium,
                                end = MaterialTheme.spacing.medium
                            ),
                        style = MaterialTheme.typography.caption,
                        fontStyle = FontStyle.Italic
                    )
                }
            }
            Row {
                Column(Modifier.padding(MaterialTheme.spacing.medium)) { // Sort DropDown
                    OutlinedTextField(
                        value = selectedSort,
                        onValueChange = { selectedSort = it },
                        enabled = false,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { expanded = !expanded }
                            .onGloballyPositioned { coordinates ->
                                //This value is used to assign to the DropDown the same width
                                textFieldSize = coordinates.size.toSize()
                            },
                        label = { Text(stringResource(R.string.sort_by)) },
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
                                selectedSort = label
                                expanded = false
                            }) {
                                Text(text = label)
                            }
                        }
                    }
                }
            }

            Row {
                LazyColumn(modifier = Modifier.fillMaxWidth()) {
                    items(viewModel.campaignList.value.size) { i ->
                        CampaignItem(
                            campaign = viewModel.campaignList.value[i],
                            search = search,
                            category = category,
                            sort = selectedSort,
                            onClick = {
                                navigator.navigate(DetailCampaignScreenDestination(id = viewModel.campaignList.value[i].id))
                            }
                        )
                    }
                }
            }
        }
    }
}