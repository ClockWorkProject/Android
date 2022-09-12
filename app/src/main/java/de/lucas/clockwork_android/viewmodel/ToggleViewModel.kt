package de.lucas.clockwork_android.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import dagger.hilt.android.lifecycle.HiltViewModel
import de.lucas.clockwork_android.model.Group
import de.lucas.clockwork_android.model.preferences.Preferences
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ToggleViewModel @Inject constructor(
    private val preferences: Preferences,
    private val database: FirebaseDatabase
) : ViewModel() {
    val noGroupFoundState: MutableState<Boolean> = mutableStateOf(false)
    val showToggleList: MutableState<Boolean> = mutableStateOf(false)
    val showEmptyState: MutableState<Boolean> = mutableStateOf(true)

    fun setNoGroupFound(state: Boolean) {
        noGroupFoundState.value = state
    }

    fun setEmptyState(state: Boolean) {
        showEmptyState.value = state
    }

    fun setShowToggleList(state: Boolean) {
        showToggleList.value = state
    }

    private fun setGroupInfo(id: String, name: String) {
        preferences.setGroupId(id)
        preferences.setGroupName(name)
    }

    fun getGroupId() = preferences.getGroupId()

    private fun getUserId() = preferences.getUserId()

    /**
     * Function to create a group, sends data to firebase
     */
    fun createGroup(name: String, createdId: (String) -> Unit) {
        try {
            // Create unique id/key for group
            val groupID = database.reference.child("groups").push().key!!
            val group = Group(groupID, name)
            // Create group in database
            database.reference.child("groups/${groupID}").setValue(group)
            // Set user who created group to admin
            database.reference.child("groups/${groupID}/user/${getUserId()}/role").setValue("admin")
            // Set username
            database.reference.child("groups/${groupID}/user/${getUserId()}/name")
                .setValue(preferences.getUsername())
            // Update groupID in User, so he is member of this group
            database.reference.child("user/${getUserId()}/groupID").setValue(groupID)
            // Set role to "admin"
            preferences.setUserRole("admin")
            // Set group data
            setGroupInfo(groupID, name)
            createdId(groupID)
        } catch (e: Exception) {
            Timber.e("Couldn't create group")
        }
    }

    /**
     * Function to join a group, sends data to firebase
     */
    fun joinGroup(groupID: String, onJoinedSuccessful: (Boolean) -> Unit) {
        database.reference.child("groups/$groupID")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                // Check if group exists in database
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        // Set groupID in user -> groupID, to "join" group
                        database.reference.child("user/${getUserId()}/groupID")
                            .setValue(snapshot.child("id").value.toString())
                        // Set username
                        database.reference.child("groups/${groupID}/user/${getUserId()}/name")
                            .setValue(preferences.getUsername())
                        // Set user who joined group to member
                        if (snapshot.child("user/${getUserId()}/role").exists()) {
                            preferences.setUserRole(snapshot.child("user/${getUserId()}/role").value.toString())
                        } else {
                            // Set user who joined group to member
                            database.reference.child("groups/${groupID}/user/${getUserId()}/role")
                                .setValue("member")
                        }
                        setGroupInfo(
                            snapshot.child("id").value.toString(),
                            snapshot.child("name").value.toString()
                        )
                        Timber.e("Got value ${snapshot.child("id").value.toString()}")
                        // Show ToggleList with all toggles of user
                        showToggleList.value = true
                        // hide empty state message
                        showEmptyState.value = false
                        onJoinedSuccessful(true)
                    } else {
                        // If group doesn't exist -> show SnackBar with info
                        onJoinedSuccessful(false)
                        noGroupFoundState.value = true
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })
    }
}