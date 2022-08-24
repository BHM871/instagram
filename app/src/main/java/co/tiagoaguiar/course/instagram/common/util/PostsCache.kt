package co.tiagoaguiar.course.instagram.common.util

import co.tiagoaguiar.course.instagram.common.base.Cache
import co.tiagoaguiar.course.instagram.common.model.Post

object PostsCache : Cache<List<Post>> {

    var posts: List<Post>? = null

    override fun isCached(): Boolean {
        if (posts == null) posts = null
        return posts != null
    }

    override fun get(key: String): List<Post>? {
        return posts
    }

    override fun put(data: List<Post>) {
        posts = data
    }

    override fun remove() {
        posts = null
    }
}