package co.tiagoaguiar.course.instagram.register.data

import co.tiagoaguiar.course.instagram.common.base.Cache
import co.tiagoaguiar.course.instagram.common.model.Database
import co.tiagoaguiar.course.instagram.common.model.UserAuth

class RegisterLocalDataSource(
    private val userCache: Cache<Pair<UserAuth, Boolean?>>,
) : RegisterDataSource {

    override fun fetchSession(): UserAuth {
        return Database.sessionAuth ?: UserAuth("a", "a", "a", "a", "a")
    }

    override fun putNewUser(response: Pair<UserAuth, Boolean?>) {
        userCache.put(response)
    }

}