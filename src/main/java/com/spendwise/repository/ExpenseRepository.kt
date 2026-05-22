package com.spendwise.repository

import com.spendwise.database.BudgetDao
import com.spendwise.database.ExpenseDao
import com.spendwise.firebase.FirestoreSyncManager
import com.spendwise.models.Budget
import com.spendwise.models.Expense
import com.spendwise.utils.DateMoneyUtils

class ExpenseRepository(
    private val expenseDao: ExpenseDao,
    private val budgetDao: BudgetDao,
    private val syncManager: FirestoreSyncManager = FirestoreSyncManager()
) {
    val expenses = expenseDao.observeExpenses()
    val total = expenseDao.observeTotal()
    val categoryTotals = expenseDao.observeCategoryTotals()

    fun search(query: String) = expenseDao.searchExpenses(query)
    fun filter(category: String) = expenseDao.filterByCategory(category)
    fun expense(id: Long) = expenseDao.observeExpense(id)
    fun monthTotal() = DateMoneyUtils.monthBounds().let { expenseDao.observeTotalBetween(it.first, it.second) }
    fun budget() = budgetDao.observeBudget(DateMoneyUtils.monthKey())

    suspend fun save(expense: Expense) {
        if (expense.id == 0L) expenseDao.insert(expense) else expenseDao.update(expense.copy(synced = false))
    }

    suspend fun delete(expense: Expense) = expenseDao.delete(expense)
    suspend fun setBudget(amount: Double) = budgetDao.upsert(Budget(DateMoneyUtils.monthKey(), amount))

    suspend fun syncNow() {
        syncManager.backup(expenseDao.unsynced())
        syncManager.restore().takeIf { it.isNotEmpty() }?.let { expenseDao.insertAll(it) }
    }
}
