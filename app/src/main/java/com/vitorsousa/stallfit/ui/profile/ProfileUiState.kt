package com.vitorsousa.stallfit.ui.profile

import com.vitorsousa.stallfit.data.local.entity.UserProfileEntity
import com.vitorsousa.stallfit.domain.model.MetabolicResult

data class ProfileUiState(
    val profile: UserProfileEntity? = null,
    val result: MetabolicResult? = null,
    val isLoading: Boolean = true
)
