@file:Suppress("DEPRECATION")

package co.tiagoaguiar.course.instagram.register.view

import android.content.Context
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.widget.Toast
import androidx.fragment.app.setFragmentResultListener
import co.tiagoaguiar.course.instagram.R
import co.tiagoaguiar.course.instagram.common.base.BaseFragment
import co.tiagoaguiar.course.instagram.common.base.DependencyInjector
import co.tiagoaguiar.course.instagram.common.extension.openDialogFromPhoto
import co.tiagoaguiar.course.instagram.common.view.ImageCroppedFragment
import co.tiagoaguiar.course.instagram.databinding.FragmentRegisterPhotoBinding
import co.tiagoaguiar.course.instagram.register.RegisterPhoto

class RegisterPhotoFragment : BaseFragment<FragmentRegisterPhotoBinding, RegisterPhoto.Presenter>(
    R.layout.fragment_register_photo,
    FragmentRegisterPhotoBinding::bind
), RegisterPhoto.View {

    override lateinit var presenter: RegisterPhoto.Presenter
    private var fragmentAttachListener: FragmentAttachListener? = null

    override fun setupPresenter() {
        presenter = DependencyInjector.registerPhotoPresenter(this)
    }

    override fun setupViews() {
        binding?.let { binding ->
            with(binding) {
                registerBtnAddPhoto.isEnabled = true
                registerBtnAddPhoto.setOnClickListener {
                    openDialog()
                }

                registerBtnJump.setOnClickListener {
                    fragmentAttachListener?.goToMainScreen()
                }
            }
        }
    }

    override fun getFragmentResult() {
        setFragmentResultListener("cropKey"){ _, bundle ->
            val uri = bundle.getParcelable<Uri>(ImageCroppedFragment.KEY_URI)
            onCropImageResult(uri)
        }
    }

    override fun showProgress(enabled: Boolean) {
        binding?.registerBtnAddPhoto?.showProgress(enabled)
    }

    override fun onUpdateFailure(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onUpdateSuccess() {
        fragmentAttachListener?.goToMainScreen()
    }

    private fun openDialog(){
        fragmentAttachListener?.let { openDialogFromPhoto(requireContext(), it) }
    }

    private fun onCropImageResult(uri: Uri?){
        uri?.let{
            val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                val source = ImageDecoder.createSource(requireContext().contentResolver, uri)
                ImageDecoder.decodeBitmap(source)
            } else {
                MediaStore.Images.Media.getBitmap(requireContext().contentResolver, uri)
            }
            binding?.registerImgProfile?.setImageBitmap(bitmap)
            presenter.updateUser(uri)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is FragmentAttachListener){
            fragmentAttachListener = context
        }
    }

    override fun onDestroy() {
        fragmentAttachListener = null
        super.onDestroy()
    }
}