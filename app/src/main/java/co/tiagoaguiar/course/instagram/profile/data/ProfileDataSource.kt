package co.tiagoaguiar.course.instagram.profile.data

import android.net.Uri
import co.tiagoaguiar.course.instagram.common.base.RequestCallback
import co.tiagoaguiar.course.instagram.common.model.Post
import co.tiagoaguiar.course.instagram.common.model.User

interface ProfileDataSource {

    fun fetchUserProfile(userUUID: String, callback: RequestCallback<Pair<User, Boolean?>>)
    fun fetchUserPost(userUUID: String, callback: RequestCallback<List<Post>>)

    fun updatePhoto(userUUID: String, photoUri: Uri, callback: RequestCallback<Uri>) { throw UnsupportedOperationException("User not found") }
    fun followUser(userUUID: String, isFollow: Boolean, callback: RequestCallback<Boolean>) { throw UnsupportedOperationException("User not found") }

    fun fetchSession() : String { throw UnsupportedOperationException("User not found") }
    fun putUser(response: Pair<User, Boolean?>) { throw UnsupportedOperationException("User not found") }
    fun putPosts(response: List<Post>) { throw UnsupportedOperationException("User not found") }
    fun removeCache() { throw UnsupportedOperationException("User not found") }
}