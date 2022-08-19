package co.tiagoaguiar.course.instagram.home.data

import android.os.Handler
import android.os.Looper
import co.tiagoaguiar.course.instagram.R
import co.tiagoaguiar.course.instagram.common.base.RequestCallback
import co.tiagoaguiar.course.instagram.common.model.Database
import co.tiagoaguiar.course.instagram.common.model.Post

class HomeFakeDataSource : HomeDataSource {

    override fun fetchFeed(uuid: String, callback: RequestCallback<List<Post>>) {
        Handler(Looper.getMainLooper()).postDelayed({
            val posts = Database.posts[uuid]

            val list = mutableListOf<Post>()
            for (i in 0 until 30) {
                Database.sessionAuth?.let {
                    list.add(
                        i,
                        Post(
                            "$i",
                            R.drawable.ic_insta_add,
                            "username",
                            1202L,
                            it
                        )
                    )
                }
            }

            callback.onSuccess(list)
            //callback.onSuccess(posts?.toList() ?: emptyList())

            callback.onComplete()
        }, 2000)
    }

}