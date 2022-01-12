package de.lucas.clockwork_android.model

import de.lucas.clockwork_android.R

// Navigation Items to populate the bottom navigation bar
enum class NavigationItem(val route: String, val icon: Int, val title: String) {
    TOGGLE("toggle", R.drawable.ic_clock, "Toggle"),
    BOARD("issue", R.drawable.ic_board, "Issues"),
    STATISTIC("statistic", R.drawable.ic_statistic, "Statistik"),
    PROFILE("profile", R.drawable.ic_profile, "Profil");

    // const val for normal navigation destinations that aren't Type NavigationItem
    companion object {
        const val LOGIN = "login"
        const val ISSUE_DETAIL = "detail"
        const val ISSUE_EDIT = "edit"
        const val INFO = "info"
        const val IMPRINT = "imprint"
        const val VERSION = "version"
        const val LICENSES = "licenses"
        const val DATA_PROTECTION = "data_protection"
    }
}