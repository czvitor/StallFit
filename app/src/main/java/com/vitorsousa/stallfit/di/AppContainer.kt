package com.vitorsousa.stallfit.di

import android.content.Context
import com.vitorsousa.stallfit.data.local.StallFitDatabase
import com.vitorsousa.stallfit.data.repository.NutritionRepository
import com.vitorsousa.stallfit.data.repository.ProfileRepository
import com.vitorsousa.stallfit.data.repository.WorkoutRepository
import com.vitorsousa.stallfit.data.repository.WorkoutTemplateRepository

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
            mealDao = database.mealDao(),
            macroGoalDao = database.macroGoalDao()
        )
    }

    val profileRepository: ProfileRepository by lazy {
        ProfileRepository(
            userProfileDao = database.userProfileDao(),
            bodyMeasurementDao = database.bodyMeasurementDao()
        )
    }

    val workoutTemplateRepository: WorkoutTemplateRepository by lazy {
        WorkoutTemplateRepository(
            templateDao = database.workoutTemplateDao(),
            sessionDao = database.workoutSessionDao()
        )
    }
}
