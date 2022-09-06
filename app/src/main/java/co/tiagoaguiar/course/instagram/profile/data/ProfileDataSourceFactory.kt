package co.tiagoaguiar.course.instagram.profile.data

import co.tiagoaguiar.course.instagram.common.base.Cache
import co.tiagoaguiar.course.instagram.common.model.Post
import co.tiagoaguiar.course.instagram.common.model.UserAuth

class ProfileDataSourceFactory(
    private val userCache: Cache<Pair<UserAuth, Boolean?>>,
    private val postCache: Cache<List<Post>>
) {

    fun createLocalDataSource() : ProfileLocalDataSource {
        return ProfileLocalDataSource(userCache, postCache)
    }

    fun createRemoteDataSource() : ProfileFakeDataSource {
        return ProfileFakeDataSource()
    }

    fun createFromUser(uuid: String?) : ProfileDataSource {
        if (uuid != null)
            return ProfileFakeDataSource()

        return if (userCache.isCached()){
            ProfileLocalDataSource(userCache, postCache)
        } else {
            ProfileFakeDataSource()
        }
    }

    fun createFromPosts(uuid: String?) : ProfileDataSource {
        if (uuid != null)
            return ProfileFakeDataSource()

        return if (postCache.isCached()){
            ProfileLocalDataSource(userCache, postCache)
        } else {
            ProfileFakeDataSource()
        }
    }

}