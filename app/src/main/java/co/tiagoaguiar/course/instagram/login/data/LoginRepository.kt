package co.tiagoaguiar.course.instagram.login.data

import co.tiagoaguiar.course.instagram.common.base.RequestCallback
import co.tiagoaguiar.course.instagram.common.model.User

class LoginRepository(private val data: LoginDataSource) {

    fun login(email: String, password: String, callback: RequestCallback<User>){
        data.login(email, password, callback)
    }

}