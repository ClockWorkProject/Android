package de.lucas.clockwork_android.model

import android.os.Bundle
import androidx.navigation.NavType
import com.squareup.moshi.Moshi

class IssueNavType : NavType<Issue>(isNullableAllowed = false) {
    override fun get(bundle: Bundle, key: String): Issue? {
        return bundle.getParcelable(key)
    }

    override fun parseValue(value: String): Issue {
        val moshi = Moshi.Builder().build()
        val jsonAdapter = moshi.adapter(Issue::class.java).lenient()
        return jsonAdapter.fromJson(value)!!
    }

    override fun put(bundle: Bundle, key: String, value: Issue) {
        bundle.putParcelable(key, value)
    }
}