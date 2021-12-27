package de.lucas.clockwork_android.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import de.lucas.clockwork_android.model.Issue
import de.lucas.clockwork_android.model.Project
import timber.log.Timber

class IssueViewModel : ViewModel() {
    private var _issueList = MutableLiveData((listOf(Project("", listOf()))))
    val issueList: LiveData<List<Project>> = _issueList

    private var _issue = MutableLiveData(Issue(-1, "", "", "", ""))
    val issue: LiveData<Issue> = _issue

    fun setIssueList(issues: List<Project>) {
        _issueList.value = issues
    }

    fun setIssue(newIssue: Issue) {
        _issue.value = newIssue
        Timber.e(issue.value.toString())
    }
}