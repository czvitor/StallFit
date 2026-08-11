package com.vitorsousa.stallfit.di

import android.content.Context
import com.vitorsousa.stallfit.data.local.StallFitDatabase
import com.vitorsousa.stallfit.data.repository.NutritionRepository
import com.vitorsousa.stallfit.data.repository.WorkoutRepository

/**
 * Hand-rolled dependency container (no Hilt/Dagger): the app is a single module with a handful
 * of repositories, so a lazily-built container wired up once in [com.vitorsousa.stallfit.StallFitApp]
 * keeps things simple without pulling in an annotation-processing framework.
 */
class AppContainer(context: Context) {
    private val database = StallFitDatabase.getInstance(context)

    val workoutRepository: WorkoutRepository by lazy {
        WorkoutRepository(
            exerciseDao = database.exerciseDao(),
            sessionDao = database.workoutSessionDao(),
            setEntryDao = database.setEntryDao()
        )
    }

    val nutritionRepository: NutritionRepository by lazy {
        NutritionRepository(
            foodDao = database.foodDao(),
            mealEntryDao = database.mealEntryDao(),
            macroGoalDao = database.macroGoalDao()
        )
    }
}
