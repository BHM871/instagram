package co.tiagoaguiar.course.instagram.register.presenter

import co.tiagoaguiar.course.instagram.R
import co.tiagoaguiar.course.instagram.common.base.RequestCallback
import co.tiagoaguiar.course.instagram.common.model.Database
import co.tiagoaguiar.course.instagram.register.RegisterNamePassword
import co.tiagoaguiar.course.instagram.register.data.RegisterRepository

class RegisterNamePasswordPresenter(
    private var view: RegisterNamePassword.View?,
    private val repository: RegisterRepository
) : RegisterNamePassword.Presenter {

    override fun create(
        email: String,
        name: String,
        username: String,
        password: String,
        confirm: String
    ) {
        val isNameValid = name.length >= 3
        val isPasswordValid = password.length >= 8
        val isConfirmValid = confirm.length >= 8

        if (!isNameValid) {
            view?.displayNameFailure(R.string.invalid_name)
            return
        } else view?.displayNameFailure(null)

        if (!isPasswordValid) {
            view?.displayPasswordFailure(R.string.invalid_password)
            return
        } else view?.displayPasswordFailure(null)

        if (!isConfirmValid) {
            view?.displayConfirmFailure(R.string.invalid_password)
            return
        } else view?.displayConfirmFailure(R.string.invalid_password)

        if (password != confirm) view?.displayPasswordNotEquals(R.string.password_not_equal)
        else view?.displayPasswordNotEquals(null)

        val usernameComparison = Database.usersAuth.firstOrNull { it.username == username }

        if (usernameComparison != null) view?.displayUsernameFailure(R.string.existing_username)
        else view?.displayUsernameFailure(null)

        if (
            isNameValid &&
            isPasswordValid &&
            isConfirmValid &&
            password == confirm &&
            usernameComparison == null
        ) {
            view?.showProgress(true)

            repository.create(email, name, username, password, object :
                RequestCallback<Boolean> {
                override fun onSuccess(data: Boolean) {
                    view?.onCreateSuccess(name)
                }

                override fun onFailure(message: String) {
                    view?.onCreateFailure(message)
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