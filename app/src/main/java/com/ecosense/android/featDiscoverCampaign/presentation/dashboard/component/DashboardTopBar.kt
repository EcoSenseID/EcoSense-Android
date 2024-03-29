package com.ecosense.android.featDiscoverCampaign.presentation.dashboard.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideOut
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.ecosense.android.R
import com.ecosense.android.core.presentation.theme.spacing

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

    TopAppBar(
        backgroundColor = MaterialTheme.colors.surface,
        elevation = 0.dp,
        modifier = Modifier.fillMaxWidth()
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
                modifier = modifier
                    .fillMaxWidth()
                    .padding(horizontal = MaterialTheme.spacing.medium),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextField(
                    value = searchInput,
                    onValueChange = { searchInput = it },
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
                    keyboardActions = keyboardActions,
                    singleLine = true,
                    placeholder = { Text(stringResource(R.string.search_for_campaign)) },
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = MaterialTheme.colors.secondary.copy(alpha = 0.1f)
                    ),
                    shape = RoundedCornerShape(4.dp),
                    modifier = modifier
                        .weight(9f)
                        .padding(top = 2.dp)
                )
                IconButton(onClick = {
                    expanded = false
                    searchInput = TextFieldValue("")
                }, modifier = modifier.weight(1f)) {
                    Icon(
                        Icons.Default.Close,
                        stringResource(R.string.close_search_bar),
                        tint = MaterialTheme.colors.secondary
                    )
                }
            }
        }

        AnimatedVisibility(visible = !expanded) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Row(
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                ) {
                    IconButton(onClick = { expanded = true }) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = stringResource(R.string.open_search_bar),
                            tint = MaterialTheme.colors.secondary
                        )
                    }
                }

                Text(
                    text = stringResource(R.string.campaign),
                    color = MaterialTheme.colors.primary,
                    style = MaterialTheme.typography.h6,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
}