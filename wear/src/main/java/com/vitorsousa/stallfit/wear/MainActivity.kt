package com.vitorsousa.stallfit.wear

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.vitorsousa.stallfit.wear.di.WearViewModelProvider
import com.vitorsousa.stallfit.wear.sensors.sensorRuntimePermissions
import com.vitorsousa.stallfit.wear.theme.StallFitWearTheme
import com.vitorsousa.stallfit.wear.ui.ActiveWorkoutScreen
import com.vitorsousa.stallfit.wear.ui.SelectTemplateScreen
import com.vitorsousa.stallfit.wear.ui.WearScreen
import com.vitorsousa.stallfit.wear.ui.WearViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: WearViewModel by viewModels { WearViewModelProvider.Factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StallFitWearTheme {
                val screen by viewModel.screen.collectAsState()
                val catalogState by viewModel.catalogState.collectAsState()
                val actionState by viewModel.actionState.collectAsState()
                val lastLoggedSetNumber by viewModel.lastLoggedSetNumber.collectAsState()
                val liveHeartRateBpm by viewModel.liveHeartRateBpm.collectAsState()

                val permissionLauncher = rememberLauncherForActivityResult(
                    ActivityResultContracts.RequestMultiplePermissions()
                ) { /* no-op: ExerciseTrackingService reconfirms the permission before starting */ }
                LaunchedEffect(Unit) { permissionLauncher.launch(sensorRuntimePermissions()) }

                when (val current = screen) {
                    is WearScreen.SelectTemplate -> SelectTemplateScreen(
                        catalogState = catalogState,
                        onSelectTemplate = viewModel::selectTemplate,
                        onRetry = viewModel::loadCatalog
                    )

                    is WearScreen.ActiveSession -> ActiveWorkoutScreen(
                        sessionName = current.sessionName,
                        exercises = current.exercises,
                        actionState = actionState,
                        lastLoggedSetNumber = lastLoggedSetNumber,
                        liveHeartRateBpm = liveHeartRateBpm,
                        onLogSet = viewModel::logSet,
                        onFinishSession = viewModel::finishSession
                    )
                }
            }
        }
    }
}
