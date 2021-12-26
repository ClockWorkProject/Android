package de.lucas.clockwork_android.ui.info

import android.app.Application
import androidx.lifecycle.AndroidViewModel

internal class VersionViewModel(private val app: Application) : AndroidViewModel(app) {

    val appName = app.packageManager.getApplicationLabel(app.applicationInfo).toString()

    fun getCurrentVersion(): String {
        val manager = app.applicationContext.packageManager
        val info = manager.getPackageInfo(app.applicationContext.packageName, 0)
        return info.versionName
    }
}

