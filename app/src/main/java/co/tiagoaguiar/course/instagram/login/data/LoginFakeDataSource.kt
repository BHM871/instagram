package co.tiagoaguiar.course.instagram.login.data

import android.os.Handler
import android.os.Looper
import co.tiagoaguiar.course.instagram.common.base.RequestCallback
import co.tiagoaguiar.course.instagram.common.model.Database
import co.tiagoaguiar.course.instagram.common.model.UserAuth

class LoginFakeDataSource : LoginDataSource {
    override fun login(email: String, password: String, callback: RequestCallback<Boolean>) {
        Handler(Looper.getMainLooper()).postDelayed({
            val userAuth = Database.usersAuth.firstOrNull { email == it.email }

            when {
                userAuth == null -> callback.onFailure("Usuario não encontrado")

                userAuth.password != password -> {
                    callback.onFailure("Senha incorreta")
                }

                else -> {
                    Database.sessionAuth = userAuth
                    callback.onSuccess(true)
                }
            }

            callback.onComplete()

        }, 1000)
    }
}