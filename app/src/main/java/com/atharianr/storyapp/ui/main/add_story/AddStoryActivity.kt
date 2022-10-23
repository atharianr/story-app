package com.atharianr.storyapp.ui.main.add_story

import android.content.Intent
import android.content.Intent.ACTION_GET_CONTENT
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.atharianr.storyapp.MyApplication.Companion.prefs
import com.atharianr.storyapp.R
import com.atharianr.storyapp.data.source.remote.response.vo.StatusResponse
import com.atharianr.storyapp.databinding.ActivityAddStoryBinding
import com.atharianr.storyapp.ui.main.MainViewModel
import com.atharianr.storyapp.utils.*
import com.atharianr.storyapp.utils.Constant.USER_TOKEN
import com.atharianr.storyapp.utils.PreferenceHelper.get
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File

class AddStoryActivity : AppCompatActivity() {

    private var _binding: ActivityAddStoryBinding? = null
    private val binding get() = _binding as ActivityAddStoryBinding

    private val mainViewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS
            )
        }

        with(binding) {
            ibBack.setOnClickListener { onBackPressedDispatcher.onBackPressed() }
            btnCamera.setOnClickListener { startCameraX() }
            btnGallery.setOnClickListener { startGallery() }
            btnUpload.setOnClickListener {
                isLoading(true)
                addNewStory()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(
                    this, "Tidak mendapatkan permission.", Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    private fun addNewStory() {
        with(binding) {
            if (getFile != null) {
                val bearerToken = "Bearer ${prefs.get(USER_TOKEN, "")}"
                val file = reduceFileImage(getFile as File)
                val description =
                    edAddDescription.text.toString().toRequestBody("text/plain".toMediaType())
                val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
                val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                    "photo", file.name, requestImageFile
                )

                mainViewModel.addNewStory(bearerToken, imageMultipart, description)
                    .observe(this@AddStoryActivity) {
                        when (it.status) {
                            StatusResponse.SUCCESS -> {
                                it.body?.apply {
                                    toast(this@AddStoryActivity, message)
                                    finish()
                                }
                            }
                            StatusResponse.ERROR -> {
                                it.message?.let { msg -> toast(this@AddStoryActivity, msg) }
                            }
                        }
                        isLoading(false)
                    }
            } else {
                Toast.makeText(
                    this@AddStoryActivity,
                    "Silakan masukkan berkas gambar terlebih dahulu.",
                    Toast.LENGTH_SHORT
                ).show()
                isLoading(false)
            }
        }
    }

    private fun startCameraX() {
        val intent = Intent(this, CameraActivity::class.java)
        launcherIntentCameraX.launch(intent)
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    private var getFile: File? = null
    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CAMERA_X_RESULT) {
            val myFile = it.data?.getSerializableExtra("picture") as File
            val isBackCamera = it.data?.getBooleanExtra("isBackCamera", true) as Boolean

            getFile = myFile
            val result = rotateBitmap(
                BitmapFactory.decodeFile(getFile?.path), isBackCamera
            )

            with(binding) {
                cvItemPhoto.visible()
                ivItemPhoto.setImageBitmap(result)
            }
        }
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri
            val myFile = uriToFile(selectedImg, this)

            getFile = myFile
            val imgResult = rotateBitmap(
                BitmapFactory.decodeFile(myFile.path), true
            )

            with(binding) {
                cvItemPhoto.visible()
                ivItemPhoto.setImageBitmap(imgResult)
            }
        }
    }

    private fun isLoading(loading: Boolean) {
        binding.apply {
            if (loading) {
                btnUpload.text = ""
                btnUpload.isEnabled = false
                progressBar.visible()
            } else {
                btnUpload.text = getString(R.string.upload)
                btnUpload.isEnabled = true
                progressBar.gone()
            }
        }
    }

    companion object {
        const val CAMERA_X_RESULT = 200

        private val REQUIRED_PERMISSIONS = arrayOf(android.Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }
}