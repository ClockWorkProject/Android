package de.lucas.clockwork_android.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.google.firebase.database.FirebaseDatabase
import de.lucas.clockwork_android.model.Preferences
import timber.log.Timber

class IssuePickerListViewModel(context: Context) : ViewModel() {
    private val preferences = Preferences(context)
    private val database = FirebaseDatabase.getInstance()

    fun getGroupID() = preferences.getGroupId()

    /**
     * Function to create a project -> send to firebase
     * @param name only a name for the project is needed
     */
    fun createProject(name: String) {
        try {
            if (preferences.getGroupId() != "") {
                // Create unique id/key for project
                val projectID =
                    database.reference.child("groups/${preferences.getGroupId()}/projects")
                        .push().key!!
                // Set Id for project
                database.reference.child("groups/${preferences.getGroupId()}/projects/${projectID}/id")
                    .setValue(projectID)
                // Create new project in database
                database.reference.child("groups/${preferences.getGroupId()}/projects/${projectID}/name")
                    .setValue(name)
            }
        } catch (e: Exception) {
            Timber.e("Couldn't create new project")
        }
    }
}