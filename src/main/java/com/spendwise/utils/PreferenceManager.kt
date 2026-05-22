package com.spendwise.utils

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate

class PreferenceManager(context: Context) {
    private val prefs = context.getSharedPreferences("spendwise_preferences", Context.MODE_PRIVATE)

    var currency: String
        get() = prefs.getString("currency", "$") ?: "$"
        set(value) = prefs.edit().putString("currency", value).apply()

    var biometricsEnabled: Boolean
        get() = prefs.getBoolean("biometrics", false)
        set(value) = prefs.edit().putBoolean("biometrics", value).apply()

    var themeMode: Int
        get() = prefs.getInt("themeMode", AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        set(value) = prefs.edit().putInt("themeMode", value).apply()

    var onboardingComplete: Boolean
        get() = prefs.getBoolean("onboardingComplete", false)
        set(value) = prefs.edit().putBoolean("onboardingComplete", value).apply()
}
