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
 * email -> state to store entered email
 * password -> state to store entered password
 * isLoading -> state to show loading indicator if needed
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

    private fun setUsername(name: String) {
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

    private fun setIsLoading(state: Boolean) {
        isLoading.value = state
    }

    fun getIsLoading() = isLoading.value

    fun getEmail() = email.value

    fun getPassword() = password.value

    fun getLogin() = loginAttempt.value

    fun getIsError() = isError.value

    fun signUpUser(auth: FirebaseAuth, context: ComponentActivity, onSignUp: () -> Unit) {
        // Check if Textfields are not empty locally
        if (email.value.isNotEmpty() && password.value.isNotEmpty()) {
            // Show loading Indicator
            setIsLoading(true)
            // Firebase call to create a user with provided email and password
            auth.createUserWithEmailAndPassword(email.value.trim(), password.value.trim())
                .addOnCompleteListener(context) { task ->
                    // disable loading indicator
                    setIsLoading(false)
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Timber.e("createUserWithEmail:success")
                        val user = auth.currentUser
                        // Save users email address as username -> can be changed in Profile
                        setUsername(user!!.displayName ?: email.value)
                        // Navigate user to ToggleScreen
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
        // Check if Textfields are not empty locally
        if (email.value.isNotEmpty() && password.value.isNotEmpty()) {
            // Show loading Indicator
            setIsLoading(true)
            // Firebase call to sign in user with provided email and password
            auth.signInWithEmailAndPassword(email.value.trim(), password.value.trim())
                .addOnCompleteListener(context) { task ->
                    // disable loading indicator
                    setIsLoading(false)
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Timber.e("signInWithEmail:success")
                        // Navigate user to ToggleScreen
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