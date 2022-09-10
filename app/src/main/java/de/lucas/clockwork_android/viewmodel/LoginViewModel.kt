package de.lucas.clockwork_android.viewmodel

import androidx.activity.ComponentActivity
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import dagger.hilt.android.lifecycle.HiltViewModel
import de.lucas.clockwork_android.model.User
import de.lucas.clockwork_android.model.preferences.Preferences
import timber.log.Timber
import javax.inject.Inject

/**
 * loginAttempt -> set true if user tries to login (LoginButton is being clicked)
 * loginAttempt -> set true if user tries to sign up (SignUpButton is being clicked)
 * isError -> set true if validation of email/password if unsuccessful
 * email -> state to store entered email
 * password -> state to store entered password
 * isLoading -> state to show loading indicator if needed
 */
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val preferences: Preferences,
    private val database: FirebaseDatabase,
    private val auth: FirebaseAuth
) : ViewModel() {
    private val state: MutableState<State> = mutableStateOf(State.DEFAULT)
    private val email: MutableState<String> = mutableStateOf("")
    private val password: MutableState<String> = mutableStateOf("")
    private val isLoading: MutableState<Boolean> = mutableStateOf(false)

    private fun setUserInfo(name: String, user_id: String, group_id: String) {
        preferences.setUsername(name)
        preferences.setUserId(user_id)
        preferences.setGroupId(group_id)
    }

    fun setEmail(text: String) {
        email.value = text
    }

    fun setPassword(text: String) {
        password.value = text
    }

    private fun setIsLoading(state: Boolean) {
        isLoading.value = state
    }

    fun setState(newState: State) {
        state.value = newState
    }

    fun getIsLoading() = isLoading.value

    fun getEmail() = email.value

    fun getPassword() = password.value

    fun getState() = state.value

    /**
     * Function to sign up user
     */
    fun signUpUser(
        context: ComponentActivity,
        onSignUp: () -> Unit
    ) {
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
                        // Get string before '@' to set as initial username
                        val user =
                            User(auth.currentUser!!.uid, email.value.substringBefore("@"), "")
                        // create user in firebase database
                        database.reference.child("user/${auth.currentUser!!.uid}").setValue(user)
                        // Save users email address as username -> can be changed in Profile
                        setUserInfo(email.value.substringBefore("@"), auth.currentUser!!.uid, "")
                        // Navigate user to ToggleScreen
                        onSignUp()
                        setState(State.DEFAULT)
                    } else {
                        // If sign in fails, display a message to the user.
                        Timber.e("createUserWithEmail:failure")
                        setState(State.ERROR)
                    }
                }
        } else {
            setState(State.ERROR)
        }
    }

    /**
     * Function to login user
     */
    fun loginUser(
        context: ComponentActivity,
        onLogin: (String, String) -> Unit
    ) {
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
                        // Sign in success, update UI with the logged in user's information
                        Timber.e("signInWithEmail:success")
                        // Get group_id from database to check if logged in user is a member of a group
                        var groupId: String
                        // Get information of user from database
                        database.reference.child("user/${auth.currentUser!!.uid}").get()
                            .addOnSuccessListener {
                                groupId = it.child("groupID").value.toString()
                                setUserInfo(
                                    email.value.substringBefore("@"),
                                    auth.currentUser!!.uid,
                                    groupId
                                )
                                // If user is in a group -> set role and login
                                if (groupId != "") {
                                    database.reference.child("groups/${groupId}/user/${auth.currentUser!!.uid}")
                                        .get().addOnSuccessListener { role ->
                                            preferences.setUserRole(role.child("role").value.toString())
                                            onLogin(groupId, role.child("role").value.toString())
                                        }
                                    return@addOnSuccessListener
                                } else {
                                    preferences.setUserRole("member")
                                }
                                // Navigate user to ToggleScreen with groupId and his role to call specific functions
                                onLogin(groupId, preferences.getUserRole()!!)
                                Timber.e("Got value ${it.child("groupID").value.toString()}")
                            }.addOnFailureListener {
                                Timber.e("Error getting data", it)
                            }
                        setState(State.DEFAULT)
                    } else {
                        // If sign in fails, display a message to the user.
                        Timber.e("signInWithEmail:failure")
                        setState(State.ERROR)
                    }
                }
        } else {
            setState(State.ERROR)
        }
    }
}

enum class State {
    DEFAULT, LOGIN, SIGNUP, ERROR
}