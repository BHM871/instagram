package co.tiagoaguiar.course.instagram.add.data

import android.net.Uri
import co.tiagoaguiar.course.instagram.common.base.RequestCallback
import co.tiagoaguiar.course.instagram.common.util.PostsCache
import co.tiagoaguiar.course.instagram.common.util.UserCache

class AddRepository(
    private val localDataSource: AddLocalDataSource,
    private val remoteDataSource: AddFakeRemoteDtaSource
) {

    fun createPost(uri: Uri, caption: String, callback: RequestCallback<Boolean>) {
        val userAuth = localDataSource.fetchSession()

        remoteDataSource.createPost(userAuth.uuid, uri, caption, object : RequestCallback<Boolean> {
            override fun onSuccess(data: Boolean) {
                localDataSource.removeCache(UserCache)
                localDataSource.removeCache(PostsCache)
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