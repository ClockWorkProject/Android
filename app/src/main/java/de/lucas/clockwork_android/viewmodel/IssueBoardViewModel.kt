package de.lucas.clockwork_android.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.database.FirebaseDatabase
import dagger.hilt.android.lifecycle.HiltViewModel
import de.lucas.clockwork_android.model.preferences.Preferences
import de.lucas.clockwork_android.ui.BoardState
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class IssueBoardViewModel @Inject constructor(
    private val preferences: Preferences,
    private val database: FirebaseDatabase
) : ViewModel() {
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