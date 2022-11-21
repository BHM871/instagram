package co.tiagoaguiar.course.instagram.add.data

import android.net.Uri
import co.tiagoaguiar.course.instagram.common.base.Cache
import co.tiagoaguiar.course.instagram.common.base.RequestCallback

interface AddDataSource {

    fun createPost(userUUID: String, uri: Uri, caption: String, callback: RequestCallback<Boolean>) { throw UnsupportedOperationException() }

    fun fetchSession() : String { throw UnsupportedOperationException() }

    fun <T> removeCache(cache: Cache<T>) { throw UnsupportedOperationException() }

}