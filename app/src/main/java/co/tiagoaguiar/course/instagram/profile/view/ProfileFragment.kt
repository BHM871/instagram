package co.tiagoaguiar.course.instagram.profile.view

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import co.tiagoaguiar.course.instagram.R
import co.tiagoaguiar.course.instagram.common.base.BaseFragment
import co.tiagoaguiar.course.instagram.common.base.DependencyInjector
import co.tiagoaguiar.course.instagram.common.model.Post
import co.tiagoaguiar.course.instagram.common.model.UserAuth
import co.tiagoaguiar.course.instagram.common.view.ImageCroppedFragment
import co.tiagoaguiar.course.instagram.common.view.PostZoom
import co.tiagoaguiar.course.instagram.databinding.FragmentMainProfileBinding
import co.tiagoaguiar.course.instagram.main.AttachListenerLogout
import co.tiagoaguiar.course.instagram.main.AttachListenerPhoto
import co.tiagoaguiar.course.instagram.main.view.MainActivity
import co.tiagoaguiar.course.instagram.profile.Profile
import co.tiagoaguiar.course.instagram.profile.util.ProfileAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView

class ProfileFragment : BaseFragment<FragmentMainProfileBinding, Profile.Presenter>(
    R.layout.fragment_main_profile,
    FragmentMainProfileBinding::bind
), Profile.View, BottomNavigationView.OnNavigationItemSelectedListener {

    companion object {
        const val KEY_USER_ID = "key_user_id"
    }

    override lateinit var presenter: Profile.Presenter

    private var attachListenerPhoto: AttachListenerPhoto? = null
    private var currentPhoto: Uri? = null

    private var logout: AttachListenerLogout? = null

    private val adapter by lazy { ProfileAdapter(onLongClickItem) }

    private var uuid: String? = null

    override fun setupPresenter() {
        presenter = DependencyInjector.mainProfilePresenter(this)
    }

    override fun setupViews() {
        binding?.let { binding ->
            with(binding) {

                uuid = arguments?.getString(KEY_USER_ID)

                menuProfileNav.selectedItemId = R.id.menu_profile_grid

                menuProfileNav.setOnNavigationItemSelectedListener(this@ProfileFragment)

                profileRecycler.layoutManager = GridLayoutManager(requireContext(), 3)
                profileRecycler.adapter = adapter

                profileIncludeProfile.profileImgIcon.setOnClickListener {
                    if (uuid == null) openDialog()
                }

                profileIncludeProfile.profileBtnEditProfile.setOnClickListener {
                    if (it.tag == true) {
                        profileIncludeProfile.profileBtnEditProfile.text = getString(R.string.follow)
                        profileIncludeProfile.profileBtnEditProfile.backgroundTintList =
                            ContextCompat.getColorStateList(requireContext(), R.color.blue_enabled)
                        it.tag = false

                        presenter.followUser(uuid, false)
                    } else if (it.tag == false) {
                        profileIncludeProfile.profileBtnEditProfile.text = getString(R.string.unfollow)
                        profileIncludeProfile.profileBtnEditProfile.backgroundTintList =
                            ContextCompat.getColorStateList(requireContext(), R.color.white)
                        it.tag = true

                        presenter.followUser(uuid, true)
                    }
                }

                if (uuid != null) profileIncludeProfile.profileImgAdd.visibility =
                    View.GONE

                presenter.fetchUserProfile(uuid)

            }
        }
    }

    override fun getMenu() = R.menu.menu_defaut

    override fun getFragmentResult() {
        setFragmentResultListener("cropKey") { _, bundle ->
            val uri = bundle.getParcelable<Uri>(ImageCroppedFragment.KEY_URI)
            currentPhoto = uri
            currentPhoto?.let { presenter.updatePhoto(it) }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                setFragmentResult("addScreen", bundleOf("screen" to "camera"))
                attachListenerPhoto?.goToFragmentCamera()
            }
            R.id.menu_add -> {
                setFragmentResult("addScreen", bundleOf("screen" to "gallery"))
                attachListenerPhoto?.gotoFragmentGallery()
            }
            R.id.menu_logout -> {
                logout?.goToLoginLogout()
            }
        }
        return false
    }

    override fun showProgress(enabled: Boolean) {
        binding?.profileProgressBar?.visibility = if (enabled) View.VISIBLE else View.GONE
    }

    override fun displayUserProfile(user: Pair<UserAuth, Boolean?>) {
        val (userAuth, following) = user

        binding?.let { binding ->
            with(binding) {
                profileIncludeProfile.profileTxtUsername.text = userAuth.username
                profileIncludeProfile.profileTxtPostCount.text = userAuth.postCount.toString()
                profileIncludeProfile.profileTxtFollowingCount.text =
                    userAuth.followingCount.toString()
                profileIncludeProfile.profileTxtFollowersCount.text =
                    userAuth.followersCount.toString()
                profileIncludeProfile.profileImgIcon.setImageURI(userAuth.photoUri)
                if (userAuth.photoUri != null) profileIncludeProfile.profileImgAdd.visibility =
                    View.GONE

                profileIncludeProfile.profileBtnEditProfile.text = when (following) {
                    null -> getString(R.string.edit_profile)
                    true -> getString(R.string.unfollow)
                    false -> {
                        profileIncludeProfile.profileBtnEditProfile.backgroundTintList =
                            ContextCompat.getColorStateList(requireContext(), R.color.blue_enabled)
                        getString(R.string.follow)
                    }
                }

                profileIncludeProfile.profileBtnEditProfile.tag = following

                presenter.fetchUserPosts(uuid)
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

    override fun follow(isFollow: Boolean) {
        if (isFollow){
            (requireActivity() as MainActivity).onPostCreated()
        }
    }

    private fun openDialog() {
        attachListenerPhoto?.openDialogForPhoto()
    }

    @SuppressLint("NewApi")
    private val onLongClickItem: (Post) -> Unit = { post: Post ->
        PostZoom(requireContext()).apply {
            setImageProfile(post.publisher.photoUri ?: Uri.EMPTY)
            setTitle(post.publisher.username)
            setImage(post.uri)
            setCaption(post.description)
            show()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is AttachListenerPhoto)
            attachListenerPhoto = context

        if (context is AttachListenerLogout)
            logout = context

    }

    override fun onDestroy() {
        attachListenerPhoto = null
        logout = null
        super.onDestroy()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_profile_grid ->
                binding?.profileRecycler?.layoutManager = GridLayoutManager(requireContext(), 3)
            R.id.menu_profile_videos ->
                binding?.profileRecycler?.layoutManager = LinearLayoutManager(requireContext())
        }
        return true
    }

}