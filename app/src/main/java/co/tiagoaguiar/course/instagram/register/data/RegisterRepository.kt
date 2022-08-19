package co.tiagoaguiar.course.instagram.register.data

import android.net.Uri
import co.tiagoaguiar.course.instagram.common.base.RequestCallback

class RegisterRepository(private val data: RegisterDataSource) {

    fun create(email: String, callback: RequestCallback<Any?>){
        data.createUser(email, callback)
    }

    fun create(email: String, name: String, username: String, password: String, callback: RequestCallback<Any?>){
        data.createUser(email, name, username, password, callback)
    }

    fun updateUser(photoUri: Uri, callback: RequestCallback<Any?>) {
        data.updateUser(photoUri, callback)
    }

}