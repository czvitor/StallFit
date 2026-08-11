package com.vitorsousa.stallfit.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/** A reusable, saved meal option (e.g. "Omelete fitness") that belongs to one [mealType] category. */
@Entity(tableName = "meals")
data class MealEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val mealType: MealType,
    val createdAt: Long
)
