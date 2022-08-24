package co.tiagoaguiar.course.instagram.home.data

import co.tiagoaguiar.course.instagram.common.base.Cache
import co.tiagoaguiar.course.instagram.common.model.Post

object HomeFeedCache : Cache<List<Post>> {

    var feed: List<Post>? = null

    override fun isCached(): Boolean {
        if (feed == null) feed = null
        return feed != null
    }

    override fun get(key: String): List<Post>? {
        return feed
    }

    override fun put(data: List<Post>) {
        feed = data
    }

    override fun remove() {
        feed = null
    }
}