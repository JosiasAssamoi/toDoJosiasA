package com.example.todojosiasassamoi.userInfo

import android.Manifest
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.todojosiasassamoi.R
import com.example.todojosiasassamoi.databinding.ActivityUserInfoBinding
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.net.toFile
import androidx.core.net.toUri
import androidx.fragment.app.viewModels
import androidx.lifecycle.Transformations
import androidx.lifecycle.lifecycleScope
import coil.load
import coil.transform.CircleCropTransformation
import com.example.todojosiasassamoi.BuildConfig
import com.example.todojosiasassamoi.BuildConfig.APPLICATION_ID
import com.example.todojosiasassamoi.models.UserInfoViewModel
import com.example.todojosiasassamoi.network.Api
import com.example.todojosiasassamoi.network.UserService
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class UserInfoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserInfoBinding
    val userviewModel: UserInfoViewModel by viewModels()

    private val requestPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
                if (isGranted) openCamera()
                else showExplanationDialog()
            }

    private fun requestCameraPermission() =
            requestPermissionLauncher.launch(Manifest.permission.CAMERA)

    private fun askCameraPermissionAndOpenCamera() {
        when {
            // check si j'ai deja la permisssion
            ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED -> openCamera()
            // check si besoin d'expliquer si on a besoin de la camera
            shouldShowRequestPermissionRationale(Manifest.permission.CAMERA) -> showExplanationDialog()

            else -> requestCameraPermission()
        }
    }

    private fun showExplanationDialog() {
        AlertDialog.Builder(this).apply {
            setMessage("On a besoin de la caméra sivouplé ! 🥺")
            setPositiveButton("Bon, ok") { _, _ ->
                requestCameraPermission()
            }
            setCancelable(true)
            show()
        }
    }


    // convert
    private fun convert(uri: Uri) =
            MultipartBody.Part.createFormData(
                    name = "avatar",
                    filename = "temp.jpeg",
                    body = contentResolver.openInputStream(uri)!!.readBytes().toRequestBody()
            )

    @kotlinx.serialization.ExperimentalSerializationApi
    private fun handleImage(uri: Uri) {
        lifecycleScope.launch() {
            val response = Api.userService.updateAvatar(convert(uri))
            val user = response.body()!!
            binding.userInfoPageImage.load(user.avatar) {
                transformations(CircleCropTransformation())
            }

        }

    }

    private val photoUri by lazy {
        FileProvider.getUriForFile(
                this,
                BuildConfig.APPLICATION_ID + ".fileprovider",
                File.createTempFile("avatar", ".jpeg", externalCacheDir)

        )
    }

    // register => il  ouvre une activite externe ( la cam par defaut en general)
    @kotlinx.serialization.ExperimentalSerializationApi
    private val takePicture = registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success) handleImage(photoUri)
        else Toast.makeText(this, "Erreur ! 😢", Toast.LENGTH_LONG).show()
    }

    // use
    private fun openCamera() = takePicture.launch(photoUri)

    // register
    private val pickInGallery =
            registerForActivityResult(ActivityResultContracts.GetContent()) {
                handleImage(it)
            }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserInfoBinding.inflate(layoutInflater)
        val userInfoView = binding.root
        setContentView(userInfoView)
        userviewModel.loadInfo()
        userviewModel.userInfos.observe(this) { user ->
            binding.nomEditText.setText(user.lastName)
            binding.prenomEditText.setText(user.firstName)
            binding.emailEditText.setText(user.email)
            binding.userInfoPageImage.load(user.avatar) {
                transformations(CircleCropTransformation())
            }
        }


        binding.ValidateUserButton.setOnClickListener() {
            val user = UserInfo(binding.emailEditText.text.toString(), binding.prenomEditText.text.toString(), binding.nomEditText.text.toString())
            userviewModel.update(user)
        }
        
        lifecycleScope.launch() {
            val userInfo = Api.userService.getInfo().body()!!
            binding.userInfoPageImage.load(userInfo.avatar) {
                transformations(CircleCropTransformation())
            }
        }
        binding.takePictureButton.setOnClickListener() {
            askCameraPermissionAndOpenCamera()
        }
        binding.uploadImageButton.setOnClickListener() {
            // use
            pickInGallery.launch("image/*")
        }


    }
}