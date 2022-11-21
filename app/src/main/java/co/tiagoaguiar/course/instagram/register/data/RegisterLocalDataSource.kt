package co.tiagoaguiar.course.instagram.register.data

import co.tiagoaguiar.course.instagram.common.base.Cache
import co.tiagoaguiar.course.instagram.common.model.User
import com.google.firebase.auth.FirebaseAuth

class RegisterLocalDataSource(
    private val userCache: Cache<Pair<User, Boolean?>>,
) : RegisterDataSource {

    override fun fetchSession(): String{
        return FirebaseAuth.getInstance().uid ?: throw RuntimeException("User not found")
    }

    override fun putNewUser(response: Pair<User, Boolean?>) {
        userCache.put(response)
    }

}