package de.lucas.clockwork_android.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.moshi.Moshi
import dagger.hilt.android.lifecycle.HiltViewModel
import de.lucas.clockwork_android.model.*
import de.lucas.clockwork_android.model.preferences.Preferences
import de.lucas.clockwork_android.ui.BoardState
import timber.log.Timber
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import javax.inject.Inject
import kotlin.math.roundToInt

@HiltViewModel
class RootViewModel @Inject constructor(
    private val preferences: Preferences,
    private val database: FirebaseDatabase,
    private val moshi: Moshi,
    private val auth: FirebaseAuth
) : ViewModel() {
    var projectList by mutableStateOf(listOf<Project>())
    var toggleList by mutableStateOf(listOf<TotalToggle>())
    var memberList by mutableStateOf(listOf<UserStatistic>())
    val isLoading: MutableState<Boolean> = mutableStateOf(false)
    private val totalTime: MutableState<Double> = mutableStateOf(0.0)

    // Listeners for the ValueEventListeners of firebase
    private var toggleListener: ValueEventListener? = null
    private var projectListener: ValueEventListener? = null
    private var memberListener: ValueEventListener? = null

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

    fun currentUser() = auth.currentUser

    fun signOut() = auth.signOut()

    // Get sort list by dates of current user
    fun getSortedToggles(): List<TotalToggle> {
        val dateTimeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")

        return toggleList.sortedByDescending { toggle ->
            LocalDate.parse(toggle.date, dateTimeFormatter)
        }
    }

    // Get sorted list of other member
    fun getSortedToggles(list: List<TotalToggle>): List<TotalToggle> {
        val dateTimeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")

        return list.sortedByDescending { toggle ->
            LocalDate.parse(toggle.date, dateTimeFormatter)
        }
    }

    // Empty all lists, to then add new items
    fun removeAll() {
        toggleList = listOf()
        projectList = listOf()
        memberList = listOf()
    }

    // Empty all lists and remove listeners
    fun removeAllListeners() {
        removeAll()
        database.reference.removeEventListener(toggleListener!!)
        database.reference.removeEventListener(projectListener!!)
        if (memberListener != null) {
            database.reference.removeEventListener(memberListener!!)
        }
    }

    fun getGroupId() = preferences.getGroupId()

    fun setProjectIndex(index: Int) = preferences.setProjectId(index)

    fun getIsLoading() = isLoading.value

    fun getUserId() = preferences.getUserId()

    fun getUserRole() = preferences.getUserRole()

    // Get current date
    private fun currentDate(): String =
        SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())

    /**
     * Get all projects and its issue from specific group (from groupID)
     */
    fun getProjectData(groupId: String) {
        // Show loading indicator
        isLoading.value = true
        // Check if group exists
        if (groupId != "") {
            // Listener on projects child in database
            projectListener = database.reference.child("groups/$groupId/projects")
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        projectList = listOf()
                        // Get issues for every project child
                        snapshot.children.forEach { project ->
                            val issues = mutableListOf<Issue>()
                            project.child("issues").children.forEach { issue ->
                                issues.add(
                                    Issue(
                                        issue.child("id").value.toString(),
                                        issue.child("name").value.toString(),
                                        issue.child("number").value.toString(),
                                        issue.child("description").value.toString(),
                                        setIssueState(issue.child("issueState").value.toString()),
                                    )
                                )
                            }
                            // Add project with its issues to global projectList variable
                            projectList = projectList + listOf(
                                Project(
                                    project.key!!,
                                    project.child("name").value.toString(),
                                    issues
                                )
                            )
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Timber.e("getting projects data canceled")
                    }
                })
        }
    }

    /**
     * Save closed/finished toggle -> send total counted time to firebase
     */
    fun saveToggle(time: String, issue: Issue, project: Project) {
        try {
            // Get the total time of an toggle if its exist, to add the new time
            getTotalTime()
            val date = currentDate()
            val toggle = Toggle(issue.name, project.project_name, time.toDouble().toString())
            database.reference.child("groups/${getGroupId()}/user/${getUserId()}/dates/$date/issues/${issue.id}")
                .get().addOnSuccessListener {
                    // Check if issueId exists -> add toggle time to already set time
                    if (it.exists()) {
                        database.reference.child("groups/${getGroupId()}/user/${getUserId()}/dates/$date/issues/${issue.id}/issueTime")
                            .setValue(
                                (it.child("issueTime").value.toString()
                                    .toDouble() + time.toDouble()).toString()
                            )
                        database.reference.child("groups/${getGroupId()}/user/${getUserId()}/dates/$date/totalTime")
                            .setValue((totalTime.value + time.toDouble()).toString())
                    } else {
                        // Create date for toggles in database
                        database.reference.child("groups/${getGroupId()}/user/${getUserId()}/dates/$date")
                            .get().addOnSuccessListener {
                                // Set toggled issue
                                database.reference.child("groups/${getGroupId()}/user/${getUserId()}/dates/$date/issues/${issue.id}")
                                    .setValue(toggle)
                                // update total toggle time
                                database.reference.child("groups/${getGroupId()}/user/${getUserId()}/dates/$date/totalTime")
                                    .setValue((totalTime.value + time.toDouble()).toString())
                            }
                    }
                }
        } catch (e: Exception) {
            Timber.e("Couldn't save toggle")
        }
    }

    /**
     * Get all toggles of user from group
     */
    fun getAllToggles(groupID: String, userID: String) {
        // Check if user is in a group
        if (groupID != "") {
            toggleListener = database.reference.child("groups/$groupID/user/$userID/dates")
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        toggleList = listOf()
                        // Get all toggles with its date -> reverse list to have newest date at top
                        snapshot.children.reversed().forEach { toggle ->
                            val totalTime = mutableStateOf("")
                            val issues = mutableListOf<Toggle>()
                            toggle.child("issues").children.forEach { issue ->
                                issues.add(
                                    Toggle(
                                        issue.child("issueName").value.toString(),
                                        issue.child("projectName").value.toString(),
                                        issue.child("issueTime").value.toString().toDouble()
                                            .roundDoubleToString(false)
                                    )
                                )
                            }
                            // Get totalTime if exists
                            if (toggle.child("totalTime").exists()) {
                                totalTime.value =
                                    toggle.child("totalTime").value.toString()
                                        .toDouble()
                                        .roundDoubleToString(true)
                            }
                            // Add toggle with its issues and time to global toggleList variable
                            toggleList = toggleList + listOf(
                                TotalToggle(
                                    toggle.key!!.replace("-", "."),
                                    totalTime.value,
                                    issues
                                )
                            )
                        }
                        Timber.e(toggleList.toString())
                        isLoading.value = false
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Timber.e("getting toggles canceled")
                    }
                })
        }
    }

    /**
     * Get all members from the group
     */
    fun getAllMembers(groupID: String) {
        memberListener = database.reference.child("groups/$groupID/user")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    memberList = listOf()
                    // For all member child get its toggles
                    snapshot.children.forEach { member ->
                        val username = mutableStateOf("")
                        val totalToggleList = mutableListOf<TotalToggle>()
                        var totalTime = "0.0"
                        username.value = member.child("name").value.toString()
                        member.child("dates").children.forEach { date ->
                            val issues = mutableListOf<Toggle>()
                            date.child("issues").children.forEach { issue ->
                                issues.add(
                                    Toggle(
                                        issue.child("issueName").value.toString(),
                                        issue.child("projectName").value.toString(),
                                        issue.child("issueTime").value.toString().toDouble()
                                            .roundDoubleToString(false)
                                    )
                                )
                            }
                            if (date.child("totalTime").exists()) {
                                totalTime = date.child("totalTime").value.toString()
                                    .toDouble()
                                    .roundDoubleToString(true)
                            }
                            totalToggleList.add(
                                TotalToggle(
                                    date.key!!.replace("-", "."),
                                    totalTime,
                                    issues
                                )
                            )
                        }
                        // Add member with its toggles to global memberList variable
                        memberList = memberList + listOf(
                            UserStatistic(
                                username.value,
                                getSortedToggles(totalToggleList)
                            )
                        )
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Timber.e("IAAAMMMM CANCCEEELLLEEEDDD")
                }
            })
    }

    /**
     * Get the totalTime of a date from the user
     * If exists set time from database, else set 0.0
     */
    private fun getTotalTime() {
        try {
            val date = currentDate()
            database.reference.child("groups/${getGroupId()}/user/${getUserId()}/dates/$date/totalTime")
                .get().addOnSuccessListener {
                    if (it.exists()) {
                        totalTime.value = it.value.toString().toDouble()
                    } else {
                        totalTime.value = 0.0
                    }
                    return@addOnSuccessListener
                }.addOnFailureListener {
                    totalTime.value = 0.0
                }
        } catch (e: Exception) {
            Timber.e("Couldn't get toggle time")
        }
    }

    fun Double.roundDoubleToString(isTotalTime: Boolean): String =
        getTimeString(roundToInt(), isTotalTime)

    /*
 calculates the time from milliseconds to hours, minutes and seconds
 calls fun to build time as String
 */
    private fun getTimeString(time: Int, isTotalTime: Boolean): String {
        val hours = time % 86400 / 3600
        var minutes = time % 86400 % 3600 / 60
        val seconds = time % 86400 % 3600 % 60

        return if (isTotalTime) {
            if (seconds >= 30) {
                minutes += 1
            }
            makeTotalTimeString(hours, minutes)
        } else {
            makeTimeString(hours, minutes, seconds)
        }
    }

    // Formats the given time to String format
    private fun makeTimeString(hour: Int, min: Int, sec: Int): String =
        String.format("%02d:%02d:%02d", hour, min, sec)

    private fun makeTotalTimeString(hour: Int, min: Int): String =
        String.format("%2d Std. %2d Min.", hour, min)
}

private fun setIssueState(stateString: String): BoardState {
    return when (stateString) {
        "open" -> BoardState.OPEN
        "todo" -> BoardState.TODO
        "doing" -> BoardState.DOING
        "blocker" -> BoardState.BLOCKER
        "review" -> BoardState.REVIEW
        else -> BoardState.CLOSED
    }
}