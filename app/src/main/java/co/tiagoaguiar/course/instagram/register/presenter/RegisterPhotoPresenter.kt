package co.tiagoaguiar.course.instagram.register.presenter

import android.net.Uri
import co.tiagoaguiar.course.instagram.common.base.RequestCallback
import co.tiagoaguiar.course.instagram.common.model.Photo
import co.tiagoaguiar.course.instagram.register.RegisterPhoto
import co.tiagoaguiar.course.instagram.register.data.RegisterRepository

class RegisterPhotoPresenter(
    private var view: RegisterPhoto.View?,
    private val repository: RegisterRepository
) : RegisterPhoto.Presenter {

    override fun updateUser(photoUri: Uri) {
        view?.showProgress(true)
        repository.updateUser(photoUri, object : RequestCallback<Photo> {
            override fun onSuccess(data: Photo) {
                view?.onUpdateSuccess()
            }

            override fun onFailure(message: String) {
                view?.onUpdateFailure(message)
            }

            override fun onComplete() {
                view?.showProgress(false)
            }

        })
    }

    override fun onDestroy() {
        view = null
    }
}