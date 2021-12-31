package de.lucas.clockwork_android.model

import android.content.Context
import com.squareup.moshi.Moshi
import de.lucas.clockwork_android.ui.BoardState
import timber.log.Timber

class Preferences(private val context: Context) {

    private fun prefs() = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE)
    private val moshi = Moshi.Builder().build()
    private val issueAdapter = moshi.adapter(Issue::class.java)

    fun setCurrentToggleTime(pausedTime: Int, count: Int) {
        prefs().edit().putInt(TIME, pausedTime + count).apply()
        Timber.e(getCurrentToggleTime().toString())
    }

    fun resetToggle() {
        prefs().edit().apply {
            putInt(TIME, 0)
            putString(TOGGLE, "")
        }.apply()
    }

    fun getCurrentToggleTime(): Int = prefs().getInt(TIME, 0)

    fun setStartTime(time: Int) = prefs().edit().putInt(START_TIME, time).apply()

    fun getStartTime(): Int = prefs().getInt(START_TIME, 0)

    fun setToggle(issue: Issue) {
        prefs().edit().putString(TOGGLE, issueAdapter.toJson(issue)).apply()
        Timber.e(getToggle().toString())
    }

    fun getToggle(): Issue? {
        val json = prefs().getString(TOGGLE, "")
        return if (json!!.isEmpty()) {
            Issue(
                -1,
                "",
                "",
                "",
                "",
                BoardState.OPEN
            )
        } else {
            issueAdapter.fromJson(json)
        }
    }

    fun setIsTogglePaused(isPaused: Boolean) {
        prefs().edit().putBoolean(TOGGLE_PAUSED, isPaused).apply()
    }

    fun getIsTogglePaused() = prefs().getBoolean(TOGGLE_PAUSED, false)

    fun setPauseTime() {
        prefs().edit().putInt(
            PAUSE_TIME,
            ((System.currentTimeMillis() / 1000).toInt() - getStartTime()) - getCurrentToggleTime()
        ).apply()
    }

    fun getPauseTime() = prefs().getInt(PAUSE_TIME, 0)

    companion object {
        const val PREFERENCES = "PREFERENCES"
        const val TIME = "TIME"
        const val TOGGLE = "TOGGLE"
        const val START_TIME = "START_TIME"
        const val TOGGLE_PAUSED = "TOGGLE_PAUSED"
        const val PAUSE_TIME = "PAUSE_TIME"
    }
}