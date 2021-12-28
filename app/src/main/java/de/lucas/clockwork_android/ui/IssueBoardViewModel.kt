package de.lucas.clockwork_android.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class IssueBoardViewModel : ViewModel() {
    private var _projectID = MutableLiveData(1)
    val projectID = _projectID

    fun changeProject(projectID: Int) {
        _projectID.value = projectID
    }
}