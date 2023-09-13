package com.alwihabsyi.storyapp.ui.auth

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.alwihabsyi.storyapp.R
import com.alwihabsyi.storyapp.data.Result
import com.alwihabsyi.storyapp.databinding.FragmentLoginBinding
import com.alwihabsyi.storyapp.ui.ViewModelFactory
import com.alwihabsyi.storyapp.ui.auth.viewmodel.LoginViewModel
import com.alwihabsyi.storyapp.ui.main.MainActivity
import com.alwihabsyi.storyapp.utils.hide
import com.alwihabsyi.storyapp.utils.show
import com.alwihabsyi.storyapp.utils.toast

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<LoginViewModel> {
        ViewModelFactory.getInstance(
            requireActivity()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupLogin()
    }

    private fun setupLogin() {
        binding.registerLink.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        binding.btnLogin.setOnClickListener {
            val email = binding.edLoginEmail.text.toString()
            val password = binding.edLoginPassword.text.toString()
            login(email, password)
        }
    }

    private fun login(email: String, password: String) {
        viewModel.login(email, password).observe(viewLifecycleOwner) {
            when (it) {
                is Result.Loading -> {
                    binding.progressBar.show()
                    binding.btnLogin.text = ""
                }
                is Result.Success -> {
                    viewModel.saveSession(it.data)
                    binding.progressBar.hide()

                    val intent = Intent(requireContext(), MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                    activity?.finish()
                }
                is Result.Error -> {
                    binding.progressBar.hide()
                    binding.btnLogin.text = getString(R.string.log_in)
                    toast(it.error)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}