package co.tiagoaguiar.course.instagram.common.view

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.Window
import androidx.annotation.RequiresApi
import androidx.annotation.StringRes
import co.tiagoaguiar.course.instagram.R
import co.tiagoaguiar.course.instagram.databinding.PostZoomBinding
import com.bumptech.glide.Glide

@SuppressLint("SupportAnnotationUsage")
class PostZoom(context: Context) : Dialog(context) {

    private lateinit var binding: PostZoomBinding

    private var imageProfile: Uri? = null
    private var titleId: String? = null
    private var imagePost: Uri? = null
    private var caption: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = PostZoomBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    fun setImageProfile(imageUri: Uri) {
        this.imageProfile = imageUri
    }

    override fun setTitle(titleId: Int) {
        this.titleId = context.getString(titleId)
    }

    fun setTitle(title: String) {
        this.titleId = title
    }

    fun setImage(imageUri: Uri) {
        this.imagePost = imageUri
    }

    fun setImageURL(url: String) {
        Glide.with(context).load(url).into(binding.zoomImgPhoto)
    }

    fun setCaption(captionId: Int) {
        this.caption = context.getString(captionId)
    }

    fun setCaption(caption: String) {
        this.caption = caption
    }

    fun setImageProfileURL(url: String){
        Glide.with(context).load(url).into(binding.zoomImgProfile)
    }

    @RequiresApi(Build.VERSION_CODES.R)
    override fun show() {
        super.show()

        imageProfile?.let {
            if (it != Uri.EMPTY) {
                binding.zoomImgProfile.setImageURI(it)
            }
        }

        titleId?.let {
            binding.zoomTxtUsername.text = it
        }

        imagePost?.let {
            binding.zoomImgPhoto.setImageURI(it)
        }

        caption?.let {
            binding.zoomTxtDesc.text = it
        }

        window?.setBackgroundDrawableResource(R.drawable.dialog_custom_background)

    }
}