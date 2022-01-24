package de.lucas.clockwork_android.ui

import android.content.Context
import androidx.activity.ComponentActivity
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import de.lucas.clockwork_android.model.Preferences
import de.lucas.clockwork_android.model.User
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
    private val database = FirebaseDatabase.getInstance()

    fun setLogin(state: Boolean) {
        loginAttempt.value = state
    }

    private fun setError(state: Boolean) {
        isError.value = state
    }

    private fun setUserInfo(name: String, user_id: String, group_id: String) {
        preferences.setUsername(name)
        preferences.setUserId(user_id)
        preferences.setGroupId(group_id)
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
                        // Get string before '@' to set as initial username
                        val user =
                            User(auth.currentUser!!.uid, email.value.substringBefore("@"), "")
                        // create user in firebase database
                        database.reference.child("user/${auth.currentUser!!.uid}").setValue(user)
                        // Save users email address as username -> can be changed in Profile
                        setUserInfo(email.value.substringBefore("@"), auth.currentUser!!.uid, "")
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

    fun loginUser(
        auth: FirebaseAuth,
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
                        // Sign in success, update UI with the signed-in user's information
                        Timber.e("signInWithEmail:success")
                        // Get group_id from database to check if logged in user is a member of a group
                        var groupId: String
                        database.reference.child("user/${auth.currentUser!!.uid}").get()
                            .addOnSuccessListener {
                                groupId = it.child("groupID").value.toString()
                                setUserInfo(
                                    email.value.substringBefore("@"),
                                    auth.currentUser!!.uid,
                                    groupId
                                )
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
                                // Navigate user to ToggleScreen
                                onLogin(groupId, preferences.getUserRole()!!)
                                setError(false)
                                Timber.e("Got value ${it.child("groupID").value.toString()}")
                            }.addOnFailureListener {
                                Timber.e("Error getting data", it)
                            }
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