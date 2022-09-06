package co.tiagoaguiar.course.instagram.home.data

import android.os.Handler
import android.os.Looper
import co.tiagoaguiar.course.instagram.common.base.RequestCallback
import co.tiagoaguiar.course.instagram.common.model.Database
import co.tiagoaguiar.course.instagram.common.model.Post

class HomeFakeDataSource : HomeDataSource {

    override fun fetchFeed(userUUID: String, callback: RequestCallback<List<Post>>) {
        Handler(Looper.getMainLooper()).postDelayed({
            val feed = Database.feed[userUUID]

            callback.onSuccess(feed?.toList()?.asReversed() ?: emptyList())

            callback.onComplete()
        }, 500)
    }

    override fun liked(post: Post, liked: Boolean) {
        Handler(Looper.getMainLooper()).postDelayed({
            Database.posts[post.publisher.uuid]?.first { it.uuid == post.uuid}?.like = liked
        }, 500)
    }

}