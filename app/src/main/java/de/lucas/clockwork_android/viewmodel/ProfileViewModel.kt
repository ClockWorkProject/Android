package de.lucas.clockwork_android.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.database.FirebaseDatabase
import dagger.hilt.android.lifecycle.HiltViewModel
import de.lucas.clockwork_android.model.preferences.Preferences
import timber.log.Timber
import javax.inject.Inject

/**
 * 3 states:
 * showLeaveDialogState -> set true if user clicks "leave"-button in ProfileScreen to show Dialog
 * showDeleteDialogState -> set true if user clicks "delete"-button in ProfileScreen to show Dialog
 * showEditDialogState -> set true if user clicks "edit"-button in ProfileScreen to show Dialog
 */
@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val preferences: Preferences,
    private val database: FirebaseDatabase
) : ViewModel() {
    var showLeaveDialogState: MutableState<Boolean> = mutableStateOf(false)
        private set
    var showDeleteDialogState: MutableState<Boolean> = mutableStateOf(false)
        private set
    var showEditDialogState: MutableState<Boolean> = mutableStateOf(false)
        private set

    fun setLeaveDialog(state: Boolean) {
        showLeaveDialogState.value = state
    }

    fun setDeleteDialog(state: Boolean) {
        showDeleteDialogState.value = state
    }

    fun setEditDialog(state: Boolean) {
        showEditDialogState.value = state
    }

    fun getUsername() = preferences.getUsername()

    fun getGroupId() = preferences.getGroupId()

    fun getGroupName() = preferences.getGroupName()

    /**
     * Get groupName from database
     */
    fun setGroupName() {
        var groupName: String
        // Check if groupName isn't already set. If set this call will not get executed
        if (preferences.getGroupName() == "" && getGroupId() != "") {
            database.reference.child("groups/${getGroupId()}").get()
                .addOnSuccessListener {
                    groupName = it.child("name").value.toString()
                    preferences.setGroupName(groupName)
                    Timber.e("Got value ${it.child("name").value.toString()}")
                }.addOnFailureListener {
                    Timber.e("Error getting data", it)
                }
        }
    }

    // On logout "reset" all necessary preference variables
    fun logOut() {
        preferences.setGroupId("")
        preferences.setGroupName("")
        preferences.setUserRole("member")
        preferences.setUserId("")
        preferences.setUsername("")
        preferences.setProjectId(-1)
    }

    // On leave current group "reset" all necessary preference variables
    fun leaveGroup() {
        database.reference.child("user/${preferences.getUserId()}/groupID").setValue("")
        preferences.setGroupId("")
        preferences.setGroupName("")
        preferences.setUserRole("member")
        preferences.setProjectId(-1)
    }

    // Update the username -> sent to firebase
    fun updateUsername(name: String) {
        database.reference.child("user/${preferences.getUserId()}/username").setValue(name)
        preferences.setUsername(name)
    }
}