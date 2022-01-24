package de.lucas.clockwork_android.model

/**
 * data class for the total toggle of a date
 * @param date date of the toggled day
 * @param totalTime combined time of all toggled issue on that day
 * @param toggleList list of all the issue that were toggled on that day
 */
data class TotalToggle(
    val date: String,
    val totalTime: String,
    val toggleList: List<Toggle>
)

/**
 * data class for a single toggle item
 * @param issueName name of the issue that was toggled
 * @param projectName the project, that the issue is from
 * @param issueTime the toggled time of this issue
 */
data class Toggle(
    val issueName: String,
    val projectName: String,
    val issueTime: String
)