package com.spendwise.firebase

import android.content.Context
import android.util.Log
import com.google.firebase.FirebaseApp

object FirebaseInitializer {
    private const val TAG = "FirebaseInitializer"

    fun initialize(context: Context): Boolean {
        if (FirebaseApp.getApps(context).isNotEmpty()) return true

        return runCatching { FirebaseApp.initializeApp(context) != null }
            .onFailure { Log.w(TAG, "Firebase is not configured; cloud features are disabled.", it) }
            .getOrDefault(false)
    }
}
