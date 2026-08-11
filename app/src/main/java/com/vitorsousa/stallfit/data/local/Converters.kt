package com.vitorsousa.stallfit.data.local

import androidx.room.TypeConverter
import com.vitorsousa.stallfit.data.local.entity.MealType

class Converters {
    @TypeConverter
    fun fromMealType(value: MealType): String = value.name

    @TypeConverter
    fun toMealType(value: String): MealType = MealType.valueOf(value)
}
