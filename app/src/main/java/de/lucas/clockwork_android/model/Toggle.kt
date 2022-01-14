package de.lucas.clockwork_android.model

/**
 * data class for the total toggle of a date
 * @param date date of the toggled day
 * @param total_time combined time of all toggled issue on that day
 * @param toggle_list list of all the issue that were toggled on that day
 */
data class TotalToggle(
    val date: String,
    val total_time: String,
    val toggle_list: List<Toggle>
)

/**
 * data class for a single toggle item
 * @param issue name of the issue that was toggled
 * @param project the project, that the issue is from
 * @param time the toggled time of this issue
 */
data class Toggle(
    val issue: String,
    val project: String,
    val time: String
)