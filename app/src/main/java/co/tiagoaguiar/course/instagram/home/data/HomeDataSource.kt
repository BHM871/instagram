package co.tiagoaguiar.course.instagram.home.data

import co.tiagoaguiar.course.instagram.common.base.RequestCallback
import co.tiagoaguiar.course.instagram.common.model.Post
import co.tiagoaguiar.course.instagram.common.model.UserAuth

interface HomeDataSource {

    fun fetchFeed(userUUId: String, callback: RequestCallback<List<Post>>)

    fun fetchSession() : UserAuth { throw UnsupportedOperationException("User not found") }
    fun putFeed(response: List<Post>) { throw UnsupportedOperationException("User not found") }

}