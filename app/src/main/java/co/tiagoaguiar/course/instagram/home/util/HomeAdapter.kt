package co.tiagoaguiar.course.instagram.home.util

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import co.tiagoaguiar.course.instagram.R
import co.tiagoaguiar.course.instagram.common.model.Post
import de.hdodenhof.circleimageview.CircleImageView

class HomeAdapter : RecyclerView.Adapter<HomeAdapter.HomeHolder>() {

    var list: List<Post> = mutableListOf()
    private var listener: ((Post, View, HashMap<String, View>) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeHolder =
        HomeHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_home_post, parent, false)
        )

    override fun onBindViewHolder(holder: HomeHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size

    fun setListener(listener: (Post, View, HashMap<String, View>) -> Unit) {
        this.listener = listener
    }

    inner class HomeHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private lateinit var imgProfile: CircleImageView
        private lateinit var imgPost: ImageView

        private lateinit var imgLike: ImageView
        private lateinit var iconLike: ImageView

        fun bind(post: Post) = with(itemView) {
            imgProfile = findViewById(R.id.item_home_img_user)
            imgPost = findViewById(R.id.item_home_img_post)
            imgLike = findViewById(R.id.item_home_img_heart)
            iconLike = findViewById(R.id.item_home_container_img_like)

            imgPost.setImageResource(R.drawable.ic_insta_add)
            iconLike.isActivated = false

            val a = ContextCompat.getDrawable(context, R.color.like)
            iconLike.background = a

            val listViews = hashMapOf<String, View>()
            listViews["imgLike"] = imgLike
            listViews["iconLike"] = iconLike


            imgPost.setOnClickListener {
                listener?.invoke(post, it, listViews)
            }
            iconLike.setOnClickListener {
                listener?.invoke(post, it, listViews)
            }
        }

    }

}