package co.tiagoaguiar.course.instagram.search.utiil

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import co.tiagoaguiar.course.instagram.R
import co.tiagoaguiar.course.instagram.common.model.User
import com.bumptech.glide.Glide
import de.hdodenhof.circleimageview.CircleImageView

class SearchAdapter(
    private val itemClick: (String) -> Unit
) : RecyclerView.Adapter<SearchAdapter.SearchHolder>() {

    val list = mutableListOf<User>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchHolder{
        return SearchHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_search_users, parent, false))
    }

    override fun onBindViewHolder(holder: SearchHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size

    inner class SearchHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(user: User) = with(itemView){

            Glide.with(context).load(user.photoUrl).into(findViewById<CircleImageView>(R.id.item_search_img_user))

            findViewById<TextView>(R.id.item_search_txt_username).text = user.username

            setOnClickListener {
                itemClick.invoke(user.uuid!!)
            }
        }

    }

}