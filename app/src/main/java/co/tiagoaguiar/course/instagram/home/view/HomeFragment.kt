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

    private val homeAdapter by lazy { HomeAdapter<Post>() }

    override fun setupPresenter() {
        presenter = DependencyInjector.mainHomePresenter(this)
    }

    override fun setupViews() {
        binding?.homeContainerMyStory?.containerMyStory?.itemHomeStoryImg?.setImageURI(Database.sessionAuth?.photoUri)
        binding?.homeContainerMyStory?.containerMyStory?.itemHomeStoryTxt?.text =
            getText(R.string.your_story)
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