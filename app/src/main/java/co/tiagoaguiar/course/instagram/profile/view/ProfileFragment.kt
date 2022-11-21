package co.tiagoaguiar.course.instagram.profile.view

import android.annotation.SuppressLint
import android.content.Context
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import co.tiagoaguiar.course.instagram.R
import co.tiagoaguiar.course.instagram.common.base.BaseFragment
import co.tiagoaguiar.course.instagram.common.base.DependencyInjector
import co.tiagoaguiar.course.instagram.common.model.Post
import co.tiagoaguiar.course.instagram.common.model.User
import co.tiagoaguiar.course.instagram.common.view.PostZoom
import co.tiagoaguiar.course.instagram.databinding.FragmentMainProfileBinding
import co.tiagoaguiar.course.instagram.main.AttachListenerLogout
import co.tiagoaguiar.course.instagram.main.AttachListenerPhoto
import co.tiagoaguiar.course.instagram.profile.Profile
import co.tiagoaguiar.course.instagram.profile.util.ProfileAdapter
import com.bumptech.glide.Glide
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

    private var follow: Follow? = null

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
                        profileIncludeProfile.profileBtnEditProfile.text =
                            getString(R.string.follow)
                        profileIncludeProfile.profileBtnEditProfile.backgroundTintList =
                            ContextCompat.getColorStateList(requireContext(), R.color.blue_enabled)
                        it.tag = false

                        presenter.followUser(uuid, false)

                    } else if (it.tag == false) {
                        profileIncludeProfile.profileBtnEditProfile.text =
                            getString(R.string.unfollow)
                        profileIncludeProfile.profileBtnEditProfile.backgroundTintList =
                            ContextCompat.getColorStateList(requireContext(), R.color.white)
                        it.tag = true

                        presenter.followUser(uuid, true)
                    }
                }

                presenter.fetchUserProfile(uuid)

            }
        }
    }

    override fun getMenu() = R.menu.menu_defaut

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

    override fun displayUserProfile(user: Pair<User, Boolean?>) {
        val (userAuth, following) = user

        binding?.let { binding ->
            with(binding) {
                profileIncludeProfile.profileTxtUsername.text = userAuth.username
                profileIncludeProfile.profileTxtPostCount.text = userAuth.postCount.toString()
                profileIncludeProfile.profileTxtFollowingCount.text =
                    userAuth.following.toString()
                profileIncludeProfile.profileTxtFollowersCount.text =
                    userAuth.followers.toString()
                Glide.with(requireContext()).load(userAuth.photoUrl)
                    .into(profileIncludeProfile.profileImgIcon)

                if (userAuth.photoUrl != null) profileIncludeProfile.profileImgAdd.visibility =
                    View.GONE


                profileIncludeProfile.profileBtnEditProfile.text = when (following) {
                    null -> getString(R.string.edit_profile)
                    true -> {
                        profileIncludeProfile.profileImgAdd.visibility = View.GONE
                        getString(R.string.unfollow)
                    }
                    false -> {
                        profileIncludeProfile.profileImgAdd.visibility = View.GONE
                        profileIncludeProfile.profileBtnEditProfile.backgroundTintList =
                            ContextCompat.getColorStateList(
                                requireContext(),
                                R.color.blue_enabled
                            )
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

    @SuppressLint("NotifyDataSetChanged")
    override fun onUpdatePostsSuccess(posts: List<Post>) {
        binding?.profileRecycler?.visibility = View.VISIBLE
        adapter.list = posts
        adapter.notifyDataSetChanged()
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

    private fun openDialog() {
        attachListenerPhoto?.openDialogForPhoto()
    }

    @SuppressLint("NewApi")
    private val onLongClickItem: (Post) -> Unit = { post: Post ->
        PostZoom(requireContext()).apply {
            setImageProfileURL(post.publisher?.photoUrl!!)
            setTitle(post.publisher.username)
            setImageURL(post.photoUrl!!)
            setCaption(post.description ?: "")
            show()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is AttachListenerPhoto)
            attachListenerPhoto = context

        if (context is AttachListenerLogout)
            logout = context

        if (context is Follow)
            follow = context

    }

    override fun onDestroy() {
        attachListenerPhoto = null
        follow = null
        logout = null
        super.onDestroy()
    }

    interface Follow{
        fun onFollow()
    }

}