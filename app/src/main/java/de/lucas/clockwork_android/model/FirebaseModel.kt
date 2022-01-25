package de.lucas.clockwork_android.model

data class User(val id: String, val username: String, val groupID: String)

data class Group(val id: String, val name: String)

/**
 * @param totalToggles list of all toggles (with all it's info) for the different days of a user
 */
data class UserStatistic(val username: String, val totalToggles: List<TotalToggle>)