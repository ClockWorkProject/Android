package de.lucas.clockwork_android.model

import de.lucas.clockwork_android.ui.BoardState

data class Project(val project_name: String, val issues: List<Issue>)

data class Issue(
    val number: Int,
    val title: String,
    val project_name: String,
    val description: String,
    val created_by: String,
    var board_state: BoardState
)