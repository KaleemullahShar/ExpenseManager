package com.spendwise.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.spendwise.R
import com.spendwise.adapters.ExpenseAdapter
import com.spendwise.charts.ChartRenderer
import com.spendwise.databinding.FragmentDashboardBinding
import com.spendwise.utils.DateMoneyUtils
import com.spendwise.utils.PreferenceManager
import com.spendwise.viewmodel.ExpenseViewModel
import com.spendwise.viewmodel.ViewModelFactory
import kotlinx.coroutines.launch

class DashboardFragment : Fragment() {
    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ExpenseViewModel by viewModels { ViewModelFactory(requireActivity().application) }
    private lateinit var adapter: ExpenseAdapter
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        FragmentDashboardBinding.inflate(inflater, container, false).also { _binding = it }.root
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val currency = PreferenceManager(requireContext()).currency
        adapter = ExpenseAdapter(currency) { findNavController().navigate(R.id.action_dashboard_to_detail, bundleOf("expenseId" to it.id)) }
        binding.recentRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.recentRecycler.adapter = adapter
        lifecycleScope.launch { viewModel.expenses.collect { adapter.submitList(it.take(5)) } }
        lifecycleScope.launch { viewModel.total.collect { binding.totalExpenses.text = "Total\n${DateMoneyUtils.formatMoney(it, currency)}" } }
        lifecycleScope.launch { viewModel.monthTotal.collect { binding.monthTotal.text = DateMoneyUtils.formatMoney(it, currency); updateBudgetText(it, currency) } }
        lifecycleScope.launch { viewModel.categoryTotals.collect { ChartRenderer.renderPie(binding.pieChart, it) } }
    }
    private fun updateBudgetText(monthTotal: Double, currency: String) {
        val budget = viewModel.budget.value?.amount ?: 0.0
        binding.budgetCard.text = "Budget\n${DateMoneyUtils.formatMoney(budget, currency)}"
        binding.remainingBudget.text = if (budget > 0) "Remaining ${DateMoneyUtils.formatMoney(budget - monthTotal, currency)}" else "Set a budget to track remaining money"
    }
    override fun onDestroyView() { super.onDestroyView(); _binding = null }
}
