package co.tiagoaguiar.course.instagram.profile

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import co.tiagoaguiar.course.instagram.common.base.BasePresenter
import co.tiagoaguiar.course.instagram.common.base.BaseView
import co.tiagoaguiar.course.instagram.common.model.Post
import co.tiagoaguiar.course.instagram.common.model.UserAuth

interface Profile {

    interface Presenter : BasePresenter {
        fun fetchUserProfile(uuid: String?)
        fun fetchUserPosts(uuid: String?)
        fun followUser(uuid: String?, follow: Boolean)
        fun updatePhoto(photoUri: Uri)

        fun clear()
    }

    interface View : BaseView<Presenter>{
        fun showProgress(enabled: Boolean)

        fun displayUserProfile(user: Pair<UserAuth, Boolean?>)
        fun displayRequestFailure(message: String)

        fun displayEmptyPosts()
        fun displayFullPosts(posts: List<Post>)

        fun onUpdateFailure(message: String)
        fun onUpdateUserSuccess(image: Uri)
        fun onUpdatePostsSuccess(posts: List<Post>)

        fun follow(isFollow: Boolean)
    }

}