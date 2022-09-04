package de.lucas.clockwork_android.viewmodel

import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.database.FirebaseDatabase
import de.lucas.clockwork_android.model.Preferences
import de.lucas.clockwork_android.ui.BoardState
import timber.log.Timber

class IssueBoardViewModel(context: Context) : ViewModel() {
    private val preferences = Preferences(context)
    private val database = FirebaseDatabase.getInstance()
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

    /**
     * Send updated state of issue to database
     */
    fun updateIssueState(projectID: String, issueID: String, state: BoardState) {
        try {
            if (projectID.isNotEmpty()) {
                // Update state of issue
                database.reference.child("groups/${preferences.getGroupId()}/projects/${projectID}/issues/${issueID}/issueState")
                    .setValue(state.name.lowercase())
            }
        } catch (e: Exception) {
            Timber.e("Couldn't update issue")
        }
    }
}