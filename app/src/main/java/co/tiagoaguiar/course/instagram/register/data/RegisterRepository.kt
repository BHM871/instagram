package co.tiagoaguiar.course.instagram.register.data

import android.net.Uri
import co.tiagoaguiar.course.instagram.common.base.RequestCallback
import co.tiagoaguiar.course.instagram.common.model.UserAuth

class RegisterRepository(
    private val localDataSource: RegisterDataSource,
    private val remoteDataSource: RegisterDataSource
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
            object : RequestCallback<Pair<UserAuth, Boolean?>> {
                override fun onSuccess(data: Pair<UserAuth, Boolean?>) {
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

    fun updateUser(photoUri: Uri, callback: RequestCallback<Uri?>) {
        val userAuth = localDataSource.fetchSession()

        remoteDataSource.updateUser(userAuth.uuid, photoUri, object : RequestCallback<Uri?> {
            override fun onSuccess(data: Uri?) {
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