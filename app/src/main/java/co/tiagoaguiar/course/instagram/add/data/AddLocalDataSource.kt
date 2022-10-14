package co.tiagoaguiar.course.instagram.add.data

import co.tiagoaguiar.course.instagram.common.base.Cache
import co.tiagoaguiar.course.instagram.common.model.Database
import co.tiagoaguiar.course.instagram.common.model.UserAuth
import com.google.firebase.auth.FirebaseAuth

class AddLocalDataSource : AddDataSource {

    override fun fetchSession(): String {
        return FirebaseAuth.getInstance().uid ?: throw RuntimeException("User not found")
    }

    override fun <T> removeCache(cache: Cache<T>) {
        cache.remove()
    }

}