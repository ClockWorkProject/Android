package de.lucas.clockwork_android

import android.app.Application
import timber.log.Timber

class Application : Application() {
    override fun onCreate() {
        super.onCreate()
        // activate timber
        Timber.plant(Timber.DebugTree())
    }
}