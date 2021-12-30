package de.lucas.clockwork_android.model

import android.content.Context
import timber.log.Timber

class Preferences(private val context: Context) {

    private fun prefs() = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE)

    fun setCurrentToggle(pausedTime: Int, count: Int) {
        prefs().edit().apply {
            putInt(TIME, pausedTime + count)
        }.apply()
        Timber.e(getCurrentToggleTime().toString())
    }

    fun resetToggleTime() = prefs().edit().putInt(TIME, 0).apply()

    fun getCurrentToggleTime(): Int = prefs().getInt(TIME, 0)

    companion object {
        const val PREFERENCES = "PREFERENCES"
        const val TIME = "TIME"
        const val TOGGLE = "TOGGLE"
    }
}