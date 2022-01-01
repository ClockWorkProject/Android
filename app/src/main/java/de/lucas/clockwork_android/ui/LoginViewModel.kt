package de.lucas.clockwork_android.ui

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class LoginViewModel : ViewModel() {
    val loginAttempt: MutableState<Boolean> = mutableStateOf(false)
    val isError: MutableState<Boolean> = mutableStateOf(false)

    fun setLogin(state: Boolean) {
        loginAttempt.value = state
    }

    fun setError(state: Boolean) {
        isError.value = state
    }

    fun getLogin() = loginAttempt.value

    fun getIsError() = isError.value
}