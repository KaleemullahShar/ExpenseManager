package com.spendwise.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.spendwise.R
import com.spendwise.databinding.FragmentAuthBinding
import com.spendwise.utils.BiometricHelper
import com.spendwise.utils.PreferenceManager
import com.spendwise.viewmodel.AuthViewModel
import com.spendwise.viewmodel.ViewModelFactory
import kotlinx.coroutines.launch

class LoginFragment : Fragment() {
    private var _binding: FragmentAuthBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AuthViewModel by viewModels { ViewModelFactory(requireActivity().application) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        FragmentAuthBinding.inflate(inflater, container, false).also { _binding = it }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        with(binding) {
        title.text = "Welcome back"
        primaryButton.text = "Login"
        secondaryButton.text = "Create account"
        statusText.text = "Forgot password?"
        if (PreferenceManager(requireContext()).biometricsEnabled) BiometricHelper.authenticate(this@LoginFragment) { findNavController().navigate(R.id.action_login_to_dashboard) }
        primaryButton.setOnClickListener {
            if (emailInput.text.isNullOrBlank() || passwordInput.text.isNullOrBlank()) statusText.text = "Email and password are required"
            else viewModel.login(emailInput.text.toString(), passwordInput.text.toString())
        }
        secondaryButton.setOnClickListener { findNavController().navigate(R.id.action_login_to_signup) }
        statusText.setOnClickListener { findNavController().navigate(R.id.action_login_to_forgot) }
        emailInput.doAfterTextChanged { statusText.text = "Forgot password?" }
        lifecycleScope.launch {
            viewModel.state.collect {
                primaryButton.isEnabled = !it.loading
                if (it.success) findNavController().navigate(R.id.action_login_to_dashboard)
                it.error?.let { error -> statusText.text = error }
            }
        }
    }
    }
    override fun onDestroyView() { super.onDestroyView(); _binding = null }
}
