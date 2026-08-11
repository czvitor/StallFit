package com.vitorsousa.stallfit.data.local.relation

import androidx.room.Embedded
import com.vitorsousa.stallfit.data.local.entity.SetEntryEntity

/** Result of joining `set_entries` with `exercises` — avoids a second query just to show a name. */
data class SetEntryWithExercise(
    @Embedded val setEntry: SetEntryEntity,
    val exerciseName: String
)
