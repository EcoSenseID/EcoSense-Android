package com.ecosense.android.core.presentation.component

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import com.ecosense.android.R
import com.ecosense.android.core.presentation.theme.FontPlusJakartaSans
import com.ecosense.android.core.presentation.theme.SuperDarkGrey
import com.ecosense.android.destinations.*
import com.ramcosta.composedestinations.spec.DirectionDestinationSpec

@OptIn(ExperimentalUnitApi::class)
@Composable
fun BottomBar(
    currentDestination: @Composable () -> Destination,
    onItemClick: (BottomBarDestination) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier
            .fillMaxWidth()
            .background(MaterialTheme.colors.surface)
            .height(56.dp)
            .selectableGroup(),
    ) {
        BottomBarDestination.values().forEach { destination ->
            val isSelected = currentDestination() == destination.direction

            Column(
                horizontalAlignment = CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f)
                    .clickable { onItemClick(destination) },
            ) {
                Icon(
                    painter = painterResource(id = destination.iconResId),
                    contentDescription = null,
                    tint = if (isSelected) MaterialTheme.colors.primary else SuperDarkGrey,
                )

                Text(
                    text = stringResource(destination.label),
                    style = TextStyle(
                        fontFamily = FontPlusJakartaSans,
                        fontSize = TextUnit(10f, TextUnitType.Sp),
                    ),
                    color = if (isSelected) MaterialTheme.colors.primary else SuperDarkGrey,
                )
            }
        }
    }
}

enum class BottomBarDestination(
    val direction: DirectionDestinationSpec,
    @DrawableRes val iconResId: Int,
    @StringRes val label: Int,
) {
    Campaign(
        direction = DiscoverCampaignScreenDestination,
        iconResId = R.drawable.ic_campaign,
        label = R.string.campaign,
    ),
    EcoWorld(
        direction = ForumsScreenDestination,
        iconResId = R.drawable.ic_ecoworld,
        label = R.string.ecoworld,
    ),
    EcoReward(
        direction = RewardsScreenDestination,
        iconResId = R.drawable.ic_ecoreward,
        label = R.string.ecoreward,
    ),
    Notifications(
        direction = NotificationsScreenDestination,
        iconResId = R.drawable.ic_notifications,
        label = R.string.notifications,
    ),
    Profile(
        direction = ProfileScreenDestination,
        iconResId = R.drawable.ic_profile,
        label = R.string.profile,
    )
}