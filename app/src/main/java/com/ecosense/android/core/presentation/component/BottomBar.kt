package com.ecosense.android.core.presentation.component

import androidx.annotation.StringRes
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Campaign
import androidx.compose.material.icons.outlined.Eco
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ecosense.android.NavGraphs
import com.ecosense.android.R
import com.ecosense.android.appCurrentDestinationAsState
import com.ecosense.android.destinations.Destination
import com.ecosense.android.destinations.DiscoverCampaignScreenDestination
import com.ecosense.android.destinations.DiseaseRecognitionScreenDestination
import com.ecosense.android.destinations.ProfileScreenDestination
import com.ecosense.android.startAppDestination
import com.ramcosta.composedestinations.navigation.navigateTo
import com.ramcosta.composedestinations.spec.DirectionDestinationSpec

@Composable
fun BottomBar(
    navController: NavController
) {
    val currentDestination: Destination = navController.appCurrentDestinationAsState().value
        ?: NavGraphs.root.startAppDestination

    BottomNavigation(
        backgroundColor = MaterialTheme.colors.surface,
        contentColor = MaterialTheme.colors.primary,
        elevation = 16.dp,
    ) {
        BottomBarDestination.values().forEach { destination ->
            val isSelected = currentDestination == destination.direction
            BottomNavigationItem(
                selected = isSelected,
                onClick = {
                    navController.navigateTo(destination.direction) { launchSingleTop = true }
                },
                icon = {
                    Icon(
                        imageVector = if (isSelected) destination.selectedIcon else destination.icon,
                        contentDescription = stringResource(destination.label),
                    )
                },
                label = { Text(stringResource(destination.label)) },
            )
        }
    }
}

private enum class BottomBarDestination(
    val direction: DirectionDestinationSpec,
    val icon: ImageVector,
    val selectedIcon: ImageVector,
    @StringRes val label: Int
) {
    DiscoverCampaign(
        direction = DiscoverCampaignScreenDestination,
        icon = Icons.Outlined.Campaign,
        selectedIcon = Icons.Filled.Campaign,
        label = R.string.campaign
    ),

    DiseaseRecognition(
        direction = DiseaseRecognitionScreenDestination,
        icon = Icons.Outlined.Eco,
        selectedIcon = Icons.Filled.Eco,
        label = R.string.plant
    ),

    Profile(
        direction = ProfileScreenDestination,
        icon = Icons.Outlined.AccountCircle,
        selectedIcon = Icons.Filled.AccountCircle,
        label = R.string.profile
    )
}