package com.vitorsousa.stallfit.navigation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.FitnessCenter
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Restaurant
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.composable
import androidx.navigation.NavHostController
import androidx.navigation.navArgument
import androidx.navigation.NavType
import com.vitorsousa.stallfit.R
import com.vitorsousa.stallfit.ui.components.StallFitTopBar
import com.vitorsousa.stallfit.ui.dashboard.DashboardScreen
import com.vitorsousa.stallfit.ui.goals.GoalsScreen
import com.vitorsousa.stallfit.ui.nutrition.CreateMealScreen
import com.vitorsousa.stallfit.ui.nutrition.MealDetailScreen
import com.vitorsousa.stallfit.ui.nutrition.NutritionScreen
import com.vitorsousa.stallfit.ui.profile.ProfileScreen
import com.vitorsousa.stallfit.ui.workout.ActiveWorkoutScreen
import com.vitorsousa.stallfit.ui.workout.CreateWorkoutScreen
import com.vitorsousa.stallfit.ui.workout.WorkoutHomeScreen
import com.vitorsousa.stallfit.ui.workout.WorkoutTemplateDetailScreen

private val bottomNavItems = listOf(
    BottomNavItem(Destination.Dashboard, "Início", Icons.Filled.Home, Icons.Outlined.Home),
    BottomNavItem(Destination.Workout, "Treino", Icons.Filled.FitnessCenter, Icons.Outlined.FitnessCenter),
    BottomNavItem(Destination.Nutrition, "Nutrição", Icons.Filled.Restaurant, Icons.Outlined.Restaurant),
    BottomNavItem(Destination.Profile, "Perfil", Icons.Filled.AccountCircle, Icons.Outlined.AccountCircle)
)

private val topLevelRoutes = bottomNavItems.map { it.destination.route }.toSet()

/**
 * Navigates to a bottom-nav tab the same way tapping it in the [NavigationBar] would.
 * Reusing this (instead of a plain `navigate(route)`) from elsewhere in the app — e.g. a
 * Dashboard shortcut card — keeps the back stack consistent, so switching tabs afterwards
 * always lands back on the tab last shown instead of getting stuck on the pushed screen.
 */
private fun NavHostController.navigateToTopLevel(route: String) {
    navigate(route) {
        popUpTo(graph.findStartDestination().id) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}

private fun topBarTitleFor(route: String?): String = when (route) {
    Destination.Dashboard.route -> "Resumo de hoje"
    Destination.Workout.route -> "Treino"
    Destination.Nutrition.route -> "Cardápio"
    Destination.Profile.route -> "Perfil"
    else -> ""
}

@Composable
fun StallFitApp() {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route
    val showBottomBar = currentRoute in topLevelRoutes

    Scaffold(
        topBar = {
            if (showBottomBar) {
                StallFitTopBar(
                    title = topBarTitleFor(currentRoute),
                    actions = {
                        if (currentRoute == Destination.Nutrition.route) {
                            IconButton(onClick = { navController.navigate(Destination.Goals.route) }) {
                                Icon(
                                    imageVector = Icons.Filled.Tune,
                                    contentDescription = "Metas de macros",
                                    tint = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                )
            }
        },
        bottomBar = {
            if (showBottomBar) {
                Column {
                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                    NavigationBar(
                        containerColor = MaterialTheme.colorScheme.surfaceContainer,
                        tonalElevation = 0.dp
                    ) {
                        val currentDestination = backStackEntry?.destination
                        bottomNavItems.forEach { item ->
                            val selected = currentDestination?.hierarchy?.any { it.route == item.destination.route } == true
                            NavigationBarItem(
                                selected = selected,
                                onClick = { navController.navigateToTopLevel(item.destination.route) },
                                icon = {
                                    Icon(
                                        imageVector = if (selected) item.selectedIcon else item.unselectedIcon,
                                        contentDescription = item.label
                                    )
                                },
                                label = {
                                    Text(
                                        text = item.label,
                                        style = MaterialTheme.typography.labelMedium,
                                        fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal
                                    )
                                },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = MaterialTheme.colorScheme.primary,
                                    selectedTextColor = MaterialTheme.colorScheme.primary,
                                    indicatorColor = MaterialTheme.colorScheme.primaryContainer,
                                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            )
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            Image(
                painter = painterResource(id = R.drawable.stallfit_symbol),
                contentDescription = null,
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground),
                modifier = Modifier
                    .align(Alignment.Center)
                    .fillMaxWidth(0.75f)
                    .alpha(0.05f)
            )
            StallFitNavHost(
                navController = navController,
                modifier = Modifier.fillMaxSize()
            )
        }
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
                        navController.navigateToTopLevel(Destination.Workout.route)
                    }
                }
            )
        }

        composable(Destination.Workout.route) {
            WorkoutHomeScreen(
                onOpenSession = { sessionId ->
                    navController.navigate(Destination.WorkoutSession.createRoute(sessionId))
                },
                onOpenTemplate = { templateId ->
                    navController.navigate(Destination.WorkoutTemplateDetail.createRoute(templateId))
                },
                onCreateWorkout = { navController.navigate(Destination.CreateWorkout.route) }
            )
        }

        composable(
            route = Destination.WorkoutSession.route,
            arguments = listOf(navArgument(Destination.WorkoutSession.ARG_SESSION_ID) { type = NavType.LongType })
        ) {
            ActiveWorkoutScreen(onBack = { navController.popBackStack() })
        }

        composable(Destination.CreateWorkout.route) {
            CreateWorkoutScreen(onDone = { navController.popBackStack() })
        }

        composable(
            route = Destination.WorkoutTemplateDetail.route,
            arguments = listOf(navArgument(Destination.WorkoutTemplateDetail.ARG_TEMPLATE_ID) { type = NavType.LongType })
        ) {
            WorkoutTemplateDetailScreen(onBack = { navController.popBackStack() })
        }

        composable(Destination.Nutrition.route) {
            NutritionScreen(
                onCreateMeal = { mealType ->
                    navController.navigate(Destination.CreateMeal.createRoute(mealType.name))
                },
                onOpenMeal = { mealId ->
                    navController.navigate(Destination.MealDetail.createRoute(mealId))
                }
            )
        }

        composable(
            route = Destination.CreateMeal.route,
            arguments = listOf(
                navArgument(Destination.CreateMeal.ARG_MEAL_TYPE) { type = NavType.StringType }
            )
        ) {
            CreateMealScreen(onDone = { navController.popBackStack() })
        }

        composable(
            route = Destination.MealDetail.route,
            arguments = listOf(
                navArgument(Destination.MealDetail.ARG_MEAL_ID) { type = NavType.LongType }
            )
        ) {
            MealDetailScreen(onBack = { navController.popBackStack() })
        }

        composable(Destination.Goals.route) {
            GoalsScreen(onBack = { navController.popBackStack() })
        }

        composable(Destination.Profile.route) {
            ProfileScreen()
        }
    }
}
