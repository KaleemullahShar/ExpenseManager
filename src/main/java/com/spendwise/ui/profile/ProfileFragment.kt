package com.spendwise.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.spendwise.databinding.FragmentProfileBinding
import com.spendwise.firebase.FirebaseAuthManager

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        FragmentProfileBinding.inflate(inflater, container, false).also { _binding = it }.root
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.emailText.text = FirebaseAuthManager().currentUser?.email ?: "Guest profile"
    }
    override fun onDestroyView() { super.onDestroyView(); _binding = null }
}
