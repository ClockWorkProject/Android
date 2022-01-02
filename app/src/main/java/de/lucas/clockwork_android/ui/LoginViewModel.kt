package de.lucas.clockwork_android.ui

import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import de.lucas.clockwork_android.model.Preferences

class LoginViewModel(context: Context) : ViewModel() {
    private val preferences = Preferences(context)
    private val loginAttempt: MutableState<Boolean> = mutableStateOf(false)
    private val signUpAttempt: MutableState<Boolean> = mutableStateOf(false)
    private val isError: MutableState<Boolean> = mutableStateOf(false)

    fun setLogin(state: Boolean) {
        loginAttempt.value = state
    }

    fun setError(state: Boolean) {
        isError.value = state
    }

    fun setUsername(name: String) {
        preferences.setUsername(name)
    }

    fun setSignUp(state: Boolean) {
        signUpAttempt.value = state
    }

    fun getSignUp() = signUpAttempt.value

    fun getLogin() = loginAttempt.value

    fun getIsError() = isError.value
}