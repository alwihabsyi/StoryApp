package com.alwihabsyi.storyapp.ui.story

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.alwihabsyi.storyapp.R
import com.alwihabsyi.storyapp.data.Preferences
import com.alwihabsyi.storyapp.data.Result
import com.alwihabsyi.storyapp.databinding.ActivityStoryBinding
import com.alwihabsyi.storyapp.ui.ViewModelFactory
import com.alwihabsyi.storyapp.ui.main.MainActivity
import com.alwihabsyi.storyapp.utils.Constants.TOKEN
import com.alwihabsyi.storyapp.utils.createTempFile
import com.alwihabsyi.storyapp.utils.hide
import com.alwihabsyi.storyapp.utils.reduceFileImage
import com.alwihabsyi.storyapp.utils.show
import com.alwihabsyi.storyapp.utils.toast
import com.alwihabsyi.storyapp.utils.uriToFile
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class StoryActivity : AppCompatActivity() {

    private var _binding: ActivityStoryBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<StoryViewModel> { ViewModelFactory(this, this) }

    private var photoPath: String? = null
    private var setFile: File? = null

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(
                    this,
                    getString(R.string.permission),
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupAction()
    }

    private fun setupAction() {
        binding.btnCamera.setOnClickListener { startCamera() }
        binding.btnGallery.setOnClickListener { startGallery() }
        binding.buttonAdd.setOnClickListener { uploadImage() }
    }

    private fun uploadImage() {
        val description = binding.edAddDescription.text.toString()
        if (setFile != null && description.isNotEmpty()) {
            val file = reduceFileImage(setFile as File)
            val requestImageFile = file.asRequestBody("image/jpeg".toMediaType())
            val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                "photo",
                file.name,
                requestImageFile
            )
            val descriptionCast = description.toRequestBody("text/plain".toMediaType())
            val sharedPref = Preferences.init(this, "session")
            val token = sharedPref.getString(TOKEN, "")

            viewModel.uploadImage(token!!, imageMultipart, descriptionCast).observe(this) {
                when (it) {
                    is Result.Loading -> {
                        binding.buttonAdd.text = ""
                        binding.progressBar.show()
                    }
                    is Result.Success -> {
                        binding.progressBar.hide()
                        binding.buttonAdd.text = getString(R.string.addstory)
                        toast(it.data)
                        backToMain()
                    }
                    is Result.Error -> {
                        binding.progressBar.hide()
                        binding.buttonAdd.text = getString(R.string.addstory)
                        toast(it.error)
                    }
                }
            }
        } else {
            toast(getString(R.string.upload_picture))
        }
    }

    private fun backToMain() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launchGallery.launch(chooser)
    }

    @SuppressLint("QueryPermissionsNeeded")
    private fun startCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.resolveActivity(packageManager)
        createTempFile(application).also {
            val photoURI: Uri = FileProvider.getUriForFile(
                this@StoryActivity,
                "com.alwihbsyi.storyapp.ui.story",
                it
            )
            photoPath = it.absolutePath
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            launchCamera.launch(intent)
        }
    }

    private val launchCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            val myFile = File(photoPath!!)

            myFile.let { file ->
                setFile = file
                binding.ivPhotoPreview.setImageBitmap(BitmapFactory.decodeFile(file.path))
            }
        }
    }

    private val launchGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg = result.data?.data as Uri
            selectedImg.let { uri ->
                val myFile = uriToFile(uri, this@StoryActivity)
                setFile = myFile
                binding.ivPhotoPreview.setImageURI(uri)
            }
        }
    }

    companion object {
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }
}