package com.ecosense.android.featProfile.presentation.component

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ecosense.android.R

@Composable
fun ProfileTopBar(
    onExpandDropdownMenu: () -> Unit,
    isDropdownMenuExpanded: Boolean,
    onDropdownMenuDismissRequest: () -> Unit,
    dropdownMenuContent: @Composable ColumnScope.() -> Unit
) {
    TopAppBar(
        backgroundColor = MaterialTheme.colors.surface,
        elevation = 0.dp,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Spacer(modifier = Modifier.weight(1f))
            IconButton(onClick = onExpandDropdownMenu) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = stringResource(R.string.cd_options_menu),
                    tint = MaterialTheme.colors.onSurface
                )
            }
        }

        Box {
            DropdownMenu(
                expanded = isDropdownMenuExpanded,
                onDismissRequest = onDropdownMenuDismissRequest,
                content = dropdownMenuContent
            )
        }
    }
}