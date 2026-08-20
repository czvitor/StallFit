package com.vitorsousa.stallfit.wear.di

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.vitorsousa.stallfit.wear.StallFitWearApp
import com.vitorsousa.stallfit.wear.ui.WearViewModel

/** Retrieves the [StallFitWearApp] from the [CreationExtras] the platform hands every ViewModel factory. */
fun CreationExtras.stallFitWearApp(): StallFitWearApp =
    this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as StallFitWearApp

/** Manual-DI counterpart to [com.vitorsousa.stallfit.di.AppViewModelProvider] on the phone side. */
object WearViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            WearViewModel(
                phoneConnector = stallFitWearApp().phoneConnector,
                exerciseTrackingConnector = stallFitWearApp().exerciseTrackingConnector
            )
        }
    }
}
