package com.spendwise.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spendwise.firebase.FirebaseAuthManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class UiState(val loading: Boolean = false, val error: String? = null, val success: Boolean = false)

class AuthViewModel(private val auth: FirebaseAuthManager) : ViewModel() {
    private val _state = MutableStateFlow(UiState())
    val state: StateFlow<UiState> = _state
    val isConfigured get() = auth.isConfigured
    val isLoggedIn get() = auth.isLoggedIn

    fun login(email: String, password: String) = runAuth { auth.login(email, password) }
    fun signup(email: String, password: String) = runAuth { auth.signup(email, password) }
    fun forgot(email: String) = runAuth { auth.forgotPassword(email) }
    fun logout() = auth.logout()

    private fun runAuth(block: suspend () -> Unit) {
        viewModelScope.launch {
            _state.value = UiState(loading = true)
            _state.value = try {
                block()
                UiState(success = true)
            } catch (t: Throwable) {
                UiState(error = t.localizedMessage ?: "Authentication failed")
            }
        }
    }
}
