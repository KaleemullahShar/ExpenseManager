package com.spendwise.firebase

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await

class FirebaseAuthManager(private val auth: FirebaseAuth? = runCatching { FirebaseAuth.getInstance() }.getOrNull()) {
    val isConfigured get() = auth != null
    val currentUser get() = auth?.currentUser
    val isLoggedIn get() = currentUser != null

    suspend fun login(email: String, password: String) =
        requireAuth().signInWithEmailAndPassword(email, password).await()

    suspend fun signup(email: String, password: String) =
        requireAuth().createUserWithEmailAndPassword(email, password).await()

    suspend fun forgotPassword(email: String) =
        requireAuth().sendPasswordResetEmail(email).await()

    fun logout() = auth?.signOut()

    private fun requireAuth(): FirebaseAuth =
        auth ?: throw IllegalStateException("Firebase is not configured. Add google-services.json to enable authentication.")
}
