package co.tiagoaguiar.course.instagram.search.view

import android.annotation.SuppressLint
import android.app.SearchManager
import android.content.Context
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.recyclerview.widget.LinearLayoutManager
import co.tiagoaguiar.course.instagram.R
import co.tiagoaguiar.course.instagram.common.base.BaseFragment
import co.tiagoaguiar.course.instagram.common.base.DependencyInjector
import co.tiagoaguiar.course.instagram.common.model.UserAuth
import co.tiagoaguiar.course.instagram.databinding.FragmentMainSearchBinding
import co.tiagoaguiar.course.instagram.main.AttachListenerPhoto
import co.tiagoaguiar.course.instagram.search.Search
import co.tiagoaguiar.course.instagram.search.utiil.SearchAdapter

class SearchFragment : BaseFragment<FragmentMainSearchBinding, Search.Presenter>(
    R.layout.fragment_main_search,
    FragmentMainSearchBinding::bind
), Search.View {

    override lateinit var presenter: Search.Presenter

    private var attachListenerPhoto: AttachListenerPhoto? = null

    private val adapter by lazy { SearchAdapter(onClickItem) }

    private var searchListener: SearchListener? = null

    override fun setupPresenter() {
        presenter = DependencyInjector.searchPresenter(this)
    }

    override fun setupViews() {
        binding?.let { binding ->
            with(binding) {
                searchRecycler.layoutManager = LinearLayoutManager(requireContext())
                searchRecycler.adapter = adapter
            }
        }
    }

    override fun getMenu(): Int = R.menu.menu_seach

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        val serviceManager = requireActivity().getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.menu_users_search).actionView as SearchView

        searchView.apply {
            setSearchableInfo(serviceManager.getSearchableInfo(requireActivity().componentName))
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    if (newText != null){
                        presenter.fetchUsers(newText)
                    }
                    return false
                }
            })
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
        binding?.searchProgressBar?.visibility = if (enabled) View.VISIBLE else View.GONE
    }

    override fun displayEmptyUsers() {
        binding?.searchTxtEmptyList?.visibility = View.VISIBLE
        binding?.searchRecycler?.visibility = View.GONE
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun displayFullUsers(users: List<UserAuth>) {
        binding?.searchTxtEmptyList?.visibility = View.GONE
        binding?.searchRecycler?.visibility = View.VISIBLE
        adapter.list.clear()
        adapter.list.addAll(users)
        adapter.notifyDataSetChanged()
    }

    private val onClickItem: (String) -> Unit = { uuid ->
        searchListener?.goToProfile(uuid)
    }

    interface SearchListener {
        fun goToProfile(uuid: String)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is AttachListenerPhoto)
            attachListenerPhoto = context

        if (context is SearchListener)
            searchListener = context
    }

    override fun onDestroy() {
        attachListenerPhoto = null
        searchListener = null
        super.onDestroy()
    }

}