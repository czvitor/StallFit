package com.vitorsousa.stallfit.wear

import android.app.Application
import com.vitorsousa.stallfit.wear.data.PhoneConnector
import com.vitorsousa.stallfit.wear.sensors.ExerciseTrackingConnector

class StallFitWearApp : Application() {
    val phoneConnector: PhoneConnector by lazy { PhoneConnector(this) }
    val exerciseTrackingConnector: ExerciseTrackingConnector by lazy { ExerciseTrackingConnector(this) }
}
