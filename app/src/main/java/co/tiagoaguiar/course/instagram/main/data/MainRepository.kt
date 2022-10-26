package co.tiagoaguiar.course.instagram.main.data

import co.tiagoaguiar.course.instagram.common.base.Cache
import co.tiagoaguiar.course.instagram.common.base.RequestCallback
import co.tiagoaguiar.course.instagram.common.model.Post
import co.tiagoaguiar.course.instagram.common.model.User
import co.tiagoaguiar.course.instagram.common.util.PostsCache
import co.tiagoaguiar.course.instagram.common.util.UserCache

class MainRepository(
    private val remoteDataSource: MainDataSource,
    private val localDataSource: MainDataSource
) {

    fun logout(callback: RequestCallback<Boolean>) {
        remoteDataSource.logout(object : RequestCallback<Boolean> {
            override fun onSuccess(data: Boolean) {
                localDataSource.removeCache(PostsCache)
                localDataSource.removeCache(UserCache)
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