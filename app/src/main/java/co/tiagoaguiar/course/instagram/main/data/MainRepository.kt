package co.tiagoaguiar.course.instagram.main.data

import co.tiagoaguiar.course.instagram.common.base.Cache
import co.tiagoaguiar.course.instagram.common.base.RequestCallback
import co.tiagoaguiar.course.instagram.common.model.Post
import co.tiagoaguiar.course.instagram.common.model.User

class MainRepository(
    private val dataSource: MainDataSource,
    private val postsCache: Cache<List<Post>>,
    private val userCache: Cache<Pair<User, Boolean?>>
) {

    fun logout(callback: RequestCallback<Boolean>) {
        dataSource.logout(object : RequestCallback<Boolean> {
            override fun onSuccess(data: Boolean) {
                postsCache.remove()
                userCache.remove()
                callback.onSuccess(data)
            }

            override fun onFailure(message: String) {
                callback.onFailure(message)
            }

            override fun onComplete() {
                callback.onComplete()
            }
        })
    }

}