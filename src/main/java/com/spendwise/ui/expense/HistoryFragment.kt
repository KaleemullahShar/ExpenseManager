package com.spendwise.ui.expense

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AdapterView
import androidx.core.os.bundleOf
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.spendwise.R
import com.spendwise.adapters.ExpenseAdapter
import com.spendwise.databinding.FragmentHistoryBinding
import com.spendwise.utils.PreferenceManager
import com.spendwise.viewmodel.ExpenseViewModel
import com.spendwise.viewmodel.ViewModelFactory
import kotlinx.coroutines.launch

class HistoryFragment : Fragment() {
    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ExpenseViewModel by viewModels { ViewModelFactory(requireActivity().application) }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        FragmentHistoryBinding.inflate(inflater, container, false).also { _binding = it }.root
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val adapter = ExpenseAdapter(PreferenceManager(requireContext()).currency) { findNavController().navigate(R.id.action_history_to_detail, bundleOf("expenseId" to it.id)) }
        binding.expensesRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.expensesRecycler.adapter = adapter
        val categories = listOf("All", "Food", "Transport", "Shopping", "Bills", "Education", "Entertainment", "Health", "Other")
        val sortModes = listOf("Newest first", "Oldest first", "Highest amount", "Lowest amount")
        binding.categorySpinner.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, categories)
        binding.sortSpinner.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, sortModes)
        binding.searchInput.doAfterTextChanged { viewModel.setSearch(it.toString()) }
        val refresh = {
            val category = binding.categorySpinner.selectedItem?.toString() ?: "All"
            val sort = binding.sortSpinner.selectedItem?.toString() ?: "Newest first"
            val filtered = viewModel.expenses.value
                .filter { category == "All" || it.category == category }
                .let {
                    when (sort) {
                        "Oldest first" -> it.sortedBy { expense -> expense.date }
                        "Highest amount" -> it.sortedByDescending { expense -> expense.amount }
                        "Lowest amount" -> it.sortedBy { expense -> expense.amount }
                        else -> it.sortedByDescending { expense -> expense.date }
                    }
                }
            adapter.submitList(filtered)
            binding.emptyText.visibility = if (filtered.isEmpty()) View.VISIBLE else View.GONE
        }
        val listener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) = refresh()
            override fun onNothingSelected(parent: AdapterView<*>?) = Unit
        }
        binding.categorySpinner.onItemSelectedListener = listener
        binding.sortSpinner.onItemSelectedListener = listener
        lifecycleScope.launch { viewModel.expenses.collect { refresh() } }
    }
    override fun onDestroyView() { super.onDestroyView(); _binding = null }
}
