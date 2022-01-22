package de.lucas.clockwork_android.ui

import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import de.lucas.clockwork_android.model.Group
import de.lucas.clockwork_android.model.Preferences
import timber.log.Timber

class ToggleViewModel(context: Context) : ViewModel() {
    private val preferences = Preferences(context)
    private val database = FirebaseDatabase.getInstance()
    val noGroupFoundState: MutableState<Boolean> = mutableStateOf(false)
    val showToggleList: MutableState<Boolean> = mutableStateOf(false)
    val showEmptyState: MutableState<Boolean> = mutableStateOf(true)

    fun setNoGroupFound(state: Boolean) {
        noGroupFoundState.value = state
    }

    fun getGroupId() = preferences.getGroupId()

    private fun setGroupInfo(id: String, name: String) {
        preferences.setGroupId(id)
        preferences.setGroupName(name)
    }

    private fun getUserId() = preferences.getUserId()

    fun createGroup(name: String) {
        try {
            val groupID = database.reference.child("groups").push().key!!
            val group = Group(groupID, name)
            // Create group in database
            database.reference.child("groups/${groupID}").setValue(group)
            // Set user who created group to admin
            database.reference.child("groups/${groupID}/user/${getUserId()}/role").setValue("admin")
            // Set username
            database.reference.child("groups/${groupID}/user/${getUserId()}/username")
                .setValue(preferences.getUsername())
            // Update groupID in User, so he is member of this group
            database.reference.child("user/${getUserId()}/groupID").setValue(groupID)
            setGroupInfo(groupID, name)
        } catch (e: Exception) {
            Timber.e("Couldn't create group")
        }
    }

    fun joinGroup(groupID: String) {
        database.reference.child("groups/$groupID")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        database.reference.child("user/${getUserId()}/groupID")
                            .setValue(snapshot.child("id").value.toString())
                        // Set username
                        database.reference.child("groups/${groupID}/user/${getUserId()}/username")
                            .setValue(preferences.getUsername())
                        // Set user who joined group to member
                        database.reference.child("groups/${groupID}/user/${getUserId()}/role")
                            .setValue("member")
                        setGroupInfo(
                            snapshot.child("id").value.toString(),
                            snapshot.child("name").value.toString()
                        )
                        Timber.e("Got value ${snapshot.child("id").value.toString()}")
                        showToggleList.value = true
                        showEmptyState.value = false
                    } else {
                        noGroupFoundState.value = true
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })
    }
}