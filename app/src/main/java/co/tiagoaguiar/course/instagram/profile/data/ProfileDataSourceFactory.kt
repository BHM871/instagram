package co.tiagoaguiar.course.instagram.profile.data

import co.tiagoaguiar.course.instagram.common.base.Cache
import co.tiagoaguiar.course.instagram.common.model.Photo
import co.tiagoaguiar.course.instagram.common.model.Post
import co.tiagoaguiar.course.instagram.common.model.UserAuth

class ProfileDataSourceFactory(
    private val userCache: Cache<UserAuth>,
    private val photoCache: Cache<Photo>,
    private val postCache: Cache<List<Post>>
) {

    fun createLocalDataSource() : ProfileLocalDataSource {
        return ProfileLocalDataSource(userCache, photoCache, postCache)
    }

    fun createFromUser() : ProfileDataSource {
        return if (userCache.isCached()){
            ProfileLocalDataSource(userCache, photoCache, postCache)
        } else {
            ProfileFakeDataSource()
        }
    }

    fun createFromPosts() : ProfileDataSource {
        return if (postCache.isCached()){
            ProfileLocalDataSource(userCache, photoCache, postCache)
        } else {
            ProfileFakeDataSource()
        }
    }

}