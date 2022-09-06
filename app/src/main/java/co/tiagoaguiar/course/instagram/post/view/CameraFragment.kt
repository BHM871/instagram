package co.tiagoaguiar.course.instagram.post.view

import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.util.Size
import android.view.View
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import co.tiagoaguiar.course.instagram.R
import co.tiagoaguiar.course.instagram.common.util.Files
import co.tiagoaguiar.course.instagram.databinding.FragmentMainCameraBinding

class CameraFragment : Fragment(R.layout.fragment_main_camera) {

    private var binding: FragmentMainCameraBinding? = null

    private var cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

    private lateinit var previewView: PreviewView
    private var imgCapture: ImageCapture? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setFragmentResultListener("cameraKey") { _, bundle ->
            val shouldStart = bundle.getBoolean("startCamera")
            if (shouldStart) starCamera()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentMainCameraBinding.bind(view)

        previewView = binding?.previewViewCamera!!

        binding?.cameraBtnCameraSelector?.setOnClickListener {
            if (cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA) {
                cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA
                starCamera()
            }
            else {
                cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
                starCamera()
            }
        }

        binding?.cameraBtnCapture?.setOnClickListener {
            it.isEnabled = false
            takePhoto()
            Handler(Looper.getMainLooper()).postDelayed({
                it.isEnabled = true
            }, 1500)
        }
    }

    private fun takePhoto() {
        val imgCapture = imgCapture ?: return

        val photoFile = Files.generatedFile(requireActivity())
        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        imgCapture.takePicture(outputOptions, ContextCompat.getMainExecutor(requireContext()), object : ImageCapture.OnImageSavedCallback {
            override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                val savedUri = Uri.fromFile(photoFile)
                setFragmentResult("uriKey", bundleOf("uri" to savedUri))
            }

            override fun onError(exception: ImageCaptureException) {
                Log.e("CaptureError", "Failure to take photo", exception)
            }})
    }

    private fun starCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())

        cameraProviderFuture.addListener({

            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(previewView.surfaceProvider)
                }

            imgCapture = ImageCapture.Builder()
                .setTargetResolution(Size(480, 480))
                .build()

            try {
                cameraProvider.unbindAll()

                cameraProvider.bindToLifecycle(this, cameraSelector, preview, imgCapture)

            } catch (e: Exception) {
                Log.e("Test", "Failure initialize camera", e)
            }

        }, ContextCompat.getMainExecutor(requireContext()))
    }

    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }

}