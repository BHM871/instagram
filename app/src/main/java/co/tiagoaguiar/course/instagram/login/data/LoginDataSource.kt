package co.tiagoaguiar.course.instagram.login.data

import co.tiagoaguiar.course.instagram.common.base.RequestCallback
import co.tiagoaguiar.course.instagram.common.model.UserAuth

interface LoginDataSource {

    fun login(email:String, password: String, callback: RequestCallback<UserAuth>)

}