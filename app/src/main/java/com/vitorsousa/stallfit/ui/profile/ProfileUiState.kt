package com.vitorsousa.stallfit.ui.profile

import com.vitorsousa.stallfit.data.local.entity.BodyMeasurementEntity
import com.vitorsousa.stallfit.data.local.entity.UserProfileEntity
import com.vitorsousa.stallfit.domain.model.MetabolicResult

data class ProfileUiState(
    val profile: UserProfileEntity? = null,
    val latestMeasurement: BodyMeasurementEntity? = null,
    val measurementHistory: List<BodyMeasurementEntity> = emptyList(),
    val result: MetabolicResult? = null,
    val isLoading: Boolean = true
)
