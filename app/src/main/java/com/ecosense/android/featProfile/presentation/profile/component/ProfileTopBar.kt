package com.ecosense.android.featProfile.presentation.profile.component

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
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

            Text(
                text = stringResource(R.string.my_profile),
                style = MaterialTheme.typography.h6,
                fontWeight = FontWeight.Bold,
            )

            Row(
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
            ) {
                IconButton(onClick = onExpandDropdownMenu) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = stringResource(R.string.cd_options_menu),
                        tint = MaterialTheme.colors.onSurface,
                    )
                }
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