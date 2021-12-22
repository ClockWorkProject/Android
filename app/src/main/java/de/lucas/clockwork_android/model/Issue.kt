package de.lucas.clockwork_android.model

data class ProjectIssues(val project_name: String, val issues: List<Issue>)

data class Issue(
    val number: Int,
    val title: String,
    val description: String,
    val created_by: String
)