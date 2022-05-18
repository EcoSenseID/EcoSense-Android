package com.ecosense.android.featProfile.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.ecosense.android.R
import com.ecosense.android.core.presentation.theme.spacing
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Composable
@Destination
fun ProfileScreen(
    navigator: DestinationsNavigator,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val scaffoldState = rememberScaffoldState()

    Scaffold(
        scaffoldState = scaffoldState,
        modifier = Modifier.padding(MaterialTheme.spacing.medium)
    ) {
        val user = viewModel.user.collectAsState().value
        Column(modifier = Modifier.fillMaxSize()) {
            Text("Profile Screen")

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

            Text(text = "Email: ${user.email}")

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

            Button(onClick = { viewModel.onLogoutClick() }) {
                Text(text = stringResource(R.string.logout))
            }
        }
    }
}