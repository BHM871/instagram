package co.tiagoaguiar.course.instagram.register.data

import android.net.Uri
import co.tiagoaguiar.course.instagram.common.base.RequestCallback
import co.tiagoaguiar.course.instagram.common.model.UserAuth

interface RegisterDataSource {

    fun createUser(email: String, callback: RequestCallback<Boolean>) { throw UnsupportedOperationException() }
    fun createUser(email: String, name: String, username:String, password: String, callback: RequestCallback<UserAuth>) { throw UnsupportedOperationException() }
    fun updateUser(userUUID: String, photoUri: Uri, callback: RequestCallback<Uri?>) { throw UnsupportedOperationException() }

    fun fetchSession() : UserAuth { throw UnsupportedOperationException() }
    fun putNewUser(response: UserAuth) { throw UnsupportedOperationException() }
}