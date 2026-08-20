package com.vitorsousa.stallfit.wear.sensors

/** Snapshot of what [ExerciseTrackingService] has captured so far in the current session. */
data class ExerciseMetrics(
    val liveHeartRateBpm: Int? = null,
    val avgHeartRateBpm: Double? = null,
    val totalCalories: Double? = null
)
