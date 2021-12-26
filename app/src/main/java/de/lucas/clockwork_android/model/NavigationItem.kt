package de.lucas.clockwork_android.model

import de.lucas.clockwork_android.R

enum class NavigationItem(val route: String, val icon: Int, val title: String) {
    TOGGLE("home", R.drawable.ic_clock, "Toggle"),
    BOARD("garage", R.drawable.ic_board, "Issues"),
    STATISTIC("hiking", R.drawable.ic_statistic, "Statistik"),
    PROFILE("hotel", R.drawable.ic_profile, "Profil");

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