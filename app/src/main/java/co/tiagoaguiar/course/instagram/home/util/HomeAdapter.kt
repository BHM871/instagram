package co.tiagoaguiar.course.instagram.home.util

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import co.tiagoaguiar.course.instagram.R
import co.tiagoaguiar.course.instagram.common.model.Post
import co.tiagoaguiar.course.instagram.common.model.UserAuth
import de.hdodenhof.circleimageview.CircleImageView

class HomeAdapter<T>(
    private val listenerPost: ((Post, View, HashMap<String, View>) -> Unit)? = null
) : RecyclerView.Adapter<HomeAdapter<T>.HomeHolder>() {

    var list: List<T> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeHolder {
        return try {
            val item = list.first() as Post
            HomeHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_home_post, parent, false))
        } catch (e: Exception) {
            HomeHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_home_story, parent, false))
        }
    }

    override fun onBindViewHolder(holder: HomeHolder, position: Int) {
        try {
            val item = list[position] as Post
            holder.bindPost(list[position])
        } catch (e: Exception){
            holder.bindStory(list[position])
        }
    }

    override fun getItemCount(): Int = list.size

    inner class HomeHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindPost(post: T) = with(itemView) {
            post as Post
            val imgProfile = findViewById<CircleImageView>(R.id.item_home_img_user)

            val imgPost = findViewById<ImageView>(R.id.item_home_img_post)
            val imgLike = findViewById<ImageView>(R.id.item_home_img_heart)
            val iconLike = findViewById<ImageView>(R.id.item_home_container_img_like)

            val txtUser = findViewById<TextView>(R.id.item_home_txt_username)
            val txtDesc = findViewById<TextView>(R.id.item_home_txt_caption)

            imgProfile.setImageURI(post.publisher.photoUri)

            imgPost.setImageURI(post.uri)
            iconLike.isSelected = post.like

            txtUser.text = post.publisher.username
            txtDesc.text = post.description
            if (post.description != "")
                txtDesc.visibility = View.VISIBLE

            val listViews = hashMapOf<String, View>()
            listViews["imgLike"] = imgLike
            listViews["iconLike"] = iconLike

            imgPost.setOnClickListener {
                listenerPost?.invoke(post, it, listViews)
            }
            iconLike.setOnClickListener {
                listenerPost?.invoke(post, it, listViews)
            }
        }

        fun bindStory(user: T) = with(itemView) {
            user as UserAuth
            val img = findViewById<CircleImageView>(R.id.item_home_story_img)
            val txt = findViewById<TextView>(R.id.item_home_story_txt)

            img.setImageURI(user.photoUri)
            txt.text = user.username
        }

    }

}