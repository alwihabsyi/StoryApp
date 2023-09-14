package com.alwihabsyi.storyapp.ui.auth

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.alwihabsyi.storyapp.R
import com.alwihabsyi.storyapp.data.Preferences
import com.alwihabsyi.storyapp.databinding.FragmentIntroductionBinding
import com.alwihabsyi.storyapp.ui.main.MainActivity
import com.alwihabsyi.storyapp.utils.Constants.TOKEN

class IntroductionFragment : Fragment() {

    private var _binding: FragmentIntroductionBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentIntroductionBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupAction()
    }

    private fun setupAction() {
        binding.btnContinue.setOnClickListener {
            findNavController().navigate(R.id.action_introductionFragment_to_loginFragment)
        }
    }

    private fun observer() {
        val sharedPref = Preferences.init(requireContext(), "session")
        val token = sharedPref.getString(TOKEN, "")

        if (token != "") {
            val intent = Intent(requireContext(), MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }
    }

    override fun onStart() {
        super.onStart()
        observer()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}