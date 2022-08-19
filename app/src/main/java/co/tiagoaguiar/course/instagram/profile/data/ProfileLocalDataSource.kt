package co.tiagoaguiar.course.instagram.profile.data

import co.tiagoaguiar.course.instagram.common.base.Cache
import co.tiagoaguiar.course.instagram.common.base.RequestCallback
import co.tiagoaguiar.course.instagram.common.model.Database
import co.tiagoaguiar.course.instagram.common.model.Post
import co.tiagoaguiar.course.instagram.common.model.UserAuth

class ProfileLocalDataSource(
    private val profileUserCache: Cache<UserAuth>,
    private val profilePostCache: Cache<List<Post>>
) : ProfileDataSource {

    override fun fetchUserProfile(uuid: String, callback: RequestCallback<UserAuth>) {
        val userAuth = profileUserCache.get(uuid)

        if (userAuth == null){
            callback.onFailure("User not found")
        } else {
            callback.onSuccess(userAuth)
        }

        callback.onComplete()
    }

    override fun fetchUserPost(uuid: String, callback: RequestCallback<List<Post>>) {
        val posts = profilePostCache.get(uuid)

        if (posts == null){
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
        profileUserCache.put(response)
    }

    override fun putPosts(response: List<Post>) {
        profilePostCache.put(response)
    }

}