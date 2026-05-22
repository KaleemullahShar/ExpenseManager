package com.spendwise.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.spendwise.charts.ChartRenderer
import com.spendwise.charts.InsightsGenerator
import com.spendwise.databinding.FragmentAnalyticsBinding
import com.spendwise.utils.PdfReportExporter
import com.spendwise.utils.PreferenceManager
import com.spendwise.viewmodel.ExpenseViewModel
import com.spendwise.viewmodel.ViewModelFactory
import kotlinx.coroutines.launch

class AnalyticsFragment : Fragment() {
    private var _binding: FragmentAnalyticsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ExpenseViewModel by viewModels { ViewModelFactory(requireActivity().application) }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        FragmentAnalyticsBinding.inflate(inflater, container, false).also { _binding = it }.root
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        lifecycleScope.launch { viewModel.categoryTotals.collect {
            ChartRenderer.renderPie(binding.pieChart, it)
            binding.insightsText.text = InsightsGenerator.create(viewModel.monthTotal.value, viewModel.budget.value, it).joinToString("\n")
        } }
        lifecycleScope.launch { viewModel.expenses.collect { expenses ->
            ChartRenderer.renderWeeklyBars(binding.barChart, expenses)
            binding.exportButton.setOnClickListener { PdfReportExporter.exportAndShare(requireContext(), expenses, PreferenceManager(requireContext()).currency) }
        } }
    }
    override fun onDestroyView() { super.onDestroyView(); _binding = null }
}
