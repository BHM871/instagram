package co.tiagoaguiar.course.instagram.profile.data

import android.net.Uri
import android.os.Handler
import android.os.Looper
import co.tiagoaguiar.course.instagram.common.base.RequestCallback
import co.tiagoaguiar.course.instagram.common.model.Database
import co.tiagoaguiar.course.instagram.common.model.Post
import co.tiagoaguiar.course.instagram.common.model.UserAuth

class ProfileFakeDataSource : ProfileDataSource {

    override fun fetchUserProfile(userUUID: String, callback: RequestCallback<Pair<UserAuth, Boolean?>>) {
        Handler(Looper.getMainLooper()).postDelayed({
            val userAuth = Database.usersAuth.firstOrNull { userUUID == it.uuid }

            if (userAuth != null) {

                if (userAuth.uuid == Database.sessionAuth!!.uuid){
                    callback.onSuccess(Pair(userAuth, null))
                } else {

                    val followings = Database.followers[Database.sessionAuth!!.uuid]

                    val destUser = followings?.firstOrNull{ it == userUUID }

                    callback.onSuccess(Pair(userAuth, destUser != null))
                }
            } else {
                callback.onFailure("User not found")
            }

            callback.onComplete()
        }, 2000)
    }

    override fun fetchUserPost(userUUID: String, callback: RequestCallback<List<Post>>) {
        Handler(Looper.getMainLooper()).postDelayed({
            val posts = Database.posts[userUUID]

            callback.onSuccess(posts?.toList()?.asReversed() ?: emptyList())

            callback.onComplete()
        }, 2000)
    }

    override fun updatePhoto(userUUID: String, photoUri: Uri, callback: RequestCallback<Uri>) {
        Handler(Looper.getMainLooper()).postDelayed({
            val userAuth = Database.sessionAuth

            if (userAuth == null) {
                callback.onFailure("User not found")
                return@postDelayed
            }
            Database.usersAuth.first { it.uuid == userUUID }.photoUri = photoUri
            Database.sessionAuth?.photoUri = photoUri

            callback.onSuccess(photoUri)

            callback.onComplete()
        }, 2000)
    }

    override fun followUser(userUUID: String, isFollow: Boolean, callback: RequestCallback<Boolean>
    ) {
        Handler(Looper.getMainLooper()).postDelayed({
            var followers = Database.followers[Database.sessionAuth!!.uuid]

            if (followers == null){
                followers = mutableSetOf()
                Database.followers[Database.sessionAuth!!.uuid] = followers
            }

            if (isFollow){
                Database.followers[Database.sessionAuth!!.uuid]!!.add(userUUID)
                Database.usersAuth.firstOrNull{ it.uuid == Database.sessionAuth!!.uuid }!!.followingCount += 1
                Database.usersAuth.firstOrNull{ it.uuid == userUUID }!!.followersCount += 1

                val following = Database.usersAuth.firstOrNull{ it.uuid == userUUID }!!.uuid
                Database.feed[Database.sessionAuth!!.uuid]!!.addAll(Database.feed[following]!!)
            }
            else {
                Database.followers[Database.sessionAuth!!.uuid]!!.remove(userUUID)
                Database.usersAuth.firstOrNull{ it.uuid == Database.sessionAuth!!.uuid }!!.followingCount -= 1
                Database.usersAuth.firstOrNull{ it.uuid == userUUID }!!.followersCount -= 1

                val following = Database.usersAuth.firstOrNull{ it.uuid == userUUID }!!.uuid
                Database.feed[Database.sessionAuth!!.uuid]!!.removeAll(Database.feed[following]!!)
            }

            callback.onSuccess(true)
            callback.onComplete()

        }, 500)
    }
}