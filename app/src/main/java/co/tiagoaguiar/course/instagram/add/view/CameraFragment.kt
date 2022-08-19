package co.tiagoaguiar.course.instagram.add.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import co.tiagoaguiar.course.instagram.R

class CameraFragment : Fragment() {

    private lateinit var previewView: PreviewView


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main_add_camera, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        previewView = view.findViewById(R.id.preview_view_camera)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setFragmentResultListener("cameraKey") { _, bundle ->
            val shouldStart = bundle.getBoolean("startCamera")
            if (shouldStart) starCamera()
        }
    }

    private fun starCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())

        val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

        val preview = Preview.Builder()
            .build()
            .also {
                it.setSurfaceProvider(previewView.surfaceProvider)
            }
        val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

        try {
            cameraProvider.unbindAll()

            cameraProvider.bindToLifecycle(this, cameraSelector, preview)

        } catch (e: Exception) {
            Log.e("Test", "Failure initialize camera", e)
        }

        cameraProviderFuture.addListener({}, ContextCompat.getMainExecutor(requireContext()))
    }

}