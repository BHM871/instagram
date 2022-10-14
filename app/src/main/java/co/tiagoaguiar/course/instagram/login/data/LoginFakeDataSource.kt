package co.tiagoaguiar.course.instagram.login.data

import android.os.Handler
import android.os.Looper
import co.tiagoaguiar.course.instagram.common.base.RequestCallback
import co.tiagoaguiar.course.instagram.common.model.Database
import co.tiagoaguiar.course.instagram.common.model.User

class LoginFakeDataSource : LoginDataSource {
    override fun login(email: String, password: String, callback: RequestCallback<User>) {
        Handler(Looper.getMainLooper()).postDelayed({
            val userAuth = Database.usersAuth.firstOrNull { email == it.email }

            when {
                userAuth == null -> callback.onFailure("Usuario não encontrado")

                userAuth.password != password -> {
                    callback.onFailure("Senha incorreta")
                }

                else -> {
                    Database.sessionAuth = userAuth
                    callback.onSuccess((userAuth as User))
                }
            }

            callback.onComplete()

        }, 1000)
    }
}