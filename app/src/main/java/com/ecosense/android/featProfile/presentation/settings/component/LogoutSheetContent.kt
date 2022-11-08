package com.ecosense.android.featProfile.presentation.settings.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import com.ecosense.android.R
import com.ecosense.android.core.presentation.modifier.brushForeground
import com.ecosense.android.core.presentation.theme.DarkRed
import com.ecosense.android.core.presentation.theme.Gradient
import com.ecosense.android.core.presentation.theme.spacing

@OptIn(ExperimentalUnitApi::class)
@Composable
fun LogoutSheetContent(
    onClickYes: () -> Unit,
    onClickCancel: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier,
    ) {
        Spacer(modifier = Modifier.height(MaterialTheme.spacing.large))

        Text(
            text = stringResource(R.string.confirm_logout),
            fontSize = TextUnit(18f, TextUnitType.Sp),
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colors.primary,
        )

        Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

        Row(modifier = Modifier.fillMaxWidth()) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .clickable { onClickYes() }
                    .background(DarkRed)
                    .padding(MaterialTheme.spacing.small)
                    .weight(1f),
            ) {
                Text(
                    text = stringResource(R.string.yes),
                    color = MaterialTheme.colors.onPrimary,
                    fontWeight = FontWeight.SemiBold,
                )
            }

            Spacer(modifier = Modifier.width(MaterialTheme.spacing.medium))

            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .clickable { onClickCancel() }
                    .background(MaterialTheme.colors.surface)
                    .border(1.dp, Gradient, RoundedCornerShape(8.dp))
                    .padding(MaterialTheme.spacing.small)
                    .weight(1f),
            ) {
                Text(
                    text = stringResource(id = R.string.cancel),
                    modifier = Modifier.brushForeground(Gradient),
                    fontWeight = FontWeight.SemiBold,
                    )
            }
        }

        Spacer(modifier = Modifier.height(MaterialTheme.spacing.large))
    }
}