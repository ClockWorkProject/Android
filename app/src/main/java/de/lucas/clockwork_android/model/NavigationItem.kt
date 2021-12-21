package de.lucas.clockwork_android.model

import de.lucas.clockwork_android.R

enum class NavigationItem(val route: String, val icon: Int, val title: String) {
    TOGGLE("home", R.drawable.ic_clock, "Toggle"),
    BOARD("garage", R.drawable.ic_board, "Issues"),
    STATISTIC("hiking", R.drawable.ic_statistic, "Statistik"),
    PROFILE("hotel", R.drawable.ic_profile, "Profil");

    companion object {
        const val LOGIN = "login"
    }
}