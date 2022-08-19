package co.tiagoaguiar.course.instagram.profile.data

import android.net.Uri
import android.os.Handler
import android.os.Looper
import co.tiagoaguiar.course.instagram.common.base.RequestCallback
import co.tiagoaguiar.course.instagram.common.model.Database
import co.tiagoaguiar.course.instagram.common.model.Photo
import co.tiagoaguiar.course.instagram.common.model.Post
import co.tiagoaguiar.course.instagram.common.model.UserAuth

class ProfileFakeDataSource : ProfileDataSource {

    override fun fetchUserProfile(uuid: String, callback: RequestCallback<UserAuth>) {
        Handler(Looper.getMainLooper()).postDelayed({
            val userAuth = Database.usersAuth.firstOrNull { uuid == it.uuid }

            if (userAuth != null) {
                callback.onSuccess(userAuth)
            } else {
                callback.onFailure("Usuario não encontrado")
            }

            callback.onComplete()
        }, 2000)
    }

    override fun fetchUserPost(uuid: String, callback: RequestCallback<List<Post>>) {
        Handler(Looper.getMainLooper()).postDelayed({
            val posts = Database.posts[uuid]

            callback.onSuccess(posts?.toList() ?: emptyList())

            callback.onComplete()
        }, 2000)
    }

    override fun updateProfile(uuid: String, photoUri: Uri, callback: RequestCallback<Photo>) {
        Handler(Looper.getMainLooper()).postDelayed({
            val userAuth = Database.usersAuth.firstOrNull{ it.uuid == uuid }

            if (userAuth == null) {
                callback.onFailure("Usuario não encontrado")
            } else {
                val photo = Photo(uuid, photoUri)
                val created = Database.photo.add(photo)

                if (created) {
                    callback.onSuccess(photo)
                }
            }

            callback.onComplete()
        }, 2000)
    }
}