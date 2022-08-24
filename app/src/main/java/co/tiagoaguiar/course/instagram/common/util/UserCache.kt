package co.tiagoaguiar.course.instagram.common.util

import co.tiagoaguiar.course.instagram.common.base.Cache
import co.tiagoaguiar.course.instagram.common.model.UserAuth

object UserCache : Cache<UserAuth> {

    var userAuth: UserAuth? = null

    override fun isCached(): Boolean {
        return userAuth != null
    }

    override fun get(key: String): UserAuth? {
        return if (userAuth?.uuid == key){
            userAuth
        } else {
            null
        }
    }

    override fun put(data: UserAuth) {
        userAuth = data
    }

    override fun remove() {
        userAuth = null
    }
}