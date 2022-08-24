package co.tiagoaguiar.course.instagram.common.util

import co.tiagoaguiar.course.instagram.common.base.Cache
import co.tiagoaguiar.course.instagram.common.model.Photo

object PhotoCache : Cache<Photo> {

    var photo: Photo? = null

    override fun isCached(): Boolean {
        return photo != null
    }

    override fun get(key: String): Photo? {
        return photo
    }

    override fun put(data: Photo) {
        photo = data
    }

    override fun remove() {
        photo = null
    }
}