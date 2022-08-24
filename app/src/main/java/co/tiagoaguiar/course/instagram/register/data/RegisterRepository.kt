package co.tiagoaguiar.course.instagram.register.data

import android.net.Uri
import co.tiagoaguiar.course.instagram.common.base.RequestCallback
import co.tiagoaguiar.course.instagram.common.model.Photo
import co.tiagoaguiar.course.instagram.common.model.UserAuth

class RegisterRepository(
    private val localDataSource: RegisterLocalDataSource,
    private val remoteDataSource: RegisterFakeDataSource
) {

    fun create(email: String, callback: RequestCallback<Boolean>) {
        remoteDataSource.createUser(email, callback)
    }

    fun create(
        email: String,
        name: String,
        username: String,
        password: String,
        callback: RequestCallback<Boolean>
    ) {
        remoteDataSource.createUser(
            email,
            name,
            username,
            password,
            object : RequestCallback<UserAuth> {
                override fun onSuccess(data: UserAuth) {
                    localDataSource.putNewUser(data)
                    callback.onSuccess(true)
                }

                override fun onFailure(message: String) {
                    callback.onFailure(message)
                }

                override fun onComplete() {
                    callback.onComplete()
                }
            })
    }

    fun updateUser(photoUri: Uri, callback: RequestCallback<Photo>) {
        val userAuth = localDataSource.fetchSession()

        remoteDataSource.updateUser(userAuth.uuid, photoUri, object : RequestCallback<Photo> {
            override fun onSuccess(data: Photo) {
                localDataSource.putNewPhoto(data)
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