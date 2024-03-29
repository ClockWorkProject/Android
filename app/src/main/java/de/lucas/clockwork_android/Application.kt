package de.lucas.clockwork_android

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class Application : Application() {
    override fun onCreate() {
        super.onCreate()
        // activate timber
        Timber.plant(Timber.DebugTree())
    }
}