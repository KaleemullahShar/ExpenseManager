package com.spendwise.utils

import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

object BiometricHelper {
    fun authenticate(fragment: Fragment, onSuccess: () -> Unit) {
        val prompt = BiometricPrompt(fragment, ContextCompat.getMainExecutor(fragment.requireContext()), object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) = onSuccess()
        })
        prompt.authenticate(BiometricPrompt.PromptInfo.Builder()
            .setTitle("Unlock SpendWise")
            .setSubtitle("Use biometrics to continue")
            .setNegativeButtonText("Use password")
            .build())
    }
}
