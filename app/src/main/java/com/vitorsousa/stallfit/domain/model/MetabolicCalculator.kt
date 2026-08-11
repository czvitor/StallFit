package com.vitorsousa.stallfit.domain.model

import com.vitorsousa.stallfit.data.local.entity.ActivityLevel
import com.vitorsousa.stallfit.data.local.entity.BiologicalSex
import com.vitorsousa.stallfit.data.local.entity.NutritionGoal
import kotlin.math.roundToInt

data class MetabolicResult(
    val bmr: Int,
    val tdee: Int,
    val waterGoalMl: Int,
    val calorieTarget: Int,
    val proteinGramsGoal: Int,
    val carbGramsGoal: Int,
    val fatGramsGoal: Int
)

/**
 * Pure calculation logic for BMR/TDEE/water/macro targets — no Android or Room dependencies so it
 * stays trivially unit-testable and reusable outside [com.vitorsousa.stallfit.ui.profile].
 */
object MetabolicCalculator {

    // Mifflin-St Jeor: more accurate than Harris-Benedict across modern body compositions.
    private fun bmr(weightKg: Double, heightCm: Double, ageYears: Int, sex: BiologicalSex): Double {
        val base = 10 * weightKg + 6.25 * heightCm - 5 * ageYears
        return when (sex) {
            BiologicalSex.MALE -> base + 5
            BiologicalSex.FEMALE -> base - 161
        }
    }

    // 35-40 ml per kg of body weight is the commonly recommended daily water intake range; 37.5 is the midpoint.
    private const val WATER_ML_PER_KG = 37.5

    // Calorie surplus/deficit applied to TDEE depending on the user's goal.
    private const val HYPERTROPHY_SURPLUS = 1.10
    private const val CUTTING_DEFICIT = 0.80

    // Protein target in g/kg of body weight, by goal.
    private const val PROTEIN_G_PER_KG_HYPERTROPHY = 2.0
    private const val PROTEIN_G_PER_KG_CUTTING = 2.2
    private const val PROTEIN_G_PER_KG_MAINTENANCE = 1.8

    // Fat is fixed at 25% of the calorie target; carbs absorb whatever calories remain.
    private const val FAT_CALORIE_SHARE = 0.25
    private const val KCAL_PER_GRAM_PROTEIN = 4
    private const val KCAL_PER_GRAM_CARB = 4
    private const val KCAL_PER_GRAM_FAT = 9

    fun calculate(
        ageYears: Int,
        weightKg: Double,
        heightCm: Double,
        sex: BiologicalSex,
        activityLevel: ActivityLevel,
        goal: NutritionGoal
    ): MetabolicResult {
        val bmr = bmr(weightKg, heightCm, ageYears, sex)
        val tdee = bmr * activityLevel.multiplier

        val calorieTarget = when (goal) {
            NutritionGoal.HYPERTROPHY -> tdee * HYPERTROPHY_SURPLUS
            NutritionGoal.CUTTING -> tdee * CUTTING_DEFICIT
            NutritionGoal.MAINTENANCE -> tdee
        }

        val proteinGPerKg = when (goal) {
            NutritionGoal.HYPERTROPHY -> PROTEIN_G_PER_KG_HYPERTROPHY
            NutritionGoal.CUTTING -> PROTEIN_G_PER_KG_CUTTING
            NutritionGoal.MAINTENANCE -> PROTEIN_G_PER_KG_MAINTENANCE
        }
        val proteinGrams = proteinGPerKg * weightKg
        val fatCalories = calorieTarget * FAT_CALORIE_SHARE
        val fatGrams = fatCalories / KCAL_PER_GRAM_FAT
        val remainingCalories = calorieTarget - proteinGrams * KCAL_PER_GRAM_PROTEIN - fatCalories
        val carbGrams = (remainingCalories / KCAL_PER_GRAM_CARB).coerceAtLeast(0.0)

        return MetabolicResult(
            bmr = bmr.roundToInt(),
            tdee = tdee.roundToInt(),
            waterGoalMl = (weightKg * WATER_ML_PER_KG).roundToInt(),
            calorieTarget = calorieTarget.roundToInt(),
            proteinGramsGoal = proteinGrams.roundToInt(),
            carbGramsGoal = carbGrams.roundToInt(),
            fatGramsGoal = fatGrams.roundToInt()
        )
    }
}
