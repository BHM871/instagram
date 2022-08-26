package co.tiagoaguiar.course.instagram.search.view

import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import co.tiagoaguiar.course.instagram.R
import co.tiagoaguiar.course.instagram.databinding.FragmentMainSearchBinding
import co.tiagoaguiar.course.instagram.databinding.ItemSearchUsersBinding
import co.tiagoaguiar.course.instagram.main.AttachListenerPhoto
import de.hdodenhof.circleimageview.CircleImageView

class SearchFragment : Fragment(R.layout.fragment_main_search) {

    private var binding: FragmentMainSearchBinding? = null

    private var attachListenerPhoto: AttachListenerPhoto? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_profile, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentMainSearchBinding.bind(view)

        binding?.let { binding ->
            with(binding) {
                searchRecycler.layoutManager = LinearLayoutManager(requireContext())
                searchRecycler.adapter = SearchAdapter()
            }
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

    private class SearchAdapter : RecyclerView.Adapter<SearchAdapter.SearchHolder>() {

        private var binding: ItemSearchUsersBinding? = null
        private lateinit var context: Context

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchHolder{
            context = parent.context
            return SearchHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_search_users, parent, false))
        }

        override fun onBindViewHolder(holder: SearchHolder, position: Int) {
            holder.bind(R.drawable.ic_insta_add)
        }

        override fun getItemCount(): Int = 10

        private inner class SearchHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

            fun bind(image: Int) {
                itemView.findViewById<CircleImageView>(R.id.item_search_img_user).setImageResource(image)
            }

        }

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is AttachListenerPhoto)
            attachListenerPhoto = context
    }

    override fun onDestroy() {
        binding = null
        attachListenerPhoto = null
        super.onDestroy()
    }

}