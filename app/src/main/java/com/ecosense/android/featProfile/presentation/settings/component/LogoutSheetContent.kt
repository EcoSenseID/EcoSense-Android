package com.ecosense.android.featProfile.presentation.settings.component

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.ecosense.android.R
import com.ecosense.android.core.presentation.theme.DarkRed
import com.ecosense.android.core.presentation.theme.spacing

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
        Text(
            text = stringResource(R.string.confirm_logout),
            style = MaterialTheme.typography.h6,
            fontWeight = FontWeight.SemiBold,
        )

        Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

        Row(modifier = Modifier.fillMaxWidth()) {
            Button(
                onClick = onClickYes,
                colors = ButtonDefaults.buttonColors(backgroundColor = DarkRed),
                modifier = Modifier.weight(1f),
            ) {
                Text(
                    text = stringResource(R.string.yes),
                    color = MaterialTheme.colors.onPrimary,
                )
            }

            Spacer(modifier = Modifier.width(MaterialTheme.spacing.medium))

            OutlinedButton(
                onClick = onClickCancel,
                modifier = Modifier.weight(1f),
            ) {
                Text(text = stringResource(id = R.string.cancel))
            }
        }
    }
}