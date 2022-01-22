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
    val projectList: MutableList<Project> = mutableListOf()
    private val isLoading: MutableState<Boolean> = mutableStateOf(false)

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

    fun setProjectIndex(index: Int) = preferences.setProjectId(index)

    fun getIsLoading() = isLoading.value

    fun getAllProjects(groupId: String) {
        isLoading.value = true
        if (groupId != "") {
            database.reference.child("groups/$groupId/projects")
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        projectList.clear()
                        snapshot.children.forEach { project ->
                            val issues = mutableListOf<Issue>()
                            project.child("issues").children.forEach { issue ->
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
                        isLoading.value = false
                        Timber.e(projectList.toString())
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Timber.e("IAAAMMMM CANCCEEELLLEEEDDD")
                    }
                })
        } else {
            projectList.clear()
            isLoading.value = false
        }
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