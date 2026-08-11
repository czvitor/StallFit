package com.vitorsousa.stallfit.data.local

import androidx.room.TypeConverter
import com.vitorsousa.stallfit.data.local.entity.ActivityLevel
import com.vitorsousa.stallfit.data.local.entity.BiologicalSex
import com.vitorsousa.stallfit.data.local.entity.Equipment
import com.vitorsousa.stallfit.data.local.entity.Intensity
import com.vitorsousa.stallfit.data.local.entity.MealType
import com.vitorsousa.stallfit.data.local.entity.NutritionGoal

class Converters {
    @TypeConverter
    fun fromMealType(value: MealType): String = value.name

    @TypeConverter
    fun toMealType(value: String): MealType = MealType.valueOf(value)

    @TypeConverter
    fun fromBiologicalSex(value: BiologicalSex): String = value.name

    @TypeConverter
    fun toBiologicalSex(value: String): BiologicalSex = BiologicalSex.valueOf(value)

    @TypeConverter
    fun fromActivityLevel(value: ActivityLevel): String = value.name

    @TypeConverter
    fun toActivityLevel(value: String): ActivityLevel = ActivityLevel.valueOf(value)

    @TypeConverter
    fun fromNutritionGoal(value: NutritionGoal): String = value.name

    @TypeConverter
    fun toNutritionGoal(value: String): NutritionGoal = NutritionGoal.valueOf(value)

    @TypeConverter
    fun fromEquipment(value: Equipment): String = value.name

    @TypeConverter
    fun toEquipment(value: String): Equipment = Equipment.valueOf(value)

    @TypeConverter
    fun fromIntensity(value: Intensity): String = value.name

    @TypeConverter
    fun toIntensity(value: String): Intensity = Intensity.valueOf(value)
}
