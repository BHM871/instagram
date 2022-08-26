package co.tiagoaguiar.course.instagram.home.data

import co.tiagoaguiar.course.instagram.common.base.Cache
import co.tiagoaguiar.course.instagram.common.base.RequestCallback
import co.tiagoaguiar.course.instagram.common.model.Database
import co.tiagoaguiar.course.instagram.common.model.Post
import co.tiagoaguiar.course.instagram.common.model.UserAuth

class HomeLocalDataSource(
    private val homeFeedCache: Cache<List<Post>>
) : HomeDataSource {

    override fun fetchFeed(userUUId: String, callback: RequestCallback<List<Post>>) {
        val posts = homeFeedCache.get(userUUId)

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

    override fun putFeed(response: List<Post>) {
        homeFeedCache.put(response)
    }

    override fun removeCache() {
        homeFeedCache.remove()
    }

}