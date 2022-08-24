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

    override fun fetchUserProfile(userUUID: String, callback: RequestCallback<UserAuth>) {
        Handler(Looper.getMainLooper()).postDelayed({
            val userAuth = Database.usersAuth.firstOrNull { userUUID == it.uuid }

            if (userAuth != null) {
                callback.onSuccess(userAuth)
            } else {
                callback.onFailure("User not found")
            }

            callback.onComplete()
        }, 2000)
    }

    override fun fetchUserPhoto(userUUID: String, callback: RequestCallback<Photo?>) {
        val photo = Database.photo[userUUID]

        if (photo != null){
            callback.onSuccess(photo)
        } else {
            callback.onFailure("Photo not found")
        }

        callback.onComplete()
    }

    override fun fetchUserPost(userUUID: String, callback: RequestCallback<List<Post>>) {
        Handler(Looper.getMainLooper()).postDelayed({
            val posts = Database.posts[userUUID]

            callback.onSuccess(posts?.toList() ?: emptyList())

            callback.onComplete()
        }, 2000)
    }

    override fun updatePhoto(userUUID: String, photoUri: Uri, callback: RequestCallback<Photo>) {
        Handler(Looper.getMainLooper()).postDelayed({
            val userAuth = Database.sessionAuth

            if (userAuth == null) {
                callback.onFailure("User not found")
            } else {
                val photo = Photo(userUUID, photoUri)
                Database.photo[userUUID] = photo

                callback.onSuccess(photo)
            }

            callback.onComplete()
        }, 2000)
    }
}