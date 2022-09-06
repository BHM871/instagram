package co.tiagoaguiar.course.instagram.home.view

import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.recyclerview.widget.LinearLayoutManager
import co.tiagoaguiar.course.instagram.R
import co.tiagoaguiar.course.instagram.common.base.BaseFragment
import co.tiagoaguiar.course.instagram.common.base.DependencyInjector
import co.tiagoaguiar.course.instagram.common.model.Database
import co.tiagoaguiar.course.instagram.common.model.Post
import co.tiagoaguiar.course.instagram.common.model.UserAuth
import co.tiagoaguiar.course.instagram.databinding.FragmentMainHomeBinding
import co.tiagoaguiar.course.instagram.home.Home
import co.tiagoaguiar.course.instagram.home.util.HomeAdapter
import co.tiagoaguiar.course.instagram.main.AttachListenerLogout
import co.tiagoaguiar.course.instagram.main.AttachListenerPhoto

@SuppressLint("ResourceType")
class HomeFragment : BaseFragment<FragmentMainHomeBinding, Home.Presenter>(
    R.layout.fragment_main_home,
    FragmentMainHomeBinding::bind
), Home.View {

    override lateinit var presenter: Home.Presenter

    private var attachListenerPhoto: AttachListenerPhoto? = null
    private val storyAdapter = HomeAdapter<UserAuth>()

    private var logout: AttachListenerLogout? = null

    private val homeAdapter by lazy { HomeAdapter<Post>(onClickItemPost) }

    override fun setupPresenter() {
        presenter = DependencyInjector.mainHomePresenter(this)
    }

    override fun setupViews() {
        binding?.homeContainerMyStory?.containerMyStory?.itemHomeStoryImg?.setImageURI(Database.sessionAuth?.photoUri)
        binding?.homeContainerMyStory?.containerMyStory?.itemHomeStoryTxt?.text =
            getText(R.string.your_story)
        val list: MutableList<UserAuth> =
            Database.usersAuth.filter { it.name != Database.sessionAuth!!.name } as MutableList<UserAuth>
        storyAdapter.list = list

        binding?.homeRecyclerStory?.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding?.homeRecyclerStory?.adapter = storyAdapter

        binding?.homeRecyclerFeed?.layoutManager =
            LinearLayoutManager(requireContext())
        binding?.homeRecyclerFeed?.adapter = homeAdapter

        presenter.fetchFeed()
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
        binding?.homeProgressBar?.visibility = if (enabled) View.VISIBLE else View.GONE
    }

    override fun displayRequestFailure(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun displayEmptyFeed() {
        binding?.homeRecyclerFeed?.visibility = View.GONE
        binding?.profileTxtEmptyList?.visibility = View.VISIBLE
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun displayFullFeed(posts: List<Post>) {
        binding?.homeRecyclerFeed?.visibility = View.VISIBLE
        homeAdapter.list = posts
        homeAdapter.notifyDataSetChanged()
    }

    private val onClickItemPost: (Post, View, HashMap<String, View>) -> Unit =
        { post: Post, viewClicked: View, views: HashMap<String, View> ->
            val imgLike = (views["imgLike"] as ImageView)
            val iconLike = (views["iconLike"] as ImageView)

            when (viewClicked.id) {
                R.id.item_home_img_post -> onDoubleClickItem(post, imgLike, iconLike)
                R.id.item_home_container_img_like -> like(post, imgLike, iconLike)
            }
        }

    private fun onDoubleClickItem(post: Post, imgLike: ImageView, iconLike: ImageView) {
        post.countClick++
        if (post.countClick == 2) {
            imgLike.animate().apply {
                duration = 250
                alpha(1.0f)
                start()
            }

            iconLike.isSelected = !iconLike.isSelected
            presenter.liked(post, iconLike.isSelected)

            Handler(Looper.getMainLooper()).postDelayed({
                imgLike.animate().apply {
                    duration = 250
                    alpha(0f)
                    start()
                }
            }, 250)
        }
        Handler(Looper.getMainLooper()).postDelayed({
            post.countClick = 0
        }, 200)
    }

    private fun like(post: Post, imgLike: ImageView, iconLike: ImageView) {
        imgLike.animate().apply {
            duration = 250
            alpha(1.0f)
            start()
        }

        iconLike.isSelected = !iconLike.isSelected
        presenter.liked(post, iconLike.isSelected)

        Handler(Looper.getMainLooper()).postDelayed({
            imgLike.animate().apply {
                duration = 250
                alpha(0f)
                start()
            }
        }, 250)
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
}