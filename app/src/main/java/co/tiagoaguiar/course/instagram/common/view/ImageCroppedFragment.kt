package co.tiagoaguiar.course.instagram.common.view

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import co.tiagoaguiar.course.instagram.R
import co.tiagoaguiar.course.instagram.databinding.FragmentImageCroppedBinding
import co.tiagoaguiar.course.instagram.main.view.MainActivity
import com.google.android.material.appbar.AppBarLayout
import java.io.File

class ImageCroppedFragment : Fragment(R.layout.fragment_image_cropped) {

    private var binding: FragmentImageCroppedBinding? = null

    private var isMainActivity = false

    companion object {
        const val KEY_URI = "key_uri"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentImageCroppedBinding.bind(view)

        if (isMainActivity) {
            marginMainActivity()
        }

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

    private fun marginMainActivity() {
        val tv = TypedValue()
        var actionBarHeight = 0;
        if (activity?.theme?.resolveAttribute(R.attr.actionBarSize, tv, true) == true)
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, resources.displayMetrics)

        val layoutParams = FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            AppBarLayout.LayoutParams.MATCH_PARENT
        )
        layoutParams.setMargins(0, 0, 0, actionBarHeight)

        binding?.containerCropped?.layoutParams = layoutParams
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is MainActivity) {
            isMainActivity = true
        }
    }

    override fun onDestroy() {
        binding = null
        isMainActivity = false
        super.onDestroy()
    }

}