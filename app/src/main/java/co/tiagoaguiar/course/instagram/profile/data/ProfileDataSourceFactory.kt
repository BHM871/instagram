package co.tiagoaguiar.course.instagram.profile.data

import co.tiagoaguiar.course.instagram.common.base.Cache
import co.tiagoaguiar.course.instagram.common.model.Post
import co.tiagoaguiar.course.instagram.common.model.User
import co.tiagoaguiar.course.instagram.common.model.UserAuth

class ProfileDataSourceFactory(
    private val userCache: Cache<Pair<User, Boolean?>>,
    private val postCache: Cache<List<Post>>
) {

    fun createLocalDataSource() : ProfileDataSource {
        return ProfileLocalDataSource(userCache, postCache)
    }

    fun createRemoteDataSource() : ProfileDataSource {
        return ProfileFireDataSource()
    }

    fun createFromUser(uuid: String?) : ProfileDataSource {
        if (uuid != null)
            return createRemoteDataSource()

        return if (userCache.isCached()){
            createLocalDataSource()
        } else {
            createRemoteDataSource()
        }
    }

    fun createFromPosts(uuid: String?) : ProfileDataSource {
        if (uuid != null)
            return createRemoteDataSource()

        return if (postCache.isCached()){
            createLocalDataSource()
        } else {
            createRemoteDataSource()
        }
    }

}