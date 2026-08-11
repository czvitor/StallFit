package com.vitorsousa.stallfit.navigation

sealed class Destination(val route: String) {
    object Dashboard : Destination("dashboard")
    object Workout : Destination("workout")
    object Nutrition : Destination("nutrition")
    object Goals : Destination("goals")

    object WorkoutSession : Destination("workout/session/{sessionId}") {
        const val ARG_SESSION_ID = "sessionId"
        fun createRoute(sessionId: Long) = "workout/session/$sessionId"
    }

    object AddFoodEntry : Destination("nutrition/add/{mealType}/{dateEpochDay}") {
        const val ARG_MEAL_TYPE = "mealType"
        const val ARG_DATE_EPOCH_DAY = "dateEpochDay"
        fun createRoute(mealType: String, dateEpochDay: Long) = "nutrition/add/$mealType/$dateEpochDay"
    }
}

data class BottomNavItem(
    val destination: Destination,
    val label: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
)
