package com.alwihabsyi.storyapp.ui.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.alwihabsyi.storyapp.data.Preferences
import com.alwihabsyi.storyapp.data.Result
import com.alwihabsyi.storyapp.data.remote.ListStory
import com.alwihabsyi.storyapp.databinding.ActivityMainBinding
import com.alwihabsyi.storyapp.ui.ViewModelFactory
import com.alwihabsyi.storyapp.ui.auth.AuthActivity
import com.alwihabsyi.storyapp.ui.detail.DetailActivity
import com.alwihabsyi.storyapp.ui.story.StoryActivity
import com.alwihabsyi.storyapp.utils.Constants.TOKEN
import com.alwihabsyi.storyapp.utils.hide
import com.alwihabsyi.storyapp.utils.show
import com.alwihabsyi.storyapp.utils.toast

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<MainViewModel> { ViewModelFactory() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRv()
        observer()
        setupAction()
    }

    private fun setupAction() {
        binding.actionLogout.setOnClickListener {
            Preferences.logOut(this)
            val intent = Intent(this, AuthActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }

        binding.btnAddStory.setOnClickListener {
            startActivity(Intent(this, StoryActivity::class.java))
        }
    }

    private fun observer() {
        val sharedPref = Preferences.init(this, "session")
        val token = sharedPref.getString(TOKEN, "")

        if (token != "") {
            viewModel.getAllStories(token!!).observe(this) { result ->
                when (result) {
                    is Result.Loading -> {
                        binding.progressBar.show()
                    }

                    is Result.Success -> {
                        binding.progressBar.hide()
                        setupRvData(result.data)
                    }

                    is Result.Error -> {
                        binding.progressBar.hide()
                        toast(result.error)
                    }
                }
            }
        }
    }

    private fun setupRvData(data: List<ListStory>) {
        val adapter = StoryAdapter()
        adapter.differ.submitList(data)
        binding.recyclerView.adapter = adapter

        adapter.onClick = {
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra("id", it.id)
            startActivity(intent)
        }
    }

    private fun setupRv() {
        binding.recyclerView.apply {
            val rvLayoutManager = LinearLayoutManager(this@MainActivity)
            layoutManager = rvLayoutManager
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}