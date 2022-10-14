package co.tiagoaguiar.course.instagram.profile.data

import co.tiagoaguiar.course.instagram.common.base.Cache
import co.tiagoaguiar.course.instagram.common.base.RequestCallback
import co.tiagoaguiar.course.instagram.common.model.Post
import co.tiagoaguiar.course.instagram.common.model.User
import com.google.firebase.auth.FirebaseAuth

class ProfileLocalDataSource(
    private val userCache: Cache<Pair<User, Boolean?>>,
    private val postCache: Cache<List<Post>>
) : ProfileDataSource {

    override fun fetchUserProfile(userUUID: String, callback: RequestCallback<Pair<User, Boolean?>>) {
        val userAuth = userCache.get(userUUID)

        if (userAuth == null) {
            callback.onFailure("User not found")
        } else {
            callback.onSuccess(userAuth)
        }

        callback.onComplete()
    }

    override fun fetchUserPost(userUUID: String, callback: RequestCallback<List<Post>>) {
        val posts = postCache.get(userUUID)

        if (posts == null) {
            callback.onFailure("List not found")
        } else {
            callback.onSuccess(posts)
        }

        callback.onComplete()
    }

    override fun fetchSession(): String {
        return FirebaseAuth.getInstance().uid ?: throw RuntimeException("User not found")
    }

    override fun putUser(response: Pair<User, Boolean?>) {
        userCache.put(response)
    }

    override fun putPosts(response: List<Post>) {
        postCache.put(response)
    }

    override fun removeCache() {
        postCache.remove()
        userCache.remove()
    }

}