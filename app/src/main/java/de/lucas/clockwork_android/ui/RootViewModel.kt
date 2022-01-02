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

    fun setShowBottomNavigation(state: Boolean) {
        showBottomNavigation.value = state
    }

    fun setShowIssuePicker(state: Boolean) {
        showIssuePickerList.value = state
    }

    fun setAppTitle(text: String) {
        appTitle.value = text
    }

    fun setShowNavigationIcon(state: Boolean) {
        showNavigationIcon.value = state
    }

    fun setShowTogglePlayer(state: Boolean) {
        showTogglePlayer.value = state
    }
}