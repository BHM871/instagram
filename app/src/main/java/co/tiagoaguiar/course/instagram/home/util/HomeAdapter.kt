package co.tiagoaguiar.course.instagram.home.util

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
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

        fun bind(post: Post) = with(itemView) {
            val imgProfile = findViewById<CircleImageView>(R.id.item_home_img_user)

            val imgPost = findViewById<ImageView>(R.id.item_home_img_post)
            val imgLike = findViewById<ImageView>(R.id.item_home_img_heart)
            val iconLike = findViewById<ImageView>(R.id.item_home_container_img_like)

            val txtUser = findViewById<TextView>(R.id.item_home_txt_username)
            val txtDesc = findViewById<TextView>(R.id.item_home_txt_caption)

            imgProfile.setImageURI(post.publisher.photoUri)

            imgPost.setImageURI(post.uri)

            txtUser.text = post.publisher.username
            txtDesc.text = post.description
            if (post.description != "")
                txtDesc.visibility = View.VISIBLE

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