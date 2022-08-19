package co.tiagoaguiar.course.instagram.login

import androidx.annotation.StringRes
import co.tiagoaguiar.course.instagram.common.base.BasePresenter
import co.tiagoaguiar.course.instagram.common.base.BaseView

interface Login {

    interface Presenter : BasePresenter {
        fun login(email: String, password: String)
    }

    interface View : BaseView<Presenter> {
        fun showProgress(enabled: Boolean)
        fun displayEmailFailure(@StringRes messageError: Int?)
        fun displayPasswordFailure(@StringRes messageError: Int?)
        fun onUserAuthenticated()
        fun onUserUnauthorized(message: String)
    }

}