package com.spendwise.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.spendwise.models.CategoryTotal
import com.spendwise.models.Expense
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseDao {
    @Query("SELECT * FROM expenses ORDER BY date DESC, timestamp DESC")
    fun observeExpenses(): Flow<List<Expense>>

    @Query("SELECT * FROM expenses WHERE title LIKE '%' || :query || '%' OR notes LIKE '%' || :query || '%' ORDER BY date DESC")
    fun searchExpenses(query: String): Flow<List<Expense>>

    @Query("SELECT * FROM expenses WHERE category = :category ORDER BY date DESC")
    fun filterByCategory(category: String): Flow<List<Expense>>

    @Query("SELECT * FROM expenses WHERE id = :id")
    fun observeExpense(id: Long): Flow<Expense?>

    @Query("SELECT COALESCE(SUM(amount), 0) FROM expenses")
    fun observeTotal(): Flow<Double>

    @Query("SELECT COALESCE(SUM(amount), 0) FROM expenses WHERE date BETWEEN :start AND :end")
    fun observeTotalBetween(start: Long, end: Long): Flow<Double>

    @Query("SELECT category, COALESCE(SUM(amount), 0) as total FROM expenses GROUP BY category ORDER BY total DESC")
    fun observeCategoryTotals(): Flow<List<CategoryTotal>>

    @Query("SELECT * FROM expenses WHERE synced = 0")
    suspend fun unsynced(): List<Expense>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(expense: Expense): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(expenses: List<Expense>)

    @Update
    suspend fun update(expense: Expense)

    @Delete
    suspend fun delete(expense: Expense)
}
