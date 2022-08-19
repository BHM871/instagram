package co.tiagoaguiar.course.instagram.register.data

import android.net.Uri
import co.tiagoaguiar.course.instagram.common.base.RequestCallback

interface RegisterDataSource {

    fun createUser(email: String, callback: RequestCallback<Any?>)
    fun createUser(email: String, name: String, username:String, password: String, callback: RequestCallback<Any?>)
    fun updateUser(photoUri: Uri, callback: RequestCallback<Any?>)

}