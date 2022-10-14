package co.tiagoaguiar.course.instagram.home.data

import co.tiagoaguiar.course.instagram.common.base.Cache
import co.tiagoaguiar.course.instagram.common.model.Post

class HomeDataSourceFactory(
    private val homeFeedCache: Cache<List<Post>>
) {

    fun createLocalDataSource() : HomeDataSource {
        return HomeLocalDataSource(homeFeedCache)
    }

    fun createRemoteDataSource() : HomeDataSource {
        return HomeFireDataSource()
    }

    fun createFromFeed() : HomeDataSource {
        return if (homeFeedCache.isCached()){
            HomeLocalDataSource(homeFeedCache)
        } else {
            HomeFireDataSource()
        }
    }

}