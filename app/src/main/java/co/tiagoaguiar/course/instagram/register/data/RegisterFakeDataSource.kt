package co.tiagoaguiar.course.instagram.register.data

import android.net.Uri
import android.os.Handler
import android.os.Looper
import co.tiagoaguiar.course.instagram.common.base.RequestCallback
import co.tiagoaguiar.course.instagram.common.model.Database
import co.tiagoaguiar.course.instagram.common.model.UserAuth
import java.util.*

class RegisterFakeDataSource : RegisterDataSource {

    override fun createUser(email: String, callback: RequestCallback<Boolean>) {
        Handler(Looper.getMainLooper()).postDelayed({

            when(Database.usersAuth.firstOrNull{it.email == email}){
                null -> callback.onSuccess(true)
                else -> callback.onFailure("Usuario já cadastrado")
            }

            callback.onComplete()
        }, 1000)
    }

    override fun createUser(
        email: String,
        name: String,
        username: String,
        password: String,
        callback: RequestCallback<Pair<UserAuth, Boolean?>>
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

                val newUser = Database.usersAuth.first { it.email == email }

                if (created){
                    Database.sessionAuth = newUser

                    var followers = Database.followers[newUser.uuid]
                    if (followers == null){
                        followers = mutableSetOf()
                        Database.followers[newUser.uuid] = followers
                    }

                    var posts = Database.posts[newUser.uuid]
                    if (posts == null){
                        posts = mutableSetOf()
                        Database.posts[newUser.uuid] = posts
                    }

                    var feed = Database.feed[newUser.uuid]
                    if (feed == null){
                        feed = mutableSetOf()
                        Database.feed[newUser.uuid] = feed
                    }

                    callback.onSuccess(Pair(Database.sessionAuth!!, null))
                }
            }

            callback.onComplete()
        }, 1000)
    }

    override fun updateUser(userUUID: String, photoUri: Uri, callback: RequestCallback<Uri?>) {
        Handler(Looper.getMainLooper()).postDelayed({
            val userAuth = Database.sessionAuth

            if (userAuth == null){
                callback.onFailure("Usuario não encontrado")
            } else {
                val index = Database.usersAuth.indexOf(userAuth)
                Database.usersAuth[index] = userAuth.copy(photoUri = photoUri)
                Database.sessionAuth = Database.usersAuth[index]

                userAuth.photoUri = photoUri
                callback.onSuccess(photoUri)
            }

            callback.onComplete()
        }, 1000)
    }

}