package com.spendwise.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "expenses")
data class Expense(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String = "",
    val amount: Double = 0.0,
    val category: String = "Other",
    val date: Long = System.currentTimeMillis(),
    val notes: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    val userId: String = "",
    val synced: Boolean = false
)
