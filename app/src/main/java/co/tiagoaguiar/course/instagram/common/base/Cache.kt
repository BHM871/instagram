package co.tiagoaguiar.course.instagram.common.base

interface Cache<T> {

    fun isCached() : Boolean
    fun get(key: String? = null) : T?
    fun put(data: T)
    fun remove()

}