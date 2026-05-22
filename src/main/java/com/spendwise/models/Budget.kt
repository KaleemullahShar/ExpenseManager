package com.spendwise.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "budgets")
data class Budget(
    @PrimaryKey val monthKey: String,
    val amount: Double,
    val updatedAt: Long = System.currentTimeMillis()
)
