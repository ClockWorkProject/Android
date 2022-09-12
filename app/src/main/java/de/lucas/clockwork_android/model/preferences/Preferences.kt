package de.lucas.clockwork_android.model.preferences

import android.content.SharedPreferences
import com.squareup.moshi.Moshi
import de.lucas.clockwork_android.model.Issue
import de.lucas.clockwork_android.model.Project
import de.lucas.clockwork_android.ui.BoardState
import timber.log.Timber
import javax.inject.Inject

class Preferences @Inject constructor(
    private val prefs: SharedPreferences,
    private val moshi: Moshi
) {
    private val issueAdapter = moshi.adapter(Issue::class.java)
    private val projectAdapter = moshi.adapter(Project::class.java)

    // Sets the current toggle time to be able to continue counting after user paused
    fun setCurrentToggleTime(pausedTime: Int, count: Int) {
        prefs.edit().putInt(TIME, pausedTime + count).apply()
        Timber.e(getCurrentToggleTime().toString())
    }

    // Resets the values for the timer, so the user can start a new toggle
    fun resetToggle() {
        prefs.edit().apply {
            putInt(TIME, 0)
            putString(TOGGLE_ISSUE, "")
            putString(TOGGLE_PROJECT, "")
        }.apply()
    }

    fun getCurrentToggleTime(): Int = prefs.getInt(TIME, 0)

    /**
     * Sets the start time of the toggle, for the scenario, that the user closes the app while toggle
     * is active. On app reopen -> can be calculated with current time to get app closed time difference
     */
    fun setStartTime(time: Int) = prefs.edit().putInt(START_TIME, time).apply()

    fun getStartTime(): Int = prefs.getInt(START_TIME, 0)

    // Sets the chosen issue for the activated toggle
    fun setToggle(issue: Issue, project: Project) {
        prefs.edit().putString(TOGGLE_ISSUE, issueAdapter.toJson(issue)).apply()
        prefs.edit().putString(TOGGLE_PROJECT, projectAdapter.toJson(project)).apply()
        Timber.e(getToggleIssue().toString())
    }

    // Returns currently saved toggle. If no toggle saved, return empty Issue
    fun getToggleIssue(): Issue? {
        val json = prefs.getString(TOGGLE_ISSUE, "")
        return if (json!!.isEmpty()) {
            Issue(
                "wqe",
                "",
                "",
                "",
                BoardState.OPEN
            )
        } else {
            issueAdapter.fromJson(json)
        }
    }

    // Returns currently saved toggle. If no toggle saved, return empty Issue
    fun getToggleProject(): Project? {
        val json = prefs.getString(TOGGLE_PROJECT, "")
        return if (json!!.isEmpty()) {
            Project(
                "wqe",
                "",
                listOf()
            )
        } else {
            projectAdapter.fromJson(json)
        }
    }

    // Set boolean true if toggle is currently paused -> otherwise false
    fun setIsTogglePaused(isPaused: Boolean) {
        prefs.edit().putBoolean(TOGGLE_PAUSED, isPaused).apply()
    }

    fun getIsTogglePaused() = prefs.getBoolean(TOGGLE_PAUSED, false)

    // Sets calculated paused time, to later be able to subtract from total time
    fun setPauseTime() {
        prefs.edit().putInt(
            PAUSE_TIME,
            ((System.currentTimeMillis() / 1000).toInt() - getStartTime()) - getCurrentToggleTime()
        ).apply()
    }

    fun getPauseTime() = prefs.getInt(PAUSE_TIME, 0)

    // Set project id to show its issues in IssueBoard and send to backend if necessary action
    fun setProjectId(id: Int) = prefs.edit().putInt(PROJECT_ID, id).apply()

    fun getProjectId() = prefs.getInt(PROJECT_ID, -1)

    // Set group name to show in Profile
    fun setGroupName(name: String) = prefs.edit().putString(GROUP_NAME, name).apply()

    fun getGroupName() = prefs.getString(GROUP_NAME, "")

    // Set Username to show in Profile
    fun setUsername(name: String) = prefs.edit().putString(USER_NAME, name).apply()

    fun getUsername() = prefs.getString(USER_NAME, "")

    // Set Username to show in Profile
    fun setUserId(id: String) = prefs.edit().putString(USER_ID, id).apply()

    fun getUserId() = prefs.getString(USER_ID, "")

    // Set role of user for group
    fun setUserRole(id: String) = prefs.edit().putString(USER_ROLE, id).apply()

    fun getUserRole() = prefs.getString(USER_ROLE, "member")

    // Set group id to send to backend for all actions
    fun setGroupId(id: String) = prefs.edit().putString(GROUP_ID, id).apply()

    fun getGroupId() = prefs.getString(GROUP_ID, "")

    companion object {
        const val TIME = "TIME"
        const val TOGGLE_ISSUE = "TOGGLE_ISSUE"
        const val TOGGLE_PROJECT = "TOGGLE_PROJECT"
        const val START_TIME = "START_TIME"
        const val TOGGLE_PAUSED = "TOGGLE_PAUSED"
        const val PAUSE_TIME = "PAUSE_TIME"
        const val PROJECT_ID = "PROJECT_ID"
        const val USER_NAME = "USER_NAME"
        const val USER_ID = "USER_ID"
        const val USER_ROLE = "USER_ROLE"
        const val GROUP_NAME = "GROUP_NAME"
        const val GROUP_ID = "GROUP_ID"
    }
}