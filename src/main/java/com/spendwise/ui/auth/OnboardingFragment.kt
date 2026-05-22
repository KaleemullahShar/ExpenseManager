package com.spendwise.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.spendwise.R
import com.spendwise.databinding.FragmentOnboardingBinding
import com.spendwise.utils.PreferenceManager

class OnboardingFragment : Fragment() {
    private var _binding: FragmentOnboardingBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        FragmentOnboardingBinding.inflate(inflater, container, false).also { _binding = it }.root
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.startButton.setOnClickListener {
            PreferenceManager(requireContext()).onboardingComplete = true
            findNavController().navigate(R.id.action_onboarding_to_login)
        }
    }
    override fun onDestroyView() { super.onDestroyView(); _binding = null }
}
