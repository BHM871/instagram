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
        fun fetchUserProfile(context: Context)
        fun fetchUserPosts()
        fun updatePhoto(context: Context, photoUri: Uri)
    }

    interface View : BaseView<Presenter>{
        fun showProgress(enabled: Boolean)

        fun displayUserProfile(userAuth: UserAuth, image: Bitmap?)
        fun displayRequestFailure(message: String)

        fun displayEmptyPosts()
        fun displayFullPosts(posts: List<Post>)

        fun onUpdateFailure(message: String)
        fun onUpdateUserSuccess(image: Bitmap)
        fun onUpdatePostsSuccess(posts: List<Post>)
    }

}