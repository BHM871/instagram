package co.tiagoaguiar.course.instagram.common.view

import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import co.tiagoaguiar.course.instagram.R
import co.tiagoaguiar.course.instagram.databinding.FragmentImageCroppedBinding
import java.io.File

class ImageCroppedFragment : Fragment(R.layout.fragment_image_cropped) {

    private var binding: FragmentImageCroppedBinding? = null

    companion object {
        const val KEY_URI = "key_uri"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentImageCroppedBinding.bind(view)

        val uri = arguments?.getParcelable<Uri>(KEY_URI)

        binding?.let {
            with(it) {
                cropperContainer.setAspectRatio(1, 1)
                cropperContainer.setFixedAspectRatio(true)

                cropperContainer.setImageUriAsync(uri)

                cropperContainer.setOnCropImageCompleteListener { _, result ->
                    setFragmentResult("cropKey", bundleOf(KEY_URI to result.uri))

                    parentFragmentManager.popBackStack()
                }

                cropperBtnSave.setOnClickListener {
                    val dir = context?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)

                    if (dir != null) {
                        val uriToSaved = Uri.fromFile(
                            File(
                                dir.path,
                                System.currentTimeMillis().toString() + ".jpg"
                            )
                        )
                        cropperContainer.saveCroppedImageAsync(uriToSaved)
                    }
                }

                cropperBtnCancel.setOnClickListener {
                    parentFragmentManager.popBackStack()
                }
            }
        }
    }

    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }

}