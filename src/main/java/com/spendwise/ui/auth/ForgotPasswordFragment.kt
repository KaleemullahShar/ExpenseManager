package com.spendwise.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.spendwise.databinding.FragmentAuthBinding
import com.spendwise.viewmodel.AuthViewModel
import com.spendwise.viewmodel.ViewModelFactory
import kotlinx.coroutines.launch

class ForgotPasswordFragment : Fragment() {
    private var _binding: FragmentAuthBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AuthViewModel by viewModels { ViewModelFactory(requireActivity().application) }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        FragmentAuthBinding.inflate(inflater, container, false).also { _binding = it }.root
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        with(binding) {
        title.text = "Reset password"
        passwordLayout.visibility = View.GONE
        primaryButton.text = "Send reset link"
        secondaryButton.text = "Back"
        primaryButton.setOnClickListener { if (emailInput.text.isNullOrBlank()) statusText.text = "Email is required" else viewModel.forgot(emailInput.text.toString()) }
        secondaryButton.setOnClickListener { requireActivity().onBackPressedDispatcher.onBackPressed() }
        lifecycleScope.launch { viewModel.state.collect { if (it.success) statusText.text = "Reset email sent"; it.error?.let { e -> statusText.text = e } } }
    }
    }
    override fun onDestroyView() { super.onDestroyView(); _binding = null }
}
