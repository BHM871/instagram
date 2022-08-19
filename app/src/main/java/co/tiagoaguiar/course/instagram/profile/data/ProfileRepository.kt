package co.tiagoaguiar.course.instagram.profile.data

import android.net.Uri
import co.tiagoaguiar.course.instagram.common.base.RequestCallback
import co.tiagoaguiar.course.instagram.common.model.Photo
import co.tiagoaguiar.course.instagram.common.model.Post
import co.tiagoaguiar.course.instagram.common.model.UserAuth

class ProfileRepository(private val dataSourceFactory: ProfileDataSourceFactory) {

    fun fetchUserProfile(callback: RequestCallback<UserAuth>){
        val localDataSource = dataSourceFactory.createLocalDataSource()
        val userAuth = localDataSource.fetchSession()

        val data = dataSourceFactory.createFromUser()
        data.fetchUserProfile(userAuth.uuid, object : RequestCallback<UserAuth> {
            override fun onSuccess(data: UserAuth) {
                localDataSource.putUser(data)
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

    fun fetchUserPost(callback: RequestCallback<List<Post>>){
        val localDataSource = dataSourceFactory.createLocalDataSource()
        val userAuth = localDataSource.fetchSession()

        val data = dataSourceFactory.createFromPosts()
        data.fetchUserPost(userAuth.uuid, object : RequestCallback<List<Post>> {
            override fun onSuccess(data: List<Post>) {
                localDataSource.putPosts(data)
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

    fun updateProfile(photoUri: Uri, callback: RequestCallback<Photo>){
        val localDataSource = dataSourceFactory.createLocalDataSource()
        val userAuth = localDataSource.fetchSession()

        val data = ProfileFakeDataSource()

        data.updateProfile(userAuth.uuid, photoUri, object : RequestCallback<Photo> {
            override fun onSuccess(data: Photo) {
                localDataSource.putUser(userAuth)
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