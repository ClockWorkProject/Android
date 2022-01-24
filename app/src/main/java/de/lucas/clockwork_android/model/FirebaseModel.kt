package de.lucas.clockwork_android.model

data class User(val id: String, val username: String, val groupID: String)

data class Group(val id: String, val name: String)

data class UserStatistic(val username: String, val totalToggles: List<TotalToggle>)