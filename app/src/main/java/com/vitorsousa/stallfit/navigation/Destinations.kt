package com.vitorsousa.stallfit.navigation

sealed class Destination(val route: String) {
    object Dashboard : Destination("dashboard")
    object Workout : Destination("workout")
    object Nutrition : Destination("nutrition")
    object Goals : Destination("goals")
    object Profile : Destination("profile")

    object WorkoutSession : Destination("workout/session/{sessionId}") {
        const val ARG_SESSION_ID = "sessionId"
        fun createRoute(sessionId: Long) = "workout/session/$sessionId"
    }

    object CreateWorkout : Destination("workout/create")

    object WorkoutTemplateDetail : Destination("workout/template/{templateId}") {
        const val ARG_TEMPLATE_ID = "templateId"
        fun createRoute(templateId: Long) = "workout/template/$templateId"
    }

    object CreateMeal : Destination("nutrition/create/{mealType}") {
        const val ARG_MEAL_TYPE = "mealType"
        fun createRoute(mealType: String) = "nutrition/create/$mealType"
    }

    object MealDetail : Destination("nutrition/meal/{mealId}") {
        const val ARG_MEAL_ID = "mealId"
        fun createRoute(mealId: Long) = "nutrition/meal/$mealId"
    }
}

data class BottomNavItem(
    val destination: Destination,
    val label: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
)
