package co.tiagoaguiar.course.instagram.register.presenter

import android.util.Patterns
import co.tiagoaguiar.course.instagram.R
import co.tiagoaguiar.course.instagram.common.base.RequestCallback
import co.tiagoaguiar.course.instagram.register.RegisterEmail
import co.tiagoaguiar.course.instagram.register.data.RegisterRepository

class RegisterEmailPresenter(
    private var view: RegisterEmail.View?,
    private val repository: RegisterRepository
) : RegisterEmail.Presenter{

    override fun create(email: String) {
        val isEmailValid = Patterns.EMAIL_ADDRESS.matcher(email).matches()
        if (!isEmailValid){
            view?.displayEmailFailure(R.string.invalid_email)
        }

        if (isEmailValid){
            view?.showProgress(true)
            repository.create(email, object : RequestCallback<Boolean> {
                override fun onSuccess(data: Boolean) {
                    view?.goToNamePasswordScreen(email)
                }

                override fun onFailure(message: String) {
                    view?.onEmailError(message)
                }

                override fun onComplete() {
                    view?.showProgress(false)
                }
            })
        }
    }

    override fun onDestroy() {
        view = null
    }
}