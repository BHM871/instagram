package co.tiagoaguiar.course.instagram.profile

import android.net.Uri
import co.tiagoaguiar.course.instagram.common.base.BasePresenter
import co.tiagoaguiar.course.instagram.common.base.BaseView
import co.tiagoaguiar.course.instagram.common.model.Post
import co.tiagoaguiar.course.instagram.common.model.User

interface Profile {

    interface Presenter : BasePresenter {
        fun fetchUserProfile(uuid: String?)
        fun fetchUserPosts(uuid: String?)
        fun followUser(uuid: String?, follow: Boolean)

        fun clear()
    }

    interface View : BaseView<Presenter>{
        fun showProgress(enabled: Boolean)

        fun displayUserProfile(user: Pair<User, Boolean?>)
        fun displayRequestFailure(message: String)

        fun displayEmptyPosts()
        fun displayFullPosts(posts: List<Post>)

        fun onUpdatePostsSuccess(posts: List<Post>)
    }

}