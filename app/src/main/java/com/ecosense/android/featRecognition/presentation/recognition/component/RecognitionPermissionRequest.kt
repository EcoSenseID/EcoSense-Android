package com.ecosense.android.featRecognition.presentation.recognition.component

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.ecosense.android.R
import com.ecosense.android.core.presentation.component.RoundedEndsButton
import com.ecosense.android.core.presentation.theme.spacing
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun RecognitionPermissionRequest(
    modifier: Modifier = Modifier,
    camPermission: PermissionState,
) {
    val context = LocalContext.current

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxWidth()
            .padding(MaterialTheme.spacing.large)
    ) {
        Text(
            text = stringResource(
                id = when {
                    !camPermission.permissionRequested -> R.string.permission_request_message
                    camPermission.shouldShowRationale -> R.string.permission_request_rationale
                    else -> R.string.permission_request_denied_permanently
                }
            )
        )

        AsyncImage(
            model = R.drawable.ic_robot_face_rafiki,
            contentDescription = null,
            modifier = Modifier.size(300.dp)
        )

        RoundedEndsButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                when {
                    !camPermission.permissionRequested -> camPermission.launchPermissionRequest()
                    camPermission.shouldShowRationale -> camPermission.launchPermissionRequest()
                    else -> {
                        Intent(
                            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                            Uri.fromParts("package", context.packageName, null)
                        ).apply {
                            this.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            context.startActivity(this)
                        }
                    }
                }
            }) {
            Text(
                text = stringResource(
                    id = when {
                        !camPermission.permissionRequested -> R.string.grant_permission
                        camPermission.shouldShowRationale -> R.string.grant_permission
                        else -> R.string.open_app_settings
                    }
                )
            )
        }
    }
}