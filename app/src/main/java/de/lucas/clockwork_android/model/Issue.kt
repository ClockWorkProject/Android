package de.lucas.clockwork_android.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class Project(val project_name: String, val issues: List<Issue>)

@Parcelize
data class Issue(
    val number: Int,
    val title: String,
    val project_name: String,
    val description: String,
    val created_by: String
) : Parcelable