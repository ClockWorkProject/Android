package de.lucas.clockwork_android.ui

import android.content.Context
import androidx.activity.ComponentActivity
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import de.lucas.clockwork_android.model.Preferences
import timber.log.Timber

/**
 * 3 states:
 * loginAttempt -> set true if user tries to login (LoginButton is being clicked)
 * loginAttempt -> set true if user tries to sign up (SignUpButton is being clicked)
 * isError -> set true if validation of email/password if unsuccessful
 */
class LoginViewModel(context: Context) : ViewModel() {
    private val preferences = Preferences(context)
    private val loginAttempt: MutableState<Boolean> = mutableStateOf(false)
    private val signUpAttempt: MutableState<Boolean> = mutableStateOf(false)
    private val isError: MutableState<Boolean> = mutableStateOf(false)
    private val email: MutableState<String> = mutableStateOf("")
    private val password: MutableState<String> = mutableStateOf("")
    private val isLoading: MutableState<Boolean> = mutableStateOf(false)

    fun setLogin(state: Boolean) {
        loginAttempt.value = state
    }

    private fun setError(state: Boolean) {
        isError.value = state
    }

    fun setUsername(name: String) {
        preferences.setUsername(name)
    }

    fun setSignUp(state: Boolean) {
        signUpAttempt.value = state
    }

    fun getSignUp() = signUpAttempt.value

    fun setEmail(text: String) {
        email.value = text
    }

    fun setPassword(text: String) {
        password.value = text
    }

    fun setIsLoading(state: Boolean) {
        isLoading.value = state
    }

    fun getIsLoading() = isLoading.value

    fun getEmail() = email.value

    fun getPassword() = password.value

    fun getLogin() = loginAttempt.value

    fun getIsError() = isError.value

    fun signUpUser(auth: FirebaseAuth, context: ComponentActivity, onSignUp: () -> Unit) {
        if (email.value.isNotEmpty() && password.value.isNotEmpty()) {
            setIsLoading(true)
            auth.createUserWithEmailAndPassword(email.value.trim(), password.value.trim())
                .addOnCompleteListener(context) { task ->
                    setIsLoading(false)
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Timber.e("createUserWithEmail:success")
                        val user = auth.currentUser
                        setUsername(user!!.displayName ?: email.value)
                        onSignUp()
                        setError(false)
                    } else {
                        // If sign in fails, display a message to the user.
                        Timber.e("createUserWithEmail:failure")
                        setError(true)
                    }
                }
        } else {
            setError(true)
        }
    }

    fun loginUser(auth: FirebaseAuth, context: ComponentActivity, onLogin: () -> Unit) {
        if (email.value.isNotEmpty() && password.value.isNotEmpty()) {
            setIsLoading(true)
            auth.signInWithEmailAndPassword(email.value.trim(), password.value.trim())
                .addOnCompleteListener(context) { task ->
                    setIsLoading(false)
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Timber.e("signInWithEmail:success")
                        val user = auth.currentUser
                        setUsername(user!!.displayName ?: email.value)
                        onLogin()
                        setError(false)
                    } else {
                        // If sign in fails, display a message to the user.
                        Timber.e("signInWithEmail:failure")
                        setError(true)
                    }
                }
        } else {
            setError(true)
        }
    }
}