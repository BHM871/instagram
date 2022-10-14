package co.tiagoaguiar.course.instagram.splash.data

import co.tiagoaguiar.course.instagram.common.base.Cache
import co.tiagoaguiar.course.instagram.common.model.User

class SplashRepository(private val data: SplashDataSource, private val userCache: Cache<Pair<User, Boolean?>>) {

    fun session(callback: SplashCallback){
        data.session(object : SplashCallback {
            override fun onSuccess(data: Pair<User, Boolean?>) {
                userCache.put(data)
                callback.onSuccess(data)
            }

            override fun onFailure() {
                callback.onFailure()
            }
        })
    }

}