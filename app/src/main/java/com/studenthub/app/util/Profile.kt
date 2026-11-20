package com.studenthub.app.util

import android.content.Context

/** Stores the user's name on the phone. */
object Profile {
    private fun prefs(c: Context) = c.getSharedPreferences("profile", Context.MODE_PRIVATE)

    fun getName(c: Context): String = prefs(c).getString("name", "") ?: ""

    fun setName(c: Context, name: String) {
        prefs(c).edit().putString("name", name).apply()
    }
}
