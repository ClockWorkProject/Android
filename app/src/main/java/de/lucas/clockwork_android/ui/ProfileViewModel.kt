package de.lucas.clockwork_android.ui

import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.database.FirebaseDatabase
import de.lucas.clockwork_android.model.Preferences

/**
 * 3 states:
 * showLeaveDialogState -> set true if user clicks "leave"-button in ProfileScreen to show Dialog
 * showDeleteDialogState -> set true if user clicks "delete"-button in ProfileScreen to show Dialog
 * showEditDialogState -> set true if user clicks "edit"-button in ProfileScreen to show Dialog
 */
class ProfileViewModel(context: Context) : ViewModel() {
    private val preferences = Preferences(context)
    private val database = FirebaseDatabase.getInstance()
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

    fun getGroupName() = preferences.getGroupName()

    fun getUsername() = preferences.getUsername()

    fun updateUsername(name: String) {
        database.reference.child("user/${preferences.getUserId()}/username").setValue(name)
        preferences.setUsername(name)
    }
}