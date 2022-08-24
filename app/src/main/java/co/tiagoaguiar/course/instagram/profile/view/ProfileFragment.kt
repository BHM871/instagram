package co.tiagoaguiar.course.instagram.profile.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.fragment.app.setFragmentResultListener
import androidx.recyclerview.widget.GridLayoutManager
import co.tiagoaguiar.course.instagram.R
import co.tiagoaguiar.course.instagram.common.base.BaseFragment
import co.tiagoaguiar.course.instagram.common.base.DependencyInjector
import co.tiagoaguiar.course.instagram.common.model.Post
import co.tiagoaguiar.course.instagram.common.model.UserAuth
import co.tiagoaguiar.course.instagram.common.view.ImageCroppedFragment
import co.tiagoaguiar.course.instagram.databinding.FragmentMainProfileBinding
import co.tiagoaguiar.course.instagram.main.AttachListenerPhoto
import co.tiagoaguiar.course.instagram.profile.Profile
import co.tiagoaguiar.course.instagram.profile.util.ProfileAdapter

class ProfileFragment : BaseFragment<FragmentMainProfileBinding, Profile.Presenter>(
    R.layout.fragment_main_profile,
    FragmentMainProfileBinding::bind
), Profile.View {

    override lateinit var presenter: Profile.Presenter

    private var mainPhoto: AttachListenerPhoto? = null
    private var currentPhoto: Uri? = null

    private lateinit var adapter: ProfileAdapter
    private lateinit var posts: MutableList<Post>

    override fun setupPresenter() {
        presenter = DependencyInjector.mainProfilePresenter(this)
    }

    override fun setupViews() {
        binding?.let { binding ->
            with(binding) {

                posts = mutableListOf()
                adapter = ProfileAdapter()

                profileRecycler.layoutManager =
                    GridLayoutManager(requireContext(), 3)
                profileRecycler.adapter = adapter

                profileIncludeProfile.profileImgIcon.setOnClickListener {
                    openDialog()
                }

                presenter.fetchUserProfile(requireContext())

            }
        }
    }

    override fun getMenu() = R.menu.menu_profile

    override fun getFragmentResult() {
        setFragmentResultListener("cropKey") { _, bundle ->
            val uri = bundle.getParcelable<Uri>(ImageCroppedFragment.KEY_URI)
            currentPhoto = uri
            currentPhoto?.let { presenter.updatePhoto(requireContext(), it) }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.menu_add -> {

            }
        }
        return false
    }

    override fun showProgress(enabled: Boolean) {
        binding?.profileProgressBar?.visibility = if (enabled) View.VISIBLE else View.GONE
    }

    override fun displayUserProfile(userAuth: UserAuth, image: Bitmap?) {
        if (image != null) {
            binding?.profileIncludeProfile?.profileImgIcon?.setImageBitmap(image)
            binding?.profileIncludeProfile?.profileImgAdd?.visibility = View.GONE
        }

        binding?.let { binding ->
            with(binding) {
                profileIncludeProfile.profileTxtUsername.text = userAuth.username
                profileIncludeProfile.profileTxtPostCount.text = userAuth.postCount.toString()
                profileIncludeProfile.profileTxtFollowingCount.text =
                    userAuth.followingCount.toString()
                profileIncludeProfile.profileTxtFollowersCount.text =
                    userAuth.followersCount.toString()

                presenter.fetchUserPosts()
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
    override fun onUpdateUserSuccess(image: Bitmap) {
        binding?.profileIncludeProfile?.profileImgIcon?.setImageBitmap(image)
        binding?.profileIncludeProfile?.profileImgAdd?.visibility = View.GONE
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onUpdatePostsSuccess(posts: List<Post>) {
        binding?.profileRecycler?.visibility = View.VISIBLE
        adapter.list = posts
        adapter.notifyDataSetChanged()
    }

    private fun openDialog() {
        mainPhoto?.openDialogForPhoto()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is AttachListenerPhoto) {
            mainPhoto = context
        }
    }

    override fun onDestroy() {
        mainPhoto = null
        super.onDestroy()
    }

}