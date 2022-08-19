package co.tiagoaguiar.course.instagram.home.view

import android.annotation.SuppressLint
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import co.tiagoaguiar.course.instagram.R
import co.tiagoaguiar.course.instagram.common.base.BaseFragment
import co.tiagoaguiar.course.instagram.common.base.DependencyInjector
import co.tiagoaguiar.course.instagram.common.model.Database
import co.tiagoaguiar.course.instagram.common.model.Post
import co.tiagoaguiar.course.instagram.databinding.FragmentMainHomeBinding
import co.tiagoaguiar.course.instagram.home.Home
import co.tiagoaguiar.course.instagram.home.util.HomeAdapter

@SuppressLint("ResourceType")
class HomeFragment : BaseFragment<FragmentMainHomeBinding, Home.Presenter>(
    R.layout.fragment_main_home,
    FragmentMainHomeBinding::bind
), Home.View {

    override lateinit var presenter: Home.Presenter

    private var adapter = HomeAdapter()

    override fun setupPresenter() {
        presenter = DependencyInjector.mainHomePresenter(this)
    }

    override fun setupViews() {
        adapter.setListener(onClickItem)
        binding?.homeRecycler?.layoutManager = LinearLayoutManager(requireContext())
        binding?.homeRecycler?.adapter = adapter

        presenter.fetchFeed()
    }

    override fun getMenu() = R.menu.menu_profile

    override fun showProgress(enabled: Boolean) {
        binding?.homeProgressBar?.visibility = if (enabled) View.VISIBLE else View.GONE
    }

    override fun displayRequestFailure(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun displayEmptyFeed() {
        binding?.homeRecycler?.visibility = View.GONE
        binding?.profileTxtEmptyList?.visibility = View.VISIBLE
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun displayFullFeed(posts: List<Post>) {
        binding?.homeRecycler?.visibility = View.VISIBLE
        adapter.list = posts
        adapter.notifyDataSetChanged()
    }

    private val onClickItem = object : (Post, View, HashMap<String, View>) -> Unit {
        override fun invoke(post: Post, clivkView: View, views: HashMap<String, View>) {
            val imgLike = (views["imgLike"] as ImageView)
            val iconLike = (views["iconLike"] as ImageView)

            when(clivkView.id) {
                R.id.item_home_img_post -> doubleClickLike(post, imgLike, iconLike)
                R.id.item_home_container_img_like -> like(imgLike, iconLike)
            }
        }
    }

    private fun doubleClickLike(post: Post, imgLike: ImageView, iconLike: ImageView) {
        post.countClick++
        if (post.countClick == 2) {
            imgLike.animate().apply {
                duration = 200
                alpha(1.0f)
                start()
            }

            iconLike.isSelected = !iconLike.isSelected

            Handler(Looper.getMainLooper()).postDelayed({
                imgLike.animate().apply {
                    duration = 200
                    alpha(0f)
                    start()
                }
            }, 200)
        }
        Handler(Looper.getMainLooper()).postDelayed({
            post.countClick = 0
        }, 200)
    }

    private fun like(imgLike: ImageView, iconLike: ImageView) {
        imgLike.animate().apply {
            duration = 200
            alpha(1.0f)
            start()
        }

        iconLike.isSelected = !iconLike.isSelected

        Handler(Looper.getMainLooper()).postDelayed({
            imgLike.animate().apply {
                duration = 200
                alpha(0f)
                start()
            }
        }, 200)
    }
}