package com.spendwise.ui.budget

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.spendwise.databinding.FragmentBudgetBinding
import com.spendwise.notifications.NotificationHelper
import com.spendwise.utils.DateMoneyUtils
import com.spendwise.utils.PreferenceManager
import com.spendwise.viewmodel.ExpenseViewModel
import com.spendwise.viewmodel.ViewModelFactory
import kotlinx.coroutines.launch

class BudgetFragment : Fragment() {
    private var _binding: FragmentBudgetBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ExpenseViewModel by viewModels { ViewModelFactory(requireActivity().application) }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        FragmentBudgetBinding.inflate(inflater, container, false).also { _binding = it }.root
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val currency = PreferenceManager(requireContext()).currency
        binding.saveBudgetButton.setOnClickListener {
            val amount = binding.budgetInput.text.toString().toDoubleOrNull()
            if (amount == null) Toast.makeText(requireContext(), "Enter a valid budget", Toast.LENGTH_SHORT).show() else viewModel.setBudget(amount)
        }
        lifecycleScope.launch { viewModel.monthTotal.collect { render(currency) } }
        lifecycleScope.launch { viewModel.budget.collect { render(currency) } }
    }
    private fun render(currency: String) {
        val spent = viewModel.monthTotal.value
        val budget = viewModel.budget.value?.amount ?: 0.0
        val percent = if (budget > 0) ((spent / budget) * 100).toInt().coerceAtMost(100) else 0
        binding.budgetProgress.progress = percent
        binding.summaryText.text = "Spent ${DateMoneyUtils.formatMoney(spent, currency)} of ${DateMoneyUtils.formatMoney(budget, currency)}\nRemaining ${DateMoneyUtils.formatMoney(budget - spent, currency)}"
        if (budget > 0 && spent >= budget) NotificationHelper.notify(requireContext(), 202, "Budget exceeded", "You have crossed your monthly spending limit.")
    }
    override fun onDestroyView() { super.onDestroyView(); _binding = null }
}
