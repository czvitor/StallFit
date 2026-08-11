package com.vitorsousa.stallfit

import android.app.Application
import com.vitorsousa.stallfit.di.AppContainer

class StallFitApp : Application() {
    lateinit var container: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
    }
}
