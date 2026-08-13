package com.vitorsousa.stallfit.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

/** A reusable, saved meal option (e.g. "Omelete fitness") that belongs to one [mealType] category. */
@Serializable
@Entity(tableName = "meals")
data class MealEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val mealType: MealType,
    val createdAt: Long
)
