package com.spendwise.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AdapterView
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.spendwise.R
import com.spendwise.databinding.FragmentSettingsBinding
import com.spendwise.viewmodel.SettingsViewModel
import com.spendwise.viewmodel.ViewModelFactory

class SettingsFragment : Fragment() {
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SettingsViewModel by viewModels { ViewModelFactory(requireActivity().application) }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        FragmentSettingsBinding.inflate(inflater, container, false).also { _binding = it }.root
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(binding) {
        darkModeSwitch.isChecked = viewModel.prefs.themeMode == AppCompatDelegate.MODE_NIGHT_YES
        biometricSwitch.isChecked = viewModel.prefs.biometricsEnabled
        darkModeSwitch.setOnCheckedChangeListener { _, checked -> viewModel.setDarkMode(checked) }
        biometricSwitch.setOnCheckedChangeListener { _, checked -> viewModel.prefs.biometricsEnabled = checked }
        val currencies = listOf("$", "€", "£", "PKR ", "INR ")
        currencySpinner.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, currencies)
        currencySpinner.setSelection(currencies.indexOf(viewModel.prefs.currency).coerceAtLeast(0))
        currencySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) = viewModel.setCurrency(currencies[position])
            override fun onNothingSelected(parent: AdapterView<*>?) = Unit
        }
        profileButton.setOnClickListener { findNavController().navigate(R.id.action_settings_to_profile) }
        syncButton.setOnClickListener { viewModel.sync() }
        logoutButton.setOnClickListener { viewModel.logout(); findNavController().navigate(R.id.action_settings_to_login) }
    }
    override fun onDestroyView() { super.onDestroyView(); _binding = null }
}
