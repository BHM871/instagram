package co.tiagoaguiar.course.instagram.register.data

import android.net.Uri
import android.os.Handler
import android.os.Looper
import co.tiagoaguiar.course.instagram.common.base.RequestCallback
import co.tiagoaguiar.course.instagram.common.model.Database
import co.tiagoaguiar.course.instagram.common.model.Photo
import co.tiagoaguiar.course.instagram.common.model.UserAuth
import java.util.*

class RegisterFakeDataSource : RegisterDataSource {

    override fun createUser(email: String, callback: RequestCallback<Any?>) {
        Handler(Looper.getMainLooper()).postDelayed({

            when(Database.usersAuth.firstOrNull{it.email == email}){
               null -> callback.onSuccess(null)
                else -> callback.onFailure("Usuario já cadastrado")
            }

            callback.onComplete()
        }, 2000)
    }

    override fun createUser(
        email: String,
        name: String,
        username: String,
        password: String,
        callback: RequestCallback<Any?>
    ) {
        Handler(Looper.getMainLooper()).postDelayed({
            val userAuth = Database.usersAuth.firstOrNull{it.email == email}

            if (userAuth != null){
                callback.onFailure("Usuario já cadastrado")
            } else {
                val created = Database.usersAuth.add(
                    UserAuth(
                        uuid = UUID.randomUUID().toString(),
                        name = name,
                        username = username,
                        email = email,
                        password = password)
                )

                if (created){
                    Database.sessionAuth = UserAuth(uuid = UUID.randomUUID().toString(), name = name, username = username, email = email, password = password)
                    callback.onSuccess(null)
                }
            }

            callback.onComplete()
        }, 2000)
    }

    override fun updateUser(photoUri: Uri, callback: RequestCallback<Any?>) {
        Handler(Looper.getMainLooper()).postDelayed({
            val userAuth = Database.sessionAuth

            if (userAuth == null){
                callback.onFailure("Usuario não encontrado")
            } else {
                val photo = Photo(userAuth.uuid, photoUri)
                val created = Database.photo.add(photo)

                if (created){
                    callback.onSuccess(null)
                }
            }

            callback.onComplete()
        }, 2000)
    }

}