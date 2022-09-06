package co.tiagoaguiar.course.instagram.add.data

import android.net.Uri
import android.os.Handler
import android.os.Looper
import co.tiagoaguiar.course.instagram.common.base.RequestCallback
import co.tiagoaguiar.course.instagram.common.model.Database
import co.tiagoaguiar.course.instagram.common.model.Post
import co.tiagoaguiar.course.instagram.common.model.UserAuth
import java.util.*

class AddFakeDataSource : AddDataSource {

    override fun createPost(
        userUUID: String,
        uri: Uri,
        caption: String,
        callback: RequestCallback<Boolean>
    ) {
        Handler(Looper.getMainLooper()).postDelayed({

            var posts = Database.posts[userUUID]

            if (posts == null) {
                posts = mutableSetOf()
                Database.posts[userUUID] = posts
            }

            val post = Post(UUID.randomUUID().toString(), uri, caption, System.currentTimeMillis(), Database.sessionAuth!!)
            Database.posts[Database.sessionAuth!!.uuid]?.add(post)
            Database.usersAuth.firstOrNull{ it.uuid == Database.sessionAuth!!.uuid }!!.postCount += 1

            var followers = Database.followers[userUUID]

            if (followers == null){
                followers = mutableSetOf()
                Database.followers[userUUID] = followers
            }

            for (follower in followers){
                if (Database.feed[follower] == null)
                    Database.feed[follower] = mutableSetOf()
                Database.feed[follower]?.add(post)
            }

            var feed = Database.feed[userUUID]

            if (feed == null){
                feed = mutableSetOf()
                Database.feed[userUUID] = feed
            }

            feed.add(post)

            callback.onSuccess(true)

            callback.onComplete()
        }, 500)
    }

    override fun fetchSession(): UserAuth {
        return Database.sessionAuth!!
    }

}