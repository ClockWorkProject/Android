package de.lucas.clockwork_android.ui

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class TogglePlayerViewModel : ViewModel() {
    private var _togglePlayerState = MutableStateFlow(false)
    val togglePlayerState: StateFlow<Boolean> = _togglePlayerState

    fun stopTogglePlayer() {
        _togglePlayerState.value = false
    }

    fun startTogglePlayer() {
        _togglePlayerState.value = true
    }
}