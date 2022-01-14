package de.lucas.clockwork_android.ui

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class RootViewModel : ViewModel() {
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
}