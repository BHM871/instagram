package co.tiagoaguiar.course.instagram.profile.data

import co.tiagoaguiar.course.instagram.common.base.Cache
import co.tiagoaguiar.course.instagram.common.model.Post
import co.tiagoaguiar.course.instagram.common.model.UserAuth

class ProfileDataSourceFactory(
    private val profileUserCache: Cache<UserAuth>,
    private val profilePostCache: Cache<List<Post>>
) {

    fun createLocalDataSource() : ProfileLocalDataSource {
        return ProfileLocalDataSource(profileUserCache, profilePostCache)
    }

    fun createFromUser() : ProfileDataSource {
        return if (profileUserCache.isCached()){
            ProfileLocalDataSource(profileUserCache, profilePostCache)
        } else {
            ProfileFakeDataSource()
        }
    }

    fun createFromPosts() : ProfileDataSource {
        return if (profilePostCache.isCached()){
            ProfileLocalDataSource(profileUserCache, profilePostCache)
        } else {
            ProfileFakeDataSource()
        }
    }

}