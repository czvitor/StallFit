package com.vitorsousa.stallfit.data.wear

/**
 * Wire paths for the watch<->phone protocol (Wearable [com.google.android.gms.wearable.MessageClient]).
 * Mirrored by hand in the `:wear` module — see that module's own `WearMessagePaths.kt`.
 */
object WearMessagePaths {
    const val GET_CATALOG = "/stallfit/get_catalog"
    const val CATALOG = "/stallfit/catalog"

    const val START_SESSION = "/stallfit/start_session"
    const val SESSION_STARTED = "/stallfit/session_started"

    const val LOG_SET = "/stallfit/log_set"
    const val SET_LOGGED = "/stallfit/set_logged"

    const val FINISH_SESSION = "/stallfit/finish_session"
    const val SESSION_FINISHED = "/stallfit/session_finished"
}
