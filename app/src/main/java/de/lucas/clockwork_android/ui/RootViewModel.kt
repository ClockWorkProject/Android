package de.lucas.clockwork_android.ui

import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import de.lucas.clockwork_android.model.Issue
import de.lucas.clockwork_android.model.Preferences
import de.lucas.clockwork_android.model.Project
import timber.log.Timber

class RootViewModel(context: Context) : ViewModel() {
    private val preferences = Preferences(context)
    private val database = FirebaseDatabase.getInstance()
    var showBottomNavigation: MutableState<Boolean> = mutableStateOf(false)
        private set

    var showIssuePickerList: MutableState<Boolean> = mutableStateOf(false)
        private set

    var appTitle: MutableState<String> = mutableStateOf("")
        private set

    var showNavigationIcon: MutableState<Boolean> = mutableStateOf(false)
        private set

    var showTogglePlayer: MutableState<Boolean> = mutableStateOf(false)
        private set

    // State to show or hide the Bottom Navigation Bar
    fun setShowBottomNavigation(state: Boolean) {
        showBottomNavigation.value = state
    }

    // State to show or hide the IssuePickerList
    fun setShowIssuePicker(state: Boolean) {
        showIssuePickerList.value = state
    }

    // State to change the title of the TopBar on screen change
    fun setAppTitle(text: String) {
        appTitle.value = text
    }

    // State to show or hide the Navigation Icon (only needed on specific screens)
    fun setShowNavigationIcon(state: Boolean) {
        showNavigationIcon.value = state
    }

    // State to show or hide the TogglePlayer
    fun setShowTogglePlayer(state: Boolean) {
        showTogglePlayer.value = state
    }

    fun getGroupId() = preferences.getGroupId()

    fun getAllProjects(): List<Project> {
        val projectList = mutableListOf<Project>()
        if (getGroupId() != "") {
            database.reference.child("groups/${preferences.getGroupId()}/projects")
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        projectList.clear()
                        snapshot.children.forEach { project ->
                            val issues = mutableListOf<Issue>()
                            project.child("issue").children.forEach { issue ->
                                issues.add(
                                    Issue(
                                        issue.child("id").value.toString(),
                                        issue.child("name").value.toString(),
                                        issue.child("number").value.toString(),
                                        issue.child("description").value.toString(),
                                        setIssueState(issue.child("issueState").value.toString()),
                                    )
                                )
                            }
                            projectList.add(
                                Project(
                                    project.key!!,
                                    project.child("name").value.toString(),
                                    issues
                                )
                            )
                        }
                        Timber.e(projectList.toString())
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }

                })
        } else {
            projectList.clear()
        }
        return projectList
    }

    private fun setIssueState(stateString: String): BoardState {
        return when (stateString) {
            "open" -> BoardState.OPEN
            "todo" -> BoardState.TODO
            "doing" -> BoardState.DOING
            "blocker" -> BoardState.BLOCKER
            "review" -> BoardState.REVIEW
            else -> BoardState.CLOSED
        }
    }
}