package com.vitorsousa.stallfit.data.wear

import kotlinx.serialization.Serializable

/**
 * Wire DTOs for the watch<->phone protocol. Mirrored by hand in the `:wear` module (see that
 * module's own `WearDto.kt`) instead of a shared `:common` module — kept deliberately small.
 */
@Serializable
data class WearExerciseDto(val id: Long, val name: String)

@Serializable
data class WearTemplateDto(val id: Long, val title: String, val exercises: List<WearExerciseDto>)

@Serializable
data class WearCatalogDto(val templates: List<WearTemplateDto>, val allExercises: List<WearExerciseDto>)

@Serializable
data class WearStartSessionRequest(val templateId: Long?)

@Serializable
data class WearSessionStartedDto(val sessionId: Long, val sessionName: String)

@Serializable
data class WearLogSetRequest(val sessionId: Long, val exerciseId: Long, val reps: Int, val weightKg: Double)

@Serializable
data class WearSetLoggedDto(val setNumber: Int)

@Serializable
data class WearFinishSessionRequest(
    val sessionId: Long,
    val avgHeartRateBpm: Double? = null,
    val totalCalories: Double? = null
)
