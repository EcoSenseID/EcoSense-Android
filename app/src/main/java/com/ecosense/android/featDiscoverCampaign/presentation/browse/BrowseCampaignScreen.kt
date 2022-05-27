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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import androidx.hilt.navigation.compose.hiltViewModel
import com.ecosense.android.core.presentation.component.CampaignItem
import com.ecosense.android.destinations.DetailCampaignScreenDestination
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Composable
@Destination
fun BrowseCampaignScreen(
    navigator: DestinationsNavigator,
    category: String?,
    viewModel: BrowseCampaignViewModel = hiltViewModel()
) {
    val scaffoldState = rememberScaffoldState()

    var expanded by remember { mutableStateOf(false) }
    val sortByList = listOf("Show All Campaign", "New Campaign", "Trending Campaign")
    var selectedSort by remember { mutableStateOf("") }

    var textFieldSize by remember { mutableStateOf(Size.Zero)}

    val icon = if (expanded)
        Icons.Filled.KeyboardArrowUp
    else
        Icons.Filled.KeyboardArrowDown

    Scaffold(scaffoldState = scaffoldState) {
        Column(modifier = Modifier.fillMaxSize()) {
            Row {
                Text("Browse Campaign")
            }
            
            Row { //DropDown
                Column(Modifier.padding(20.dp)) {
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
                        label = {Text("Sort by")},
                        trailingIcon = {
                            Icon(icon,"Show or hide the drop down")
                        }
                    )
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier
                            .width(with(LocalDensity.current){textFieldSize.width.toDp()})
                    ) {
                        sortByList.forEach { label ->
                            DropdownMenuItem(onClick = {
                                selectedSort = label
                                expanded = false
                                Log.d("TAG", "BrowseCampaignScreen: Sort by $selectedSort")
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
                            category = category,
                            sort = selectedSort,
                            onClick = {
                                navigator.navigate(DetailCampaignScreenDestination(id = viewModel.campaignList.value[i].id))
                                Log.d("TAG", "BrowseCampaignScreen: clicked $i")
                            }
                        )
                    }
                }
            }
        }
    }
}