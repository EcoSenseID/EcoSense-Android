package com.ecosense.android.featProfile.presentation.profile.model

import com.ecosense.android.core.domain.model.User
import com.ecosense.android.featProfile.domain.model.Contributions

data class ProfileScreenState(
    val user: User,
    val contributions: Contributions,
    val isLoadingContributions: Boolean,
    val isDropdownMenuExpanded: Boolean,
) {
    companion object {
        val defaultValue = ProfileScreenState(
            user = User.defaultValue,
            contributions = Contributions.defaultValue,
            isLoadingContributions = false,
            isDropdownMenuExpanded = false,
        )
    }
}