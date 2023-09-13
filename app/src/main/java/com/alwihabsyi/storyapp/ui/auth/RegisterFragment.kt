package com.alwihabsyi.storyapp.ui.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.alwihabsyi.storyapp.R
import com.alwihabsyi.storyapp.data.Result
import com.alwihabsyi.storyapp.databinding.FragmentRegisterBinding
import com.alwihabsyi.storyapp.ui.ViewModelFactory
import com.alwihabsyi.storyapp.ui.auth.viewmodel.RegisterViewModel
import com.alwihabsyi.storyapp.utils.hide
import com.alwihabsyi.storyapp.utils.show
import com.alwihabsyi.storyapp.utils.toast

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<RegisterViewModel> {
        ViewModelFactory.getInstance(
            requireActivity()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRegister()
    }

    private fun register(name: String, email: String, password: String) {
        viewModel.register(name, email, password).observe(viewLifecycleOwner) {
            when (it) {
                is Result.Loading -> {
                    binding.btnRegister.text = ""
                    binding.progressBar.show()
                }

                is Result.Success -> {
                    binding.progressBar.hide()
                    AlertDialog.Builder(requireContext()).apply {
                        setTitle("Register")
                        setMessage(getString(R.string.register_succeed))
                        setPositiveButton(getString(R.string.continue_login)) { _, _ ->
                            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
                        }
                        create()
                        show()
                    }
                }

                is Result.Error -> {
                    binding.progressBar.hide()
                    binding.btnRegister.text = getString(R.string.register_title)
                    toast(it.error)
                }
            }
        }
    }

    private fun setupRegister() {
        binding.loginLink.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }

        binding.btnRegister.setOnClickListener {
            val name = binding.edRegisterName.text.toString()
            val email = binding.edRegisterEmail.text.toString()
            val password = binding.edRegisterPassword.text.toString()

            register(name, email, password)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}