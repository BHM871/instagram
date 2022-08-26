package co.tiagoaguiar.course.instagram.post.view

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.net.Uri
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import co.tiagoaguiar.course.instagram.R
import co.tiagoaguiar.course.instagram.common.base.BaseFragment
import co.tiagoaguiar.course.instagram.common.base.DependencyInjector
import co.tiagoaguiar.course.instagram.databinding.FragmentMainGalleryBinding
import co.tiagoaguiar.course.instagram.post.Post
import co.tiagoaguiar.course.instagram.post.util.PictureAdapter

class GalleryFragment : BaseFragment<FragmentMainGalleryBinding, Post.Presenter>(
    R.layout.fragment_main_gallery,
    FragmentMainGalleryBinding::bind
), Post.View {

    override lateinit var presenter: Post.Presenter

    private lateinit var adapter: PictureAdapter

    override fun setupPresenter() {
        presenter = DependencyInjector.postPresenter(this, requireContext())
    }

    override fun setupViews() {
        binding?.let { binding ->
            with(binding) {

                adapter = PictureAdapter()
                adapter.setListener(onClickItem)

                galleryRecycler.layoutManager =
                    GridLayoutManager(requireContext(), 3)
                galleryRecycler.adapter = adapter

                presenter.fetchPictures()
            }
        }
    }

    override fun showProgress(enabled: Boolean) {
        binding?.galleryProgressBar?.visibility = if (enabled) View.VISIBLE else View.GONE
    }

    override fun displayEmptyPictures() {
        binding?.galleryContainer?.visibility = View.GONE
        binding?.galleryTxtEmptyList?.visibility = View.VISIBLE
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun displayFullPictures(pictures: List<Uri>) {
        binding?.galleryContainer?.visibility = View.VISIBLE

        binding?.galleryRecycler?.visibility = View.VISIBLE
        adapter.list = pictures
        adapter.notifyDataSetChanged()

        binding?.galleryImgPhoto?.setImageURI(adapter.list[0])
    }

    override fun displayRequestFailure(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private val onClickItem = object : (Uri) -> Unit {
        override fun invoke(picture: Uri) {
            binding?.galleryImgPhoto?.setImageURI(picture)
            binding?.galleryRecycler?.scrollTo(1, 1)
        }
    }
}