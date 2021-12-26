package de.lucas.clockwork_android.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import de.lucas.clockwork_android.model.Issue
import de.lucas.clockwork_android.model.ProjectIssues
import timber.log.Timber

class IssueViewModel : ViewModel() {
    private var _issueList = MutableLiveData((listOf(ProjectIssues("", listOf()))))
    val issueList: LiveData<List<ProjectIssues>> = _issueList

    private var _issue = MutableLiveData(Issue(-1, "", "", "", ""))
    val issue: LiveData<Issue> = _issue

    fun setIssueList(issues: List<ProjectIssues>) {
        _issueList.value = issues
    }

    fun setIssue(newIssue: Issue) {
        _issue.value = newIssue
        Timber.e(issue.value.toString())
    }
}