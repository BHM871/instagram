package co.tiagoaguiar.course.instagram.profile.data

import android.net.Uri
import co.tiagoaguiar.course.instagram.common.base.RequestCallback
import co.tiagoaguiar.course.instagram.common.model.Post
import co.tiagoaguiar.course.instagram.common.model.User

class ProfileRepository(private val dataSourceFactory: ProfileDataSourceFactory) {

    fun fetchUserProfile(uuid: String?, callback: RequestCallback<Pair<User, Boolean?>>){
        val localDataSource = dataSourceFactory.createLocalDataSource()
        val userId = uuid ?: localDataSource.fetchSession()
        val data = dataSourceFactory.createFromUser(uuid)

        data.fetchUserProfile(userId, object : RequestCallback<Pair<User, Boolean?>> {
            override fun onSuccess(data: Pair<User, Boolean?>) {
                if (uuid == null) localDataSource.putUser(data)
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

    fun fetchUserPost(uuid: String?, callback: RequestCallback<List<Post>>){
        val localDataSource = dataSourceFactory.createLocalDataSource()
        val userId = uuid ?: localDataSource.fetchSession()
        val data = dataSourceFactory.createFromPosts(uuid)

        data.fetchUserPost(userId, object : RequestCallback<List<Post>> {
            override fun onSuccess(data: List<Post>) {
                if (uuid == null) localDataSource.putPosts(data)
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

    fun updatePhoto(photoUri: Uri, callback: RequestCallback<Uri>){
        val localDataSource = dataSourceFactory.createLocalDataSource()
        val userId = localDataSource.fetchSession()

        val data = ProfileFakeDataSource()
        data.updatePhoto(userId, photoUri, callback)
    }

    fun followUser(uuid: String?, follow: Boolean, callback: RequestCallback<Boolean>) {
        val localDataSource = dataSourceFactory.createLocalDataSource()
        val userId = uuid ?: localDataSource.fetchSession()
        val dataSource = dataSourceFactory.createRemoteDataSource()

        dataSource.followUser(userId, follow, object : RequestCallback<Boolean> {
            override fun onSuccess(data: Boolean) {
                localDataSource.removeCache()
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

    fun clearCache(){
        val localDataSource = dataSourceFactory.createLocalDataSource()
        localDataSource.removeCache()
    }

}