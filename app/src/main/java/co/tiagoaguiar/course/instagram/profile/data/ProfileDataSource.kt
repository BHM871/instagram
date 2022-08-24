package co.tiagoaguiar.course.instagram.profile.data

import android.net.Uri
import co.tiagoaguiar.course.instagram.common.base.RequestCallback
import co.tiagoaguiar.course.instagram.common.model.Photo
import co.tiagoaguiar.course.instagram.common.model.Post
import co.tiagoaguiar.course.instagram.common.model.UserAuth

interface ProfileDataSource {

    fun fetchUserProfile(userUUID: String, callback: RequestCallback<UserAuth>)
    fun fetchUserPhoto(userUUID: String, callback: RequestCallback<Photo?>)
    fun fetchUserPost(userUUID: String, callback: RequestCallback<List<Post>>)

    fun fetchSession() : UserAuth { throw UnsupportedOperationException("User not found") }
    fun putUser(response: UserAuth) { throw UnsupportedOperationException("User not found") }
    fun putPhoto(response: Photo?) { throw UnsupportedOperationException("User not found") }
    fun putPosts(response: List<Post>) { throw UnsupportedOperationException("User not found") }

    fun updatePhoto(userUUID: String, photoUri: Uri, callback: RequestCallback<Photo>) { throw UnsupportedOperationException("User not found") }
    fun updatePosts(userUUID: String, post: Post, callback: RequestCallback<List<Post>>) { throw UnsupportedOperationException("User not found") }
}