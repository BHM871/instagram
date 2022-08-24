package co.tiagoaguiar.course.instagram.profile.util

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import co.tiagoaguiar.course.instagram.R
import co.tiagoaguiar.course.instagram.common.model.Post

class ProfileAdapter : RecyclerView.Adapter<ProfileAdapter.ProfileHolder>() {

    var list: List<Post>  = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileHolder =
        ProfileHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_profile_grid, parent, false)
        )

    override fun onBindViewHolder(holder: ProfileHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size

    class ProfileHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(post: Post) {
            itemView.findViewById<ImageView>(R.id.item_profile_img_grid).setImageURI(post.uri)
        }

    }

}