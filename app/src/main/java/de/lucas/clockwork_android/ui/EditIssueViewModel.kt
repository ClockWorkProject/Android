package de.lucas.clockwork_android.ui

import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.database.FirebaseDatabase
import de.lucas.clockwork_android.model.NewIssue
import de.lucas.clockwork_android.model.Preferences
import timber.log.Timber

class EditIssueViewModel(context: Context) : ViewModel() {
    private val preferences = Preferences(context)
    private val database = FirebaseDatabase.getInstance()
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

    fun createIssue(
        projectID: String,
        number: String,
        title: String,
        description: String,
        state: BoardState
    ) {
        try {
            if (title.isEmpty()) {
                setIsError(true)
            } else {
                setIsError(false)
                val issueID =
                    database.reference.child("groups/${preferences.getGroupId()}/projects(${projectID}/issues")
                        .push().key!!
                val issue = NewIssue(
                    issueID,
                    title,
                    number,
                    description,
                    state.name.lowercase()
                )
                // Create group in database
                database.reference.child("groups/${preferences.getGroupId()}/projects/${projectID}/issues/${issueID}")
                    .setValue(issue)
            }
        } catch (e: Exception) {
            Timber.e("Couldn't create issue")
        }
    }

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