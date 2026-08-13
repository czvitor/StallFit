package com.vitorsousa.stallfit.domain.model

import com.vitorsousa.stallfit.data.local.entity.ActivityLevel
import com.vitorsousa.stallfit.data.local.entity.BiologicalSex
import com.vitorsousa.stallfit.data.local.entity.NutritionGoal
import org.junit.Assert.assertEquals
import org.junit.Test

class MetabolicCalculatorTest {

    @Test
    fun `male maintenance uses the plus-5 Mifflin-St Jeor offset`() {
        val result = MetabolicCalculator.calculate(
            ageYears = 25,
            weightKg = 80.0,
            heightCm = 180.0,
            sex = BiologicalSex.MALE,
            activityLevel = ActivityLevel.MODERATE,
            goal = NutritionGoal.MAINTENANCE
        )

        // BMR = 10*80 + 6.25*180 - 5*25 + 5 = 1805; TDEE = 1805 * 1.55 = 2797.75
        assertEquals(1805, result.bmr)
        assertEquals(2798, result.tdee)
        assertEquals(2798, result.calorieTarget)
        assertEquals(144, result.proteinGramsGoal)
        assertEquals(381, result.carbGramsGoal)
        assertEquals(78, result.fatGramsGoal)
        assertEquals(3000, result.waterGoalMl)
    }

    @Test
    fun `female cutting uses the minus-161 offset and the 0,8 deficit`() {
        val result = MetabolicCalculator.calculate(
            ageYears = 30,
            weightKg = 65.0,
            heightCm = 165.0,
            sex = BiologicalSex.FEMALE,
            activityLevel = ActivityLevel.SEDENTARY,
            goal = NutritionGoal.CUTTING
        )

        // BMR = 10*65 + 6.25*165 - 5*30 - 161 = 1370.25; TDEE = 1370.25 * 1.2 = 1644.3
        assertEquals(1370, result.bmr)
        assertEquals(1644, result.tdee)
        assertEquals(1315, result.calorieTarget)
        assertEquals(143, result.proteinGramsGoal)
        assertEquals(104, result.carbGramsGoal)
        assertEquals(37, result.fatGramsGoal)
        assertEquals(2438, result.waterGoalMl)
    }

    @Test
    fun `carb goal never goes negative when protein and fat already exceed the calorie target`() {
        // A high-weight, low-height, low-activity, elderly, cutting profile pushes protein
        // (2.2 g per kg) past what's left after the 25% fat share of an already-deficit target —
        // this is the scenario the `coerceAtLeast(0.0)` guard in MetabolicCalculator exists for.
        val result = MetabolicCalculator.calculate(
            ageYears = 90,
            weightKg = 150.0,
            heightCm = 150.0,
            sex = BiologicalSex.FEMALE,
            activityLevel = ActivityLevel.SEDENTARY,
            goal = NutritionGoal.CUTTING
        )

        assertEquals(0, result.carbGramsGoal)
    }
}
