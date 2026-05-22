package com.spendwise.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.spendwise.databinding.FragmentAuthBinding
import com.spendwise.viewmodel.AuthViewModel
import com.spendwise.viewmodel.ViewModelFactory
import kotlinx.coroutines.launch

class SignupFragment : Fragment() {
    private var _binding: FragmentAuthBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AuthViewModel by viewModels { ViewModelFactory(requireActivity().application) }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        FragmentAuthBinding.inflate(inflater, container, false).also { _binding = it }.root
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        with(binding) {
        title.text = "Create account"
        nameLayout.visibility = View.VISIBLE
        primaryButton.text = "Sign up"
        secondaryButton.text = "Back to login"
        primaryButton.setOnClickListener {
            val email = emailInput.text.toString()
            val password = passwordInput.text.toString()
            if (email.isBlank() || password.length < 6) statusText.text = "Use a valid email and 6+ character password" else viewModel.signup(email, password)
        }
        secondaryButton.setOnClickListener { findNavController().popBackStack() }
        lifecycleScope.launch { viewModel.state.collect { if (it.success) findNavController().popBackStack(); it.error?.let { e -> statusText.text = e } } }
    }
    }
    override fun onDestroyView() { super.onDestroyView(); _binding = null }
}
