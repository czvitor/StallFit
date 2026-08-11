package com.vitorsousa.stallfit.di

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.vitorsousa.stallfit.StallFitApp
import com.vitorsousa.stallfit.ui.dashboard.DashboardViewModel
import com.vitorsousa.stallfit.ui.goals.GoalsViewModel
import com.vitorsousa.stallfit.ui.nutrition.AddFoodEntryViewModel
import com.vitorsousa.stallfit.ui.nutrition.NutritionDiaryViewModel
import com.vitorsousa.stallfit.ui.workout.ActiveWorkoutViewModel
import com.vitorsousa.stallfit.ui.workout.WorkoutHomeViewModel

/** Retrieves the [AppContainer] from the [CreationExtras] the platform hands every ViewModel factory. */
fun CreationExtras.stallFitContainer(): AppContainer =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as StallFitApp).container

/**
 * Wires every ViewModel in the app to its repositories by hand — the manual-DI counterpart to
 * [AppContainer]. Screens create their ViewModel with `viewModel(factory = AppViewModelProvider.Factory)`.
 */
object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            DashboardViewModel(
                workoutRepository = stallFitContainer().workoutRepository,
                nutritionRepository = stallFitContainer().nutritionRepository
            )
        }
        initializer {
            WorkoutHomeViewModel(workoutRepository = stallFitContainer().workoutRepository)
        }
        initializer {
            ActiveWorkoutViewModel(
                savedStateHandle = createSavedStateHandle(),
                workoutRepository = stallFitContainer().workoutRepository
            )
        }
        initializer {
            NutritionDiaryViewModel(nutritionRepository = stallFitContainer().nutritionRepository)
        }
        initializer {
            AddFoodEntryViewModel(
                savedStateHandle = createSavedStateHandle(),
                nutritionRepository = stallFitContainer().nutritionRepository
            )
        }
        initializer {
            GoalsViewModel(nutritionRepository = stallFitContainer().nutritionRepository)
        }
    }
}
