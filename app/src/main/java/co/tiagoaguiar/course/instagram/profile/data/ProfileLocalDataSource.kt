package co.tiagoaguiar.course.instagram.profile.data

import co.tiagoaguiar.course.instagram.common.base.Cache
import co.tiagoaguiar.course.instagram.common.base.RequestCallback
import co.tiagoaguiar.course.instagram.common.model.Database
import co.tiagoaguiar.course.instagram.common.model.Post
import co.tiagoaguiar.course.instagram.common.model.UserAuth

class ProfileLocalDataSource(
    private val userCache: Cache<UserAuth>,
    private val postCache: Cache<List<Post>>
) : ProfileDataSource {

    override fun fetchUserProfile(userUUID: String, callback: RequestCallback<UserAuth>) {
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

    override fun fetchSession(): UserAuth {
        return Database.sessionAuth ?: throw RuntimeException("User not found")
    }

    override fun putUser(response: UserAuth) {
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