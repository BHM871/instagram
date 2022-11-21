package co.tiagoaguiar.course.instagram.common.util

import co.tiagoaguiar.course.instagram.common.base.Cache
import co.tiagoaguiar.course.instagram.common.model.User

object UserCache : Cache<Pair<User, Boolean?>> {

    var userAuth: Pair<User, Boolean?>? = null

    override fun isCached(): Boolean {
        return userAuth != null
    }

    override fun get(key: String?): Pair<User, Boolean?>? {
        return userAuth
    }

    override fun put(data: Pair<User, Boolean?>) {
        userAuth = data
    }

    override fun remove() {
        userAuth = null
    }
}