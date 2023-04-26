package com.bayutb.mystoryapp

import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.Manifest
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import androidx.activity.result.contract.ActivityResultContracts
import com.bayutb.mystoryapp.api.ApiConfig
import com.bayutb.mystoryapp.api.SessionManager
import com.bayutb.mystoryapp.api.UploadResponse
import com.bayutb.mystoryapp.databinding.ActivityPostBinding
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

@Suppress("DEPRECATION")
class PostActivity : AppCompatActivity() {
    private var getFile: File?= null
    private var isBackCamera : Boolean ?= null
    private lateinit var binding: ActivityPostBinding
    private lateinit var sessionManager: SessionManager

    companion object {
        const val CAMERA_X_RESULT = 200

        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }

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
                    "Tidak mendapatkan permission.",
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
        binding = ActivityPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }

        binding.goCamera.setOnClickListener {
            startCameraX()
        }
        binding.goGallery.setOnClickListener {
            val intent = Intent()
            intent.action = Intent.ACTION_GET_CONTENT
            intent.type = "image/*"
            val chooser = Intent.createChooser(intent, "Choose a picture")
            launcherIntentGallery.launch(chooser)
        }
        binding.btnPost.setOnClickListener {
            uploadImage()
        }
    }

    private fun startCameraX() {
        val intent = Intent(this, CameraXActivity::class.java)
        launcherIntentCameraX.launch(intent)
    }

    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CAMERA_X_RESULT) {
            val myFile = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                it.data?.getSerializableExtra("picture", File::class.java)
            } else {
                @Suppress("DEPRECATION")
                it.data?.getSerializableExtra("picture")
            } as? File

            isBackCamera = it.data?.getBooleanExtra("isBackCamera", true) as Boolean

            myFile?.let { file ->
                getFile = file

                val img = rotateBitmap(BitmapFactory.decodeFile(file.path), isBackCamera!!)
                binding.postPreviewImage.setImageBitmap(img)
            }
        }
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri
            val myFile = uriToFile(selectedImg, this@PostActivity)
            getFile = myFile
            binding.postPreviewImage.setImageURI(selectedImg)
        }
    }

    private fun uploadImage() {
        if (getFile != null) {
            sessionManager = SessionManager(this)
            val token = sessionManager.checkAuth()
            val file = compressImage(getFile as File, 1000000, isBackCamera!!)
            val inputDescription = binding.etDescription.text.toString()
            val description = inputDescription.toRequestBody("text/plain".toMediaTypeOrNull())
            val requestImageFile = file.asRequestBody("image/*".toMediaTypeOrNull())
            val imageMultiPart: MultipartBody.Part = MultipartBody.Part.createFormData(
                "photo",
                file.name,
                requestImageFile
            )

            val client = ApiConfig.getApiService().postStory("Bearer $token", imageMultiPart, description)
            client.enqueue(object : Callback<UploadResponse>{
                override fun onResponse(
                    call: Call<UploadResponse>,
                    response: Response<UploadResponse>
                ) {
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        if (responseBody != null && !responseBody.error) {
                            Toast.makeText(this@PostActivity, responseBody.message, Toast.LENGTH_SHORT).show()
                            onBackPressed()
                        }
                    } else {
                        Toast.makeText(this@PostActivity, response.message(), Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<UploadResponse>, t: Throwable) {
                    Toast.makeText(this@PostActivity, "Gagal instance Retrofit", Toast.LENGTH_SHORT).show()
                }

            })
        } else {
            Toast.makeText(this@PostActivity, "Silakan masukkan berkas gambar terlebih dahulu., ${isBackCamera.toString()}", Toast.LENGTH_SHORT).show()
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        super.onBackPressed()
        Intent(this@PostActivity, HomeActivity::class.java).also {
            startActivity(it)
        }
    }
}