package com.spendwise.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.spendwise.database.AppDatabase
import com.spendwise.firebase.FirebaseAuthManager
import com.spendwise.repository.ExpenseRepository

class ViewModelFactory(private val app: Application) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(AuthViewModel::class.java) -> AuthViewModel(FirebaseAuthManager()) as T
            modelClass.isAssignableFrom(ExpenseViewModel::class.java) -> ExpenseViewModel(repository()) as T
            modelClass.isAssignableFrom(SettingsViewModel::class.java) -> SettingsViewModel(app, FirebaseAuthManager(), repository()) as T
            else -> throw IllegalArgumentException("Unknown ViewModel ${modelClass.name}")
        }
    }

    private fun repository(): ExpenseRepository {
        val db = AppDatabase.get(app)
        return ExpenseRepository(db.expenseDao(), db.budgetDao())
    }
}
