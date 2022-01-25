package de.lucas.clockwork_android.ui

import android.content.Context
import android.os.CountDownTimer
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import de.lucas.clockwork_android.model.Issue
import de.lucas.clockwork_android.model.Preferences
import de.lucas.clockwork_android.model.Project

class TogglePlayerViewModel(context: Context) : ViewModel() {
    private val preferences = Preferences(context)
    val toggleTimeDisplay: MutableState<String> = mutableStateOf(getTimeString(0))
    private val isPausedState: MutableState<Boolean> =
        mutableStateOf(preferences.getIsTogglePaused())

    // Sets the current counted time as String in the state variable to display in the TogglePlayer
    fun displayTime(currentTime: Int, count: Int) {
        toggleTimeDisplay.value = getTimeString(currentTime + count)
        preferences.setCurrentToggleTime(currentTime, count)
    }

    fun getCurrentToggleTime() = preferences.getCurrentToggleTime()

    fun resetToggle() = preferences.resetToggle()

    fun setToggle(issue: Issue, project: Project) = preferences.setToggle(issue, project)

    fun getToggleIssue() = preferences.getToggleIssue()

    fun getToggleProject() = preferences.getToggleProject()

    // Sets the systemtime when toggle is started
    fun setStartTime() = preferences.setStartTime((System.currentTimeMillis() / 1000).toInt())

    fun getAppClosedTime(time: Int): Int = time - preferences.getStartTime()

    fun setIsTogglePaused(isPaused: Boolean) = preferences.setIsTogglePaused(isPaused)

    fun getIsTogglePaused() = preferences.getIsTogglePaused()

    fun setPausedTime() = preferences.setPauseTime()

    fun getPausedTime() = preferences.getPauseTime()

    fun setIsPaused(isPaused: Boolean) {
        isPausedState.value = isPaused
    }

    fun getIsPaused() = isPausedState.value

    /*
     calculates the time from milliseconds to hours, minutes and seconds
     calls fun to build time as String
     */
    private fun getTimeString(time: Int): String {
        val hours = time % 86400 / 3600
        val minutes = time % 86400 % 3600 / 60
        val seconds = time % 86400 % 3600 % 60

        return makeTimeString(hours, minutes, seconds)
    }

    // Formats the given time to String format
    private fun makeTimeString(hour: Int, min: Int, sec: Int): String =
        String.format("%02d:%02d:%02d", hour, min, sec)
}

/*
 modified CountDownTimer to count up
 source: https://stackoverflow.com/questions/9276858/how-to-add-a-countup-timer-on-android
 */
abstract class CountUpTimer(private val secondsInFuture: Int, countUpIntervalSeconds: Int) :
    CountDownTimer(secondsInFuture.toLong() * 1000, countUpIntervalSeconds.toLong() * 1000) {

    abstract fun onCount(count: Int)

    override fun onTick(msUntilFinished: Long) {
        onCount(((secondsInFuture.toLong() * 1000 - msUntilFinished) / 1000).toInt())
    }
}