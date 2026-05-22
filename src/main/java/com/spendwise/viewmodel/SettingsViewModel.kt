package com.spendwise.viewmodel

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.spendwise.firebase.FirebaseAuthManager
import com.spendwise.repository.ExpenseRepository
import com.spendwise.utils.PreferenceManager
import kotlinx.coroutines.launch

class SettingsViewModel(
    app: Application,
    private val auth: FirebaseAuthManager,
    private val repository: ExpenseRepository
) : AndroidViewModel(app) {
    val prefs = PreferenceManager(app)
    fun setDarkMode(enabled: Boolean) {
        val mode = if (enabled) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
        prefs.themeMode = mode
        AppCompatDelegate.setDefaultNightMode(mode)
    }
    fun setCurrency(symbol: String) { prefs.currency = symbol }
    fun sync() = viewModelScope.launch { repository.syncNow() }
    fun logout() = auth.logout()
}
