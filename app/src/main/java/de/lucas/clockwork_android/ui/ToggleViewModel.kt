package de.lucas.clockwork_android.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import com.google.firebase.database.FirebaseDatabase
import de.lucas.clockwork_android.model.Group
import de.lucas.clockwork_android.model.Preferences
import timber.log.Timber

class ToggleViewModel(context: Context) : ViewModel() {
    private val preferences = Preferences(context)
    private val database = FirebaseDatabase.getInstance()

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
            // Update groupID in User, so he is member of this group
            database.reference.child("user/${getUserId()}/groupID").setValue(groupID)
            setGroupInfo(groupID, name)
        } catch (e: Exception) {
            Timber.e("Couldn't create group")
        }
    }
}