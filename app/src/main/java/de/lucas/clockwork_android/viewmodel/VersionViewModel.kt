package de.lucas.clockwork_android.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
internal class VersionViewModel @Inject constructor(private val app: Application) :
    AndroidViewModel(app) {

    // Gets the set application name
    val appName = app.packageManager.getApplicationLabel(app.applicationInfo).toString()

    // Gets the currently set versionName/versionNumber of the app
    fun getCurrentVersion(): String {
        val manager = app.applicationContext.packageManager
        val info = manager.getPackageInfo(app.applicationContext.packageName, 0)
        return info.versionName
    }
}

