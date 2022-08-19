package co.tiagoaguiar.course.instagram.register.view

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import co.tiagoaguiar.course.instagram.R
import co.tiagoaguiar.course.instagram.common.extension.hideKeyboard
import co.tiagoaguiar.course.instagram.common.extension.replaceFragment
import co.tiagoaguiar.course.instagram.common.view.ImageCroppedFragment
import co.tiagoaguiar.course.instagram.common.view.ImageCroppedFragment.Companion.KEY_URI
import co.tiagoaguiar.course.instagram.databinding.ActivityRegisterBinding
import co.tiagoaguiar.course.instagram.login.view.LoginActivity
import co.tiagoaguiar.course.instagram.main.view.MainActivity
import co.tiagoaguiar.course.instagram.register.view.RegisterNamePasswordFragment.Companion.KEY_EMAIL
import co.tiagoaguiar.course.instagram.register.view.RegisterWelcomeFragment.Companion.KEY_NAME
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("ResourceType")
class RegisterActivity : AppCompatActivity(), FragmentAttachListener {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var currentPhoto: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val fragment = RegisterEmailFragment()
        replaceFragmentRegister(fragment)
    }

    override fun goToNameAndPasswordScreen(email: String) {
        val fragment = RegisterNamePasswordFragment().apply {
            arguments = Bundle().apply {
                putString(KEY_EMAIL, email)
            }
        }
        replaceFragmentRegister(fragment)
    }

    override fun goToWelcomeScreen(name: String) {
        val fragment = RegisterWelcomeFragment().apply {
            arguments = Bundle().apply {
                putString(KEY_NAME, name)
            }
        }
        replaceFragmentRegister(fragment)
    }

    override fun goToPhotoScreen() {
        val fragment = RegisterPhotoFragment()
        replaceFragmentRegister(fragment)
    }

    override fun goToGalleryScreen() {
        getContent.launch("image/*")
    }

    private val getContent =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let { openImageCropper(it) }
        }

    override fun goToCameraScreen() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (intent.resolveActivity(packageManager) != null) {

            val photoFile: File? = try {
                createImageFile()
            } catch (e: IOException) {
                Log.e("RegisterActivityError", e.message, e)
                null
            }

            photoFile?.also {
                val photoUri = FileProvider.getUriForFile(
                    this,
                    "co.tiagoaguiar.course.instagram.fileprovider",
                    it
                )
                currentPhoto = photoUri

                getCamera.launch(photoUri)
            }
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val dir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile("JPEG_${timestamp}", ".jpg", dir)
    }

    private val getCamera =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { saved ->
            if (saved) {
                openImageCropper(currentPhoto)
            }
        }

    override fun goToMainScreen() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        finish()
    }

    override fun goToLoginScreen() {
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        finish()
    }

    private fun replaceFragmentRegister(fragment: Fragment){
        replaceFragment(R.id.register_fragment, fragment)
        hideKeyboard()
    }

    private fun openImageCropper(uri: Uri) {
        val fragment = ImageCroppedFragment().apply {
            arguments = Bundle().apply {
                putParcelable(KEY_URI, uri)
            }
        }
        replaceFragmentRegister(fragment)
    }

}