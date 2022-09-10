package de.lucas.clockwork_android.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.database.FirebaseDatabase
import dagger.hilt.android.lifecycle.HiltViewModel
import de.lucas.clockwork_android.model.NewIssue
import de.lucas.clockwork_android.model.preferences.Preferences
import de.lucas.clockwork_android.ui.BoardState
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class EditIssueViewModel @Inject constructor(
    private val preferences: Preferences,
    private val database: FirebaseDatabase
) : ViewModel() {
    private val editBoardState: MutableState<BoardState> = mutableStateOf(BoardState.OPEN)
    private val projectID: MutableState<String> = mutableStateOf("")
    private val isError: MutableState<Boolean> = mutableStateOf(false)

    private fun setIsError(state: Boolean) {
        isError.value = state
    }

    fun getIsError() = isError.value

    fun setProjectID(id: String) {
        projectID.value = id
    }

    fun getProjectID() = projectID.value

    fun setEditBoardState(state: BoardState) {
        editBoardState.value = state
    }

    fun getEditBoardState() = editBoardState.value

    /**
     * Function to create new issue and send to firebase
     * @param projectID id of the selected project in IssueBoard, where new issue is being created
     * @param number issue number of created issue
     * @param state state of issue (e.g. "open") where new issue is being created
     */
    fun createIssue(
        projectID: String,
        number: String,
        title: String,
        description: String,
        state: BoardState
    ) {
        try {
            // Check if title is empty -> show error message
            if (title.isEmpty()) {
                setIsError(true)
            } else {
                setIsError(false)
                // Get id of issue from firebase database
                val issueID =
                    database.reference.child("groups/${preferences.getGroupId()}/projects(${projectID}/issues")
                        .push().key!!
                // Create NewIssue object to send to firebase
                val issue = NewIssue(
                    issueID,
                    title,
                    number,
                    description,
                    state.name.lowercase()
                )
                // Create issue with provided NewIssue object, which hold all necessary data, in database
                database.reference.child("groups/${preferences.getGroupId()}/projects/${projectID}/issues/${issueID}")
                    .setValue(issue)
            }
        } catch (e: Exception) {
            Timber.e("Couldn't create issue")
        }
    }

    /**
     * Function to update clicked issue
     */
    fun updateIssue(projectID: String, issueID: String, title: String, description: String) {
        try {
            if (projectID.isNotEmpty()) {
                if (title.isEmpty()) {
                    setIsError(true)
                } else {
                    setIsError(false)
                    // Update title of issue
                    database.reference.child("groups/${preferences.getGroupId()}/projects/${projectID}/issues/${issueID}/name")
                        .setValue(title)
                    // Update description of issue
                    database.reference.child("groups/${preferences.getGroupId()}/projects/${projectID}/issues/${issueID}/description")
                        .setValue(description)
                }
            }
        } catch (e: Exception) {
            Timber.e("Couldn't update issue")
        }
    }
}