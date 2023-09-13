package com.alwihabsyi.storyapp.ui.detail

import android.animation.ObjectAnimator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import com.alwihabsyi.storyapp.data.Result
import com.alwihabsyi.storyapp.data.remote.ListStory
import com.alwihabsyi.storyapp.databinding.ActivityDetailBinding
import com.alwihabsyi.storyapp.ui.ViewModelFactory
import com.alwihabsyi.storyapp.utils.glide
import com.alwihabsyi.storyapp.utils.hide
import com.alwihabsyi.storyapp.utils.show
import com.alwihabsyi.storyapp.utils.toast

class DetailActivity : AppCompatActivity() {

    private var _binding: ActivityDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<DetailViewModel> { ViewModelFactory.getInstance(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        observer()
    }

    private fun observer() {
        val userId = intent.getStringExtra("id")

        userId?.let {
            viewModel.getUserDetail(it).observe(this) { result ->
                when (result) {
                    is Result.Loading -> {
                        hideUI()
                    }

                    is Result.Success -> {
                        showUI()
                        setupPage(result.data)
                    }
                    is Result.Error -> {
                        toast(result.error)
                        finish()
                    }
                }
            }
        }
    }

    private fun setupPage(data: ListStory) {
        binding.ivDetailPhoto.glide(data.photoUrl!!)
        binding.tvDetailName.text = data.name
        binding.tvDetailDescription.text = data.description

        ObjectAnimator.ofFloat(binding.ivDetailPhoto, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()
    }

    private fun hideUI() {
        binding.ivDetailPhoto.hide()
        binding.tvDetailName.hide()
        binding.ivDetailPhoto.hide()
        binding.progressBar.show()
    }

    private fun showUI() {
        binding.ivDetailPhoto.show()
        binding.tvDetailName.show()
        binding.ivDetailPhoto.show()
        binding.progressBar.hide()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}