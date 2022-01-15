package de.lucas.clockwork_android.ui

import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import de.lucas.clockwork_android.model.Preferences

class IssueBoardViewModel(context: Context) : ViewModel() {
    private val preferences = Preferences(context)
    private val projectID: MutableState<Int> = mutableStateOf(preferences.getProjectId())
    private val showBoardStateList: MutableState<Boolean> = mutableStateOf(false)

    // Save project id in preference when project in IssueBoardScreen is being changed
    fun changeProject(id: Int) {
        projectID.value = id
        preferences.setProjectId(id)
    }

    fun getProjectId() = projectID.value

    fun setShowBoardState(showBoardList: Boolean) {
        showBoardStateList.value = showBoardList
    }

    fun getShowBoardState() = showBoardStateList.value
}