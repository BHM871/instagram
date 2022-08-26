package co.tiagoaguiar.course.instagram.profile.view

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.recyclerview.widget.GridLayoutManager
import co.tiagoaguiar.course.instagram.R
import co.tiagoaguiar.course.instagram.common.base.BaseFragment
import co.tiagoaguiar.course.instagram.common.base.DependencyInjector
import co.tiagoaguiar.course.instagram.common.model.Post
import co.tiagoaguiar.course.instagram.common.model.UserAuth
import co.tiagoaguiar.course.instagram.common.view.ImageCroppedFragment
import co.tiagoaguiar.course.instagram.common.view.PostZoom
import co.tiagoaguiar.course.instagram.databinding.FragmentMainProfileBinding
import co.tiagoaguiar.course.instagram.main.AttachListenerPhoto
import co.tiagoaguiar.course.instagram.profile.Profile
import co.tiagoaguiar.course.instagram.profile.util.ProfileAdapter

class ProfileFragment : BaseFragment<FragmentMainProfileBinding, Profile.Presenter>(
    R.layout.fragment_main_profile,
    FragmentMainProfileBinding::bind
), Profile.View {

    override lateinit var presenter: Profile.Presenter

    private var attachListenerPhoto: AttachListenerPhoto? = null
    private var currentPhoto: Uri? = null

    private lateinit var adapter: ProfileAdapter

    override fun setupPresenter() {
        presenter = DependencyInjector.mainProfilePresenter(this)
    }

    override fun setupViews() {
        binding?.let { binding ->
            with(binding) {

                adapter = ProfileAdapter()
                adapter.setListener(onLongClicktem)

                profileRecycler.layoutManager =
                    GridLayoutManager(requireContext(), 3)
                profileRecycler.adapter = adapter

                profileIncludeProfile.profileImgIcon.setOnClickListener {
                    openDialog()
                }

                presenter.fetchUserProfile()

            }
        }
    }

    override fun getMenu() = R.menu.menu_profile

    override fun getFragmentResult() {
        setFragmentResultListener("cropKey") { _, bundle ->
            val uri = bundle.getParcelable<Uri>(ImageCroppedFragment.KEY_URI)
            currentPhoto = uri
            currentPhoto?.let { presenter.updatePhoto(it) }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> {
                setFragmentResult("addScreen", bundleOf("screen" to "camera"))
                attachListenerPhoto?.goToFragmentCamera()
            }
            R.id.menu_add -> {
                setFragmentResult("addScreen", bundleOf("screen" to "gallery"))
                attachListenerPhoto?.gotoFragmentGallery()
            }
        }
        return false
    }

    override fun showProgress(enabled: Boolean) {
        binding?.profileProgressBar?.visibility = if (enabled) View.VISIBLE else View.GONE
    }

    override fun displayUserProfile(userAuth: UserAuth) {
        binding?.let { binding ->
            with(binding) {
                profileIncludeProfile.profileTxtUsername.text = userAuth.username
                profileIncludeProfile.profileTxtPostCount.text = userAuth.postCount.toString()
                profileIncludeProfile.profileTxtFollowingCount.text =
                    userAuth.followingCount.toString()
                profileIncludeProfile.profileTxtFollowersCount.text =
                    userAuth.followersCount.toString()

                presenter.fetchUserPosts()

                profileIncludeProfile.profileImgIcon.setImageURI(userAuth.photoUri)
                if (userAuth.photoUri != null) profileIncludeProfile.profileImgAdd.visibility = View.GONE
            }
        }
    }

    override fun displayRequestFailure(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun displayEmptyPosts() {
        binding?.profileRecycler?.visibility = View.GONE
        binding?.profileTxtEmptyList?.visibility = View.VISIBLE
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun displayFullPosts(posts: List<Post>) {
        binding?.profileRecycler?.visibility = View.VISIBLE
        adapter.list = posts
        adapter.notifyDataSetChanged()
    }

    override fun onUpdateFailure(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onUpdateUserSuccess(image: Uri) {
        binding?.profileIncludeProfile?.profileImgIcon?.setImageURI(image)
        binding?.profileIncludeProfile?.profileImgAdd?.visibility = View.GONE
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onUpdatePostsSuccess(posts: List<Post>) {
        binding?.profileRecycler?.visibility = View.VISIBLE
        adapter.list = posts
        adapter.notifyDataSetChanged()
    }

    private fun openDialog() {
        attachListenerPhoto?.openDialogForPhoto()
    }

    private val onLongClicktem = object : (Post) -> Unit {
        override fun invoke(post: Post) {
            PostZoom(requireContext()).apply{
                setImageProfile(post.publisher.photoUri ?: Uri.EMPTY)
                setTitle(post.publisher.username)
                setImage(post.uri)
                setCaption(post.description)
                show()
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is AttachListenerPhoto) {
            attachListenerPhoto = context
        }
    }

    override fun onDestroy() {
        attachListenerPhoto = null
        super.onDestroy()
    }

}