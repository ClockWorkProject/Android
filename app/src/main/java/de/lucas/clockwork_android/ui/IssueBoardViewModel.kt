package de.lucas.clockwork_android.ui

import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import de.lucas.clockwork_android.model.Preferences

class IssueBoardViewModel(context: Context) : ViewModel() {
    private val preferences = Preferences(context)
    private val projectID: MutableState<Int> = mutableStateOf(preferences.getProjectId())

    fun changeProject(id: Int) {
        projectID.value = id
        preferences.setProjectId(id)
    }

    fun getProjectId() = projectID.value
}