package de.lucas.clockwork_android.model

import androidx.annotation.StringRes

// Data class for InfoScreen to populate shown list
data class InfoCategory(
    val id: String,
    @StringRes val titleRes: Int
)