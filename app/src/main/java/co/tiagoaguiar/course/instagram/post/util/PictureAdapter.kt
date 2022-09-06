package co.tiagoaguiar.course.instagram.post.util

import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.util.Size
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import co.tiagoaguiar.course.instagram.R

class PictureAdapter : RecyclerView.Adapter<PictureAdapter.PictureHolder>() {

    var list: List<Uri> = mutableListOf()

    private var listener: ((Uri) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PictureHolder =
        PictureHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_profile_grid, parent, false)
        )

    override fun onBindViewHolder(holder: PictureHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size

    fun setListener(listener: (Uri) -> Unit) {
        this.listener = listener
    }

    inner class PictureHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(picture: Uri) = with(itemView) {
            val img = findViewById<ImageView>(R.id.item_profile_img_grid)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            try {
                val bitmap = context.contentResolver.loadThumbnail(picture, Size(200, 200), null)
                img.setImageBitmap(bitmap)
            } catch (e: Exception){
                Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
            }

                img.setOnClickListener {
                    listener?.invoke(picture)
                }
            }
        }

    }

}