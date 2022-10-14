package com.ecosense.android.featProfile.presentation.profile.component

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.rounded.ArrowBack
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
        backgroundColor = MaterialTheme.colors.background,
        elevation = 0.dp,
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxSize(),
        ) {
            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = stringResource(R.string.my_profile),
                color = MaterialTheme.colors.primary,
                style = MaterialTheme.typography.h6,
                fontWeight = FontWeight.Bold,
            )

            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier.weight(1f),
            ) {
                IconButton(onClick = onExpandDropdownMenu) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = stringResource(R.string.cd_options_menu),
                        tint = MaterialTheme.colors.secondary,
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