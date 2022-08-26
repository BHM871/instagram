package co.tiagoaguiar.course.instagram.register.data

import co.tiagoaguiar.course.instagram.common.base.Cache
import co.tiagoaguiar.course.instagram.common.model.Database
import co.tiagoaguiar.course.instagram.common.model.UserAuth

class RegisterLocalDataSource(
    private val userCache: Cache<UserAuth>,
) : RegisterDataSource {

    override fun fetchSession(): UserAuth {
        return Database.sessionAuth ?: throw RuntimeException("user not found")
    }

    override fun putNewUser(response: UserAuth) {
        userCache.put(response)
    }

}