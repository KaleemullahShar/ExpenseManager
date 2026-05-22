package com.spendwise.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.spendwise.databinding.ItemExpenseBinding
import com.spendwise.models.Expense
import com.spendwise.utils.DateMoneyUtils

class ExpenseAdapter(
    private val currency: String,
    private val onClick: (Expense) -> Unit
) : ListAdapter<Expense, ExpenseAdapter.Holder>(Diff) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = Holder(
        ItemExpenseBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: Holder, position: Int) = holder.bind(getItem(position))

    inner class Holder(private val binding: ItemExpenseBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(expense: Expense) = with(binding) {
            title.text = expense.title
            category.text = expense.category
            date.text = DateMoneyUtils.formatDate(expense.date)
            amount.text = DateMoneyUtils.formatMoney(expense.amount, currency)
            root.setOnClickListener { onClick(expense) }
        }
    }

    object Diff : DiffUtil.ItemCallback<Expense>() {
        override fun areItemsTheSame(oldItem: Expense, newItem: Expense) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Expense, newItem: Expense) = oldItem == newItem
    }
}
