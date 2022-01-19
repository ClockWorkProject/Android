package de.lucas.clockwork_android.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import de.lucas.clockwork_android.model.Preferences

class ToggleViewModel(context: Context) : ViewModel() {
    private val preferences = Preferences(context)

    /*
    TODO SharedPreferences to check current group id (if -1 (user in no group) -> empty state)
     */
    fun getGroupId() = preferences.getGroupId()

    fun setGroupId(id: String) = preferences.setGroupId(id)
}