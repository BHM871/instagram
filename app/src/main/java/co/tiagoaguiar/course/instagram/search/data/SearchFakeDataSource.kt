package co.tiagoaguiar.course.instagram.search.data

import android.os.Looper
import co.tiagoaguiar.course.instagram.common.base.RequestCallback
import co.tiagoaguiar.course.instagram.common.model.UserAuth
import android.os.Handler
import co.tiagoaguiar.course.instagram.common.model.Database

class SearchFakeDataSource : SearchDataSource {

    override fun fetchUsers(username: String, callback: RequestCallback<List<UserAuth>>) {
        Handler(Looper.getMainLooper()).postDelayed({
            val users = Database.usersAuth.filter {
                it.username.lowercase()
                    .startsWith(username.lowercase())
            }

            (users as MutableList<UserAuth>).addAll(
                Database.usersAuth.filter { user ->
                    user.username.lowercase().contains(username.lowercase()) &&
                            users.firstOrNull { it.name == user.name } == null
                })

            if (users.isNotEmpty()) {
                callback.onSuccess(users)
            } else {
                callback.onFailure("empty")
            }

            callback.onComplete()

        }, 500)
    }
}