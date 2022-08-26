package co.tiagoaguiar.course.instagram.add.data

import android.net.Uri
import android.os.Handler
import android.os.Looper
import co.tiagoaguiar.course.instagram.common.base.RequestCallback
import co.tiagoaguiar.course.instagram.common.model.Database
import co.tiagoaguiar.course.instagram.common.model.Post
import co.tiagoaguiar.course.instagram.common.model.UserAuth
import java.util.*

class AddFakeRemoteDtaSource : AddDataSource {

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
            Database.posts[userUUID]?.add(post)
            Database.usersAuth.firstOrNull { it.uuid == userUUID }?.postCount =
                Database.usersAuth.firstOrNull { it.uuid == userUUID }?.postCount?.plus(1)!!

            var followers = Database.followers[userUUID]

            if (followers == null){
                followers = mutableSetOf()
                Database.followers[userUUID] = followers
            }

            for (follower in followers){
                Database.feed[follower]?.add(post)
            }

            Database.feed[userUUID]?.add(post)

            callback.onSuccess(true)

            callback.onComplete()
        }, 1000)
    }

    override fun fetchSession(): UserAuth {
        return Database.sessionAuth!!
    }

}