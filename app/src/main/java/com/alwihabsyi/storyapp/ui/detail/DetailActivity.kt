package com.alwihabsyi.storyapp.ui.detail

import android.animation.ObjectAnimator
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.alwihabsyi.storyapp.data.remote.ListStory
import com.alwihabsyi.storyapp.databinding.ActivityDetailBinding
import com.alwihabsyi.storyapp.utils.glide

@Suppress("DEPRECATION")
class DetailActivity : AppCompatActivity() {

    private var _binding: ActivityDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userId = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(USER, ListStory::class.java)
        } else {
            intent.getParcelableExtra(USER)
        }

        userId?.let {
            setupPage(it)
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

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        const val USER = "user"
    }
}