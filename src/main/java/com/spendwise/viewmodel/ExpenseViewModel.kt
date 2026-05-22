package com.spendwise.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spendwise.models.Expense
import com.spendwise.repository.ExpenseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
class ExpenseViewModel(private val repository: ExpenseRepository) : ViewModel() {
    private val query = MutableStateFlow("")
    val expenses = query.flatMapLatest { q ->
        if (q.isBlank()) repository.expenses else repository.search(q)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val total = repository.total.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)
    val monthTotal = repository.monthTotal().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)
    val categoryTotals = repository.categoryTotals.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val budget = repository.budget().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    fun setSearch(value: String) { query.value = value }
    fun expense(id: Long) = repository.expense(id)
    fun save(expense: Expense) = viewModelScope.launch { repository.save(expense) }
    fun delete(expense: Expense) = viewModelScope.launch { repository.delete(expense) }
    fun setBudget(amount: Double) = viewModelScope.launch { repository.setBudget(amount) }
    fun sync() = viewModelScope.launch { repository.syncNow() }
}
