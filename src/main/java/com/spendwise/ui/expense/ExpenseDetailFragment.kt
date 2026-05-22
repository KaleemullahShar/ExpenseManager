package com.spendwise.ui.expense

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.spendwise.R
import com.spendwise.databinding.FragmentExpenseDetailBinding
import com.spendwise.models.Expense
import com.spendwise.utils.DateMoneyUtils
import com.spendwise.utils.PreferenceManager
import com.spendwise.viewmodel.ExpenseViewModel
import com.spendwise.viewmodel.ViewModelFactory
import kotlinx.coroutines.launch

class ExpenseDetailFragment : Fragment() {
    private var _binding: FragmentExpenseDetailBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ExpenseViewModel by viewModels { ViewModelFactory(requireActivity().application) }
    private var current: Expense? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        FragmentExpenseDetailBinding.inflate(inflater, container, false).also { _binding = it }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val id = requireArguments().getLong("expenseId")
        val currency = PreferenceManager(requireContext()).currency
        lifecycleScope.launch {
            viewModel.expense(id).collect { expense ->
                current = expense
                if (expense != null) {
                    binding.title.text = expense.title
                    binding.amount.text = DateMoneyUtils.formatMoney(expense.amount, currency)
                    binding.meta.text = "${expense.category} - ${DateMoneyUtils.formatDate(expense.date)}"
                    binding.notes.text = expense.notes.ifBlank { "No notes" }
                }
            }
        }
        binding.editButton.setOnClickListener {
            val args = Bundle().apply { putLong("expenseId", id) }
            findNavController().navigate(R.id.addExpenseFragment, args)
        }
        binding.deleteButton.setOnClickListener {
            current?.let {
                viewModel.delete(it)
                findNavController().popBackStack()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
