package com.spendwise.ui.expense

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.spendwise.databinding.FragmentAddExpenseBinding
import com.spendwise.models.Expense
import com.spendwise.utils.ReceiptScanner
import com.spendwise.viewmodel.ExpenseViewModel
import com.spendwise.viewmodel.ViewModelFactory
import kotlinx.coroutines.launch

class AddExpenseFragment : Fragment() {
    private var _binding: FragmentAddExpenseBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ExpenseViewModel by viewModels { ViewModelFactory(requireActivity().application) }
    private var editing: Expense? = null
    private val receiptPicker = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) lifecycleScope.launch {
            val amount = ReceiptScanner.extractAmount(requireContext(), uri)
            if (amount != null) binding.amountInput.setText(amount.toString()) else Toast.makeText(requireContext(), "No amount found", Toast.LENGTH_SHORT).show()
        }
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        FragmentAddExpenseBinding.inflate(inflater, container, false).also { _binding = it }.root
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(binding) {
        categorySpinner.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, listOf("Food", "Transport", "Shopping", "Bills", "Education", "Entertainment", "Health", "Other"))
        val expenseId = arguments?.getLong("expenseId") ?: 0L
        if (expenseId > 0) lifecycleScope.launch {
            viewModel.expense(expenseId).collect { expense ->
                if (expense != null && editing == null) {
                    editing = expense
                    titleInput.setText(expense.title)
                    amountInput.setText(expense.amount.toString())
                    notesInput.setText(expense.notes)
                    (0 until categorySpinner.count).firstOrNull { categorySpinner.getItemAtPosition(it) == expense.category }?.let { categorySpinner.setSelection(it) }
                    saveButton.text = "Update expense"
                }
            }
        }
        scanButton.setOnClickListener { receiptPicker.launch("image/*") }
        saveButton.setOnClickListener {
            val amount = amountInput.text.toString().toDoubleOrNull()
            if (titleInput.text.isNullOrBlank() || amount == null) Toast.makeText(requireContext(), "Title and valid amount are required", Toast.LENGTH_SHORT).show()
            else {
                val original = editing
                viewModel.save(
                    Expense(
                        id = original?.id ?: 0L,
                        title = titleInput.text.toString(),
                        amount = amount,
                        category = categorySpinner.selectedItem.toString(),
                        date = original?.date ?: System.currentTimeMillis(),
                        notes = notesInput.text.toString(),
                        timestamp = original?.timestamp ?: System.currentTimeMillis()
                    )
                )
                findNavController().popBackStack()
            }
        }
    }
    override fun onDestroyView() { super.onDestroyView(); _binding = null }
}
