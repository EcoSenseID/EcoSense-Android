@file:OptIn(ExperimentalMaterialApi::class)

package com.ecosense.android.featDiscoverCampaign.presentation.dashboard.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ecosense.android.R

@Composable
fun DashboardTopBar(
    executeSearch: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false)}
    var searchInput by remember { mutableStateOf(TextFieldValue(""))}

    val focusManager = LocalFocusManager.current

    val keyboardActions = KeyboardActions(
        onSearch = {
            focusManager.clearFocus()
            executeSearch(searchInput.text)
        }
    )

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        AnimatedVisibility(
            visible = expanded,
            enter = slideIn{ fullSize ->
                IntOffset(fullSize.width + 100, 0)
            },
            exit = slideOut{ fullSize ->
                IntOffset(fullSize.width + 100, 0)
            },
        ) {
            Row(
                modifier = modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextField(
                    value = searchInput,
                    onValueChange = { searchInput = it },
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
                    keyboardActions = keyboardActions,
                    singleLine = true,
                    modifier = modifier.weight(9f)
                )
                IconButton(onClick = {
                    expanded = false
                    searchInput = TextFieldValue("")
                }, modifier = modifier.weight(1f)) {
                    Icon(Icons.Default.Close, stringResource(R.string.close_search_bar))
                }
            }
        }
        AnimatedVisibility(visible = !expanded) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(onClick = { expanded = true }) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = stringResource(R.string.open_search_bar)
                    )
                }
                Text(
                    text = stringResource(R.string.campaign),
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold
                )
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = stringResource(R.string.blank),
                    modifier = modifier.alpha(0f)
                )
            }
        }
    }
}