package co.tiagoaguiar.course.instagram.home.data

import co.tiagoaguiar.course.instagram.common.base.Cache
import co.tiagoaguiar.course.instagram.common.model.Post

class HomeDataSourceFactory(
    private val homeFeedCache: Cache<List<Post>>
) {

    fun createLocalDataSource() : HomeLocalDataSource {
        return HomeLocalDataSource(homeFeedCache)
    }

    fun createFromFeed() : HomeDataSource {
        return if (homeFeedCache.isCached()){
            HomeLocalDataSource(homeFeedCache)
        } else {
            HomeFakeDataSource()
        }
    }

}