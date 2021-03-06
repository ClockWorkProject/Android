package de.lucas.clockwork_android.model

import de.lucas.clockwork_android.ui.BoardState

data class Project(val id: String, val project_name: String, val issues: List<Issue>)

data class Issue(
    val id: String,
    val name: String,
    val number: String,
    val description: String,
    val issueState: BoardState
)

/**
 * data class for sending issues to firebase database
 * difference to data class Issue -> issueState must be string here
 */
data class NewIssue(
    val id: String,
    val name: String,
    val number: String,
    val description: String,
    val issueState: String
)