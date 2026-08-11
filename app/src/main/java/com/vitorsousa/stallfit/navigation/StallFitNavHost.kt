package com.vitorsousa.stallfit.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.composable
import androidx.navigation.NavHostController
import androidx.navigation.navArgument
import androidx.navigation.NavType
import com.vitorsousa.stallfit.ui.dashboard.DashboardScreen
import com.vitorsousa.stallfit.ui.goals.GoalsScreen
import com.vitorsousa.stallfit.ui.nutrition.AddFoodEntryScreen
import com.vitorsousa.stallfit.ui.nutrition.NutritionDiaryScreen
import com.vitorsousa.stallfit.ui.workout.ActiveWorkoutScreen
import com.vitorsousa.stallfit.ui.workout.WorkoutHomeScreen

private val bottomNavItems = listOf(
    BottomNavItem(Destination.Dashboard, "Início", Icons.Filled.Home),
    BottomNavItem(Destination.Workout, "Treino", Icons.Filled.FitnessCenter),
    BottomNavItem(Destination.Nutrition, "Nutrição", Icons.Filled.Restaurant)
)

private val topLevelRoutes = bottomNavItems.map { it.destination.route }.toSet()

@Composable
fun StallFitApp() {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route
    val showBottomBar = currentRoute in topLevelRoutes

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar {
                    val currentDestination = backStackEntry?.destination
                    bottomNavItems.forEach { item ->
                        val selected = currentDestination?.hierarchy?.any { it.route == item.destination.route } == true
                        NavigationBarItem(
                            selected = selected,
                            onClick = {
                                navController.navigate(item.destination.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = { androidx.compose.material3.Icon(item.icon, contentDescription = item.label) },
                            label = { androidx.compose.material3.Text(item.label) }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        StallFitNavHost(
            navController = navController,
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@Composable
private fun StallFitNavHost(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(
        navController = navController,
        startDestination = Destination.Dashboard.route,
        modifier = modifier
    ) {
        composable(Destination.Dashboard.route) {
            DashboardScreen(
                onStartOrResumeWorkout = { sessionId ->
                    if (sessionId != null) {
                        navController.navigate(Destination.WorkoutSession.createRoute(sessionId))
                    } else {
                        navController.navigate(Destination.Workout.route)
                    }
                },
                onOpenNutrition = { navController.navigate(Destination.Nutrition.route) }
            )
        }

        composable(Destination.Workout.route) {
            WorkoutHomeScreen(
                onOpenSession = { sessionId ->
                    navController.navigate(Destination.WorkoutSession.createRoute(sessionId))
                }
            )
        }

        composable(
            route = Destination.WorkoutSession.route,
            arguments = listOf(navArgument(Destination.WorkoutSession.ARG_SESSION_ID) { type = NavType.LongType })
        ) {
            ActiveWorkoutScreen(onBack = { navController.popBackStack() })
        }

        composable(Destination.Nutrition.route) {
            NutritionDiaryScreen(
                onAddFood = { mealType, dateEpochDay ->
                    navController.navigate(Destination.AddFoodEntry.createRoute(mealType.name, dateEpochDay))
                },
                onOpenGoals = { navController.navigate(Destination.Goals.route) }
            )
        }

        composable(
            route = Destination.AddFoodEntry.route,
            arguments = listOf(
                navArgument(Destination.AddFoodEntry.ARG_MEAL_TYPE) { type = NavType.StringType },
                navArgument(Destination.AddFoodEntry.ARG_DATE_EPOCH_DAY) { type = NavType.LongType }
            )
        ) {
            AddFoodEntryScreen(onDone = { navController.popBackStack() })
        }

        composable(Destination.Goals.route) {
            GoalsScreen(onBack = { navController.popBackStack() })
        }
    }
}
