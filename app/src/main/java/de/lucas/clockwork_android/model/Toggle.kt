package de.lucas.clockwork_android.model

data class TotalToggle(
    val date: String,
    val total_time: String,
    val toggle_list: List<Toggle>
)

data class Toggle(
    val issue: String,
    val project: String,
    val time: String
)