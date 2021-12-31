package de.lucas.clockwork_android.ui

import android.content.Context
import android.os.CountDownTimer
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import de.lucas.clockwork_android.model.Issue
import de.lucas.clockwork_android.model.Preferences

class TogglePlayerViewModel(context: Context) : ViewModel() {
    private val preferences = Preferences(context)
    val toggleTimeDisplay: MutableState<String> = mutableStateOf(getTimeString(0))

    fun displayTime(currentTime: Int, count: Int) {
        toggleTimeDisplay.value = getTimeString(currentTime + count)
        preferences.setCurrentToggleTime(currentTime, count)
    }

    fun getCurrentToggleTime() = preferences.getCurrentToggleTime()

    fun resetToggle() = preferences.resetToggle()

    fun setToggle(issue: Issue) = preferences.setToggle(issue)

    fun getToggle() = preferences.getToggle()

    fun setStartTime() = preferences.setStartTime((System.currentTimeMillis() / 1000).toInt())

    fun getAppClosedTime(time: Int): Int = time - preferences.getStartTime()

    fun setIsTogglePaused(isPaused: Boolean) = preferences.setIsTogglePaused(isPaused)

    fun getIsTogglePaused() = preferences.getIsTogglePaused()

    fun setPausedTime() = preferences.setPauseTime()

    fun getPausedTime() = preferences.getPauseTime()

    private fun getTimeString(time: Int): String {
        val hours = time % 86400 / 3600
        val minutes = time % 86400 % 3600 / 60
        val seconds = time % 86400 % 3600 % 60

        return makeTimeString(hours, minutes, seconds)
    }

    private fun makeTimeString(hour: Int, min: Int, sec: Int): String =
        String.format("%02d:%02d:%02d", hour, min, sec)
}

abstract class CountUpTimer(private val secondsInFuture: Int, countUpIntervalSeconds: Int) :
    CountDownTimer(secondsInFuture.toLong() * 1000, countUpIntervalSeconds.toLong() * 1000) {

    abstract fun onCount(count: Int)

    override fun onTick(msUntilFinished: Long) {
        onCount(((secondsInFuture.toLong() * 1000 - msUntilFinished) / 1000).toInt())
    }
}