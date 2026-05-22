package com.spendwise.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.spendwise.R
import com.spendwise.databinding.FragmentSplashBinding
import com.spendwise.utils.PreferenceManager
import com.spendwise.viewmodel.AuthViewModel
import com.spendwise.viewmodel.ViewModelFactory

class SplashFragment : Fragment() {
    private var _binding: FragmentSplashBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AuthViewModel by viewModels { ViewModelFactory(requireActivity().application) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentSplashBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.logo.animate().scaleX(1.12f).scaleY(1.12f).setDuration(650).withEndAction {
            val prefs = PreferenceManager(requireContext())
            val action = when {
                !viewModel.isConfigured -> R.id.action_splash_to_dashboard
                !prefs.onboardingComplete -> R.id.action_splash_to_onboarding
                viewModel.isLoggedIn -> R.id.action_splash_to_dashboard
                else -> R.id.action_splash_to_login
            }
            findNavController().navigate(action)
        }.start()
    }

    override fun onDestroyView() { super.onDestroyView(); _binding = null }
}
