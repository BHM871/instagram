package co.tiagoaguiar.course.instagram.post.data

import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.MediaStore

class PostLocalDataSource(private val context: Context) : PostDataSource {

    override suspend fun fetchPictures(): List<Uri> {
        val collection = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
        } else {
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        }

        val projection = arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.WIDTH,
            MediaStore.Images.Media.HEIGHT
        )

        val pictures = mutableListOf<Uri>()

        context.contentResolver.query(
            collection,
            projection,
            null,
            null,
            "${MediaStore.Images.Media._ID} DESC"
        )?.use { cursor ->
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)

            while (cursor.moveToNext()){
                val id = cursor.getLong(idColumn)
                val uri = ContentUris.withAppendedId(collection, id)

                pictures.add(uri)

                if (pictures.size == 99) break
            }
        }

        return pictures
    }

}