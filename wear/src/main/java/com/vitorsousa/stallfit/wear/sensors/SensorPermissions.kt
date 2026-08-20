package com.vitorsousa.stallfit.wear.sensors

import android.Manifest
import android.os.Build

/**
 * API 36 ("Baklava") supersedes [Manifest.permission.BODY_SENSORS] with the Health permission
 * group for heart rate; both are declared in the manifest (BODY_SENSORS capped at maxSdk 35) but
 * only one is meaningful to request at runtime depending on the device's API level.
 */
fun heartRateSensorPermission(): String =
    if (Build.VERSION.SDK_INT >= 36) {
        "android.permission.health.READ_HEART_RATE"
    } else {
        Manifest.permission.BODY_SENSORS
    }

/** Every runtime permission [com.vitorsousa.stallfit.wear.MainActivity] should request up front for sensor tracking to work. */
fun sensorRuntimePermissions(): Array<String> = buildList {
    add(heartRateSensorPermission())
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        add(Manifest.permission.POST_NOTIFICATIONS)
    }
}.toTypedArray()
