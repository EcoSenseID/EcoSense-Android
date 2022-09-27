package com.ecosense.android.featAuth.presentation.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.ecosense.android.R
import com.ecosense.android.core.presentation.theme.spacing

@Composable
fun AuthTopBar(
    onClickCancel: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = MaterialTheme.spacing.medium),
    ) {
        AsyncImage(
            model = R.drawable.ic_logo_full_for_bright_background,
            contentDescription = null,
            modifier = Modifier.height(50.dp),
        )

        Spacer(modifier = Modifier.width(MaterialTheme.spacing.medium))

        Text(
            text = stringResource(R.string.cancel),
            color = MaterialTheme.colors.secondary,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.clickable { onClickCancel() },
        )
    }
}