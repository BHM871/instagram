@file:Suppress("DEPRECATION")

package co.tiagoaguiar.course.instagram.profile.presenter

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import co.tiagoaguiar.course.instagram.common.base.RequestCallback
import co.tiagoaguiar.course.instagram.common.model.Photo
import co.tiagoaguiar.course.instagram.common.model.Post
import co.tiagoaguiar.course.instagram.common.model.UserAuth
import co.tiagoaguiar.course.instagram.profile.Profile
import co.tiagoaguiar.course.instagram.profile.data.ProfileRepository

class ProfilePresenter(
    private var view: Profile.View?,
    private val repository: ProfileRepository
) : Profile.Presenter {

    override fun fetchUserProfile() {
        view?.showProgress(true)
        repository.fetchUserProfile(object : RequestCallback<UserAuth> {
            override fun onSuccess(data: UserAuth) {
                view?.displayUserProfile(data)
            }

            override fun onFailure(message: String) {
                view?.displayRequestFailure(message)
            }

            override fun onComplete() {
            }
        })
    }

    override fun fetchUserPosts() {
        repository.fetchUserPost(object : RequestCallback<List<Post>> {
            override fun onSuccess(data: List<Post>) {
                if (data.isEmpty()) {
                    view?.displayEmptyPosts()
                } else {
                    view?.displayFullPosts(data)
                }
            }

            override fun onFailure(message: String) {
                view?.displayRequestFailure(message)
            }

            override fun onComplete() {
                view?.showProgress(false)
            }
        })
    }

    override fun updateProfile(context: Context, photoUri: Uri) {
        view?.showProgress(true)
        repository.updateProfile(photoUri, object : RequestCallback<Photo> {
            override fun onSuccess(data: Photo) {
                val bitmap = uriToBitmap(context, data)
                view?.onUpdateUserSuccess(bitmap ?: throw RuntimeException("Error in image"))
            }

            override fun onFailure(message: String) {
                view?.onUpdateFailure(message)
            }

            override fun onComplete() {
                view?.showProgress(false)
            }

        })
    }

    private fun uriToBitmap(context: Context, photo: Photo?): Bitmap? {
        return if (photo != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                val source = ImageDecoder.createSource(context.contentResolver, photo.photoUri)
                ImageDecoder.decodeBitmap(source)
            } else {
                MediaStore.Images.Media.getBitmap(context.contentResolver, photo.photoUri)
            }
        } else {
            null
        }
    }

    override fun onDestroy() {
        view = null
    }

}