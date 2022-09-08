package com.ecosense.android.core.presentation.component

import androidx.annotation.StringRes
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.Forum
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Campaign
import androidx.compose.material.icons.outlined.Eco
import androidx.compose.material.icons.outlined.Forum
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ecosense.android.R
import com.ecosense.android.destinations.*
import com.ramcosta.composedestinations.spec.DirectionDestinationSpec

@Composable
fun BottomBar(
    currentDestination: @Composable () -> Destination,
    onItemClick: (BottomBarDestination) -> Unit,
    modifier: Modifier = Modifier,
) {
    BottomNavigation(
        backgroundColor = MaterialTheme.colors.surface,
        contentColor = MaterialTheme.colors.primary,
        elevation = 2.dp,
    ) {
        BottomBarDestination.values().forEach { destination ->
            val isSelected = currentDestination() == destination.direction

            BottomNavigationItem(
                selected = isSelected,
                onClick = { onItemClick(destination) },
                icon = {
                    Icon(
                        imageVector = if (isSelected) destination.iconSelected else destination.icon,
                        contentDescription = null,
                    )
                },
                label = { Text(stringResource(destination.label)) },
            )
        }
    }
}

enum class BottomBarDestination(
    val direction: DirectionDestinationSpec,
    val icon: ImageVector,
    val iconSelected: ImageVector,
    @StringRes val label: Int
) {
    Forums(
        direction = ForumsScreenDestination,
        icon = Icons.Outlined.Forum,
        iconSelected = Icons.Filled.Forum,
        label = R.string.bottom_bar_label_forums,
    ),

    DiscoverCampaign(
        direction = DiscoverCampaignScreenDestination,
        icon = Icons.Outlined.Campaign,
        iconSelected = Icons.Filled.Campaign,
        label = R.string.campaign
    ),

    Recognition(
        direction = RecognitionScreenDestination,
        icon = Icons.Outlined.Eco,
        iconSelected = Icons.Filled.Eco,
        label = R.string.plant
    ),

    Profile(
        direction = ProfileScreenDestination,
        icon = Icons.Outlined.AccountCircle,
        iconSelected = Icons.Filled.AccountCircle,
        label = R.string.profile
    )
}